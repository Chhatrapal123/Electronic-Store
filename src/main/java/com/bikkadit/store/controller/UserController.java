package com.bikkadit.store.controller;

import com.bikkadit.store.dto.ApiResponseMessage;
import com.bikkadit.store.dto.UserDto;
import com.bikkadit.store.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController
{
    @Autowired
    private UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    /**
     * @apiNote Create User
     * @param userDto
     * @return
     */
    @PostMapping("/")
    public ResponseEntity<UserDto>createUser(@RequestBody UserDto userDto)
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
    public ResponseEntity<UserDto>updateUser(@PathVariable("userId")String userId,@RequestBody UserDto userDto)
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
                .message("User is deleted successfully!!")
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
    public ResponseEntity<List<UserDto>>getAllUsers()
    {
        LOGGER.info("Inside getALlUsers()");
        return new ResponseEntity<>(userService.getAllUser(), HttpStatus.OK);
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
}