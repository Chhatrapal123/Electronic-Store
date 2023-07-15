package com.bikkadit.store.service.impl;

import com.bikkadit.store.Constant.AppConstant;
import com.bikkadit.store.dto.CategoryDto;
import com.bikkadit.store.dto.PageableResponse;
import com.bikkadit.store.entity.Category;
import com.bikkadit.store.exception.ResourceNotFoundException;
import com.bikkadit.store.helper.Helper;
import com.bikkadit.store.repository.CategoryRepository;
import com.bikkadit.store.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService
{
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);
    @Override
    public CategoryDto create(CategoryDto categoryDto)
    {
        // creating categoryId randomly
        LOGGER.info("Fetching request for create()");
        String categoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);
        Category category = mapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepository.save(category);
        LOGGER.info("Request Fetched Successfully for create category");
        return mapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto, String categoryId)
    {
        //get category for given id
        LOGGER.info("Fetching request for updateCategory()");
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(AppConstant.CATEGORY_NOT_FOUND));
        // update category details
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());
        Category updatedCategory = categoryRepository.save(category);
        LOGGER.info("Request Fetched Successfully for updateCategory()");
        return mapper.map(updatedCategory, CategoryDto.class);
    }

    @Override
    public void delete(String categoryId)
    {
        LOGGER.info("Fetching request for delete() category");
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(AppConstant.DELETE_CATEGORY));
        categoryRepository.delete(category);
        LOGGER.info("Rquest Fetched Successfully for delete() category");
    }

    @Override
    public PageableResponse<CategoryDto> getAllCategory(int pageNumber,int pageSize,String sortBy,String sortDir)
    {
        LOGGER.info("Fetching request for getAllCategory()");
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Category> page = categoryRepository.findAll(pageable);
        PageableResponse<CategoryDto> pageableResponse = Helper.getPageableResponse(page, CategoryDto.class);
        LOGGER.info("Request fetched Successfully for getAllCategory()");
        return pageableResponse;
    }

    @Override
    public CategoryDto getCategory(String categoryId)
    {
        LOGGER.info("Fetching request for getCategory()");
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(AppConstant.CATEGORY_NOT_FOUND));
        LOGGER.info("Request Fetching Successfully for getCategory()");
        return mapper.map(category, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> searchCategory(String keyword)
    {
        LOGGER.info("Fetching Request for searchCategory()");
        List<Category> category = categoryRepository.findByTitleContaining(keyword);
        List<CategoryDto> categoryDtos = category.stream().map(category1 -> mapper.map(category1, CategoryDto.class)).collect(Collectors.toList());
        LOGGER.info("Request Fetched Successfully for searchCategory()");
        return categoryDtos;
    }
}
