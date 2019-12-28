package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import mapper.annotation.Column;
import mapper.annotation.JsonField;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

public class Sample {
    @Column(name = "id", primaryKey = true)
    @JsonField(key = "id")
    private int id;

    @Column(name = "id_user")
    @JsonField(key = "id_user")
    private int idUser;

    @Column(name = "biner")
    @JsonField(key = "biner")
    private String biner;

    @JsonProperty("id")
    public int getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    @JsonProperty("biner")
    public String getBiner() {
        return biner;
    }

    @JsonProperty("biner")
    public void setBiner(String biner) {
        this.biner = biner;
    }
}
