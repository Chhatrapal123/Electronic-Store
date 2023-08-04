package com.bikkadit.store.controller;

import com.bikkadit.store.dto.PageableResponse;
import com.bikkadit.store.dto.UserDto;
import com.bikkadit.store.entity.User;
import com.bikkadit.store.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest
{
    @MockBean
    private UserService userService;

    private User user;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private MockMvc mockMvc;
    @BeforeEach
    public void init()
    {
        user = User.builder()
                .name("CJG")
                .email("cj@gmail.com")
                .about("This is testing create method")
                .gender("male")
                .imageName("abc.png")
                .password("abcd")
                .build();
    }
    @Test
    public void createUserTest() throws Exception {
        UserDto dto = mapper.map(user, UserDto.class);
        Mockito.when(userService.createUser(Mockito.any())).thenReturn(dto);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonString(user))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").exists());

    }
    @Test
    public void updateUserTest() throws Exception {
        String userId = "123";
        UserDto dto = this.mapper.map(user, UserDto.class);

        Mockito.when(userService.updateUser(Mockito.any(),Mockito.anyString())).thenReturn(dto);

        this.mockMvc.perform(
                MockMvcRequestBuilders.put("/users/"+userId)
                        //.header(HttpHeaders.AUTHORIZATION,"Bearer Token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonString(user))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists());
    }

    @Test
    public void getAllUsersTest() throws Exception {
        UserDto object1 = UserDto.builder().name("Ankit").email("ankit@gmail.com").password("abcd").about("Testing").build();
        UserDto object2 = UserDto.builder().name("Sanket").email("sanket@gmail.com").password("abcd").about("Testing").build();
        UserDto object3 = UserDto.builder().name("Prasad").email("prasad@gmail.com").password("abcd").about("Testing").build();
        UserDto object4 = UserDto.builder().name("Venky").email("venky@gmail.com").password("abcd").about("Testing").build();

        PageableResponse<UserDto> pageableResponse =new PageableResponse<>();
        pageableResponse.setContent(Arrays.asList(object1,object2,object3,object4));
        pageableResponse.setLastPage(false);
        pageableResponse.setPageSize(10);
        pageableResponse.setPageNumber(100);
        pageableResponse.setTotalElement(1000L);
        Mockito.when(userService.getAllUser(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(pageableResponse);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void deleteUserTest() throws Exception
    {
//        Mockito.when(userService.deleteUser(user)).thenReturn(Optional.of(user))

    }
    private String convertObjectToJsonString(Object user)
    {
        try
        {
            return new ObjectMapper().writeValueAsString(user);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }


}
