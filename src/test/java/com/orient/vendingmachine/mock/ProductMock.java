package com.orient.vendingmachine.mock;

import com.orient.vendingmachine.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductMock {

    public static List<Product > getProducts() {
        Product product = new Product();
        product.setName("Sun Chips");
        product.setPrice(20);
        product.setQuantity(7);
        product.setProdId(1);

        Product product1 = new Product();
        product1.setName("Cheez-its");
        product1.setPrice(30);
        product1.setQuantity(6);
        product1.setProdId(2);
        List<Product> products = new ArrayList<>();
        products.add(product);
        products.add(product1);
        return products;
    }
}
