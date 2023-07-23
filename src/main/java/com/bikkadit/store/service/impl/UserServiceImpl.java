package com.bikkadit.store.service.impl;

import com.bikkadit.store.Constant.AppConstant;
import com.bikkadit.store.dto.PageableResponse;
import com.bikkadit.store.dto.UserDto;
import com.bikkadit.store.entity.User;
import com.bikkadit.store.exception.ResourceNotFoundException;
import com.bikkadit.store.helper.Helper;
import com.bikkadit.store.repository.UserRepository;
import com.bikkadit.store.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Value("${user.profile.image.path}")
    private String imagePath;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * @implNote Creating User
     * @param userDto
     * @return
     */
    @Override
    public UserDto createUser(UserDto userDto)
    {
        LOGGER.info("Inside createUser()");
        //generate unique id in String Format
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);
        // dto -> entity
        User user = dtoToEntity(userDto);
        User savedUser = userRepository.save(user);
        //entity -> dto
        UserDto newDto = entityToDto(savedUser);
        LOGGER.info("Completed Request to Saving The User for UserId: {} ",userId);
        return newDto;
    }

    /**
     * @implNote Updating User Details
     * @param userDto
     * @param userId
     * @return
     */
    @Override
    public UserDto updateUser(UserDto userDto, String userId)
    {
        LOGGER.info("Inside updateUser() Fetching User For userId: {}",userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER_NOT_FOUND));
        LOGGER.info("Fetched User successfully ");
        user.setName(userDto.getName());
        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());
        user.setPassword(userDto.getPassword());
        user.setImageName(userDto.getImageName());
        User updatedUser = userRepository.save(user);
        UserDto updatedDto = entityToDto(updatedUser);
        LOGGER.info("Completed Request to Updating The User for UserId: {}",userId);
        return updatedDto;
    }

    /**
     * @implNote Delete User
     * @param userId
     */
    @Override
    public void deleteUser(String userId)
    {
        LOGGER.info("Inside deleteUser() for userId: {}",userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER_NOT_FOUND));
       // delete user profile image
        String fullPath = imagePath + user.getImageName();
        try
        {
            Path path = Paths.get(fullPath);
            Files.delete(path);
        }catch (NoSuchFileException ex)
        {
            LOGGER.info("User Image Not Found In Folder");
            ex.printStackTrace();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        // delete User
        LOGGER.info("Completed Request to Delete User for userId: {}",userId);
        userRepository.delete(user);
    }

    /**
     * @implNote Fetching All Details Of User
     * @return
     */
    @Override
    public PageableResponse<UserDto> getAllUser(int pageNumber,int pageSize,String sortBy,String sortDir)
    {
        LOGGER.info("Inside getAllUser()");
        Sort sort = sortDir.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<User> page = userRepository.findAll(pageable);

        PageableResponse<UserDto> response = Helper.getPageableResponse(page, UserDto.class);

//        List<User> users = page.getContent();
//
//        //List<User> users = userRepo.findAll();
//        List<Object> dtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
//        PageableResponse pageableResponse = PageableResponse.builder()
//                .pageNumber(page.getNumber())
//                .pageSize(page.getSize())
//                .lastPage(page.isLast())
//                .content(dtoList)
//                .totalElement(page.getTotalElements())
//                .build();
        LOGGER.info("Completed Request for Fetching List Of Users");
        return response;
    }

    /**
     * @implNote Fetching User By ID
     * @param userId
     * @return
     */
    @Override
    public UserDto getUserById(String userId)
    {
        LOGGER.info("Inside getUserById() For UserId: {}",userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER_NOT_FOUND));
        LOGGER.info("Completed Request to Fetching UserById for userId: {}",userId);
        return entityToDto(user);
    }

    /**
     * @implNote Fetching User Details By Email
     * @param email
     * @return
     */
    @Override
    public UserDto getUserByEmail(String email)
    {
        LOGGER.info("Inside getUserByEmail() For Email: {}",email);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(AppConstant.EMAIL_NOT_FOUND));
        LOGGER.info("Completed Request for Fetching User By email: {}",email);
        return entityToDto(user);
    }

    /**
     * @implNote Search User
     * @param keyword
     * @return
     */
    @Override
    public List<UserDto> searchUser(String keyword)
    {
        LOGGER.info("Inside searchUser() For keyword: {}");
        List<User> users = userRepository.findByNameContaining(keyword);
        List<UserDto> dtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        LOGGER.info("Completed Request for Fetching User details by keyword: {}",keyword);
        return dtoList;
    }

    /**
     * @implNote Entity to Dto Conversion
     * @param savedUser
     * @return
     */
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

    /**
     * @implNote Dto to Entity Conversion
     * @param userDto
     * @return
     */
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