package model;

import mapper.annotation.Column;
import mapper.annotation.JsonField;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

public class Customer {
    @JsonField(key = "id")
    @Column(name = "id", primaryKey = true, autoIncrement = true)
    private int id;

    @JsonField(key = "firstName")
    @Column(name = "first_name")
    private String firstName;

    @JsonField(key = "lastName")
    @Column(name = "last_name")
    private String lastName;

    @JsonField(key = "user")
    @Column(name = "user_name")
    private String userName;

    @JsonField(key = "password")
    @Column(name = "password")
    private String password;

    @JsonField(key = "email")
    @Column(name = "email")
    private String email;

    @JsonField(key = "address")
    @Column(name = "address")
    private String address;

    @JsonField(key = "city")
    @Column(name = "city")
    private String city;

    @JsonField(key = "province")
    @Column(name = "province")
    private String province;

    @JsonField(key = "created")
    @Column(name = "created")
    private String created;

    @JsonField(key = "createdby")
    @Column(name = "createdby")
    private int createdBy;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }
}
