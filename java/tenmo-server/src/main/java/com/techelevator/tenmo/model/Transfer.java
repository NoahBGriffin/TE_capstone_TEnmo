package com.techelevator.tenmo.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class Transfer {

    private Integer transferId;
    @NotBlank(message="Transfer Type cannot be blank")
    private String transferType;
    private String transferStatus;
    @NotNull(message="Account from cannot be null")
    private Integer accountFromId;
    @NotNull(message="Account to cannot be null")
    private Integer accountToId;
    @Min(value=0, message="amount cannot be negative")
    private String usernameFrom;
    private String usernameTo;
    private BigDecimal amount;

    public Integer getTransferId() {
        return transferId;
    }

    public void setTransferId(Integer transferId) {
        this.transferId = transferId;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public Integer getAccountFromId() {
        return accountFromId;
    }

    public void setAccountFromId(Integer accountFromId) {
        this.accountFromId = accountFromId;
    }

    public Integer getAccountToId() {
        return accountToId;
    }

    public void setAccountToId(Integer accountToId) {
        this.accountToId = accountToId;
    }

    public String getUsernameFrom() {
        return usernameFrom;
    }

    public void setUsernameFrom(String usernameFrom) {
        this.usernameFrom = usernameFrom;
    }

    public String getUsernameTo() {
        return usernameTo;
    }

    public void setUsernameTo(String usernameTo) {
        this.usernameTo = usernameTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
