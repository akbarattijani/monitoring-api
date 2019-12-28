package database.dao;

import java.util.List;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

public interface DaoInterface<T> {
    int save(T object);
    List<T> select(T clas, String arguments, Object[] parameters);
    int delete(String arguments, Object[] parameters);
}
