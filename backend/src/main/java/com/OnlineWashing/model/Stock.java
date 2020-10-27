package com.OnlineWashing.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "stock")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String sponsor;
    private Date lastTerm;
    private int discount;
    private String lastUpdateRow;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public Date getLastTerm() {
        return lastTerm;
    }

    public void setLastTerm(Date lastTerm) {
        this.lastTerm = lastTerm;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getLastUpdateRow() {
        return lastUpdateRow;
    }

    public void setLastUpdateRow(String lastUpdateRow) {
        this.lastUpdateRow = lastUpdateRow;
    }
}
