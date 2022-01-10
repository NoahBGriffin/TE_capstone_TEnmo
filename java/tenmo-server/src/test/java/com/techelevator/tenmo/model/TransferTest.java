package com.techelevator.tenmo.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TransferTest {

    @Test
    public void transfer_gets_work() {
        Transfer transfer = new Transfer();

        transfer.setTransferId(1);
        transfer.setTransferType("Send");
        transfer.setTransferStatus("Rejected");
        transfer.setAccountFromId(1);
        transfer.setAccountToId(2);
        transfer.setUsernameFrom("user1");
        transfer.setUsernameTo("user2");
        transfer.setAmount(new BigDecimal("10.00"));

        assertEquals(1, transfer.getTransferId());
        assertEquals("Send", transfer.getTransferType());
        assertEquals("Rejected", transfer.getTransferStatus());
        assertEquals(1, transfer.getAccountFromId());
        assertEquals(2, transfer.getAccountToId());
        assertEquals("user1", transfer.getUsernameFrom());
        assertEquals("user2", transfer.getUsernameTo());
        assertEquals(new BigDecimal("10.00"), transfer.getAmount());
    }

}