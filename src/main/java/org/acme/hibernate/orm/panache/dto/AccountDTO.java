package org.acme.hibernate.orm.panache.dto;

import org.acme.hibernate.orm.panache.models.Account;

import java.math.BigDecimal;

public class AccountDTO {

    private Long id;
    private BigDecimal balanceInCents;
    private UserDTO user;
    private AgencyDTO agency;
    private BigDecimal balance;

    // Construtor
    public AccountDTO(Account account) {
        this.id = account.getId();
        this.balanceInCents = account.getBalance();
        this.user = new UserDTO(account.getUser_id().id,account.getUser_id().getName(), account.getUser_id().getUsername(), account.getUser_id().getEmail());
        this.agency = new AgencyDTO(account.getAgency());
        this.balance = account.getBalance();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBalanceInCents() {
        return balanceInCents;
    }

    public void setBalanceInCents(BigDecimal balanceInCents) {
        this.balanceInCents = balanceInCents;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public AgencyDTO getAgency() {
        return agency;
    }

    public void setAgency(AgencyDTO agency) {
        this.agency = agency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
