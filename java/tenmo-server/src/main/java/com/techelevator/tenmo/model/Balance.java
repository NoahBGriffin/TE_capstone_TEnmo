package com.techelevator.tenmo.model;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

public class Balance {

    // property:
    @Min(value=0)
    private BigDecimal balance;

    // getter:
    public BigDecimal getBalance() {
        return balance;
    }

    // setter:
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

}
