package jad.assignment.silvercare.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class BookingDAO {
  public static Integer createGuestBooking(int guestId, int productId, int caregiverId, String specialRequests,
      Timestamp bookingTimeslot) throws SQLException {
    Connection conn = null;
    Integer bookingId;
    
    try {
      conn = DBConn.getConnection();

      // Build sql query
      final String sql = new StringBuilder()
          .append("INSERT INTO guest_booking")
          .append("(guest_id, product_id, caregiver_id, special_requests, booking_timeslot)")
          .append("VALUES (?,?,?,?,?);")
          .toString();

      final PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      stmt.setInt(1, guestId);
      stmt.setInt(2, productId);
      stmt.setInt(3, caregiverId);
      stmt.setString(4, specialRequests);
      stmt.setTimestamp(5, bookingTimeslot);

      int nrow = stmt.executeUpdate();
      bookingId = stmt.getGeneratedKeys().next() ? stmt.getGeneratedKeys().getInt(1) : null;

      return bookingId;
    } catch (Exception e) {
      System.out.print("Booking DB Error:" + e);
      return null;
    } finally {
      if (conn != null) {
        conn.close();
      }
    }
  }
}
