package com.bikkadit.store.service;

import com.bikkadit.store.dto.PageableResponse;
import com.bikkadit.store.dto.UserDto;
import com.bikkadit.store.entity.User;
import com.bikkadit.store.repository.UserRepository;
import com.bikkadit.store.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class UserServiceTest
{
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl service;
    @Autowired
    private UserService userService;

    User user;

    String userId;

    @Autowired
    private ModelMapper mapper;

    @BeforeEach
    public void init()
    {

        user = User.builder()
                .name("CJ")
                .email("cj@gmail.com")
                .about("This is testing create method")
                .gender("male")
                .imageName("abc.png")
                .password("abcd")
                .build();
        userId="abc";
    }

    @Test
    public void createUser()
    {
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));

        UserDto user1 = userService.createUser(mapper.map(user, UserDto.class));
        System.out.println(user1.getName());
        Assertions.assertNotNull(user1);
        Assertions.assertEquals("CJ",user1.getName());
    }

    @Test
    public void updateUserTest()
    {
        String userId = UUID.randomUUID().toString();
        UserDto userDto = UserDto.builder()
                .name("Ankit Gangurde")
                .about("This is updated user details")
                .gender("male")
                .imageName("xyz.png")
                .build();
        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

        UserDto updatedUser = service.updateUser(userDto, userId);
        System.out.println(updatedUser.getName());
        System.out.println(updatedUser.getImageName());

        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(userDto.getName(),updatedUser.getName(),"Name is not Valid");
    }

    @Test
    public void deleteUserTest()
    {
        String userId = UUID.randomUUID().toString();
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        service.deleteUser(userId);
        Mockito.verify(userRepository,Mockito.times(1)).delete(user);
    }

    @Test
    public void getAllUsersTest()
    {
       User user1 = User.builder()
                .name("Shree")
                .email("cj@gmail.com")
                .about("This is testing create method")
                .gender("male")
                .imageName("abc.png")
                .password("abcd")
                .build();
        User user2 = User.builder()
                .name("Kumar")
                .email("cj@gmail.com")
                .about("This is testing create method")
                .gender("male")
                .imageName("abc.png")
                .password("abcd")
                .build();

        List<User>userList = Arrays.asList(user,user1,user2);
        Page<User>page = new PageImpl<>(userList);
        Mockito.when(userRepository.findAll((Pageable) Mockito.any())).thenReturn(page);

        Sort sort = Sort.by("name").ascending();
        Pageable pageable = PageRequest.of(1,2,sort);
        PageableResponse<UserDto> allUser = service.getAllUser(1,2,"name","asc");
        Assertions.assertEquals(3,allUser.getContent().size());
    }
}
