package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.InsufficientFundsException;
import com.techelevator.tenmo.exception.UserNotFoundException;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserWeb;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal retrieveBalance(int userId) {

        String sql = "SELECT balance " + "FROM accounts " + "WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);

        BigDecimal balance = null;

        if (results.next()) {
            balance = results.getBigDecimal("balance");
        }

        return balance;
    }

    public int getAccountId(int userId) throws UserNotFoundException {
        String sql = "SELECT account_id " + "FROM accounts " + "WHERE user_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);
        int accountId = 0;
        if (result.next()) {
            accountId = result.getInt("account_id");
        }
        else {
            throw new UserNotFoundException();
        }
        return accountId;
    }

}
