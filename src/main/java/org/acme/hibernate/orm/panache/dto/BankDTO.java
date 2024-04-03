package org.acme.hibernate.orm.panache.dto;

import org.acme.hibernate.orm.panache.models.Bank;

public class BankDTO {
    private Long id;
    private String nameBank;

    // Construtor
    public BankDTO(Bank bank) {
        this.id = bank.getId();
        this.nameBank = bank.getNameBank();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameBank() {
        return nameBank;
    }

    public void setNameBank(String nameBank) {
        this.nameBank = nameBank;
    }
}
