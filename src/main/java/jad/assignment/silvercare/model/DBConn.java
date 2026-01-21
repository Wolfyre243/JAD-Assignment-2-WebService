package jad.assignment.silvercare.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConn {
  public static Connection getConnection() {
    String dbUrlString = "jdbc:postgresql://ep-calm-water-a18qegew-pooler.ap-southeast-1.aws.neon.tech/neondb?user=neondb_owner&password=npg_6dLgQzjR9OEa&sslmode=require&channelBinding=require";
    String dbUser = "neondb_owner";
    String dbPassword = "npg_6dLgQzjR9OEa";
    String dbClass = "org.postgresql.Driver";

    Connection connection = null;

    try {
      Class.forName(dbClass);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

    try {
      connection = DriverManager.getConnection(dbUrlString, dbUser, dbPassword);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return connection;
  }
}
