package jad.assignment.silvercare.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GuestDAO {
  public static Integer createGuest(String firstName, String lastName, Date dob, String gender, String phone,
      String email) throws SQLException {
    Connection conn = null;
    int nrow;

    try {
      conn = DBConn.getConnection();

      // Build sql query
      final String sql = new StringBuilder()
          .append("INSERT INTO guest")
          .append("(first_name, last_name, dob, gender, phone, email)")
          .append("VALUES (?,?,?,?,?,?);")
          .toString();

      final PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setString(1,firstName);
      stmt.setString(2, lastName);
      stmt.setDate(3, dob);
      stmt.setString(4, gender);
      stmt.setString(5, phone);
      stmt.setString(6, email);

      nrow = stmt.executeUpdate();

      return nrow;
    } catch (Exception e) {
      System.out.print("Guest DB Error:" + e);
      return null;
    } finally {
      if (conn != null) {
        conn.close();
      }
    }
  }
}
