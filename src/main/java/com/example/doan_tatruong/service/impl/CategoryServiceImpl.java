package com.example.doan_tatruong.service.impl;

import com.example.doan_tatruong.dto.CategoryDto;
import com.example.doan_tatruong.model.Category;
import com.example.doan_tatruong.repository.CategoryRepository;
import com.example.doan_tatruong.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category save(Category category) {
        try {
            Category categorySave = new Category(category.getName());
            return categoryRepository.save(categorySave);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public Category update(Category category) {
        Category categoryUpdate = null;
        try {
            categoryUpdate= categoryRepository.findById(category.getId()).get();
            categoryUpdate.setName(category.getName());
            categoryUpdate.set_activated(category.is_activated());
            categoryUpdate.set_deleted(category.is_deleted());
        }catch (Exception e){
            e.printStackTrace();
        }
        return categoryRepository.save(categoryUpdate);
    }

    @Override
    public void deleteById(Long id) {
        Category category = categoryRepository.getById(id);
        category.set_activated(false);
        category.set_deleted(true);
        categoryRepository.save(category);
    }

    @Override
    public void enableById(Long id) {
       Category category = categoryRepository.getById(id);
       category.set_activated(true);
       category.set_deleted(false);
       categoryRepository.save(category);
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id).get();
    }

    @Override
    public List<Category> findAllByActivatedTrue() {
        return categoryRepository.findAllByActivatedTrue();
    }

    @Override
    public List<CategoryDto> getCategoryAndProduct() {
        return categoryRepository.getCategoryAndProduct();
    }
}
