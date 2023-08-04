package com.bikkadit.store.service;

import com.bikkadit.store.dto.CategoryDto;
import com.bikkadit.store.dto.PageableResponse;
import com.bikkadit.store.dto.UserDto;
import com.bikkadit.store.entity.Category;
import com.bikkadit.store.entity.User;
import com.bikkadit.store.repository.CategoryRepository;
import com.bikkadit.store.repository.UserRepository;
import com.bikkadit.store.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

@SpringBootTest
public class CategoryServiceTest
{
    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;
    Category category;

    @Autowired
    private ModelMapper mapper;

    @BeforeEach
    public void init()
    {

        category = Category.builder()
                .title("Desktop")
                .coverImage("abc.png")
                .description("This is High performance device")
                .build();
    }
    @Test
    public void createCategoryTest()
    {
        Mockito.when(categoryRepository.save(Mockito.any())).thenReturn(category);
        Mockito.when(categoryRepository.findById(Mockito.anyString())).thenReturn(Optional.of(category));

        CategoryDto category1 = categoryService.create(mapper.map(category, CategoryDto.class));
        System.out.println(category1.getTitle());
        Assertions.assertNotNull(category1);
        Assertions.assertEquals("Desktop",category1.getTitle());
    }
    @Test
    public void updateCategory()
    {
        String categoryId = "hasdfvjyehf";
        CategoryDto categoryDto = CategoryDto.builder()
                .title("Laptop")
                .description("This is updated user details")
                .coverImage("xyz.png")
                .build();
        Mockito.when(categoryRepository.findById(Mockito.anyString())).thenReturn(Optional.of(category));
        Mockito.when(categoryRepository.save(Mockito.any())).thenReturn(category);

        CategoryDto updateUser = categoryService.update(categoryDto, categoryId);
        System.out.println(updateUser.getTitle());

        Assertions.assertNotNull(updateUser);
        Assertions.assertEquals(updateUser.getTitle(),updateUser.getTitle(),"Name is not Valid");
    }

    @Test
    public void deleteCategory()
    {
        String categoryId = UUID.randomUUID().toString();
        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        categoryService.delete(categoryId);
        Mockito.verify(categoryRepository,Mockito.times(1)).delete(category);
    }

    @Test
    public void getAllCategory()
    {
        Category category1 = Category.builder()
                .title("Laptop")
                .description("This is testing create method")
                .coverImage("abc.png")
                .build();
        Category category2 = Category.builder()
                .title("Laptop")
                .description("This is testing create method")
                .coverImage("abc.png")
                .build();

        List<Category> categoryList = Arrays.asList(category,category1,category2);
        Page<Category> page = new PageImpl<>(categoryList);
        Mockito.when(categoryRepository.findAll((Pageable) Mockito.any())).thenReturn(page);

        //Sort sort = Sort.by("name").ascending();
        //Pageable pageable = PageRequest.of(1,2,sort);
        PageableResponse<CategoryDto> allUser = categoryService.getAllCategory(1,2,"name","asc");
        Assertions.assertEquals(3,allUser.getContent().size());
    }

    @Test
    public void getCategoryById()
    {
        String categoryId = "d7da596d-2a1c-4292-be7d-4f3a71e65f60";

        Mockito.when(categoryRepository.findById(Mockito.anyString())).thenReturn(Optional.of(category));
        // actual call of service method
        CategoryDto categoryDto = categoryService.getCategory(categoryId);

        Assertions.assertNotNull(categoryDto);
        Assertions.assertEquals(category.getTitle(),categoryDto.getTitle(),"Title not matched");
    }
}
