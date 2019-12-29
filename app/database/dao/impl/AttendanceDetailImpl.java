package database.dao.impl;

import database.dao.Dao;
import database.dao.DaoImpl;
import model.AttendanceDetail;

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
}

