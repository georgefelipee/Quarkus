package org.acme.hibernate.orm.panache.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "premium_accounts")
public class PremiumAccount extends PanacheEntity {

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    public Account account_id;

    @Column(name = "has_credit_card")
    private boolean hasCreditCard;

    @Column(name = "credit_card_balance")
    private BigDecimal creditCardBalance;

    @Column(name = "has_lis")
    private boolean hasLis;

    @Column(name = "lis_balance")
    private BigDecimal lisBalance;


}
