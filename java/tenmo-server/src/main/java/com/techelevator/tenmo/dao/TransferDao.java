package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.IllegalTransactionException;
import com.techelevator.tenmo.exception.TransferNotFoundException;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    public String transferBalance(int fromUserId, int toUserId, BigDecimal transferAmount);

    public Transfer addTransfer(Transfer transfer) throws IllegalTransactionException;

    public List<Transfer> getAllTransfers(int userId);

    public Transfer getTransfer(int transferId) throws TransferNotFoundException;

}
