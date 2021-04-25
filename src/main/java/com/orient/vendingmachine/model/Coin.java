package com.orient.vendingmachine.model;

public enum Coin {

    COIN(1),TWO(2),FIVE(5),TEN(10);
    private int coinValue;

    Coin(int i) {
        this.coinValue = i;
    }
    public int getCoinValue(){
        return this.coinValue;
    }
}
