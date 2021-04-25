package com.orient.vendingmachine.repository;

import com.orient.vendingmachine.model.Wallet;
import org.springframework.data.repository.CrudRepository;

public interface WalletRepository extends CrudRepository<Wallet, Integer> {

}
