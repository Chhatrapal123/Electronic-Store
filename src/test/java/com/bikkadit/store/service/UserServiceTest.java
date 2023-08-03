package com.bikkadit.store.service;

import com.bikkadit.store.dto.UserDto;
import com.bikkadit.store.entity.User;
import com.bikkadit.store.repository.UserRepository;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class UserServiceTest
{
    @Mock
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    User user;

    @Autowired
    private ModelMapper mapper;
    String userId;
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

        String userId="abjdhgcjhvc";
        UserDto userDto = UserDto.builder()
                .name("Ankit Gangurde")
                .about("This is updated user details")
                .gender("male")
                .imageName("xyz.png")
                .build();
        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

        UserDto updatedUser = userService.updateUser(userDto, userId);
        System.out.println(updatedUser.getName());

        Assertions.assertNotNull(userDto);
    }

}
