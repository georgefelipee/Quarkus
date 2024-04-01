package org.acme.hibernate.orm.panache.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class DepositAccountDTO {

    @NotNull(message = "Você não informou a quantidade! ")
    public BigDecimal balanceInCents;


    public BigDecimal getBalance() {
        return balanceInCents; // Convertendo centavos para BigDecimal
    }

    public void setBalance(BigDecimal balance) {
        this.balanceInCents = balance; // Convertendo BigDecimal para centavos
    }

}
