package com.bikkadit.store.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "product")
public class Product
{
    @Id
    private String productId;
    private String title;
    @Column(length = 1000)
    private String description;
    private int quantity;
    private int discountPrice;
    private int price;
    private Date addedDate;
    private boolean live;
    private boolean stock;
    private String productImageName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product")
    private Category category;
}

