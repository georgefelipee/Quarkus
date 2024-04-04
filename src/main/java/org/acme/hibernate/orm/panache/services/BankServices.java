package org.acme.hibernate.orm.panache.services;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.hibernate.orm.panache.dto.BankDTO;
import org.acme.hibernate.orm.panache.dto.CreateBankDTO;
import org.acme.hibernate.orm.panache.models.Bank;

@ApplicationScoped
public class BankServices {

    public Bank createBank(CreateBankDTO bankDTO){
        Bank bank = new Bank();
        bank.setNameBank(bankDTO.getNameBank());
        bank.persist();

        return bank;
    }
}
