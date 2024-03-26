package org.acme.hibernate.orm.panache.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.acme.hibernate.orm.panache.models.Bank;

public class CreateAgencyDTO {

    @NotBlank(message = "Nome da agência é obrigatório")
    private String nameAgency;

    @NotNull(message = "Banco é obrigatório")
    private int bank_id;

    public String getNameAgency() {
        return nameAgency;
    }

    public void setNameAgency(String nameAgency) {
        this.nameAgency = nameAgency;
    }

    public int getBank_id() {
        return bank_id;
    }

    public void setBank_id(int bank_id) {
        this.bank_id = bank_id;
    }
}
