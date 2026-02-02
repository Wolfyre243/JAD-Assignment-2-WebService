package jad.assignment.silvercare.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class EventBookingDAO {
  public static Integer createEventBooking(int eventId, String guestName, String guestEmail) {
    Connection conn = null;
    Integer bookingId;

    try {
      conn = DBConn.getConnection();

      // Build sql query
      final String sql = new StringBuilder()
          .append("INSERT INTO event_booking")
          .append("(event_id, guest_name, guest_email)")
          .append("VALUES (?,?,?);")
          .toString();

      final PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      stmt.setInt(1, eventId);
      stmt.setString(2, guestName);
      stmt.setString(3, guestEmail);

      int nrow = stmt.executeUpdate();
      bookingId = stmt.getGeneratedKeys().next() ? stmt.getGeneratedKeys().getInt(1) : null;

    } catch (Exception e) {
      System.out.print("Event Booking DB Error:" + e);
      return null;
    } finally {
      if (conn != null) {
        try {
          conn.close();
        } catch (Exception e) {
          System.out.print("DB Connection Close Error:" + e);
        }
      }
    }

    return bookingId;
  }
}
