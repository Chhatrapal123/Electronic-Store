package com.bikkadit.store.service;

import com.bikkadit.store.dto.PageableResponse;
import com.bikkadit.store.dto.ProductDto;

public interface ProductService
{
    //create
    ProductDto create (ProductDto productDto);

    //update
    ProductDto update(ProductDto productDto,String productId);

    //delete
    void delete(String productId);

    // get product
    ProductDto get (String productId);

    // getAll Product
    PageableResponse<ProductDto> getAll(int PageNumber, int PageSize, String sortBy, String sortDir);

    //search product
    PageableResponse<ProductDto> searchByTitle(String subTitle, int pageNumber, int pageSize, String sortBy, String sortDir);

    //get all live
    PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir);

    //create product with category
    ProductDto createWithCategory(ProductDto productDto, String categoryId);

    // update category of product
    ProductDto updateCategory(String productId, String categoryId);

    // get all category
    PageableResponse<ProductDto> getAllOfCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir);
}
