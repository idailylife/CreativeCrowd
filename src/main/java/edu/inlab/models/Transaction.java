package edu.inlab.models;

import javax.persistence.*;

/**
 * Created by inlab-dell on 2016/6/6.
 */
@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "usertask_id")
    private Integer usertaskId;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "pay_method", nullable = false)
    private String payMethod;

    @Column(name = "pay_account")
    private String payAccount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUsertaskId() {
        return usertaskId;
    }

    public void setUsertaskId(Integer usertaskId) {
        this.usertaskId = usertaskId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }
}
