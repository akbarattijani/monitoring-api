package database.Connection;

import database.Connection.Contants.SyncConstants;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.sql.DriverManager;

import static enums.Result.RESULT_DATABASE_NOT_CONNECTED;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */
public class DatabaseAction extends Action<CheckDatabase> {

    @Override
    public F.Promise<Result> call(Http.Context context) {
        try {
            CheckDatabase check = configuration;
            String host = check.host();
            String port = check.port();
            String databaseName = check.databaseName();
            String schema = check.schema();
            String username = check.userName();
            String password = check.password();

            Class.forName(check.database().toString());
            switch (check.database()) {
                case ACCESS:
                    Connection.setConnection(DriverManager.getConnection(SyncConstants.access(host, port, databaseName, username, password)));
                    break;
                case MYSQL:
                    Connection.setConnection(DriverManager.getConnection(SyncConstants.mysql(host, port, databaseName, username, password)));
                    break;
                case ORACLE:
                    Connection.setConnection(DriverManager.getConnection(SyncConstants.oracle(host, port, databaseName, username, password)));
                    break;
                case POSTGRESQL:
                    Connection.setConnection(DriverManager.getConnection(SyncConstants.postgresql(host, port, databaseName, username, password, schema)));
                    break;
                case SQLSERVER:
                    Connection.setConnection(DriverManager.getConnection(SyncConstants.sqlserver(host, port, databaseName, username, password)));
                    break;
                case SQLITE:
                    Connection.setConnection(DriverManager.getConnection(SyncConstants.sqlite(host, port, databaseName, username, password)));
                    break;

            }

            context.args.put("connetion", Connection.getConnection());

            return delegate.call(context);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return F.Promise.promise(() -> status(401, RESULT_DATABASE_NOT_CONNECTED.toString()));
        }
    }
}
