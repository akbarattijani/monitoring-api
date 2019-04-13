package database.Connection;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */
public class Connection {
    private static java.sql.Connection connected;

//    public boolean openConnection(String host, String port, String username, String password, String databaseName, Database type) {
//
//        try {
//            Class.forName(type.toString());
//
//            if(type == Database.ACCESS) {
//                connected = DriverManager.getConnection(SyncConstants.access(host, port, databaseName, username, password));
//            } else if(type == Database.MYSQL) {
//                connected = DriverManager.getConnection(SyncConstants.mysql(host, port, databaseName, username, password));
//            } else if(type == Database.ORACLE) {
//                connected = DriverManager.getConnection(SyncConstants.oracle(host, port, databaseName, username, password));
//            } else if(type == Database.POSTGRESQL) {
//                connected = DriverManager.getConnection(SyncConstants.postgresql(host, port, databaseName, username, password));
//            } else if(type == Database.SQLSERVER) {
//                connected = DriverManager.getConnection(SyncConstants.sqlserver(host, port, databaseName, username, password));
//            } else if(type == Database.SQLITE) {
//                connected = DriverManager.getConnection(SyncConstants.sqlite(host, port, databaseName, username, password));
//            }
//
//            return connected == null ? false : true;
//
//        } catch (ClassNotFoundException a) {
//            a.printStackTrace();
//            return false;
//        } catch (SQLException b) {
//            b.printStackTrace();
//            return false;
//        }
//    }

    public static void setConnection(java.sql.Connection connected) {
        Connection.connected = connected;
    }

    public static java.sql.Connection getConnection() {
        return connected;
    }
}
