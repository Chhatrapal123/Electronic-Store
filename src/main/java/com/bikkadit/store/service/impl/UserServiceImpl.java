package com.bikkadit.store.service.impl;

import com.bikkadit.store.Constant.AppConstant;
import com.bikkadit.store.dto.PageableResponse;
import com.bikkadit.store.dto.UserDto;
import com.bikkadit.store.entity.User;
import com.bikkadit.store.exception.ResourceNotFoundException;
import com.bikkadit.store.helper.Helper;
import com.bikkadit.store.repository.UserRepo;
import com.bikkadit.store.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        LOGGER.info("Generated unique Id: "+ userId);
        userDto.setUserId(userId);

        // dto -> entity
        User user = dtoToEntity(userDto);
        User savedUser = userRepo.save(user);
        //entity -> dto
        UserDto newDto = entityToDto(savedUser);
        LOGGER.info("Completed Request For Saving The User "+ newDto);
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
        LOGGER.info("Inside updateUser() Fetching User For userId :"+ userId);
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER_NOT_FOUND));
        LOGGER.info("Fetched User is: "+ user);
        user.setName(userDto.getName());
        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());
        user.setPassword(userDto.getPassword());
        user.setImageName(userDto.getImageName());
        User updatedUser = userRepo.save(user);
        UserDto updatedDto = entityToDto(updatedUser);
        LOGGER.info("Completed Request For Updating The User  "+updatedDto);
        return updatedDto;
    }

    /**
     * @implNote Delete User
     * @param userId
     */
    @Override
    public void deleteUser(String userId)
    {
        LOGGER.info("Inside deleteUser() for id "+userId);
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER_NOT_FOUND));
        LOGGER.info("Completed Request to Delete User : "+user);
        userRepo.delete(user);
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
        Pageable pageable = PageRequest.of(pageNumber-1,pageSize,sort);
        Page<User> page = userRepo.findAll(pageable);

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
        LOGGER.info("Completed Request for Fetching List Of Users: "+response);
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
        LOGGER.info("Inside getUserById() For User Id: "+userId);
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER_NOT_FOUND));
        LOGGER.info("Completed Request for Fetching User with Id: "+user);
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
        LOGGER.info("Inside getUserByEmail() For Email: "+email);
        User user = userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(AppConstant.EMAIL_NOT_FOUND));
        LOGGER.info("Completed Request for Fetching User By email: "+user);
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
        LOGGER.info("Inside searchUser() For keyword: "+keyword);
        List<User> users = userRepo.findByNameContaining(keyword);
        List<UserDto> dtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        LOGGER.info("Completed Request for Fetching User details :"+dtoList);
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
