package jad.assignment.silvercare.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AvailabilityDAO {
  public static ArrayList<Availability> getAvailabilityByProductId(int productId) throws SQLException {
    Connection conn = null;

    ArrayList<Availability> availabilityArr = new ArrayList<>();
    Availability availabilityBean;

    try {
      conn = DBConn.getConnection();

      // Build sql query
      final String sql = new StringBuilder()
          .append("SELECT * FROM caregiver_availability ")
          .append("WHERE product_id = ? ")
          .append("ORDER BY availability_date, start_time;")
          .toString();

      final PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setInt(1, productId);

      final ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        availabilityBean = new Availability();
        availabilityBean.setAvailabilityId(rs.getInt("availability_id"));
        availabilityBean.setCaregiverId(rs.getInt("caregiver_id"));
        availabilityBean.setAvailabilityDate(rs.getDate("availability_date"));
        availabilityBean.setStartTime(rs.getTime("start_time"));
        availabilityBean.setEndTime(rs.getTime("end_time"));
        availabilityBean.setCreatedAt(rs.getTimestamp("created_at"));
        availabilityBean.setUpdatedAt(rs.getTimestamp("updated_at"));

        availabilityArr.add(availabilityBean);
      }
    } catch (Exception e) {
      System.out.print("Availability DB Error:" + e);
      return availabilityArr;
    } finally {
      if (conn != null) {
        conn.close();
      }
    }

    return availabilityArr;
  }

  public static Availability getAvailabilityById(int availabilityId) throws SQLException {
    Connection conn = null;

    Availability availabilityBean = null;

    try {
      conn = DBConn.getConnection();

      // Build sql query
      final String sql = new StringBuilder()
          .append("SELECT * FROM caregiver_availability ")
          .append("WHERE availability_id = ?;")
          .toString();

      final PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setInt(1, availabilityId);

      final ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        availabilityBean = new Availability();
        availabilityBean.setAvailabilityId(rs.getInt("availability_id"));
        availabilityBean.setCaregiverId(rs.getInt("caregiver_id"));
        availabilityBean.setAvailabilityDate(rs.getDate("availability_date"));
        availabilityBean.setStartTime(rs.getTime("start_time"));
        availabilityBean.setEndTime(rs.getTime("end_time"));
        availabilityBean.setCreatedAt(rs.getTimestamp("created_at"));
        availabilityBean.setUpdatedAt(rs.getTimestamp("updated_at"));
      }
    } catch (Exception e) {
      System.out.print("Availability DB Error:" + e);
      return availabilityBean;
    } finally {
      if (conn != null) {
        conn.close();
      }
    }

    return availabilityBean;
  }
}