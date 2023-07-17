package com.bikkadit.store.controller;

import com.bikkadit.store.Constant.AppConstant;
import com.bikkadit.store.dto.*;
import com.bikkadit.store.service.CategoryService;
import com.bikkadit.store.service.FileService;
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
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/categories")
public class CategoryController
{
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FileService fileService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    @Value("${category.profile.image.path}")
    private String imageUploadPath;



    /**
     * @apiNote create
     * @param categoryDto
     * @return
     */
    @PostMapping
    public ResponseEntity<CategoryDto>createCategory(@Valid @RequestBody CategoryDto categoryDto)
    {
        // call service to save object
        LOGGER.info("Inside createCategory()");
        CategoryDto categoryDto1 = categoryService.create(categoryDto);
        LOGGER.info("Complted request for createCategory()");
        return new ResponseEntity<>(categoryDto1, HttpStatus.CREATED);
    }

    /**
     * @apiNote update
     * @param categoryDto
     * @param categoryId
     * @return
     */
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto>updateCategory(@Valid @RequestBody CategoryDto categoryDto,@PathVariable String categoryId)
    {
        LOGGER.info("Inside updateCategory()");
        CategoryDto updatedCategory = categoryService.update(categoryDto, categoryId);
        LOGGER.info("Completed Request for updateCategory()");
        return new ResponseEntity<>(updatedCategory,HttpStatus.OK);
    }

    /**
     * @apiNote delete
     * @param categoryId
     * @return
     */
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponseMessage>deleteCategory(@PathVariable String categoryId)
    {
        LOGGER.info("Inside deleteCategory()");
        categoryService.delete(categoryId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .message(AppConstant.DELETE_CATEGORY)
                .success(true)
                .status(HttpStatus.OK)
                .build();
        LOGGER.info("Completed Request for deleteCategory()");
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);
    }

    /**
     * @apiNote getAll
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortDir
     * @return
     */
    @GetMapping
    public ResponseEntity<PageableResponse<CategoryDto>>getAllCategory(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false)int pageNumber,
            @RequestParam(value = "pageSize",defaultValue ="10",required = false)int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false)String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false)String sortDir
    )
    {
        LOGGER.info("Inside getAllCategory()");
        PageableResponse<CategoryDto> pageableResponse = categoryService.getAllCategory(pageNumber, pageSize, sortBy, sortDir);
        LOGGER.info("Completed request for getAllCategory()");
        return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
    }

    /**
     * @apiNote get single
     * @param categoryId
     * @return
     */
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto>getSingleCategory(@PathVariable String categoryId)
    {
        LOGGER.info("Inside getSingleCategory()");
        CategoryDto categoryDto = categoryService.getCategory(categoryId);
        LOGGER.info("Completed Request for getSingleCategory()");
        return ResponseEntity.ok(categoryDto);
    }

    /**
     * @apiNote search user
     * @param title
     * @return
     */
    @GetMapping("/search/{title}")
    public ResponseEntity<CategoryDto> searchCategory(@PathVariable String title)
    {
        LOGGER.info("Completed request for fetching searchCategory()");
        return new ResponseEntity(categoryService.searchCategory(title), HttpStatus.OK);
    }

    /**
     * @apiNote uploadImage
     * @param image
     * @param categoryId
     * @return
     * @throws IOException
     */
    @PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageResponse>uploadCategoryImage(@RequestParam("categoryImage") MultipartFile image, @PathVariable String categoryId) throws IOException
    {
        LOGGER.info("Inside uploadCategoryImage()");
        String coverImage = fileService.uploadFile(image, imageUploadPath);
        CategoryDto category = categoryService.getCategory(categoryId);
        category.setCoverImage(coverImage);
        categoryService.update(category, categoryId);


        ImageResponse imageResponse = ImageResponse.builder().imageName(coverImage).message(AppConstant.IMAGE_UPLOAD).success(true).status(HttpStatus.CREATED).build();
        LOGGER.info("Completed request for uploadCategoryImage()");
        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }

    /**
     * @apiNote serve Image
     * @param categoryId
     * @param response
     * @throws IOException
     */
    @GetMapping("/image/{categoryId}")
    public void serveCategoryImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException
    {
        //
        LOGGER.info("Inside serveCategoryImage()");
        CategoryDto category = categoryService.getCategory(categoryId);
        LOGGER.info("category image name: {} ",category.getCoverImage());
        InputStream resource = fileService.getResource(imageUploadPath, category.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }
}
