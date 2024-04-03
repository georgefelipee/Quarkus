package org.acme.hibernate.orm.panache.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class DepositAccountDTO {

    @NotNull(message = "Você não informou a quantidade! ")
    @Min(value = 1, message = "O valor deve ser maior que zero! ")
    public BigDecimal balanceInReal;


    public BigDecimal getBalance() {
        return balanceInReal;
    }

    public void setBalance(BigDecimal balance) {
        this.balanceInReal = balance;
    }

}
