package com.OnlineWashing.model;

import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;

@Entity
@Table(name = "machine")
public class Machine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "stock_id", nullable = true)
    private Stock stock;
    private String name;
    private Long capacity;
    @Column(columnDefinition = "bigint default 0")
    private Long percentReady;
    @Column(length = 500)
    private String description;
    private String status;
    private Long price;
    @Column(length = 10000)
    private String img;
    private String lastUpdateRow;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCapacity() {
        return capacity;
    }

    public void setCapacity(Long capacity) {
        this.capacity = capacity;
    }

    public Long getPercentReady() {
        return percentReady;
    }

    public void setPercentReady(Long percentReady) {
        this.percentReady = percentReady;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLastUpdateRow() {
        return lastUpdateRow;
    }

    public void setLastUpdateRow(String lastUpdateRow) {
        this.lastUpdateRow = lastUpdateRow;
    }
}
