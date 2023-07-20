package com.bikkadit.store.service.impl;

import com.bikkadit.store.Constant.AppConstant;
import com.bikkadit.store.dto.PageableResponse;
import com.bikkadit.store.dto.ProductDto;
import com.bikkadit.store.entity.Category;
import com.bikkadit.store.entity.Product;
import com.bikkadit.store.exception.ResourceNotFoundException;
import com.bikkadit.store.helper.Helper;
import com.bikkadit.store.repository.CategoryRepository;
import com.bikkadit.store.repository.ProductRepository;
import com.bikkadit.store.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService
{
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public ProductDto create(ProductDto productDto)
    {
        Product product = mapper.map(productDto, Product.class);

        // product id
        String productId = UUID.randomUUID().toString();
        product.setAddedDate(new Date());
        product.setProductId(productId);

        //added
        Product saveProduct = productRepository.save(product);
        return mapper.map(saveProduct, ProductDto.class);
    }

    @Override
    public ProductDto update(ProductDto productDto, String productId)
    {
        // Fetch the product of given id
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(AppConstant.PRODUCT_NOT_FOUND));
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setDiscountPrice(productDto.getDiscountPrice());
        product.setLive(productDto.isLive());
        product.setStock(productDto.isStock());
        product.setProductImageName(productDto.getProductImageName());

        //Save Entity
        Product updateProduct = productRepository.save(product);
        return mapper.map(updateProduct,ProductDto.class);
    }

    @Override
    public void delete(String productId)
    {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(AppConstant.PRODUCT_NOT_FOUND));
        productRepository.delete(product);
    }

    @Override
    public ProductDto get(String productId)
    {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(AppConstant.PRODUCT_NOT_FOUND));
        return mapper.map(product,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAll(int PageNumber, int PageSize, String sortBy, String sortDir)
    {
        Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(PageNumber, PageSize,sort);
        Page<Product> page = productRepository.findAll(pageable);
        return Helper.getPageableResponse(page,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> searchByTitle(String subTitle, int pageNumber, int pageSize, String sortBy, String sortDir)
    {
        Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
        Page<Product> page = productRepository.findByTitleContaining(subTitle, pageable);
        return Helper.getPageableResponse(page,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir)
    {
        Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> page = productRepository.findByLiveTrue(pageable);
        return Helper.getPageableResponse(page,ProductDto.class);
    }

    @Override
    public ProductDto createWithCategory(ProductDto productDto, String categoryId)
    {
        // fetch the category from db
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(AppConstant.CATEGORY_NOT_FOUND));
        Product product = mapper.map(productDto, Product.class);
        //product id
        String productId = UUID.randomUUID().toString();
        product.setProductId(productId);
        //aadded
        product.setAddedDate(new Date());
        product.setCategory(category);
        Product saveProduct = productRepository.save(product);
        return mapper.map(saveProduct, ProductDto.class);
    }
    @Override
    public ProductDto updateCategory(String productId, String categoryId)
    {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(AppConstant.PRODUCT_NOT_FOUND));
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(AppConstant.CATEGORY_NOT_FOUND));
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        return mapper.map(savedProduct, ProductDto.class);
    }
    @Override
    public PageableResponse<ProductDto> getAllOfCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir)
    {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(AppConstant.CATEGORY_NOT_FOUND));
        Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> page = productRepository.findByCategory(category, pageable);
        return Helper.getPageableResponse(page, ProductDto.class);
    }
}
