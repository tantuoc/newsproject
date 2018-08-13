package vn.hvbcvt.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import vn.hvbcvt.dto.NewsDTO;

public interface INewsService {
	List<NewsDTO> getNews(String title, Pageable pageable);
	int getTotalItems(String title);
	NewsDTO insert(NewsDTO newData);
	NewsDTO findNewsById(long id);
	NewsDTO update(long id, NewsDTO newNews);
	List<NewsDTO> findNewsByCategory(long id, Pageable pageable, String title);
	int getTotalItemsByCategoryAndTitle(String title, long id);
	NewsDTO getNewsDetail(long id);
	NewsDTO getAndSearchNews(String title, long id, String code);
	void deleteNews(long[] ids);
}
