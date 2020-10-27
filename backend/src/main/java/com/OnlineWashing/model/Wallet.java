package com.OnlineWashing.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long balance;
    @OneToMany
    @JoinColumn(name = "history_id")
    private List<HistoryTransaction> historyTransactions;
    private String lastUpdateRow;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public List<HistoryTransaction> getHistoryTransactions() {
        return historyTransactions;
    }

    public void setHistoryTransactions(List<HistoryTransaction> historyTransactions) {
        this.historyTransactions = historyTransactions;
    }

    public String getLastUpdateRow() {
        return lastUpdateRow;
    }

    public void setLastUpdateRow(String lastUpdateRow) {
        this.lastUpdateRow = lastUpdateRow;
    }
}
