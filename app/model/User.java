package model;

import mapper.annotation.Column;
import mapper.annotation.JsonField;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

public class User {
    @Column(name = "id", primaryKey = true, autoIncrement = true)
    @JsonField(key = "id")
    private int id;

    @Column(name = "nip")
    @JsonField(key = "nip")
    private String nip;

    @Column(name = "name")
    @JsonField(key = "name")
    private String name;

    @Column(name = "password")
    @JsonField(key = "password")
    private String password;

    @Column(name = "supervisor")
    @JsonField(key = "supervisor")
    private int supervisor;

    @Column(name = "step")
    @JsonField(key = "step")
    private int step;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNip() {
        return nip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(int supervisor) {
        this.supervisor = supervisor;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
}
