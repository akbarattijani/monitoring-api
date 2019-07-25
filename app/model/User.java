package model;

import mapper.annotation.Column;
import mapper.annotation.JsonField;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

public class User {
    @Column(name = "nip")
    @JsonField(key = "nip")
    private String nip;

    @Column(name = "password")
    @JsonField(key = "password")
    private String password;

    @Column(name = "supervisor")
    @JsonField(key = "supervisor")
    private int supervisor;

    public String getNip() {
        return nip;
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
}
