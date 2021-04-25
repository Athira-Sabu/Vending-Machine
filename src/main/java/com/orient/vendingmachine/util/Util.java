package com.orient.vendingmachine.util;

import com.orient.vendingmachine.model.Coin;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Util {

    public static List<Integer> getCoinValues() {
        return Stream.of(Coin.values())
                .map(Coin::getCoinValue)
                .collect(Collectors.toList());
    }
}
