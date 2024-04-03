package org.acme.hibernate.orm.panache.dto;

import org.acme.hibernate.orm.panache.models.Agency;

public class AgencyDTO {
    private Long id;
    private String nameAgency;
    private BankDTO bank;

    // Construtor
    public AgencyDTO(Agency agency) {
        this.id = agency.getId();
        this.nameAgency = agency.getNameAgency();
        this.bank = new BankDTO(agency.getBank());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameAgency() {
        return nameAgency;
    }

    public void setNameAgency(String nameAgency) {
        this.nameAgency = nameAgency;
    }

    public BankDTO getBank() {
        return bank;
    }

    public void setBank(BankDTO bank) {
        this.bank = bank;
    }
}
