package model;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */
public class Classification {
    private int id;
    private String[] biner;

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
}
