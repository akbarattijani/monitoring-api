package model;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */
public class Classification {
    private int id;
    private String[] biner;
    private double distance;

    public int getId() {
        return id;
    }

    public Classification setId(int id) {
        this.id = id;
        return this;
    }

    public String[] getBiner() {
        return biner;
    }

    public Classification setBiner(String[] biner) {
        this.biner = biner;
        return this;
    }

    public double getDistance() {
        return distance;
    }

    public Classification setDistance(double distance) {
        this.distance = distance;
        return this;
    }
}
