package com.bikkadit.store.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "categories")
public class Category
{
    @Id
    @Column(name = "id")
    private String categoryId;

    @Column(name = "category_title",length = 60,nullable = false)
    private String title;

    @Column(name = "category_description",length = 500)
    private  String description;

    private String coverImage;
}
