package com.bikkadit.store.service.impl;

import com.bikkadit.store.Constant.AppConstant;
import com.bikkadit.store.dto.UserDto;
import com.bikkadit.store.entity.User;
import com.bikkadit.store.repository.UserRepo;
import com.bikkadit.store.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService
{
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper mapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    @Override
    public UserDto createUser(UserDto userDto)
    {
        LOGGER.info("Inside createUser()");
        //generate unique id in String Format
        String userId = UUID.randomUUID().toString();
        LOGGER.info("Generate unique Id: "+ userId);
        userDto.setUserId(userId);

        // dto -> entity
        User user = dtoToEntity(userDto);
        LOGGER.info("Saving the User: "+user);
        User savedUser = userRepo.save(user);
        LOGGER.info("saved User "+savedUser);
        //entity -> dto
        UserDto newDto = entityToDto(savedUser);
        LOGGER.info("Conversion Successful");
        return newDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId)
    {
        LOGGER.info("Inside updateUser() with User Id "+userId);
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException(AppConstant.USER_NOT_FOUND));
        user.setName(userDto.getName());
        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());
        user.setPassword(userDto.getPassword());
        user.setImageName(userDto.getImageName());
        LOGGER.info("Updated User: "+user);
        User updatedUser = userRepo.save(user);
        UserDto updatedDto = entityToDto(updatedUser);
        LOGGER.info("User updated successfully "+updatedDto);
        return updatedDto;
    }

    @Override
    public void deleteUser(String userId)
    {
        LOGGER.info("Inside deleteUser() with id "+userId);
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException(AppConstant.USER_NOT_FOUND));
        userRepo.delete(user);
        LOGGER.info("Deleted User: "+user);
    }

    @Override
    public List<UserDto> getAllUser()
    {
        LOGGER.info("Inside getAllUser");
        List<User> users = userRepo.findAll();
        List<UserDto> dtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        LOGGER.info("List Of User: "+dtoList);
        return dtoList;
    }

    @Override
    public UserDto getUserById(String userId)
    {
        LOGGER.info("Inside getUserById() with User Id: "+userId);
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException(AppConstant.USER_NOT_FOUND));
        LOGGER.info("User with Id: "+user);
        return entityToDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email)
    {
        LOGGER.info("Inside getUserByEmail() with Email: "+email);
        User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException(AppConstant.EMAIL_NOT_FOUND));
        LOGGER.info("User By email: "+user);
        return entityToDto(user);
    }

    @Override
    public List<UserDto> searchUser(String keyword)
    {
        LOGGER.info("Inside searchUser() with keyword: "+keyword);
        List<User> users = userRepo.findByNameContaining(keyword);
        List<UserDto> dtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        LOGGER.info("User details :"+dtoList);
        return dtoList;
    }
    private UserDto entityToDto(User savedUser) {
//        UserDto userDto = UserDto.builder()
//                .userId(savedUser.getUserId())
//                .name(savedUser.getName())
//                .email(savedUser.getEmail())
//                .password(savedUser.getPassword())
//                .about(savedUser.getAbout())
//                .gender(savedUser.getGender())
//                .imageName(savedUser.getImageName()).build();
        return mapper.map(savedUser,UserDto.class);
    }

    private User dtoToEntity(UserDto userDto) {
//        User user = User.builder()
//                .name(userDto.getName())
//                .userId(userDto.getUserId())
//                .email(userDto.getEmail())
//                .password(userDto.getPassword())
//                .about(userDto.getGender())
//                .gender(userDto.getGender()).build();
        return mapper.map(userDto,User.class);
    }
}
