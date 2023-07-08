package com.bikkadit.store.dto;

import com.bikkadit.store.Validate.ImageNameValid;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto
{
    private String userId;

    @Size(min = 3,max = 20 ,message = "Invalid User")
    private String name;

    //@Email(message = "Invalid User Email..!!")
    @Pattern(regexp = "^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)+[a-z]{2,5}$",message = "Invalid User Email..!!")
    @NotBlank(message = "Email is required..!!")
    private String email;

    @NotBlank(message = "Password is required..!!")
    private String password;

    @Size(min = 4,max = 6,message = "Invalid Gender")
    private String gender;

    @NotBlank(message = "Write something about yourself..!!")
    private String about;

    @ImageNameValid
    private String imageName;
}
