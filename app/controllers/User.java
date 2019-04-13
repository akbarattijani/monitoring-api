package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import database.Connection.Connection;
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

    @BodyParser.Of(value = BodyParser.Json.class , maxLength = 1024 * 1024 * 1024)
    public static Result register() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            final UserModel body = mapper.readValue(request().body().asText(), UserModel.class);

            String sql = "INSERT INTO m_user (nip, nama, password) values (?,?,?)";
            PreparedStatement preparedStatement = Connection.getConnection().prepareStatement(sql);

            preparedStatement.setString(1, body.getNip());
            preparedStatement.setString(2, body.getName());
            preparedStatement.setString(3, body.getPassword());

            int count = preparedStatement.executeUpdate();

            return Body.echo(enums.Result.REQUEST_OK, "Inserting Success : " + count);
        } catch (Exception e) {
            e.printStackTrace();
            return status(401, e.getMessage());
        }
    }

    @BodyParser.Of(value = BodyParser.Json.class , maxLength = 1024 * 1024 * 1024)
    public static Result login() {
        try {
            JSONObject object = new JSONObject();
            final JsonNode body = request().body().asJson();

            String select = "SELECT * FROM m_user WHERE nip = ? and password = ? limit 1";
            PreparedStatement preparedStatement = Connection.getConnection().prepareStatement(select);
            preparedStatement.setString(1, body.path("nip").asText());
            preparedStatement.setString(2, body.path("password").asText());

            ResultSet rs = preparedStatement.executeQuery(select);
            if (rs.next()) {
                int id = rs.getInt("id");
                String nip = rs.getString("nip");
                String name = rs.getString("name");

                object.put("id", id);
                object.put("nip", nip);
                object.put("name", name);
            }

            return Body.echo(enums.Result.REQUEST_OK, object.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return status(401, e.getMessage());
        }
    }
}
