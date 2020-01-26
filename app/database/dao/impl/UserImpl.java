package database.dao.impl;

import database.dao.Dao;
import database.dao.DaoImpl;
import database.dao.Query;
import model.User;

import java.util.List;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

@Dao(tableName = "m_user")
public class UserImpl extends DaoImpl<User, UserImpl> {

    public UserImpl() {
        super(UserImpl.class);
    }

    @Override
    public int save(User object) {
        return super.save(object);
    }

    @Override
    public List<User> select(User object, String arguments, Object[] parameters) {
        return super.select(object, arguments, parameters);
    }

    public User getById(int id) {
        return queryForObject(Query.SELECT, "SELECT * FROM m_user where id = ? LIMIT 1", id);
    }

    @Override
    public int delete(String arguments, Object[] parameters) {
        return super.delete(arguments, parameters);
    }

    public User getUserByNip(int nip) {
        return queryForObject(Query.SELECT,"SELECT * FROM m_user WHERE id = ? limit 1", nip);
    }

    public User getUser(String nip, String password, int supervisor) {
        return queryForObject(Query.SELECT, "SELECT * FROM m_user WHERE nip = ? AND password = ? AND supervisor = ? limit 1", nip, password, supervisor);
    }

    public User check(String nip) {
        return queryForObject(Query.SELECT,"SELECT * FROM m_user WHERE nip = ? AND supervisor = 0 limit 1", nip);
    }

    public List<User> getAll() {
        return queryForObject("SELECT * FROM m_user where supervisor = 0");
    }
}
