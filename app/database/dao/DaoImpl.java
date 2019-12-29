package database.dao;

import database.Connection.Connection;
import mapper.Mapper;
import mapper.annotation.Column;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

public abstract class DaoImpl<T, M> implements DaoInterface<T> {
    private String tableName;
    private Class<M> globalClass;

    private static final String[] formats = {
            "yyyy-MM-dd",
            "dd-MM-yyyy",
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd'T'HH:mm:ssZ",
            "yyyy-MM-dd'T'HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
            "yyyy-MM-dd HH:mm:ss",
            "MM/dd/yyyy HH:mm:ss",
            "MM/dd/yyyy'T'HH:mm:ss.SSS'Z'",
            "MM/dd/yyyy'T'HH:mm:ss.SSSZ",
            "MM/dd/yyyy'T'HH:mm:ss.SSS",
            "MM/dd/yyyy'T'HH:mm:ssZ",
            "MM/dd/yyyy'T'HH:mm:ss",
            "yyyy:MM:dd HH:mm:ss",
            "yyyyMMdd",
            "ddMMyyyy"
    };

    public DaoImpl(Class<M> dao) {
        try {
            // Get Class
            tableName = dao.getAnnotation(Dao.class).tableName();
            globalClass = dao;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        Connection.disconnect();
    }

    @Override
    public int save(T object) {
        String primaryKey = "";
        String autoIncrement = "";
        boolean autoIncrementBool = false;
        Class<?> dataType = null;

        Map<String, Object> column = new LinkedHashMap<>();
        try {
            // Get Fields
            Class<T> persistentClass = (Class<T>) object.getClass();
            java.lang.reflect.Field[] fields = persistentClass.getDeclaredFields();

            for (java.lang.reflect.Field f : fields) {
                if (f.isAnnotationPresent(Column.class)) {
                    Annotation field = f.getAnnotation(Column.class);
                    String fieldName = getAnnotationValue("name", field).toString();
                    f.setAccessible(true);

                    // Get Method
                    String methodName = fieldName;
                    if (methodName.contains("_")) {
                        String[] fn = methodName.split("_");
                        methodName = "";
                        for (String fix : fn) {
                            methodName += StringUtils.capitalize(fix);
                        }
                    }

                    Method method = persistentClass.getDeclaredMethod("get" + StringUtils.capitalize(methodName));
                    Object value = method.invoke(object);
                    System.out.println("Method : " + method + " -> " + value);
                    column.put(fieldName, value);

                    // Get primary key
                    boolean isPrimaryKey = Boolean.parseBoolean(getAnnotationValue("primaryKey", field).toString());
                    if (isPrimaryKey) {
                        autoIncrementBool = Boolean.parseBoolean(getAnnotationValue("autoIncrement", field).toString());
                        if (autoIncrementBool) {
                            autoIncrement = fieldName;
                        }

                        primaryKey = fieldName;
                        dataType = f.getType();
                    }
                }
            }

            String select = "SELECT * FROM " + tableName + " WHERE " + primaryKey  + " = ?";
            PreparedStatement preparedStatement = Connection.getConnection().prepareStatement(select);

            if (dataType == String.class) {
                preparedStatement.setString(1, column.get(primaryKey).toString());
            } else if (dataType == Integer.class || dataType == int.class) {
                preparedStatement.setInt(1, Integer.parseInt(column.get(primaryKey).toString()));
            } else if (dataType == Double.class || dataType == double.class) {
                preparedStatement.setDouble(1, Double.parseDouble(column.get(primaryKey).toString()));
            } else if (dataType == Long.class || dataType == long.class) {
                preparedStatement.setLong(1, Long.parseLong(column.get(primaryKey).toString()));
            } else if (dataType == Date.class) {
                Date date = new SimpleDateFormat(getFormat(column.get(primaryKey).toString())).parse(column.get(primaryKey).toString());
                preparedStatement.setDate(1, new java.sql.Date(date.getTime()));
            } else if (dataType == Boolean.class || dataType == boolean.class) {
                preparedStatement.setBoolean(1, Boolean.parseBoolean(column.get(primaryKey).toString()));
            } else {
                throw new Exception("Data Type not valid. Apply only String, Integer, Double, or Long");
            }

            System.out.print("save() on Select -> " + select + " (Parameter = " + column.get(primaryKey).toString() + ")");

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                System.out.println(" -> Exist");
                String update = "UPDATE " + tableName + " SET ";
                int index = 0;
                for (Map.Entry<String, Object> map : column.entrySet()) {
                    if (!map.getKey().equals(primaryKey)) {
                        index++;

                        if (map.getValue() instanceof Integer || map.getValue() instanceof Double || map.getValue() instanceof Long) {
                            update += map.getKey() + " = " + map.getValue();
                        } else {
                            update += map.getKey() + " = '" + map.getValue() + "'";
                        }

                        if (index < column.size() - 1) {
                            update += ", ";
                        }
                    }
                }

                update += " WHERE " + primaryKey + " = ?";
                System.out.println("save() on Update -> " + update + "(Parameter = " + column.get(primaryKey).toString() + ")");

                PreparedStatement preparedUpdate = Connection.getConnection().prepareStatement(update);
                if (dataType == String.class) {
                    preparedUpdate.setString(1, column.get(primaryKey).toString());
                } else if (dataType == Integer.class || dataType == int.class) {
                    preparedUpdate.setInt(1, Integer.parseInt(column.get(primaryKey).toString()));
                } else if (dataType == Double.class || dataType == double.class) {
                    preparedUpdate.setDouble(1, Double.parseDouble(column.get(primaryKey).toString()));
                } else if (dataType == Long.class || dataType == long.class) {
                    preparedUpdate.setLong(1, Long.parseLong(column.get(primaryKey).toString()));
                } else if (dataType == Date.class) {
                    Date date = new SimpleDateFormat(getFormat(column.get(primaryKey).toString())).parse(column.get(primaryKey).toString());
                    preparedUpdate.setDate(1, new java.sql.Date(date.getTime()));
                } else if (dataType == Boolean.class || dataType == boolean.class) {
                    preparedUpdate.setBoolean(1, Boolean.parseBoolean(column.get(primaryKey).toString()));
                }

                return preparedUpdate.executeUpdate();
            } else {
                System.out.println(" -> Dont Exist");
                int index = 0;
                String insert = "INSERT INTO " + tableName + "(";
                String values = " VALUES (";
                for (Map.Entry<String, Object> map : column.entrySet()) {
                    index++;
                    if (map.getKey().equals(autoIncrement) && autoIncrementBool) {
                        continue;
                    }

                    insert += map.getKey();
                    if (map.getValue() instanceof Integer || map.getValue() instanceof Double || map.getValue() instanceof Long) {
                        values += map.getValue();
                    } else {
                        values += "'" + map.getValue() + "'";
                    }

                    if (index < column.size()) {
                        insert += ", ";
                        values += ", ";
                    }
                }

                values += ")";
                insert += ") " + values;

                System.out.println("save() on Insert -> " + insert);

                Statement statement = Connection.getConnection().createStatement();
                return statement.executeUpdate(insert);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public List<T> select(T clas, String arguments, Object[] parameters) {
        try {
            Class<T> persistentClass = (Class<T>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            String select = "SELECT * FROM " + tableName;
            if (parameters.length > 0) {
                select += " WHERE " + arguments;
            }

            PreparedStatement preparedStatement = Connection.getConnection().prepareStatement(select);
            int param = 1;
            for (Object value : parameters) {
                if (value instanceof Integer) {
                    preparedStatement.setInt(param, Integer.parseInt(value.toString()));
                } else if (value instanceof Double) {
                    preparedStatement.setDouble(param, Double.parseDouble(value.toString()));
                } else if (value instanceof Long) {
                    preparedStatement.setLong(param, Long.parseLong(value.toString()));
                } else {
                    preparedStatement.setString(param, value.toString());
                }

                param++;
            }

            System.out.println("select() -> " + select);

            ResultSet rs = preparedStatement.executeQuery();
            return new Mapper().toModels(rs, persistentClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int delete(String arguments, Object[] parameters) {
        try {
            String select = "DELETE * FROM " + tableName;
            if (parameters.length > 0) {
                select += " WHERE " + arguments;
            }

            PreparedStatement preparedStatement = Connection.getConnection().prepareStatement(select);
            int param = 1;
            for (Object value : parameters) {
                if (value instanceof Integer) {
                    preparedStatement.setInt(param, Integer.parseInt(value.toString()));
                } else if (value instanceof Double) {
                    preparedStatement.setDouble(param, Double.parseDouble(value.toString()));
                } else if (value instanceof Long) {
                    preparedStatement.setLong(param, Long.parseLong(value.toString()));
                } else {
                    preparedStatement.setString(param, value.toString());
                }

                param++;
            }

            return preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    protected <R> R queryForObject(Query type, String query, Object... parameters) {
        try {
            PreparedStatement preparedStatement = Connection.getConnection().prepareStatement(query);
            int param = 1;
            for (Object value : parameters) {
                if (value.getClass() == int.class || value instanceof Integer) {
                    preparedStatement.setInt(param, Integer.parseInt(value.toString()));
                } else if (value.getClass() == double.class || value instanceof Double) {
                    preparedStatement.setDouble(param, Double.parseDouble(value.toString()));
                } else if (value.getClass() == long.class || value instanceof Long) {
                    preparedStatement.setLong(param, Long.parseLong(value.toString()));
                } else {
                    preparedStatement.setString(param, value.toString());
                }

                param++;
            }

            if (type == Query.INSERT || type == Query.UPDATE || type == Query.DELETE) {
                return ((R) Integer.class.cast(preparedStatement.executeUpdate()));
            } else if (type == Query.SELECT) {
                Class<R> persistentClass = (Class<R>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                ResultSet rs = preparedStatement.executeQuery();

                return new Mapper().toModel(rs, persistentClass);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public <R> List<R> queryForObject(String query, Object... parameters) {
        try {
            PreparedStatement preparedStatement = Connection.getConnection().prepareStatement(query);
            int param = 1;
            for (Object value : parameters) {
                if (value instanceof Integer) {
                    preparedStatement.setInt(param, Integer.parseInt(value.toString()));
                } else if (value instanceof Double) {
                    preparedStatement.setDouble(param, Double.parseDouble(value.toString()));
                } else if (value instanceof Long) {
                    preparedStatement.setLong(param, Long.parseLong(value.toString()));
                } else {
                    preparedStatement.setString(param, value.toString());
                }

                param++;
            }

            Class<R> persistentClass = (Class<R>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            ResultSet rs = preparedStatement.executeQuery();

            return new Mapper().toModels(rs, persistentClass);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private Object getAnnotationValue(String key, Annotation annotation) throws Exception {
        Class<? extends Annotation> type = annotation.annotationType();
        for (Method mm : type.getDeclaredMethods()) {
            if (mm.getName().equals(key)) {
                return mm.invoke(annotation, (Object[])null);
            }
        }

        return null;
    }

    private String getFormat(String d) {
        if (d != null) {
            for (String parse : formats) {
                SimpleDateFormat sdf = new SimpleDateFormat(parse);
                try {
                    sdf.parse(d);
                    System.out.println("Printing the value of " + parse);

                    return parse;
                } catch (ParseException e) {

                }
            }
        }

        return null;
    }
}
