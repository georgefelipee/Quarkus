package org.acme.hibernate.orm.panache.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "agencies")
public class Agency  extends PanacheEntity {

    @Column(length = 255, nullable = false, unique = true)
    public String nameAgency;

    @ManyToOne
    @JoinColumn(name = "bank_id", nullable = false)
    public Bank bank_id;


    @JsonIgnore
    @OneToMany(mappedBy = "agency_id",cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Account> accounts;


    public String getNameAgency() {
        return nameAgency;
    }

    public void setNameAgency(String nameAgency) {
        this.nameAgency = nameAgency;
    }

    public Bank getBank_id() {
        return bank_id;
    }

    public void setBank_id(Bank bank_id) {
        this.bank_id = bank_id;
    }
}
