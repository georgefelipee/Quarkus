package org.acme.hibernate.orm.panache.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import org.acme.hibernate.orm.panache.enums.AccountType;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "type_accounts")
public class TypeAccount extends PanacheEntity{

    @OneToMany(mappedBy = "typeAccount_id",cascade = CascadeType.ALL)
    public List<Account> account_id;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @Column(name = "has_credit_card")
    private boolean hasCreditCard;

    @Column(name = "credit_card_balance")
    private BigDecimal creditCardBalance;

    @Column(name = "has_lis")
    private boolean hasLis;

    @Column(name = "lis_balance")
    private BigDecimal lisBalance;

    public List<Account> getAccount_id() {
        return account_id;
    }

    public void setAccount_id(List<Account> account_id) {
        this.account_id = account_id;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public boolean isHasCreditCard() {
        return hasCreditCard;
    }

    public void setHasCreditCard(boolean hasCreditCard) {
        this.hasCreditCard = hasCreditCard;
    }

    public BigDecimal getCreditCardBalance() {
        return creditCardBalance;
    }

    public void setCreditCardBalance(BigDecimal creditCardBalance) {
        this.creditCardBalance = creditCardBalance;
    }

    public boolean isHasLis() {
        return hasLis;
    }

    public void setHasLis(boolean hasLis) {
        this.hasLis = hasLis;
    }

    public BigDecimal getLisBalance() {
        return lisBalance;
    }

    public void setLisBalance(BigDecimal lisBalance) {
        this.lisBalance = lisBalance;
    }
}
