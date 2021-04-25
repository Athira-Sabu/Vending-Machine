package com.orient.vendingmachine.repository;

import com.orient.vendingmachine.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {
    @Query("select SUM(p.quantity) from Product p")
    Integer getQuantitiesCount();

}
