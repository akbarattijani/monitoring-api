package controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import database.Connection.Connection;
import httpactions.ApiAuth;
import model.SampleModel;
import play.mvc.BodyParser;
import play.mvc.Result;
import utils.Body;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static play.mvc.Controller.request;
import static play.mvc.Results.status;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

@ApiAuth
public class Sample {

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

    @BodyParser.Of(value = BodyParser.Json.class , maxLength = 1024 * 1024 * 1024)
    public static Result insertAll() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            final List<SampleModel> body = mapper.readValue(request().body().asText(), new TypeReference<List<SampleModel>>(){});

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
            for (SampleModel sampleModel : body) {
                preparedStatement.setInt(numberParam, sampleModel.getId());
                preparedStatement.setString(numberParam + 1, sampleModel.getBiner());

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
