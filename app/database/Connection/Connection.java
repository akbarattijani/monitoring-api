package database.Connection;

import java.sql.SQLException;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */
public class Connection {
    private static java.sql.Connection connected;

    public static void setConnection(java.sql.Connection connected) {
        Connection.connected = connected;
    }

    public static java.sql.Connection getConnection() {
        return connected;
    }

    public static void disconnect() {
        try {
            connected.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
