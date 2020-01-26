package database.dao.impl;

import database.dao.Dao;
import database.dao.DaoImpl;
import database.dao.Query;
import model.Attendance;

import java.util.List;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

@Dao(tableName = "t_absen")
public class AttendanceImpl extends DaoImpl<Attendance, AttendanceImpl> {

    public AttendanceImpl() {
        super(AttendanceImpl.class);
    }

    @Override
    public int save(Attendance object) {
        return super.save(object);
    }

    @Override
    public int delete(String arguments, Object[] parameters) {
        return super.delete(arguments, parameters);
    }

    public Attendance getByIdUser(int id) {
        return queryForObject(Query.SELECT, "SELECT * FROM t_absen WHERE id_user = ? ORDER BY id DESC LIMIT 1", id);
    }

    public Attendance getTodayByIdUser(int id) {
        return queryForObject(Query.SELECT, "SELECT * FROM t_absen WHERE id_user = ? AND start_date >= now()::date ORDER BY id LIMIT 1", id);
    }

    public Attendance getEndTodayByIdUser(int id) {
        return queryForObject(Query.SELECT, "SELECT * FROM t_absen WHERE id_user = ? AND start_date >= now()::date AND end_date IS NULL ORDER BY id LIMIT 1", id);
    }

    public Attendance getBreakOutTodayByIdUser(int id) {
        return queryForObject(Query.SELECT, "SELECT * FROM t_absen WHERE id_user = ? AND start_date >= now()::date AND end_date IS NULL AND break_end_date IS NULL ORDER BY id LIMIT 1", id);
    }

    public Attendance getBreakInTodayByIdUser(int id) {
        return queryForObject(Query.SELECT, "SELECT * FROM t_absen WHERE id_user = ? AND start_date >= now()::date AND end_date IS NULL AND break_start_date IS NOT NULL ORDER BY id LIMIT 1", id);
    }

    public List<Attendance> getAttendances(int id, String startDate, String endDate) {
        return queryForObject("SELECT * FROM t_absen WHERE id_user = ? AND start_date::date BETWEEN ?::date AND ?::date", id, startDate, endDate);
    }

    public List<Attendance> getAll(int id) {
        return queryForObject("SELECT * FROM t_absen WHERE id_user = ?", id);
    }
}
