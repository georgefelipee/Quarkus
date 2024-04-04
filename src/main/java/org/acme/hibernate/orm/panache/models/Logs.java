package org.acme.hibernate.orm.panache.models;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "logs")
public class Logs extends PanacheEntity {

    @Column(nullable = false)
    private Long originBankId;
    @Column(nullable = false)
    private Long originAgencyId;
    @Column(nullable = false)
    private Long originAccountId;

    @Column(nullable = false)
    private Long destinyBankId;
    @Column(nullable = false)
    private Long destinyAgencyId;
    @Column(nullable = false)
    private Long destinyAccountId;

    @Column(nullable = false)
    private BigDecimal value;

    @Column(nullable = false)
    private LocalDateTime date;

    public Long getOriginBankId() {
        return originBankId;
    }

    public void setOriginBankId(Long originBankId) {
        this.originBankId = originBankId;
    }

    public Long getOriginAgencyId() {
        return originAgencyId;
    }

    public void setOriginAgencyId(Long originAgencyId) {
        this.originAgencyId = originAgencyId;
    }

    public Long getOriginAccountId() {
        return originAccountId;
    }

    public void setOriginAccountId(Long originAccountId) {
        this.originAccountId = originAccountId;
    }

    public Long getDestinyBankId() {
        return destinyBankId;
    }

    public void setDestinyBankId(Long destinyBankId) {
        this.destinyBankId = destinyBankId;
    }

    public Long getDestinyAgencyId() {
        return destinyAgencyId;
    }

    public void setDestinyAgencyId(Long destinyAgencyId) {
        this.destinyAgencyId = destinyAgencyId;
    }

    public Long getDestinyAccountId() {
        return destinyAccountId;
    }

    public void setDestinyAccountId(Long destinyAccountId) {
        this.destinyAccountId = destinyAccountId;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
