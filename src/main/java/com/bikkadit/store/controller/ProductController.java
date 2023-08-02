package com.bikkadit.store.controller;

import com.bikkadit.store.Constant.AppConstant;
import com.bikkadit.store.dto.ApiResponseMessage;
import com.bikkadit.store.dto.ImageResponse;
import com.bikkadit.store.dto.PageableResponse;
import com.bikkadit.store.dto.ProductDto;
import com.bikkadit.store.service.FileService;
import com.bikkadit.store.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/products")
public class  ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
    @Value("${product.profile.image.path}")
    private String imagePath;

    //create
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        LOGGER.info("Initialize createProduct()");
        ProductDto createdProduct = productService.create(productDto);
        LOGGER.info("Completed Request For createProduct()");
        return new ResponseEntity<>(createdProduct, HttpStatus.OK);
    }

    //update
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable String productId, @RequestBody ProductDto productDto) {
        LOGGER.info("Initialize updateProduct() for productId: "+ productId);
        ProductDto updatedProduct = productService.update(productDto, productId);
        LOGGER.info("Complete Request For updateProduct() with productId :"+productId);
        return new ResponseEntity<>(updatedProduct, HttpStatus.CREATED);
    }

    //delete
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable String productId, @RequestBody ProductDto productDto)
    {
        LOGGER.info("Initialize deleteProduct()");
        productService.delete(productId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .message(AppConstant.DELETE_PRODUCT)
                .success(true)
                .status(HttpStatus.OK)
                .build();
        LOGGER.info("Completed Request For deleteProduct()");
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    //getAll
    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>> getAll(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "asc", defaultValue = "asc", required = false) String sortDir) {
        LOGGER.info("Initialize getAll()");
        PageableResponse<ProductDto> pageableResponse = productService.getAll(pageNumber, pageSize, sortBy, sortDir);
        LOGGER.info("Complete Request For getAll()");
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    //get all live
    //product/live
    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> getAllLive(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "asc", defaultValue = "asc", required = false) String sortDir) {
        LOGGER.info("Initialize getAllLive()");
        PageableResponse<ProductDto> pageableResponse = productService.getAllLive(pageNumber, pageSize, sortBy, sortDir);
        LOGGER.info("Complete Request for getAllLive()");
        return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    //Search All
    @GetMapping("/search/{query}")
    public ResponseEntity<PageableResponse<ProductDto>> searchProduct(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir)
    {
        LOGGER.info("Initialze searchProduct()");
        PageableResponse<ProductDto> pageableResponse = productService.getAll(pageNumber, pageSize, sortBy, sortDir);
        LOGGER.info("Complete Request For searchProduct()");
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }

    //upload image
    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse>uploadProductImage(@PathVariable String productId, @RequestParam("productImage")MultipartFile image) throws IOException {
        LOGGER.info("Initialize uploadProductImage()");
        String fileName = fileService.uploadFile(image, imagePath);
        ProductDto productDto = productService.get(productId);
        productDto.setProductImageName(fileName);
        ProductDto updatedProduct = productService.update(productDto, productId);
        ImageResponse response = ImageResponse.builder().imageName(updatedProduct.getProductImageName())
                .message(AppConstant.IMAGE_UPLOAD).status(HttpStatus.CREATED)
                .success(true).build();
        LOGGER.info("Complete Request For uploadProductImage()");
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }
    //Serve image
    @GetMapping("/iamge/{productId}")
    public  void serveUserImage(@PathVariable String productId, HttpServletResponse response) throws IOException {
        LOGGER.info("Initialize serveUserImage()");
        ProductDto productDto = productService.get(productId);
        InputStream resource = fileService.getResource(imagePath, productDto.getProductImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }

}
