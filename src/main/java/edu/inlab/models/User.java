package edu.inlab.models;

import edu.inlab.utils.EncodeFactory;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

/**
 * Created by inlab-dell on 2016/5/4.
 */
@Entity
@Table(name = "user")
public class User extends CaptchaCapableModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Integer id;

    @Column(name = "password", nullable = false)
    private String password;

    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "gender")
    private String gender;        //F: Female, M:Male, null:undefined, U:Unknown

    @Column(name = "age")
    @Min(value = 0)
    @Max(value = 150)
    private Integer age;

    @Column(name = "phone_number")
    @Size(max = 30)
    private String phoneNumber;

    @Column(name = "pay_method")
    private Integer payMethod;

    @Column(name = "pay_account")
    private String payAccount;

    @Column(name = "accept_rate")
    private Double acceptRate;

    @Column(name = "nickname")
    @Length(max = 10)
    private String nickname;

    @Column(name = "salt")
    private String salt;

    @Column(name = "token_cookie")
    private String tokenCookie;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "usertask",
            joinColumns = {
                @JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "task_id", referencedColumnName = "id") })
    private List<Task> claimedTasks;

    public User(){
        //Default constructor for jackson
        this(null, null, null, null, null, null, null, 0.5, null);
    }

    public User(String email, String password){
        this(email, password, null);
    }

    public User(String email, String password, String nickname){
        this(email, password, null, null, null, null, null, 0.5, nickname);
    }

    public User(String email, String password, String gender, Integer age
            , String phoneNumber, Integer payMethod, String payAccount, Double acceptRate, String nickname){
        this.id = null;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.payMethod = payMethod;
        this.payAccount = payAccount;
        this.acceptRate = acceptRate;
        this.nickname = nickname;
        this.salt = null;
    }

    public void generateSaltPassword(){
        //MD5(salt + MD5(password))
        //Assume the password is already encoded at the frontend
        password = EncodeFactory.getEncodedString(salt + password);
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

    public String getGender() {
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

    public void setGender(String gender) {
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        if(nickname!= null && nickname.length()==0){
            this.nickname = null;
        } else {
            this.nickname = nickname;
        }
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTokenCookie() {
        return tokenCookie;
    }

    public void setTokenCookie(String tokenCookie) {
        this.tokenCookie = tokenCookie;
    }

    public List<Task> getClaimedTasks() {
        return claimedTasks;
    }

    public void setClaimedTasks(List<Task> claimedTasks) {
        this.claimedTasks = claimedTasks;
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
        if(id == null){
            return -1;
        }
        return id.intValue();
    }
}
