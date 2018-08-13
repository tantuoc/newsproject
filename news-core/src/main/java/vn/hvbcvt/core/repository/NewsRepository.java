package vn.hvbcvt.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import vn.hvbcvt.core.entity.CategoryEntity;
import vn.hvbcvt.core.entity.NewsEntity;

public interface NewsRepository extends JpaRepository<NewsEntity, Long> {
	Page<NewsEntity> findByTitleContainingIgnoreCase(String title, Pageable pageable);
	long countByTitleContainingIgnoreCase(String title);
	Page<NewsEntity> findByCategoryAndTitleContainingIgnoreCase(CategoryEntity category, String title, Pageable pageable);
	Page<NewsEntity> findByCategory(CategoryEntity category, Pageable pageable);
	long countByTitleContainingIgnoreCaseAndCategory(String title, CategoryEntity category);
	long countByCategory(CategoryEntity category);
	void deleteById(long id);
}
