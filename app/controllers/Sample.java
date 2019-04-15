package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import database.Connection.CheckDatabase;
import database.Connection.Connection;
import enums.Database;
import httpactions.ApiAuth;
import play.mvc.BodyParser;
import play.mvc.Result;
import utils.Body;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static play.mvc.Controller.request;
import static play.mvc.Results.status;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

@ApiAuth
public class Sample {

    @CheckDatabase(
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
            final int idUser = body.path("id").asInt();
            final String biner = body.path("biner").asText();

            String sql = "INSERT INTO m_sample (id_user, biner) values (?,?)";
            PreparedStatement preparedStatement = Connection.getConnection().prepareStatement(sql);

            preparedStatement.setInt(1, idUser);
            preparedStatement.setString(2, biner);

            int count = preparedStatement.executeUpdate();

            return Body.echo(enums.Result.REQUEST_OK, "Inserting Success : " + count);
        } catch (SQLException e) {
            e.printStackTrace();
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
    public static Result insertAll() {
        try {
//            ObjectMapper mapper = new ObjectMapper();
            final JsonNode body = request().body().asJson();
//            final List<SampleModel> body = mapper.treeToValue(request().body().asJson(), List.class);

            int count = 0;
            int numberParam = 1;
            String sql = "INSERT INTO m_sample (id_user, biner) values ";

            for (int i = 0; i < body.size(); i++) {
                if (i > 0) {
                    sql += ",(?, ?)";
                } else {
                    sql += "(?, ?)";
                }
            }

            PreparedStatement preparedStatement = Connection.getConnection().prepareStatement(sql);
            for (int i = 0; i < body.size(); i++) {
                JsonNode detail = body.get(i);
                preparedStatement.setInt(numberParam, detail.path("id").asInt());
                preparedStatement.setString(numberParam + 1, detail.path("biner").asText());

                numberParam++;
            }

            count = preparedStatement.executeUpdate();

            return Body.echo(enums.Result.REQUEST_OK, "Inserting Success : " + count);
        } catch (Exception e) {
            e.printStackTrace();
            return status(401, e.getMessage());
        }
    }
}
