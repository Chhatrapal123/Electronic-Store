package com.bikkadit.store.dto;

import com.bikkadit.store.entity.Category;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
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
