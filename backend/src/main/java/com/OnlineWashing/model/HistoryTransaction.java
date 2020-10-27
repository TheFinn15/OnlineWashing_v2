package com.OnlineWashing.model;

import javax.persistence.*;

@Entity
@Table
public class HistoryTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long sum;
    private String date;
    private String lastUpdateRow;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSum() {
        return sum;
    }

    public void setSum(Long sum) {
        this.sum = sum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLastUpdateRow() {
        return lastUpdateRow;
    }

    public void setLastUpdateRow(String lastUpdateRow) {
        this.lastUpdateRow = lastUpdateRow;
    }
}
