package com.orient.vendingmachine.service;

import com.orient.vendingmachine.model.Product;
import com.orient.vendingmachine.model.Wallet;

import java.util.List;

public interface IUserService {

    String setCoins(List<Wallet> coins);
    List<Product> getProductList();
    List<Wallet> cancelRequest();
    List<Wallet> selectProducts(List<Product> products);
}
