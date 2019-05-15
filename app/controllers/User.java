package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import database.Connection.CheckDatabase;
import database.Connection.Connection;
import enums.Database;
import httpactions.ApiAuth;
import model.UserModel;
import org.json.simple.JSONObject;
import play.mvc.BodyParser;
import play.mvc.Result;
import utils.Body;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static play.mvc.Controller.request;
import static play.mvc.Results.status;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

@ApiAuth
public class User {

    @CheckDatabase(
            database = Database.POSTGRESQL,
            host = "ec2-23-23-173-30.compute-1.amazonaws.com",
            databaseName = "d87s2lf0vv7l32",
            userName = "ppxiknjbrpshfp",
            password = "dadde9e960e7acc54bf9b09a35ef98f4ec01a149e1560b4a8c4f6909271cc76c",
            port = "5432"
    )
    @BodyParser.Of(value = BodyParser.Json.class , maxLength = 1024 * 1024 * 1024)
    public static Result register() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            final UserModel body = mapper.treeToValue(request().body().asJson(), UserModel.class);

            String sql = "INSERT INTO m_user (nip, name, password, step, supervisor) values (?,?,?,?,?)";
            PreparedStatement preparedStatement = Connection.getConnection().prepareStatement(sql);

            preparedStatement.setString(1, body.getNip());
            preparedStatement.setString(2, body.getName());
            preparedStatement.setString(3, body.getPassword());
            preparedStatement.setInt(4, 1);
            preparedStatement.setInt(5, body.getSupervisor());

            int count = preparedStatement.executeUpdate();

            if (count > 0) {
                JSONObject object = new JSONObject();
                String select = "SELECT * FROM m_user WHERE nip = ? limit 1";

                PreparedStatement prepaired = Connection.getConnection().prepareStatement(select);
                prepaired.setString(1, body.getNip());

                ResultSet rs = prepaired.executeQuery();
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String nipResult = rs.getString("nip");
                    String name = rs.getString("name");
                    int step = rs.getInt("step");

                    object.put("status", 1);
                    object.put("id", id);
                    object.put("nip", nipResult);
                    object.put("name", name);
                    object.put("step", step);
                } else {
                    object.put("status", 0);
                }

                // Closing database connection
                rs.close();
                preparedStatement.close();
                Connection.disconnect();

                return Body.echo(enums.Result.REQUEST_OK, object.toString());
            }

            // Closing database connection
            preparedStatement.close();
            Connection.disconnect();

            return Body.echo(enums.Result.RESPONSE_NOTHING, "Inserting Failed...");
        } catch (Exception e) {
            e.printStackTrace();
            Connection.disconnect();
            return status(401, e.getMessage());
        }
    }

    @CheckDatabase(
            database = Database.POSTGRESQL,
            host = "ec2-23-23-173-30.compute-1.amazonaws.com",
            databaseName = "d87s2lf0vv7l32",
            userName = "ppxiknjbrpshfp",
            password = "dadde9e960e7acc54bf9b09a35ef98f4ec01a149e1560b4a8c4f6909271cc76c",
            port = "5432"
    )
    public static Result login(String nip, String password, int supervisor) {
        try {
            JSONObject object = new JSONObject();
            String select = "SELECT * FROM m_user WHERE nip = ? AND password = ? AND supervisor = ? limit 1";

            PreparedStatement preparedStatement = Connection.getConnection().prepareStatement(select);
            preparedStatement.setString(1, nip);
            preparedStatement.setString(2, password);
            preparedStatement.setInt(3, supervisor);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String nipResult = rs.getString("nip");
                String name = rs.getString("name");
                int step = rs.getInt("step");

                object.put("id", id);
                object.put("nip", nipResult);
                object.put("name", name);
                object.put("step", step);

                // Closing database connection
                rs.close();
                preparedStatement.close();
                Connection.disconnect();

                return Body.echo(enums.Result.REQUEST_OK, object.toString());
            }

            // Closing database connection
            rs.close();
            preparedStatement.close();
            Connection.disconnect();

            return Body.echo(enums.Result.RESPONSE_NOTHING, "User Not Found");
        } catch (Exception e) {
            e.printStackTrace();
            Connection.disconnect();
            return status(401, e.getMessage());
        }
    }

    @CheckDatabase(
            database = Database.POSTGRESQL,
            host = "ec2-23-23-173-30.compute-1.amazonaws.com",
            databaseName = "d87s2lf0vv7l32",
            userName = "ppxiknjbrpshfp",
            password = "dadde9e960e7acc54bf9b09a35ef98f4ec01a149e1560b4a8c4f6909271cc76c",
            port = "5432"
    )
    public static Result check(String nip) {
        try {
            JSONObject object = new JSONObject();
            String select = "SELECT * FROM m_user WHERE nip = ? AND supervisor = 0 limit 1";

            PreparedStatement preparedStatement = Connection.getConnection().prepareStatement(select);
            preparedStatement.setString(1, nip);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String nipResult = rs.getString("nip");
                String name = rs.getString("name");
                int step = rs.getInt("step");

                object.put("status", 1);
                object.put("id", id);
                object.put("nip", nipResult);
                object.put("name", name);
                object.put("step", step);
            } else {
                object.put("status", 0);
            }

            // Closing database connection
            rs.close();
            preparedStatement.close();
            Connection.disconnect();

            return Body.echo(enums.Result.REQUEST_OK, object.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Connection.disconnect();
            return status(401, e.getMessage());
        }
    }

    @CheckDatabase(
            database = Database.POSTGRESQL,
            host = "ec2-23-23-173-30.compute-1.amazonaws.com",
            databaseName = "d87s2lf0vv7l32",
            userName = "ppxiknjbrpshfp",
            password = "dadde9e960e7acc54bf9b09a35ef98f4ec01a149e1560b4a8c4f6909271cc76c",
            port = "5432"
    )
    @BodyParser.Of(value = BodyParser.Json.class , maxLength = 1024 * 1024 * 1024)
    public static Result updateStep() {
        try {
            JsonNode body = request().body().asJson();
            String sql = "UPDATE m_user set step = ? where id = ?";

            PreparedStatement preparedStatement = Connection.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, body.path("step").asInt());
            preparedStatement.setInt(2, body.path("id").asInt());

            int count = preparedStatement.executeUpdate();

            // Closing database connection
            preparedStatement.close();
            Connection.disconnect();

            return Body.echo(enums.Result.REQUEST_OK, "Inserting Success : " + count);
        } catch (Exception e) {
            e.printStackTrace();
            Connection.disconnect();
            return status(401, e.getMessage());
        }
    }

    @CheckDatabase(
            database = Database.POSTGRESQL,
            host = "ec2-23-23-173-30.compute-1.amazonaws.com",
            databaseName = "d87s2lf0vv7l32",
            userName = "ppxiknjbrpshfp",
            password = "dadde9e960e7acc54bf9b09a35ef98f4ec01a149e1560b4a8c4f6909271cc76c",
            port = "5432"
    )
    @BodyParser.Of(value = BodyParser.Json.class , maxLength = 1024 * 1024 * 1024)
    public static Result updatePassword() {
        try {
            JsonNode body = request().body().asJson();
            String sql = "UPDATE m_user set password = ? where id = ?";

            PreparedStatement preparedStatement = Connection.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, body.path("password").asInt());
            preparedStatement.setInt(2, body.path("id").asInt());

            int count = preparedStatement.executeUpdate();

            // Closing database connection
            preparedStatement.close();
            Connection.disconnect();

            return Body.echo(enums.Result.REQUEST_OK, "Inserting Success : " + count);
        } catch (Exception e) {
            e.printStackTrace();
            Connection.disconnect();
            return status(401, e.getMessage());
        }
    }
}
