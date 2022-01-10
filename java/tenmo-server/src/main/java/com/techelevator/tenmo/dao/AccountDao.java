package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.InsufficientFundsException;
import com.techelevator.tenmo.exception.UserNotFoundException;
import com.techelevator.tenmo.model.UserWeb;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    public BigDecimal retrieveBalance(int userId);
    public int getAccountId(int userId) throws UserNotFoundException;

}
