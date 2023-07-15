package com.bikkadit.store.service;

import com.bikkadit.store.dto.CategoryDto;
import com.bikkadit.store.dto.PageableResponse;

import java.util.List;

public interface CategoryService
{
    // Create
    CategoryDto create(CategoryDto categoryDto);

    // update
    CategoryDto update(CategoryDto categoryDto,String categoryId);

    // delete
    void delete(String categoryId);

    //get all
    PageableResponse<CategoryDto>getAllCategory(int pageNumber,int pageSize,String sortBy,String sortDir);

    // get single category
    CategoryDto getCategory(String categoryId);

    // search
    List<CategoryDto>searchCategory(String keyword);
}
