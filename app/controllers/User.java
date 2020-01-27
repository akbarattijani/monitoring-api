package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import database.Connection.Connect;
import database.Connection.Connection;
import database.dao.impl.UserImpl;
import enums.Database;
import httpactions.ApiAuth;
import mapper.Mapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import play.mvc.BodyParser;
import play.mvc.Result;
import utils.Body;

import java.util.List;

import static play.mvc.Controller.request;
import static play.mvc.Results.status;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

@ApiAuth
public class User {

    @Connect(
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
            model.User body = new Mapper().toModel(request().body().asJson(), model.User.class);
            body.setStep(1);

            UserImpl dao = new UserImpl();
            int count = dao.save(body);

            if (count > 0) {
                JSONObject object = new JSONObject();
                model.User user = dao.getUserByNip(body.getId());

                if (user != null) {
                    int id = user.getId();
                    String nipResult = user.getNip();
                    String name = user.getName();
                    int step = user.getStep();

                    object.put("status", 1);
                    object.put("id", id);
                    object.put("nip", nipResult);
                    object.put("name", name);
                    object.put("step", step);
                } else {
                    object.put("status", 0);
                }

                // Closing database connection;
                Connection.disconnect();

                return Body.echo(enums.Result.REQUEST_OK, object.toString());
            }

            // Closing database connection
            Connection.disconnect();

            return Body.echo(enums.Result.RESPONSE_NOTHING, "Inserting Failed...");
        } catch (Exception e) {
            e.printStackTrace();
            Connection.disconnect();
            return status(401, e.getMessage());
        }
    }

    @Connect(
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
            UserImpl dao = new UserImpl();
            model.User user = dao.getUser(nip, password, supervisor);

            if (user != null) {
                int id = user.getId();
                String nipResult = user.getNip();
                String name = user.getName();
                int step = user.getStep();

                object.put("id", id);
                object.put("nip", nipResult);
                object.put("name", name);
                object.put("step", step);

                // Closing database connection
                Connection.disconnect();
                System.out.println("Anda berhasil login");

                return Body.echo(enums.Result.REQUEST_OK, object.toString());
            }

            // Closing database connection
            Connection.disconnect();

            return Body.echo(enums.Result.RESPONSE_NOTHING, "User Not Found");
        } catch (Exception e) {
            e.printStackTrace();
            Connection.disconnect();
            return status(401, e.getMessage());
        }
    }

    @Connect(
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
            UserImpl dao = new UserImpl();
            model.User user = dao.check(nip);

            if (user != null) {
                int id = user.getId();
                String nipResult = user.getNip();
                String name = user.getName();
                int step = user.getStep();

                object.put("status", 1);
                object.put("id", id);
                object.put("nip", nipResult);
                object.put("name", name);
                object.put("step", step);
            } else {
                object.put("status", 0);
            }

            // Closing database connection
            Connection.disconnect();

            return Body.echo(enums.Result.REQUEST_OK, object.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Connection.disconnect();
            return status(401, e.getMessage());
        }
    }

    @Connect(
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
            UserImpl dao = new UserImpl();
            model.User user = dao.getUserByNip(body.path("id").asInt());

            if (user != null) {
                user.setStep(body.path("step").asInt());
                int count = dao.save(user);

                // Closing database connection
                Connection.disconnect();

                return Body.echo(enums.Result.REQUEST_OK, "Update Step Success : " + count);
            } else {
                // Closing database connection
                Connection.disconnect();
                return Body.echo(enums.Result.RESPONSE_NOTHING, "User ID " +  body.path("id").asInt() + " Not Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Connection.disconnect();
            return status(401, e.getMessage());
        }
    }

    @Connect(
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
            UserImpl dao = new UserImpl();
            model.User user = dao.getUserByNip(body.path("id").asInt());

            if (user != null) {
                user.setPassword(body.path("password").asText());
                int count = dao.save(user);

                // Closing database connection
                Connection.disconnect();

                return Body.echo(enums.Result.REQUEST_OK, "Update Password Success : " + count);
            } else {
                // Closing database connection
                Connection.disconnect();
                return Body.echo(enums.Result.RESPONSE_NOTHING, "User ID " +  body.path("id").asInt() + " Not Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Connection.disconnect();
            return status(401, e.getMessage());
        }
    }

    @Connect(
            database = Database.POSTGRESQL,
            host = "ec2-23-23-173-30.compute-1.amazonaws.com",
            databaseName = "d87s2lf0vv7l32",
            userName = "ppxiknjbrpshfp",
            password = "dadde9e960e7acc54bf9b09a35ef98f4ec01a149e1560b4a8c4f6909271cc76c",
            port = "5432"
    )
    public static Result getAllUser() {
        try {
            UserImpl dao = new UserImpl();
            List<model.User> users = dao.getAll();

            if (users != null && users.size() > 0) {
                JSONArray array = new JSONArray();
                for (model.User user : users) {
                    JSONObject object = new JSONObject();

                    int id = user.getId();
                    String nipResult = user.getNip();
                    String name = user.getName();
                    int step = user.getStep();

                    object.put("id", id);
                    object.put("nip", nipResult);
                    object.put("name", name);
                    object.put("step", step);

                    array.add(object);
                }

                // Closing database connection
                Connection.disconnect();

                return Body.echo(enums.Result.REQUEST_OK, array.toString());
            }

            // Closing database connection
            Connection.disconnect();

            return Body.echo(enums.Result.RESPONSE_NOTHING, "User Not Found");
        } catch (Exception e) {
            e.printStackTrace();
            Connection.disconnect();
            return status(401, e.getMessage());
        }
    }
}
