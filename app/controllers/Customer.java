package controllers;

import database.Connection.Connect;
import database.Connection.Connection;
import database.dao.impl.CustomerImpl;
import enums.Database;
import exception.Bundle;
import exception.TranslateException;
import httpactions.ApiAuth;
import mapper.Mapper;
import org.json.simple.JSONObject;
import play.mvc.BodyParser;
import play.mvc.Result;
import utils.Body;

import java.sql.Timestamp;
import java.util.Date;

import static play.mvc.Controller.request;
import static play.mvc.Results.status;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

@ApiAuth
public class Customer {

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
            model.Customer customer = new Mapper().toModel(request().body().asJson(), model.Customer.class);

            if (customer.getFirstName() == null) {
                throw new TranslateException("RC-02");
            }

            if (customer.getLastName() == null) {
                throw new TranslateException("RC-03");
            }

            if (customer.getUserName() == null) {
                throw new TranslateException("RC-04");
            }

            Date date= new Date();
            long time = date.getTime();
            Timestamp ts = new Timestamp(time);

            customer.setCreated(ts.toString());
            customer.setCreatedBy(1);

            int count = new CustomerImpl().save(customer);
            if (count > 0) {
                // Closing database connection;
                Connection.disconnect();
                String[] bundle = new Bundle().getMessage("RC-00");
                JSONObject object = new JSONObject();
                object.put("errCode", bundle[0]);
                object.put("message", bundle[1]);
                
                return Body.echo(enums.Result.REQUEST_OK, object.toString());
            }

            // Closing database connection
            Connection.disconnect();

            throw new TranslateException("RC-01");
        } catch (Exception e) {
            e.printStackTrace();
            Connection.disconnect();
            return status(401, e.getMessage());
        }
    }
}
