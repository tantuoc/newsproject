package vn.hvbcvt.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import vn.hvbcvt.constant.SystemConstant;
import vn.hvbcvt.converter.NewsConverter;
import vn.hvbcvt.core.entity.CategoryEntity;
import vn.hvbcvt.core.entity.NewsEntity;
import vn.hvbcvt.core.repository.CategoryRepository;
import vn.hvbcvt.core.repository.NewsRepository;
import vn.hvbcvt.dto.NewsDTO;
import vn.hvbcvt.service.ICategoryService;
import vn.hvbcvt.service.INewsService;
import vn.hvbcvt.utils.StringGenerate;
import vn.hvbcvt.utils.UploadFileUtils;

@Service
public class NewsService implements INewsService {
	
	@Autowired
	private NewsRepository newsRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private NewsConverter newsConverter;
	
	@Autowired
	private ICategoryService categoryService;

	public List<NewsDTO> getNews(String title, Pageable pageable) {
		Page<NewsEntity> newsPage = null;
		if (title != null) {
			newsPage = newsRepository.findByTitleContainingIgnoreCase(title, pageable);
		} else {
			newsPage = newsRepository.findAll(pageable);
		}
		List<NewsEntity> newsEntities = newsPage.getContent();
		List<NewsDTO> result = new ArrayList<NewsDTO>();
		for (NewsEntity item: newsEntities) {
			NewsDTO newsDTO = newsConverter.convertToDto(item);
			result.add(newsDTO);
		}
		return result;
	}

	@Override
	public int getTotalItems(String title) {
		int totalItem = 0; 
		if (title != null) {
			totalItem = (int) newsRepository.countByTitleContainingIgnoreCase(title);
		} else {
			totalItem = (int) newsRepository.count();
		}
		return totalItem;
	}

	@Override
	@Transactional
	public NewsDTO insert(NewsDTO newData) {
		String thumbnail = SystemConstant.THUMBNAIL_DIR + File.separator + newData.getImageName();
		newData.setThumbnail(thumbnail);
		UploadFileUtils.writeOrUpdate(thumbnail, newData.getThumbnailBase64());
		NewsEntity newsEntity = newsConverter.convertToEntity(newData);
		newsEntity.setCode(StringGenerate.generateValue(5));
		newsEntity.setCategory(categoryRepository.findOneByCode(newData.getCategoryCode()));
		newsEntity.setView(0);
		newsEntity = newsRepository.save(newsEntity);
		return newsConverter.convertToDto(newsEntity);
	}

	@Override
	public NewsDTO findNewsById(long id) {
		NewsEntity entity = newsRepository.findOne(id);
		NewsDTO dto = newsConverter.convertToDto(entity); 
		dto.setCategoryCode(entity.getCategory().getCode());
		dto.setCategories(categoryService.getCategories());
		return dto;
	}

	@Override
	@Transactional
	public NewsDTO update(long id, NewsDTO newNews) {
		NewsEntity oldNews = newsRepository.findOne(id);
		oldNews.setTitle(newNews.getTitle());
		oldNews.setDescription(newNews.getDescription());
		oldNews.setCategory(categoryRepository.findOneByCode(newNews.getCategoryCode()));
		if (!StringUtils.isEmpty(newNews.getImageName())) {
			String thumbnail = SystemConstant.THUMBNAIL_DIR + File.separator + newNews.getImageName();
			oldNews.setThumbnail(thumbnail);
			UploadFileUtils.writeOrUpdate(thumbnail, newNews.getThumbnailBase64());
		}
		oldNews = newsRepository.save(oldNews);
		return newsConverter.convertToDto(oldNews);
	}

	@Override
	public List<NewsDTO> findNewsByCategory(long id, Pageable pageable, String title) {
		CategoryEntity categoryEntity = categoryRepository.findOne(id);
		List<NewsEntity> newEntities = new ArrayList<>();
		if (title != null) {
			newEntities = newsRepository.findByCategoryAndTitleContainingIgnoreCase(categoryEntity, title, pageable).getContent();
		} else {
			newEntities = newsRepository.findByCategoryAndTitleContainingIgnoreCase(categoryEntity, "", pageable).getContent();
		}
		List<NewsDTO> newsDTOs = new ArrayList<>();
		newEntities.forEach(item -> {
			NewsDTO newDTO = newsConverter.convertToDto(item);
			newsDTOs.add(newDTO);
		});
		return newsDTOs;
	}

	@Override
	public int getTotalItemsByCategoryAndTitle(String title, long id) {
		CategoryEntity category = categoryRepository.findOne(id);
		int totalItem = 0; 
		if (title != null) {
			totalItem = (int) newsRepository.countByTitleContainingIgnoreCaseAndCategory(title, category);
		} else {
			totalItem = (int) newsRepository.countByTitleContainingIgnoreCaseAndCategory("", category);
		}
		return totalItem;
	}

	@Override
	public NewsDTO getNewsDetail(long id) {
		NewsEntity newsEntity = newsRepository.findOne(id);
		newsEntity.setView(newsEntity.getView() + 1);
		newsEntity = newsRepository.save(newsEntity);
		return newsConverter.convertToDto(newsEntity);
	}

	@Override
	public NewsDTO getAndSearchNews(String title, long id, String code) {
		NewsDTO model = new NewsDTO();
		model.setCategoryId(id);
		model.setCategoryCode(code);
		if (title != null && StringUtils.isNotEmpty(title)) {
			model.setPage(1);
		}
		Pageable pageable = new PageRequest(model.getPage() - 1, model.getMaxPageItems());
		model.setListResult(findNewsByCategory(id, pageable, title));
		model.setTotalItems(getTotalItemsByCategoryAndTitle(title, id));
		model.setTotalPages((int) Math.ceil((double) model.getTotalItems() / model.getMaxPageItems()));
		return model;
	}

	@Override
	@Transactional
	public void deleteNews(long[] ids) {
		for (Long item: ids) {
			newsRepository.deleteById(item);
		}
	}
}
