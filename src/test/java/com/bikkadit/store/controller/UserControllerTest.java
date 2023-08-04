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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.UUID;

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
    public void updateUserTest() throws Exception
    {
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
    void getAllUSersTest() throws Exception
    {
        UserDto userDto1 = UserDto.builder().userId(UUID.randomUUID().toString()).name("anuj")
                .email("anuj@gmail.com").password("anuj56").about("developer").imageName("abc.png").build();
        UserDto userDto2 = UserDto.builder().userId(UUID.randomUUID().toString()).name("venky")
                .email("venky@gmail.com").password("venkyq1").about("tester").imageName("xyz.png").build();
        UserDto userDto3 = UserDto.builder().userId(UUID.randomUUID().toString()).name("pankaj")
                .email("pankaj@gmail.com").password("pankaj123").about("devops").imageName("def.png").build();
        UserDto userDto4 = UserDto.builder().userId(UUID.randomUUID().toString()).name("ankit")
                .email("ankit@gmail.com").password("ankit455").about("developer").imageName("qrx.png").build();

        PageableResponse<UserDto> pageableResponse=new PageableResponse<>();
        pageableResponse.setContent(Arrays.asList(userDto1,userDto2,userDto3,userDto4));
        pageableResponse.setPageNumber(0);
        pageableResponse.setPageSize(10);
        pageableResponse.setTotalElement(1000L);
        pageableResponse.setTotalPage(100);
        pageableResponse.setLastPage(false);

        Mockito.when(userService.getAllUser(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(pageableResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk());
    }

    @Test
    public void deleteUserTest() throws Exception
    {
        String userId = UUID.randomUUID().toString();

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/"+userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    void getUSerById() throws Exception {
        String userId = UUID.randomUUID().toString();

        UserDto userDto = mapper.map(user, UserDto.class);

        Mockito.when(userService.getUserById(userId)).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/" +userId)
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.name").exists());
    }
    @Test
    void getUserByEmail() throws Exception
    {
        String email="ankit@gmail.com";

        UserDto userDto = mapper.map(user, UserDto.class);
        Mockito.when(userService.getUserByEmail(email)).thenReturn(userDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/email/" +email)
                                .accept(MediaType.APPLICATION_JSON))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.email").exists());
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
