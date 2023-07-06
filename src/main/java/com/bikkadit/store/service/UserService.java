package com.bikkadit.store.service;

import com.bikkadit.store.dto.UserDto;
import com.bikkadit.store.entity.User;
import java.util.List;
public interface UserService
{
    UserDto createUser(UserDto userDto);

    UserDto updateUser(UserDto userDto,String userId);

    void deleteUser(String userId);

    List<UserDto>getAllUser();

    UserDto getUserById(String userId);

    UserDto getUserByEmail(String email);

    List<UserDto>searchUser(String keyword);


}
