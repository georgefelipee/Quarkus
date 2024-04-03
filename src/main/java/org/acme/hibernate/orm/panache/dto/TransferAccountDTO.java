package org.acme.hibernate.orm.panache.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class TransferAccountDTO {

    @NotNull(message = "O usuário de origem é obrigatório")
    public int user_account_origin_id;
    @NotNull(message = "O usuário de destino é obrigatório")
    public int user_account_destiny_id;
    @NotNull(message = "O valor é obrigatório")
    @Min(value = 1, message = "O valor deve ser maior que zero")
    public BigDecimal trasnferValue;

    public int getUser_account_origin_id() {
        return user_account_origin_id;
    }

    public void setUser_account_origin_id(int user_account_origin_id) {
        this.user_account_origin_id = user_account_origin_id;
    }

    public int getUser_account_destiny_id() {
        return user_account_destiny_id;
    }

    public void setUser_account_destiny_id(int user_account_destiny_id) {
        this.user_account_destiny_id = user_account_destiny_id;
    }

    public BigDecimal getTrasnferValue() {
        return trasnferValue;
    }

    public void setTrasnferValue(BigDecimal trasnferValue) {
        this.trasnferValue = trasnferValue;
    }
}
