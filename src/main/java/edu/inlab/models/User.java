package edu.inlab.models;

import edu.inlab.utils.EncodeFactory;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * Created by inlab-dell on 2016/5/4.
 */
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "gender")
    private Character gender;        //F: Female, M:Male, null:undefined, U:Unknown

    @Column(name = "age")
    @Min(value = 0)
    @Max(value = 150)
    private Integer age;

    @Column(name = "phone_number")
    @Size(min = 11)
    private String phoneNumber;

    @Column(name = "pay_method")
    private Integer payMethod;

    @Column(name = "pay_account")
    private String payAccount;

    @Column(name = "accept_rate")
    private Double acceptRate;

    public User(String email, String password){
        this(email, password, null, null, null, null, null, 0.5);
    }

    public User(String email, String password, Character gender, Integer age
            , String phoneNumber, Integer payMethod, String payAccount, Double acceptRate){
        this.id = null;
        this.email = email;
        this.password = EncodeFactory.getEncodedString(password);
        this.gender = gender;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.payMethod = payMethod;
        this.payAccount = payAccount;
        this.acceptRate = acceptRate;
    }

    public Integer getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public char getGender() {
        return gender;
    }

    public Integer getAge() {
        return age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Integer getPayMethod() {
        return payMethod;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public Double getAcceptRate() {
        return acceptRate;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPayMethod(Integer payMethod) {
        this.payMethod = payMethod;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    public void setAcceptRate(Double acceptRate) {
        this.acceptRate = acceptRate;
    }

    @Override
    public boolean equals(Object obj) {
        if(! (obj instanceof User)){
            return false;
        }
        return ((User) obj).id == id;
    }

    @Override
    public int hashCode() {
        return id.intValue();
    }
}
