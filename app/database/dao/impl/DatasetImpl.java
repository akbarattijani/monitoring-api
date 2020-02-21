package database.dao.impl;

import database.dao.Dao;
import database.dao.DaoImpl;
import model.Dataset;

import java.util.List;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

@Dao(tableName = "m_dataset")
public class DatasetImpl extends DaoImpl<Dataset, DatasetImpl> {

    public DatasetImpl() {
        super(DatasetImpl.class);
    }

    @Override
    public int save(Dataset object) {
        return super.save(object);
    }

    public List<Dataset> getAll() {
        return queryForObject("SELECT * FROM m_dataset");
    }
}
