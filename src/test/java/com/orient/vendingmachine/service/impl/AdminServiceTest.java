package com.orient.vendingmachine.service.impl;

import com.orient.vendingmachine.exception.BadRequestException;
import com.orient.vendingmachine.mock.CoinMock;
import com.orient.vendingmachine.mock.ProductMock;
import com.orient.vendingmachine.model.Product;
import com.orient.vendingmachine.model.Wallet;
import com.orient.vendingmachine.repository.ProductRepository;
import com.orient.vendingmachine.repository.WalletRepository;
import com.orient.vendingmachine.service.IAdminService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @InjectMocks
    private IAdminService iAdminService = new AdminService();

    @Mock
    private ProductRepository productRepository;

    @Mock
    private WalletRepository walletRepository;

    @Test
    public void addProductsWithExceedLimitTest() {
        when(productRepository.getQuantitiesCount()).thenReturn(120);
        Assertions.assertThrows(BadRequestException.class, () ->
                iAdminService.addProducts(ProductMock.getProducts()));
    }

    @Test
    public void addProductsTest() {
        when(productRepository.getQuantitiesCount()).thenReturn(10);
        List<Product> products = ProductMock.getProducts();
        iAdminService.addProducts(products);
        verify(productRepository, Mockito.times(1)).saveAll(products);
    }

    @Test
    public void addInvalidCoinsTest() {
        List<Wallet> wallets = CoinMock.getCoins();
        wallets.get(0).setDenomination(3);
        Assertions.assertThrows(BadRequestException.class, () ->
                iAdminService.addCoins(wallets));
    }

    @Test
    public void addExceedsCoinsLimitTest() {
        List<Wallet> wallets = CoinMock.getCoins();
        when(walletRepository.findAll()).thenReturn(wallets);
        wallets.get(0).setCount(1000);
        Assertions.assertThrows(BadRequestException.class, () ->
                iAdminService.addCoins(wallets));
    }

    @Test
    public void addCoins() {
        List<Wallet> wallets = CoinMock.getCoins();
        when(walletRepository.findAll()).thenReturn(wallets);
        iAdminService.addCoins(wallets);
        verify(walletRepository, Mockito.times(1)).saveAll(wallets);

    }
    @Test
    public void debitInvalidCoinsTest() {
        List<Wallet> wallets = CoinMock.getCoins();
        when(walletRepository.findAll()).thenReturn(CoinMock.getCoins());
        wallets.get(0).setDenomination(3);
        Assertions.assertThrows(BadRequestException.class, () ->
                iAdminService.debitCoins(wallets));
    }

    @Test
    public void debitCoins() {
        List<Wallet> wallets = CoinMock.getCoins();
        when(walletRepository.findAll()).thenReturn(wallets);
        iAdminService.debitCoins(wallets);
        verify(walletRepository, Mockito.times(1)).saveAll(wallets);

    }
}