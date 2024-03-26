package org.acme.hibernate.orm.panache.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "banks")
public class Bank extends PanacheEntity {

    @Column(length = 255, nullable = false, unique = true)
    public String nameBank;

    @OneToMany(mappedBy = "bank_id", cascade = CascadeType.REMOVE)
    @JsonIgnore
    public List<Agency> agencies;

    public String getNameBank() {
        return nameBank;
    }

    public void setNameBank(String nameBank) {
        this.nameBank = nameBank;
    }
}
