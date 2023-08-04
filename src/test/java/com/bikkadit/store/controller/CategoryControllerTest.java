package com.bikkadit.store.controller;

import com.bikkadit.store.dto.CategoryDto;
import com.bikkadit.store.dto.PageableResponse;
import com.bikkadit.store.dto.UserDto;
import com.bikkadit.store.entity.Category;
import com.bikkadit.store.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest
{
    @MockBean
    private CategoryService categoryService;

    private Category category;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void init()
    {
        category = Category.builder()
                .title("Laptop")
                .coverImage("abc.png")
                .description("Best features")
                .build();
    }
    @Test
    public void createCategoryTest() throws Exception
    {
        CategoryDto dto = mapper.map(category, CategoryDto.class);
        Mockito.when(categoryService.create(Mockito.any())).thenReturn(dto);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonString(category))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").exists());
    }

    private String convertObjectToJsonString(Category category)
    {
        try
        {
            return new ObjectMapper().writeValueAsString(category);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    public void updateCategoryTest() throws Exception
    {
        String categoryId= UUID.randomUUID().toString();
        CategoryDto categoryDto=CategoryDto.builder()
                .categoryId(categoryId)
                .title("Mobiles")
                .description("Mobiles available with discounts")
                .coverImage("abc.png")
                .build();

        Mockito.when(categoryService.update(Mockito.any(),Mockito.anyString())).thenReturn(categoryDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/categories/" +categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonString(category))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void deleteCategoryTest() throws Exception
    {
        String categoryId=UUID.randomUUID().toString();

        mockMvc.perform(MockMvcRequestBuilders.delete("/categories/"+categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

//    @Test
//    public void getAllCategoriesTest() throws Exception
//    {
//        CategoryDto object1 = CategoryDto.builder().title("Laptop").coverImage("abcd.png").description("Best Features").build();
//        CategoryDto object2 = CategoryDto.builder().title("Mobiles").coverImage("abce.png").description("Best Speed").build();
//        CategoryDto object3 = CategoryDto.builder().title("Gadgets").coverImage("abcg.png").description("Best quality").build();
//
//        PageableResponse<CategoryDto>pageableResponse=new PageableResponse<>();
//        pageableResponse.setContent(Arrays.asList(
//                object1,object2,object3
//        ));
//        pageableResponse.setLastPage(true);
//        pageableResponse.setPageSize(10);
//        pageableResponse.setTotalElement(1000L);
//
//        Mockito.when(categoryService.getAllCategory(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(pageableResponse);
//        this.mockMvc.perform(MockMvcRequestBuilders.get("/")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk());
//
//    }

}
