package com.orient.vendingmachine.mock;

import com.orient.vendingmachine.model.Wallet;

import java.util.ArrayList;
import java.util.List;

public class CoinMock {
    public static List<Wallet> getCoins() {

        Wallet wallet = new Wallet();
        wallet.setDenomination(2);
        wallet.setCount(3);

        Wallet wallet1 = new Wallet();
        wallet1.setDenomination(5);
        wallet1.setCount(5);
        List<Wallet> wallets = new ArrayList<>();
        wallets.add(wallet);
        wallets.add(wallet1);
        return wallets;
    }
}
