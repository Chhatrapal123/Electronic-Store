package com.bikkadit.store.dto;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto
{
    private String userId;

    private String name;

    private String email;

    private String password;

    private String gender;

    private String about;

    private String imageName;
}
