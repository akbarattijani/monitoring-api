package controllers;

import algoritm.KMeans;
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
                object.put("responseCode", bundle[0]);
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

    @Connect(
            database = Database.POSTGRESQL,
            host = "ec2-23-23-173-30.compute-1.amazonaws.com",
            databaseName = "d87s2lf0vv7l32",
            userName = "ppxiknjbrpshfp",
            password = "dadde9e960e7acc54bf9b09a35ef98f4ec01a149e1560b4a8c4f6909271cc76c",
            port = "5432"
    )
    public static Result login(String userName, String password) {
        try {
//            String[] from = {"A", "A", "A", "B", "B", "C", "D", "D", "E", "E", "E", "F", "F", "G", "G", "H", "H", "H", "I", "I", "J", "J",
//                             "K", "K", "K", "L", "L"};
//            String[] to = {"B", "D", "F", "C", "D", "I", "I", "H", "F", "G", "H", "G", "N", "H", "N", "I", "K", "N", "J", "K", "M", "K",
//                             "M", "L", "N", "N", "M"};
//            double[] weight = {2, 5, 3, 2, 3, 3, 2, 1, 2, 4, 5, 7, 10, 1, 4, 1, 1, 6, 1, 1, 9, 1, 7, 6, 7, 6, 5};
//
//            new MinimumSpanningTree().kruskal(from, to, weight);

            new KMeans().test();

            model.Customer customer = new CustomerImpl().login(userName, password);
            if (customer != null) {
                JSONObject object = new JSONObject();
                object.put("id", customer.getId());
                object.put("firstName", customer.getFirstName());
                object.put("lastName", customer.getLastName());
                object.put("userName", customer.getUserName());
                object.put("email", customer.getEmail());
                object.put("address", customer.getAddress());
                object.put("city", customer.getCity());
                object.put("province", customer.getProvince());
                object.put("created", customer.getCreated());
                object.put("createdBy", customer.getCreatedBy());

                // Closing database connection
                Connection.disconnect();

                return Body.echo(enums.Result.REQUEST_OK, object.toString());
            } else {
                Connection.disconnect();
                throw new TranslateException("RC-22");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Connection.disconnect();
            return status(401, e.getMessage());
        }
    }
}
