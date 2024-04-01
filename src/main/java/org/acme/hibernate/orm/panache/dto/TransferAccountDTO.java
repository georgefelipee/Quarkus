package org.acme.hibernate.orm.panache.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class TransferAccountDTO {

    @NotNull(message = "O usuário de origem é obrigatório")
    public int user_origin_id;
    @NotNull(message = "O usuário de destino é obrigatório")
    public int user_destiny_id;
    @NotNull(message = "O valor é obrigatório")
    public BigDecimal trasnferValue;

    public int getUser_origin_id() {
        return user_origin_id;
    }

    public void setUser_origin_id(int user_origin_id) {
        this.user_origin_id = user_origin_id;
    }

    public int getUser_destiny_id() {
        return user_destiny_id;
    }

    public void setUser_destiny_id(int user_destiny_id) {
        this.user_destiny_id = user_destiny_id;
    }

    public BigDecimal getTrasnferValue() {
        return trasnferValue;
    }

    public void setTrasnferValue(BigDecimal trasnferValue) {
        this.trasnferValue = trasnferValue;
    }
}
