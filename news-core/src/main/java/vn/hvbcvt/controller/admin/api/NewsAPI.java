package vn.hvbcvt.controller.admin.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import vn.hvbcvt.dto.NewsDTO;
import vn.hvbcvt.service.INewsService;

@RestController
@RequestMapping("/ajax/news")
public class NewsAPI {
	
	@Autowired
	private INewsService newsService;
	
	@PostMapping
	public ResponseEntity<NewsDTO> createNews(@RequestBody NewsDTO newsDTO) {
		return ResponseEntity.ok(newsService.insert(newsDTO));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<NewsDTO> updateNews(@PathVariable("id") long id, 
											  @RequestBody NewsDTO newsDTO) {
		return ResponseEntity.ok(newsService.update(id, newsDTO));
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteNews(@RequestBody long[] newIds) {
		if (newIds.length > 0) {
			newsService.deleteNews(newIds);
		}
		return ResponseEntity.noContent().build();
	}
}
