package database.Connection.Contants;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */
public class SyncConstants {

    public static String mysql(String host, String port, String databaseName, String username, String password) {
        return "jdbc:mysql://"
                + host + "/"
                + databaseName + "?"
                + "user="
                + username + "&"
                + "password="
                + password;
    }

    public static String postgresql(String host, String port, String databaseName, String username, String password, String schema) {
        return "jdbc:postgresql://"
                + host + ":"
                + port +"/"
                + databaseName + "?"
                + "currentSchema=" + schema
                + "&user=" + username
                + "&password=" + password
                + "&ssl=true&sslmode=require";
    }

    public static String sqlserver(String host, String port, String databaseName, String username, String password) {
        return "jdbc:sqlserver://"
                + host + ":"
                + port + ";"
                + "databaseName=" + databaseName + ";"
                + "user=" + username + ";"
                + "password=" + password;
    }

    public static String oracle(String host, String port, String databaseName, String username, String password) {
        return "jdbc:oracle:thin:@"
                + host + ":"
                + port + ":"
                + databaseName + ", \""
                + username + "\""
                + ",\"" + password;
    }

    public static String access(String host, String port, String databaseName, String username, String password) {
        return "jdbc:odbc:"
                + host + ":"
                + username + "\""
                + ",\"" + password;
    }

    public static String sqlite(String host, String port, String databaseName, String username, String password) {
        return "jdbc:sqlite:" + host;
    }
}
