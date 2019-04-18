package model;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */
public class ClassificationModel {
    private int id;
    private String[] biner;

    public int getId() {
        return id;
    }

    public ClassificationModel setId(int id) {
        this.id = id;
        return this;
    }

    public String[] getBiner() {
        return biner;
    }

    public ClassificationModel setBiner(String[] biner) {
        this.biner = biner;
        return this;
    }
}
