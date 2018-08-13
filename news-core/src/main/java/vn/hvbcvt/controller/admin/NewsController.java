package vn.hvbcvt.controller.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import vn.hvbcvt.constant.SystemConstant;
import vn.hvbcvt.dto.NewsDTO;
import vn.hvbcvt.service.ICategoryService;
import vn.hvbcvt.service.INewsService;
import vn.hvbcvt.utils.DisplayTagUtils;

@Controller(value ="newsControllerOfAdmin")
public class NewsController {
	
	@Autowired
	private INewsService newsService;
	
	@Autowired
	private ICategoryService categoryService;
	
	@RequestMapping(value = "/admin/news/list", method = RequestMethod.GET)
	public ModelAndView getNews(@ModelAttribute(SystemConstant.MODEL) NewsDTO model,
									 HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("admin/news/list");
		DisplayTagUtils.initSearchBean(request, model);
		List<NewsDTO> news = newsService.getNews(model.getTitle() , new PageRequest(model.getPage() - 1, model.getMaxPageItems()));
		model.setListResult(news);
		model.setTotalItems(newsService.getTotalItems(model.getTitle()));
		mav.addObject(SystemConstant.MODEL, model);
		return mav;
	}
	
	@RequestMapping(value = "/admin/news/{id}", method = RequestMethod.GET)
	public ModelAndView getNewsById(@PathVariable("id") Long id) {
		ModelAndView mav = new ModelAndView("admin/news/edit");
		mav.addObject(SystemConstant.MODEL, newsService.findNewsById(id));
		return mav;
	}
	
	@RequestMapping(value = "/admin/news/edit", method = RequestMethod.GET)
	public ModelAndView editNewsPage() {
		ModelAndView mav = new ModelAndView("admin/news/edit");
		NewsDTO news = new NewsDTO();
		news.setCategories(categoryService.getCategories());
		mav.addObject(SystemConstant.MODEL, news);
		return mav;
	}
}
