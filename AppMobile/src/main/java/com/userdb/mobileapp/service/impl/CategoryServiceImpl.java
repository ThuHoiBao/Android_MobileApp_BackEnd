package com.userdb.mobileapp.service.impl;

import com.userdb.mobileapp.convert.CategoryConvert;
import com.userdb.mobileapp.dto.responseDTO.CategoryResponseDTO;
import com.userdb.mobileapp.entity.Category;
import com.userdb.mobileapp.repository.CategoryRepository;
import com.userdb.mobileapp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryConvert categoryConvert;
    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponseDTO> categoriesResponseDTO = new ArrayList<CategoryResponseDTO>();
        for (Category category : categories) {
            CategoryResponseDTO categoryResponseDTO= new CategoryResponseDTO();
            categoryResponseDTO=categoryConvert.convertCategory(category);
            categoriesResponseDTO.add(categoryResponseDTO);
        }
        return categoriesResponseDTO;
    }

}
