package com.bikkadit.store.dto;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto
{
    private String categoryId;

    @NotBlank(message = "Title is Required")
    @Size(min = 4,message = "Title must be of minimum 4 characters")
    private String title;

    @NotBlank(message = "Description required!!")
    private  String description;

    private String coverImage;


}
