package database.dao.impl;

import database.dao.Dao;
import database.dao.DaoImpl;
import model.AttendanceDetail;

import java.util.List;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

@Dao(tableName = "t_absen_detail")
public class AttendanceDetailImpl extends DaoImpl<AttendanceDetail, AttendanceDetailImpl> {

    public AttendanceDetailImpl() {
        super(AttendanceDetailImpl.class);
    }

    @Override
    public int save(AttendanceDetail object) {
        return super.save(object);
    }

    public List<AttendanceDetail> getDetailByIdAbsen(int id) {
        return queryForObject("SELECT * FROM t_absen_detail WHERE id_absen = ?", id);
    }
}

