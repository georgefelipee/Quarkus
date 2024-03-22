package org.acme.hibernate.orm.panache.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateBankDTO {
    @NotBlank(message = "Nome do banco é obrigatório")
    private String nameBank;

    public String getNameBank() {
        return nameBank;
    }

    public void setNameBank(String nameBank) {
        this.nameBank = nameBank;
    }
}
