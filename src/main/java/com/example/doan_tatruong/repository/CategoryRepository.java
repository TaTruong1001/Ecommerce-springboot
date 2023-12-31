package com.example.doan_tatruong.repository;

import com.example.doan_tatruong.dto.CategoryDto;
import com.example.doan_tatruong.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "update Category set name = ?1 where id = ?2")
    Category update(String name, Long id);
    @Query(value = "select * from categories where is_activated = true", nativeQuery = true)
    List<Category> findAllByActivatedTrue();
    @Query("select new com.example.doan_tatruong.dto.CategoryDto(c.id, c.name, count(p.category.id))" +
            " from Category c inner join Product p on p.category.id = c.id where c.is_activated = true and c.is_deleted = false and p.currentQuantity > 0 group by c.id")
    List<CategoryDto> getCategoryAndProduct();
}
