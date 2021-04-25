package com.orient.vendingmachine.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Setter
@Getter
@Entity
public class Wallet {
    @Id
    @GeneratedValue
    private Integer id;
    private Integer denomination;
    private Integer count;

    public Wallet(Integer denomination, Integer count) {
        this.denomination = denomination;
        this.count = count;
    }

    public Wallet() {
    }
}
