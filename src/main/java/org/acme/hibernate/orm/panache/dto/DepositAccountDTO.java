package org.acme.hibernate.orm.panache.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class DepositAccountDTO {

    @NotNull(message = "Você não informou a quantidade! ")
    public int balanceInCents;


    public BigDecimal getBalance() {
        return BigDecimal.valueOf(balanceInCents, 2); // Convertendo centavos para BigDecimal
    }

    public void setBalance(BigDecimal balance) {
        this.balanceInCents = balance.movePointRight(2).intValue(); // Convertendo BigDecimal para centavos
    }

}
