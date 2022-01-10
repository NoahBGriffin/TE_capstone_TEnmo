package com.techelevator.tenmo.model;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.jta.UserTransactionAdapter;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BalanceTest {

    @Test
    public void test_balance_instantiation() {
        Balance balance = new Balance();
        balance.setBalance(new BigDecimal("10.00"));
        BigDecimal expectedResult = new BigDecimal("10.00");
        assertEquals(expectedResult, balance.getBalance());
    }

}