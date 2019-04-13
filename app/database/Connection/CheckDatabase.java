package database.Connection;

import enums.Database;
import play.mvc.With;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

@With(DatabaseAction.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface CheckDatabase {
    Database database() default Database.MYSQL;
    String host() default "localhost";
    String port() default "3306";
    String databaseName() default "akbarapi";
    String schema() default "public";
    String userName() default "root";
    String password() default "";
}
