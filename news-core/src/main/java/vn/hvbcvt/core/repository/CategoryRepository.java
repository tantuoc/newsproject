package vn.hvbcvt.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.hvbcvt.core.entity.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
	CategoryEntity findOneByCode(String code);
}
