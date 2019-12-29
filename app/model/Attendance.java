package model;

import mapper.annotation.Column;
import mapper.annotation.JsonField;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

public class Attendance {
    @Column(name = "id", primaryKey = true, autoIncrement = true)
    @JsonField(key = "id")
    private int id;

    @Column(name = "id_user")
    @JsonField(key = "id_user")
    private int idUser;

    @Column(name = "start_date")
    @JsonField(key = "start_date")
    private String startDate;

    @Column(name = "end_date")
    @JsonField(key = "end_date")
    private String endDate;

    @Column(name = "break_start_date")
    @JsonField(key = "break_start_date")
    private String breakStartDate;

    @Column(name = "break_end_date")
    @JsonField(key = "break_end_date")
    private String breakEndDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getBreakStartDate() {
        return breakStartDate;
    }

    public void setBreakStartDate(String breakStartDate) {
        this.breakStartDate = breakStartDate;
    }

    public String getBreakEndDate() {
        return breakEndDate;
    }

    public void setBreakEndDate(String breakEndDate) {
        this.breakEndDate = breakEndDate;
    }
}
