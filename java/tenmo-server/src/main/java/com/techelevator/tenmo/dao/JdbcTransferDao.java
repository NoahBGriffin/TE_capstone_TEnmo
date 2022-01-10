package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.IllegalTransactionException;
import com.techelevator.tenmo.exception.TransferNotFoundException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Transfer addTransfer(Transfer transfer) throws IllegalTransactionException {
        Integer accountToId = transfer.getAccountToId();
        Integer accountFromId = transfer.getAccountFromId();
        BigDecimal amount = transfer.getAmount();

        if (accountToId.equals(accountFromId)) {
            throw new IllegalTransactionException();
        }

        String sqlGetTypeId = "SELECT transfer_type_id " +
                "FROM transfer_types " +
                "WHERE transfer_type_desc = ?";
        SqlRowSet transferTypeIdResult = jdbcTemplate.queryForRowSet(sqlGetTypeId, transfer.getTransferType());
        Integer transferTypeId = 0;
        if(transferTypeIdResult.next()) {
            transferTypeId = transferTypeIdResult.getInt("transfer_type_id");
        }


        String transferStatus = transferBalance(accountFromId, accountToId, amount);

        String sqlGetStatusId = "SELECT transfer_status_id " +
                "FROM transfer_statuses " +
                "WHERE transfer_status_desc = ?";

        SqlRowSet transferStatusIdResult = jdbcTemplate.queryForRowSet(sqlGetStatusId, transferStatus);

        Integer transferStatusId = 0;
        if (transferStatusIdResult.next()) {
            transferStatusId = transferStatusIdResult.getInt("transfer_status_id");
        }


        String sql = "INSERT INTO transfers(transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES(?, ?, ?, ?, ?) RETURNING transfer_id";

        Integer transferId = jdbcTemplate.queryForObject(sql, Integer.class, transferTypeId, transferStatusId, accountFromId, accountToId, amount);

        transfer.setTransferStatus(transferStatus);
        transfer.setTransferId(transferId);

        return transfer;
    }

    //TODO accountID vs userID problemmmmmm needs fixing
    public String transferBalance(int accountFromId, int accountToId, BigDecimal transferAmount) {

        String sql = "SELECT balance " + "FROM accounts " + "WHERE account_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountFromId);

        BigDecimal fromUserBalance = null;

        if (results.next()) {
            fromUserBalance = results.getBigDecimal("balance");
        }

        System.out.println();
        if (transferAmount.compareTo(fromUserBalance) == 1) { // if transferAmount is > fromUserBalance
            return "Rejected";
        }

        String sql2 = "UPDATE accounts " +
                "SET balance = balance - ? " +
                "WHERE account_id = ?";
        jdbcTemplate.update(sql2, transferAmount, accountFromId);
        String sql3 = "UPDATE accounts " +
                "SET balance = balance + ? " +
                "WHERE account_id = ?";
        jdbcTemplate.update(sql3, transferAmount, accountToId);

        return "Approved";
    }

    public List<Transfer> getAllTransfers(int userId) {

        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfers " +
                "JOIN accounts ON transfers.account_from = accounts.account_id OR transfers.account_to = accounts.account_id " +
                "WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);

        List<Transfer> allTransfers = new ArrayList<>();

        while (results.next()) {

            Transfer transfer = createTransfer(results);
            allTransfers.add(transfer);
        }

        return allTransfers;
    }

    public Transfer getTransfer(int transferId) throws TransferNotFoundException {

        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfers " +
                "WHERE transfer_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferId);
        Transfer transfer = null;
        if (result.next()) {
            transfer = createTransfer(result);
            return transfer;
        } else {
            throw new TransferNotFoundException();
        }
        //return transfer;
    }

    private Transfer createTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(results.getInt("transfer_id"));
        transfer.setAccountFromId(results.getInt("account_from"));
        transfer.setAccountToId(results.getInt("account_to"));
        transfer.setAmount(results.getBigDecimal("amount"));
        int transferStatusId = results.getInt("transfer_status_id");
        String transferStatusDesc = getTransferStatusDesc(transferStatusId);
        transfer.setTransferStatus(transferStatusDesc);
        int transferTypeId = results.getInt("transfer_type_id");
        String transferTypeDesc = getTransferTypeDesc(transferTypeId);
        transfer.setTransferType(transferTypeDesc);
        int accountFromId = results.getInt("account_from");
        String usernameFrom = getUsername(accountFromId);
        transfer.setUsernameFrom(usernameFrom);
        int accountToId = results.getInt("account_to");
        String usernameTo = getUsername(accountToId);
        transfer.setUsernameTo(usernameTo);

        return transfer;

    }

    private String getTransferTypeDesc(int transferTypeId) {
        String sql = "SELECT transfer_type_desc FROM transfer_types WHERE transfer_type_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferTypeId);

        String transferTypeDesc = "";
        if (result.next()) {
            transferTypeDesc = result.getString("transfer_type_desc");
        }
        return transferTypeDesc;
    }

    private String getTransferStatusDesc(int transferStatusId) {

        String sql = "SELECT transfer_status_desc FROM transfer_statuses WHERE transfer_status_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferStatusId);

        String transferStatusDesc = "";

        if (result.next()) {
            transferStatusDesc = result.getString("transfer_status_desc");
        }

        return transferStatusDesc;
    }

    private String getUsername(int accountId) {

        String sql = "SELECT username " + "FROM users " + "JOIN accounts ON users.user_id = accounts.user_id " + "WHERE account_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, accountId);

        String username = "";

        if (result.next()) {
            username = result.getString("username");
        }

        return username;
    }

    private int getUserId(String userName) {
        String sql = "SELECT user_id FROM users WHERE username = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userName);
        int id = 0;
        if (result.next()) {
            id = result.getInt("user_id");
        }
        return id;
    }

}
