package com.orient.vendingmachine.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Setter
@Getter
@Entity
public class Wallet {
    @Id
    @GeneratedValue
    private Integer id;
    private Integer denomination;
    private Integer count;
}
