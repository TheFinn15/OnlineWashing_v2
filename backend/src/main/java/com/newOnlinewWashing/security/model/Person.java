package com.newOnlinewWashing.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.newOnlinewWashing.models.Machine;
import com.newOnlinewWashing.models.Wallet;
import com.sun.istack.Nullable;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "machine_id")
    private List<Machine> machine;
    @JsonIgnoreProperties({"hibernateLazyInitializer"})
    @OneToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;
    private String fName;
    private String sName;
    private String pwd;
    private String login;
    private String email;
    @Nullable
    private String phone;
    private String lastUpdateRow;
    @Column(columnDefinition = "char(10000) default 'data:image/png;base64,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'")
    private String avatar;

    @JsonIgnore
    @Column(name = "activated")
    private boolean activated;

    @ManyToMany
    @JoinTable(
            name = "USER_AUTHORITY",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "AUTHORITY_NAME", referencedColumnName = "NAME")})
    @BatchSize(size = 20)
    private Set<Authority> authorities = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Machine> getMachine() {
        return machine;
    }

    public void setMachine(List<Machine> machine) {
        this.machine = machine;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLastUpdateRow() {
        return lastUpdateRow;
    }

    public void setLastUpdateRow(String lastUpdateRow) {
        this.lastUpdateRow = lastUpdateRow;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }
}