package com.bikkadit.store.dto;

import com.bikkadit.store.entity.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductDto
{
        private String productId;
        private String title;
        private String description;
        private int quantity;
        private int discountPrice;
        private int price;
        private Date addedDate;
        private boolean live;
        private boolean stock;
        private String productImageName;
        private Category category;
}
