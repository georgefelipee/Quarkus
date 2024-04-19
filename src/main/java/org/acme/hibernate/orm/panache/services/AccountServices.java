package org.acme.hibernate.orm.panache.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.acme.hibernate.orm.panache.User;
import org.acme.hibernate.orm.panache.dto.AccountDTO;
import org.acme.hibernate.orm.panache.dto.CreateAccountDTO;
import org.acme.hibernate.orm.panache.dto.DepositAccountDTO;
import org.acme.hibernate.orm.panache.dto.TransferAccountDTO;
import org.acme.hibernate.orm.panache.enums.AccountType;
import org.acme.hibernate.orm.panache.exceptions.ErrorResponseEdit;
import org.acme.hibernate.orm.panache.models.Account;
import org.acme.hibernate.orm.panache.models.Agency;
import org.acme.hibernate.orm.panache.models.Logs;
import org.acme.hibernate.orm.panache.models.TypeAccount;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static io.quarkus.hibernate.orm.panache.PanacheEntity_.id;

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

        if(accountRequestDTO.getRole() == 0){
            typeAccount.setAccountType(AccountType.NORMAL);
        }else if (accountRequestDTO.getRole() == 1) {
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

    public void transferAccount(@Valid TransferAccountDTO transferRequestDTO) {
        Account accountOrigin = Account.findById(transferRequestDTO.getUser_account_origin_id());

        if (accountOrigin == null) {
            throw new NotFoundException("Conta de origem não encontrada!");
        }

        Account accountDestiny = Account.findById(transferRequestDTO.getUser_account_destiny_id());
        if (accountDestiny == null) {
            throw new NotFoundException("Conta de destino não encontrada!");
        }

        BigDecimal transferValue = transferRequestDTO.getTrasnferValue().multiply(BigDecimal.valueOf(100));
        BigDecimal balanceOrigin = accountOrigin.getBalance();

        if (balanceOrigin.compareTo(transferValue) < 0) {
            throw new BadRequestException("Saldo insuficiente!");
        }
        BigDecimal newBalanceOrigin = balanceOrigin.subtract(transferValue);
        accountOrigin.setBalance(newBalanceOrigin);

        BigDecimal balanceDestiny = accountDestiny.getBalance();
        BigDecimal newBalanceDestiny = balanceDestiny.add(transferValue);
        accountDestiny.setBalance(newBalanceDestiny);

        createLogsTransferAccount(accountOrigin, accountDestiny, transferValue);
    }

    public void depositAccount(Long idAccount, DepositAccountDTO depositRequestDTO) {
        //Recebo em REAL R$1,00
        Account account = Account.findById(idAccount);
        if(account == null){
           throw new NotFoundException("Conta não encontrada!");
        }
        // Convertendo o valor do depósito para centavos
        BigDecimal depositAmount = depositRequestDTO.getBalance().multiply(BigDecimal.valueOf(100));

        // Adicionando o valor do depósito ao saldo da conta
        BigDecimal balanceInCents = account.getBalance();
        BigDecimal newBalanceInCents = balanceInCents.add(depositAmount);

        // Atualizando o saldo da conta
        account.setBalance(newBalanceInCents);
    }

    public void createLogsTransferAccount(Account accountOrigin, Account accountDestiny, BigDecimal transferValue) {
        Logs logs = new Logs();
        logs.setOriginBankId(accountOrigin.getAgency_id().getBank_id().id);
        logs.setOriginAgencyId(accountOrigin.getAgency_id().id);
        logs.setOriginAccountId(accountOrigin.getId());

        logs.setDestinyBankId(accountDestiny.getAgency().getBank_id().id);
        logs.setDestinyAgencyId(accountDestiny.getAgency_id().getId());
        logs.setDestinyAccountId(accountDestiny.getId());

        logs.setDate(LocalDateTime.now());

        logs.setValue(transferValue);

        logs.persist();
    }
}
