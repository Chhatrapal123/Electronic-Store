package com.bikkadit.store.controller;

import com.bikkadit.store.Constant.AppConstant;
import com.bikkadit.store.dto.ApiResponseMessage;
import com.bikkadit.store.dto.ImageResponse;
import com.bikkadit.store.dto.PageableResponse;
import com.bikkadit.store.dto.UserDto;
import com.bikkadit.store.service.FileService;
import com.bikkadit.store.service.UserService;
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
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController
{
    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    /**
     * @apiNote Create User
     * @param userDto
     * @return
     */
    @PostMapping
    public ResponseEntity<UserDto>createUser(@Valid @RequestBody UserDto userDto)
    {
        LOGGER.info("Inside createUser()");
        UserDto user = userService.createUser(userDto);
        LOGGER.info("Completed Request of Create User");
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    /**
     * @apiNote Update User
     * @param userId
     * @param userDto
     * @return
     */
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto>updateUser(@Valid @PathVariable("userId")String userId,@RequestBody UserDto userDto)
    {
        LOGGER.info("Inside updateUser()");
        UserDto updatedUserDto = userService.updateUser(userDto, userId);
        LOGGER.info("Completed Request For Update User");
        return new ResponseEntity<>(updatedUserDto, HttpStatus.OK);
    }

    /**
     * @apiNote Delete User
     * @param userId
     * @return
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage>deleteUser(@PathVariable("userId") String userId)
    {
        LOGGER.info("Inside deleteUser()");
        userService.deleteUser(userId);
        ApiResponseMessage message = ApiResponseMessage
                .builder()
                .message(AppConstant.DELETE_USER)
                .success(true)
                .status(HttpStatus.OK)
                .build();
        LOGGER.info("Completed Request For Delete User");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /**
     * @apiNote Get All User
     * @return
     */
    @GetMapping("/")
    public ResponseEntity<PageableResponse<UserDto>>getAllUsers(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber ,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "name",required = false)String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    )
    {
        LOGGER.info("Inside getALlUsers()");
        PageableResponse<UserDto> allUserDtos = userService.getAllUser(pageNumber, pageSize, sortBy, sortDir);
        LOGGER.info("Completed the Request For Getting All Users");
        return new ResponseEntity<>(allUserDtos, HttpStatus.OK);
    }

    /**
     * @apiNote Get Users
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUsers(@PathVariable String userId)
    {
        LOGGER.info("Inside getUsers()");
        return new ResponseEntity<UserDto>(userService.getUserById(userId), HttpStatus.OK);
    }

    /**
     * @apiNote Get User By Email
     * @param email
     * @return
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email)
    {
        LOGGER.info("Inside getUserByEmail()");
        return new ResponseEntity<>(userService.getUserByEmail(email), HttpStatus.OK);
    }

    /**
     * @apiNote Search User
     * @param keywords
     * @return
     */
    @GetMapping("/search/{keyword}")
    public ResponseEntity<UserDto> searchUser(@PathVariable String keywords)
    {
        LOGGER.info("Inside searchUser()");
        return new ResponseEntity(userService.searchUser(keywords), HttpStatus.OK);
    }


    /**
     * @apiNote upload user image
     * @param image
     * @param userId
     * @return
     * @throws IOException
     */
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse>uploadUserImage(@RequestParam("userImage")MultipartFile image, @PathVariable String userId) throws IOException
    {
        LOGGER.info("Inside uploadUserImage()");
        String imageName = fileService.uploadFile(image, imageUploadPath);

        UserDto user = userService.getUserById(userId);

        user.setImageName(imageName);

        UserDto userDto = userService.updateUser(user, userId);

        ImageResponse imageResponse = ImageResponse.builder().imageName(imageName).message(AppConstant.IMAGE_UPLOAD).success(true).status(HttpStatus.CREATED).build();
        LOGGER.info("Completed request for uploadUserImage()");
        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
    }

    //Serve User Image
    @GetMapping("/image/{userId}")
    public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException
    {
        //
        LOGGER.info("Inside serveUserImage()");
        UserDto user = userService.getUserById(userId);
        LOGGER.info("User image name: ",user.getImageName());
        InputStream resource = fileService.getResource(imageUploadPath, user.getImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }
}