package jad.assignment.silvercare.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EventDAO {
  public static ArrayList<Event> getAllEvents(String search) throws SQLException {
    Connection conn = null;

    ArrayList<Event> eventArr = new ArrayList<>();
    Event eventBean;

    try {
      conn = DBConn.getConnection();

      final String sql = new StringBuilder()
          .append("SELECT * ")
          .append("FROM event ")
          .append("WHERE ( ? IS NULL OR LOWER(title) LIKE LOWER(?) OR LOWER(description) LIKE LOWER(?) OR LOWER(location) LIKE LOWER(?) ) ")
          .append("ORDER BY updated_at, created_at DESC;")
          .toString();

      final PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setString(1, search);
      String searchPattern = (search != null) ? "%" + search + "%" : null;
      stmt.setString(2, searchPattern);
      stmt.setString(3, searchPattern);
      stmt.setString(4, searchPattern);
      final ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        eventBean = new Event();
        eventBean.setEventId(rs.getInt("event_id"));
        eventBean.setTitle(rs.getString("title"));
        eventBean.setDescription(rs.getString("description"));
        eventBean.setLocation(rs.getString("location"));
        eventBean.setStartTime(rs.getTimestamp("start_time"));
        eventBean.setEndTime(rs.getTimestamp("end_time"));
        eventBean.setCapacity(rs.getInt("capacity"));
        eventBean.setActive(rs.getBoolean("is_active"));
        eventBean.setCreatedBy(rs.getInt("created_by"));
        eventBean.setImagePath(rs.getString("image_path"));
        eventBean.setCreatedAt(rs.getTimestamp("created_at"));
        eventBean.setUpdatedAt(rs.getTimestamp("updated_at"));
        eventArr.add(eventBean);
      }

    } catch (Exception e) {
      System.out.print("Events DB Error:" + e);
    } finally {
      if (conn != null) conn.close();
    }

    return eventArr;
  }

  public static Event getEventById(int eventId) throws SQLException {
    Connection conn = null;
    Event eventBean = null;

    try {
      conn = DBConn.getConnection();

      final String sql = new StringBuilder()
          .append("SELECT * FROM event WHERE event_id = ?;")
          .toString();
      final PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setInt(1, eventId);
      final ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        eventBean = new Event();
        eventBean.setEventId(rs.getInt("event_id"));
        eventBean.setTitle(rs.getString("title"));
        eventBean.setDescription(rs.getString("description"));
        eventBean.setLocation(rs.getString("location"));
        eventBean.setStartTime(rs.getTimestamp("start_time"));
        eventBean.setEndTime(rs.getTimestamp("end_time"));
        eventBean.setCapacity(rs.getInt("capacity"));
        eventBean.setActive(rs.getBoolean("is_active"));
        eventBean.setCreatedBy(rs.getInt("created_by"));
        eventBean.setImagePath(rs.getString("image_path"));
        eventBean.setCreatedAt(rs.getTimestamp("created_at"));
        eventBean.setUpdatedAt(rs.getTimestamp("updated_at"));
      }

    } catch (Exception e) {
      System.out.print("Event DB Error:" + e);
    } finally {
      if (conn != null) conn.close();
    }

    return eventBean;
  }
}
