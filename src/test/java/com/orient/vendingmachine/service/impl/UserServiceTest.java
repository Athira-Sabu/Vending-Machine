package com.orient.vendingmachine.service.impl;

import com.orient.vendingmachine.exception.BadRequestException;
import com.orient.vendingmachine.mock.CoinMock;
import com.orient.vendingmachine.mock.ProductMock;
import com.orient.vendingmachine.model.Product;
import com.orient.vendingmachine.model.Wallet;
import com.orient.vendingmachine.repository.ProductRepository;
import com.orient.vendingmachine.repository.WalletRepository;
import com.orient.vendingmachine.service.IUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    IUserService iUserService = new UserService();

    @Mock
    private ProductRepository productRepository;

    @Mock
    private WalletRepository walletRepository;

    @Test
    public void addInvalidCoinsTest() {
        List<Wallet> wallets = CoinMock.getCoins();
        wallets.get(0).setDenomination(3);
        Assertions.assertThrows(BadRequestException.class, () ->
                iUserService.setCoins(wallets));
    }

    @Test
    public void addCoinsTest() {
        List<Wallet> wallets = CoinMock.getCoins();
        iUserService.setCoins(wallets);
        List<Wallet> userAddedCoins = (List<Wallet>) ReflectionTestUtils.getField(iUserService, "userAddedCoins");
        assertEquals(wallets, userAddedCoins);
    }

    @Test
    public void getProductsTest() {
        List<Product> products = ProductMock.getProducts();
        when(productRepository.findAll()).thenReturn(products);
        List<Product> productListExpected = iUserService.getProductList();
        assertEquals(products,productListExpected);

    }

    @Test
    public void cancelRequestTest() {
        iUserService.cancelRequest();
        Object userAddedCoins = ReflectionTestUtils.getField(iUserService, "userAddedCoins");
        assertEquals(userAddedCoins, null);
    }

    @Test
    public void productSelectionInvalidRequest() {
        List<Wallet> wallets = CoinMock.getCoins();
        ReflectionTestUtils.setField(iUserService, "userAddedCoins", wallets);
        List<Product> products = ProductMock.getProducts();
        Assertions.assertThrows(BadRequestException.class, () ->
                iUserService.selectProducts(products));
    }

    @Test
    public void selectProducts() {
        List<Wallet> wallets = CoinMock.getCoins();
        List<Product> products = ProductMock.getProducts();
        wallets.get(0).setCount(80);
        ReflectionTestUtils.setField(iUserService, "userAddedCoins", wallets);
        when(productRepository.findAllById(anyList())).thenReturn(products);
        when(walletRepository.findAll()).thenReturn(wallets);
        iUserService.selectProducts(products);
        verify(productRepository, times(1)).saveAll(anyList());
        verify(walletRepository, times(1)).saveAll(anyList());
        Object userAddedCoins = ReflectionTestUtils.getField(iUserService, "userAddedCoins");
        assertEquals(userAddedCoins, null);
    }


}