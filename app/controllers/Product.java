package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import database.Connection.Connect;
import database.Connection.Connection;
import enums.Database;
import org.json.simple.JSONArray;
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

public class Product {
    @Connect(
            database = Database.POSTGRESQL,
            host = "ec2-23-23-173-30.compute-1.amazonaws.com",
            databaseName = "d87s2lf0vv7l32",
            userName = "ppxiknjbrpshfp",
            password = "dadde9e960e7acc54bf9b09a35ef98f4ec01a149e1560b4a8c4f6909271cc76c",
            port = "5432"
    )
    @BodyParser.Of(value = BodyParser.Json.class , maxLength = 1024 * 1024 * 1024)
    public static Result insert() {
        try {
            final JsonNode body = request().body().asJson();
            final String name = body.get("name").asText();
            final int stock = body.get("stock").asInt();
            final String pemasok = body.get("pemasok").asText();
            final String date = body.get("date").asText();

            String sql = "INSERT INTO m_produk (name, stock, pemasok, date) values (?,?,?,?)";
            PreparedStatement preparedStatement = Connection.getConnection().prepareStatement(sql);

            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, stock);
            preparedStatement.setString(3, pemasok);
            preparedStatement.setString(4, date);

            int count = preparedStatement.executeUpdate();
            if (count > 0) {
                preparedStatement.close();
                Connection.disconnect();

                return Body.echo(enums.Result.REQUEST_OK, "Inserting SUCCESS");
            } else {
                preparedStatement.close();
                Connection.disconnect();

                return Body.echo(enums.Result.REQUEST_OK, "Inserting FAILED");
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
    public static Result getProducts() {
        try {
            JSONArray result = new JSONArray();
            String select = "SELECT * FROM m_produk";

            PreparedStatement preparedStatement = Connection.getConnection().prepareStatement(select);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                JSONObject object = new JSONObject();
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int stock = rs.getInt("stock");
                String pemasok = rs.getString("pemasok");
                String date = rs.getString("date");

                object.put("id", id);
                object.put("name", name);
                object.put("stock", stock);
                object.put("pemasok", pemasok);
                object.put("date", date);

                result.add(object);
            }

            // Closing database connection
            rs.close();
            preparedStatement.close();
            Connection.disconnect();

            return Body.echo(enums.Result.REQUEST_OK, result.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Connection.disconnect();
            return status(401, e.getMessage());
        }
    }
}
