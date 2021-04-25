package com.orient.vendingmachine.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Product {

    @Id
    @GeneratedValue
    private Integer prodId;
    private String name;
    private Integer quantity;
    private Integer price;
}
