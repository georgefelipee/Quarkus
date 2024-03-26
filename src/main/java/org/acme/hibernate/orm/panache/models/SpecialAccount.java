package org.acme.hibernate.orm.panache.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "special_accounts")
public class SpecialAccount  extends PanacheEntity {

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    public Account account_id;

    @Column(name = "has_credit_card")
    private boolean has_credit_card;

    @Column(name = "credit_card_balance")
    private BigDecimal credit_card_balance;

    public boolean isHas_credit_card() {
        return has_credit_card;
    }

    public void setHas_credit_card(boolean has_credit_card) {
        this.has_credit_card = has_credit_card;
    }

    public BigDecimal getCredit_card_balance() {
        return credit_card_balance;
    }

    public void setCredit_card_balance(BigDecimal credit_card_balance) {
        this.credit_card_balance = credit_card_balance;
    }
}
