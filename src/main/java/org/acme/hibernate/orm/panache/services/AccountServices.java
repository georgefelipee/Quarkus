package org.acme.hibernate.orm.panache.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.acme.hibernate.orm.panache.User;
import org.acme.hibernate.orm.panache.dto.AccountDTO;
import org.acme.hibernate.orm.panache.dto.CreateAccountDTO;
import org.acme.hibernate.orm.panache.enums.AccountType;
import org.acme.hibernate.orm.panache.exceptions.ErrorResponseEdit;
import org.acme.hibernate.orm.panache.models.Account;
import org.acme.hibernate.orm.panache.models.Agency;
import org.acme.hibernate.orm.panache.models.TypeAccount;

@ApplicationScoped
public class AccountServices {


    public AccountDTO createAccount(CreateAccountDTO accountRequestDTO){
        Account account = new Account();
        TypeAccount typeAccount = new TypeAccount();

        User user = User.findById(accountRequestDTO.getUser_id());
        if(user == null){
            throw new NotFoundException("Usuario não encontrado");
        }
        account.setUser_id(user);

        Agency agency = Agency.findById(accountRequestDTO.getAgency_id());
        if(agency == null){
            throw new NotFoundException("Agência não encontrada");
        }
        account.setAgency_id(agency);

        if (accountRequestDTO.getRole() == 1) {
            typeAccount.setAccountType(AccountType.SPECIAL);
            typeAccount.setHasCreditCard(accountRequestDTO.isHasCreditCard());

        } else if(accountRequestDTO.getRole() == 2) {
            typeAccount.setAccountType(AccountType.PREMIUM);
            typeAccount.setHasCreditCard(accountRequestDTO.isHasCreditCard());
            typeAccount.setHasLis(accountRequestDTO.isHasLis());
        }

        typeAccount.persist();
        account.setTypeAccount_id(typeAccount);
        account.persist();

        return new AccountDTO(account);

    }
}
