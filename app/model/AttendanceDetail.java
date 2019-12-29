package model;

import mapper.annotation.Column;
import mapper.annotation.JsonField;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

public class AttendanceDetail {
    @Column(name = "id", primaryKey = true, autoIncrement = true)
    @JsonField(key = "id")
    private int id;

    @Column(name = "id_absen")
    @JsonField(key = "id_absen")
    private int idAbsen;

    @Column(name = "longitude")
    @JsonField(key = "longitude")
    private double longitude;

    @Column(name = "latitude")
    @JsonField(key = "latitude")
    private double latitude;

    @Column(name = "date")
    @JsonField(key = "date")
    private String date;

    @Column(name = "custom")
    @JsonField(key = "custom")
    private int custom;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdAbsen() {
        return idAbsen;
    }

    public void setIdAbsen(int idAbsen) {
        this.idAbsen = idAbsen;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCustom() {
        return custom;
    }

    public void setCustom(int custom) {
        this.custom = custom;
    }
}
