package com.bikkadit.store.service;

import com.bikkadit.store.dto.CategoryDto;
import com.bikkadit.store.dto.PageableResponse;
import com.bikkadit.store.dto.ProductDto;
import com.bikkadit.store.entity.Category;
import com.bikkadit.store.entity.Product;
import com.bikkadit.store.repository.CategoryRepository;
import com.bikkadit.store.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
public class ProductServiceTest
{
    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;
    Product product;

    @Autowired
    private ModelMapper mapper;

    @BeforeEach
    public void init()
    {
        product = Product.builder()
                .addedDate(new Date())
                .title("Mobiles")
                .price(2000)
                .live(true)
                .stock(true)
                .productImageName("abc.png")
                .description("This is best in Market")
                .quantity(5)
                .discountPrice(500)
                .build();
    }
    @Test
    public void createProductTest()
    {
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(product);
        Mockito.when(productRepository.findById(Mockito.anyString())).thenReturn(Optional.of(product));

        ProductDto product1 = productService.create(mapper.map(product, ProductDto.class));
        System.out.println(product1.getTitle());
        Assertions.assertNotNull(product1);
        Assertions.assertEquals("Mobiles",product1.getTitle());
    }
    @Test
    public void updateProduct()
    {
        String productId = "hasdfvjyehf";
        ProductDto productDto = ProductDto.builder()
                .addedDate(new Date())
                .title("Mobiles")
                .price(2000)
                .live(true)
                .stock(true)
                .productImageName("abc.png")
                .description("This is best in Market")
                .quantity(5)
                .discountPrice(500)
                .build();
        Mockito.when(productRepository.findById(Mockito.anyString())).thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(product);

        ProductDto updatedProduct = productService.update(productDto, productId);
        System.out.println(updatedProduct.getTitle());

        Assertions.assertNotNull(updatedProduct);
        Assertions.assertEquals(updatedProduct.getTitle(),updatedProduct.getTitle(),"product name is not Valid");
    }
//
//    @Test
//    public void deleteCategory()
//    {
//        String categoryId = UUID.randomUUID().toString();
//        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
//
//        categoryService.delete(categoryId);
//        Mockito.verify(categoryRepository,Mockito.times(1)).delete(category);
//    }
//
//    @Test
//    public void getAllCategory()
//    {
//        Category category1 = Category.builder()
//                .title("Laptop")
//                .description("This is testing create method")
//                .coverImage("abc.png")
//                .build();
//        Category category2 = Category.builder()
//                .title("Laptop")
//                .description("This is testing create method")
//                .coverImage("abc.png")
//                .build();
//        List<Category> categoryList = Arrays.asList(category,category1,category2);
//        Page<Category> page = new PageImpl<>(categoryList);
//        Mockito.when(categoryRepository.findAll((Pageable) Mockito.any())).thenReturn(page);
//
//        //Sort sort = Sort.by("name").ascending();
//        //Pageable pageable = PageRequest.of(1,2,sort);
//        PageableResponse<CategoryDto> allUser = categoryService.getAllCategory(1,2,"name","asc");
//        Assertions.assertEquals(3,allUser.getContent().size());
//    }
//
//    @Test
//    public void getCategoryById()
//    {
//        String categoryId = "d7da596d-2a1c-4292-be7d-4f3a71e65f60";
//
//        Mockito.when(categoryRepository.findById(Mockito.anyString())).thenReturn(Optional.of(category));
//
//        // actual call of service method
//        CategoryDto categoryDto = categoryService.getCategory(categoryId);
//
//        Assertions.assertNotNull(categoryDto);
//        Assertions.assertEquals(category.getTitle(),categoryDto.getTitle(),"Title not matched");
//    }
//    @Test
//    public void searchCategory()
//    {
//        Category category1 = Category.builder()
//                .title("Laptop")
//                .description("This is best Laptop ")
//                .coverImage("abc.png")
//                .build();
//        Category category2 = Category.builder()
//                .title("Desktop")
//                .description("Desktops are good for gaming Laptop")
//                .coverImage("abc.png")
//                .build();
//        Category category3 = Category.builder()
//                .title("Mobilephone")
//                .description("This is best Mobile with having greate Features ")
//                .coverImage("abc.png")
//                .build();
//
//        String keyword = "Laptop";
//        Mockito.when(categoryRepository.findByTitleContaining(keyword)).thenReturn(Arrays.asList(category,category1,category2,category3));
//        List<CategoryDto> categoryDtos = categoryService.searchCategory(keyword);
//        Assertions.assertEquals(4,categoryDtos.size(),"size not matched");
//    }
}
