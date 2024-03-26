package org.acme.hibernate.orm.panache.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import org.acme.hibernate.orm.panache.User;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
public class Account  extends PanacheEntity {
    @Column(nullable = false)
    public int balanceInCents;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public User user_id;

    @ManyToOne
    @JoinColumn(name = "agency_id", nullable = false)
    public Agency agency_id;

    public Agency getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(Agency agency_id) {
        this.agency_id = agency_id;
    }

    public User getUser_id() {
        return user_id;
    }

    public void setUser_id(User user_id) {
        this.user_id = user_id;
    }

    public BigDecimal getBalance() {
        return BigDecimal.valueOf(balanceInCents, 2); // Convertendo centavos para BigDecimal
    }

    public void setBalance(BigDecimal balance) {
        this.balanceInCents = balance.movePointRight(2).intValue(); // Convertendo BigDecimal para centavos
    }
}
