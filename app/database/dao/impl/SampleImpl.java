package database.dao.impl;

import database.dao.Dao;
import database.dao.DaoImpl;
import model.Sample;

import java.util.List;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

@Dao(tableName = "m_sample")
public class SampleImpl extends DaoImpl<Sample, SampleImpl> {

    public SampleImpl() {
        super(SampleImpl.class);
    }

    @Override
    public int save(Sample object) {
        return super.save(object);
    }

    public List<Sample> getAll() {
        return queryForObject("SELECT * FROM m_sample");
    }
}
