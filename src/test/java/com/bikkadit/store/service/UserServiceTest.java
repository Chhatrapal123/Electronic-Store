package com.bikkadit.store.service;

import com.bikkadit.store.dto.PageableResponse;
import com.bikkadit.store.dto.UserDto;
import com.bikkadit.store.entity.User;
import com.bikkadit.store.repository.UserRepository;
import com.bikkadit.store.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@SpringBootTest
public class UserServiceTest
{
    @MockBean
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl service;
    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper mapper;
    User user;

    User user1;

    UserDto userDto;

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
        String userId = "hasdfvjyehf";
        UserDto userDto = UserDto.builder()
                .name("Ankit Gangurde")
                .about("This is updated user details")
                .gender("male")
                .imageName("xyz.png")
                .build();
        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

        UserDto updateUser = userService.updateUser(userDto, userId);
        System.out.println(updateUser.getName());

        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(userDto.getName(),updateUser.getName(),"Name is not Valid");
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

    @Test
    public void getUserByIdTest()
    {
        String userId = "d7da596d-2a1c-4292-be7d-4f3a71e65f60";

        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
        // actual call of service method
        UserDto userDto = userService.getUserById(userId);

        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(user.getName(),userDto.getName(),"name not matched");
    }

    public void getUserByEmailTest()
    {
        String emailId = "cj@gmail.com";

        //Mockito.when(userRepository.findById());
    }
}
