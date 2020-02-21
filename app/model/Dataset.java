package model;

import mapper.annotation.Column;
import mapper.annotation.JsonField;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

public class Dataset {
    @Column(name = "id", primaryKey = true, autoIncrement = true)
    @JsonField(key = "id")
    private int id;

    @Column(name = "category")
    @JsonField(key = "category")
    private String category;

    @Column(name = "attributes")
    @JsonField(key = "attributes")
    private String attributes;

    @Column(name = "nim")
    @JsonField(key = "nim")
    private String nim;

    private double distance;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
