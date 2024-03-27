package org.acme.hibernate.orm.panache.dto;

import jakarta.validation.constraints.NotNull;
import org.acme.hibernate.orm.panache.enums.AccountType;

public class CreateAccountDTO {

    @NotNull(message = "O usuario é obrigatório")
    public int user_id;
    @NotNull(message = "A agência é obrigatória")
    public int agency_id;

    @NotNull(message = "O tipo de conta é obrigatória")
    public int accountType;

    public boolean hasCreditCard;

    public boolean hasLis;

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public boolean isHasCreditCard() {
        return hasCreditCard;
    }

    public void setHasCreditCard(boolean hasCreditCard) {
        this.hasCreditCard = hasCreditCard;
    }

    public boolean isHasLis() {
        return hasLis;
    }

    public void setHasLis(boolean hasLis) {
        this.hasLis = hasLis;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(int agency_id) {
        this.agency_id = agency_id;
    }

    public int getRole() {
        return accountType;
    }

    public void setRole(int accountType) {
        this.accountType = accountType;
    }
}
