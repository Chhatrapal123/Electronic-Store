package com.bikkadit.store.service;

import com.bikkadit.store.dto.CategoryDto;
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

import java.util.Locale;
import java.util.Optional;

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

}
