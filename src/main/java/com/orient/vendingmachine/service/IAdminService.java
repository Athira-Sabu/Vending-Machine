package com.orient.vendingmachine.service;

import com.orient.vendingmachine.model.Product;
import com.orient.vendingmachine.model.Wallet;

import java.util.List;

public interface IAdminService {

    String addProducts(List<Product> products);
    String addCoins(List<Wallet> coins);
    String debitCoins(List<Wallet> coins);
}
