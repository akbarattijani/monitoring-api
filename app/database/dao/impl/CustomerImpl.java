package database.dao.impl;

import database.dao.Dao;
import database.dao.DaoImpl;
import database.dao.Query;
import model.Customer;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

@Dao(tableName = "m_customer")
public class CustomerImpl extends DaoImpl<Customer, CustomerImpl> {

    public CustomerImpl() {
        super(CustomerImpl.class);
    }

    @Override
    public int save(Customer object) {
        return super.save(object);
    }

    public Customer login(String userName, String password) {
        return queryForObject(Query.SELECT, "SELECT * FROM m_customer WHERE user_name = ? AND password = ?", userName, password);
    }
}
