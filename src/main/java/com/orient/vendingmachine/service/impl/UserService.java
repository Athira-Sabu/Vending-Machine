package com.orient.vendingmachine.service.impl;

import com.orient.vendingmachine.exception.BadRequestException;
import com.orient.vendingmachine.model.Product;
import com.orient.vendingmachine.model.Wallet;
import com.orient.vendingmachine.repository.ProductRepository;
import com.orient.vendingmachine.repository.WalletRepository;
import com.orient.vendingmachine.service.IUserService;
import com.orient.vendingmachine.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {

    private List<Wallet> userAddedCoins;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WalletRepository walletRepository;

    /**
     * Initialize user's wallet
     *
     * @param coins: amount
     * @return
     */
    @Override
    public String setCoins(List<Wallet> coins) {
        List<Integer> coinsList = Util.getCoinValues();
        List<Integer> coinDenominations = coins.stream().map(Wallet::getDenomination).collect(Collectors.toList());
        boolean isValidCoins = coinsList.containsAll(coinDenominations);
        if (!isValidCoins)
            throw new BadRequestException("Invalid coins");
        userAddedCoins = coins;
        return "Coins added";
    }

    /**
     * List all products
     *
     * @return items list
     */
    @Override
    public List<Product> getProductList() {
        List<Product> products = new ArrayList<>();
        productRepository.findAll().forEach(products::add);
        return products;
    }

    /**
     * Reset user's wallet and withdraw money
     *
     * @return
     */
    @Override
    public List<Wallet> cancelRequest() {
        List<Wallet> wallet = userAddedCoins;
        clearUserAddedCoins();
        return wallet;
    }

    /**
     * Select products from vending machine
     *
     * @param products
     * @return
     */
    @Override
    public List<Wallet> selectProducts(List<Product> products) {
        int totalPrice = products.stream().mapToInt(Product::getPrice).sum();
        int userWallet = userAddedCoins.stream().mapToInt(Wallet::getCount).sum();
        List<Wallet> remainderCoins = null;
        if (totalPrice > userWallet) {
            throw new BadRequestException("Invalid request");
        } else if (userWallet > totalPrice) {
            remainderCoins = calculateChange(userWallet - totalPrice);
        }
        updateProductDetails(products);
        updateWallet();
        clearUserAddedCoins();
        return remainderCoins;
    }

    /**
     * Method to find the remainder
     *
     * @param remainder
     * @return
     */
    private List<Wallet> calculateChange(int remainder) {
        List<Wallet> remainderCoins = new ArrayList<>();
        Map<Integer, Integer> coinsMap = new TreeMap<>(Collections.reverseOrder());
        walletRepository.findAll().forEach(c -> coinsMap.put(c.getDenomination(), c.getCount()));

        for (Map.Entry<Integer, Integer> entry : coinsMap.entrySet()) {
            if (remainder <= 0)
                break;
            int quotient = remainder / entry.getKey();
            if (entry.getValue() >= quotient) {
                remainder = remainder % entry.getKey();
                remainderCoins.add(new Wallet(entry.getKey(), quotient));
            } else {
                int max = entry.getValue() * entry.getKey();
                remainder = remainder - max;
                remainderCoins.add(new Wallet(entry.getKey(), entry.getValue()));
            }
        }
        if (remainder > 0)
            throw new BadRequestException("Insufficient coins to withdraw");
        return remainderCoins;
    }

    /**
     * Update product details in DB
     *
     * @param products
     */
    private void updateProductDetails(List<Product> products) {
        List<Integer> productIds = products.stream().map(Product::getProdId).collect(Collectors.toList());
        Iterable<Product> productsFromDb = productRepository.findAllById(productIds);
        productsFromDb.forEach(product -> {
            Optional<Product> optionalProduct
                    = products.stream().filter(item -> product.getProdId().equals(item.getProdId())).findFirst();
            if (product.getQuantity() >= optionalProduct.get().getQuantity()) {
                product.setQuantity(product.getQuantity() - optionalProduct.get().getQuantity());
            } else {
                throw new BadRequestException("Invalid request");
            }
        });
        productRepository.saveAll(productsFromDb);
    }

    /**
     * Update wallet
     */
    private void updateWallet() {
        List<Wallet> coinsInDb = new ArrayList<>();
        walletRepository.findAll().forEach(coinsInDb::add);
        coinsInDb.forEach(coin -> {
            Optional<Wallet> wallet
                    = userAddedCoins.stream().filter(c -> c.getDenomination().equals(coin.getDenomination())).findFirst();
            coin.setCount(coin.getCount() + wallet.get().getCount());

        });
        walletRepository.saveAll(coinsInDb);
    }
    private void clearUserAddedCoins() {
        userAddedCoins = null;
    }

}
