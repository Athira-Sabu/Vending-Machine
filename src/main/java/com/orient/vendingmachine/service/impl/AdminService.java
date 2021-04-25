package com.orient.vendingmachine.service.impl;

import com.orient.vendingmachine.exception.BadRequestException;
import com.orient.vendingmachine.model.Product;
import com.orient.vendingmachine.model.Wallet;
import com.orient.vendingmachine.repository.ProductRepository;
import com.orient.vendingmachine.repository.WalletRepository;
import com.orient.vendingmachine.service.IAdminService;
import com.orient.vendingmachine.util.Constant;
import com.orient.vendingmachine.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminService implements IAdminService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WalletRepository walletRepository;

    /**
     * Method for add products to vending meachine
     *
     * @param products: Items list
     * @return
     */
    @Override
    public String addProducts(List<Product> products) {

        Integer prodCountInDb = productRepository.getQuantitiesCount();
        Integer prodCount = products.stream().mapToInt(Product::getQuantity).sum();
        prodCountInDb = prodCountInDb == null ? 0 : prodCountInDb;
        if (prodCountInDb + prodCount < Constant.TOTAL_ITEM_COUNT) {
            productRepository.saveAll(products);
            return "Successfully added";
        } else {
            throw new BadRequestException("Product limit exceeds");
        }
    }

    /**
     * Add coins to vending machine
     *
     * @param coins: coins
     * @return
     */
    @Override
    public String addCoins(List<Wallet> coins) {
        List<Integer> coinsList = Util.getCoinValues();
        List<Integer> coinDenominations = coins.stream().map(Wallet::getDenomination).collect(Collectors.toList());
        boolean isValidCoins = coinsList.containsAll(coinDenominations);
        if (!isValidCoins)
            throw new BadRequestException("Invalid coins");
        List<Wallet> walletInDb = new ArrayList<>();
        walletRepository.findAll().forEach(walletInDb::add);
        Integer coinsCountInDb = walletInDb.stream().mapToInt(Wallet::getCount).sum();
        int requestCoinCount = coins.stream().mapToInt(Wallet::getCount).sum();
        coinsCountInDb = coinsCountInDb == null ? 0 : coinsCountInDb;
        if (coinsCountInDb + requestCoinCount < Constant.TOTAL_COIN_COUNT) {
            walletRepository.saveAll(coins);
        } else {
            throw new BadRequestException("Coins count reached maximum");
        }
        log.info("Coins added successfully");
        return "Coins added successfully";
    }

    /**
     * Withdraw coins from machine
     *
     * @param coins: amount
     * @return
     */
    @Override
    public String debitCoins(List<Wallet> coins) {

        List<Wallet> coinsInDb = new ArrayList<>();
        walletRepository.findAll().forEach(coinsInDb::add);

        Map<Integer, Integer> coinsInDbMap = new HashMap<>();
        coinsInDb.forEach(c -> coinsInDbMap.put(c.getDenomination(), c.getCount()));

        coins.forEach(wallet -> {
            if (coinsInDbMap.containsKey(wallet.getDenomination())
                    && wallet.getCount() <= coinsInDbMap.get(wallet.getDenomination())) {
                int deductedCoins = coinsInDbMap.get(wallet.getDenomination()) - wallet.getCount();
                coinsInDbMap.put(wallet.getDenomination(), deductedCoins);
            } else {
                throw new BadRequestException("Invalid request");
            }
        });

        coinsInDb.forEach(c -> {
            if (coinsInDbMap.containsKey(c.getDenomination())) {
                c.setCount(coinsInDbMap.get(c.getDenomination()));
            }
        });
        walletRepository.saveAll(coinsInDb);
        return "Coins deducted successfully";
    }
}
