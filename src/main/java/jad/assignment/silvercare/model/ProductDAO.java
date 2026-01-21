package jad.assignment.silvercare.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductDAO {
  public static ArrayList<Product> getAllProducts(String search) throws SQLException {
    Connection conn = null;

    ArrayList<Product> productArr = new ArrayList<>();
    Product productBean;

    try {
      conn = DBConn.getConnection();

      // Build sql query
      final String sql = new StringBuilder()
          .append("SELECT p.*, c.name as category ")
          .append("FROM product p, category c ")
          .append("WHERE p.category_id = c.category_id ")
          .append("AND ( ? IS NULL OR LOWER(p.name) LIKE LOWER(?) OR LOWER(p.description) LIKE LOWER(?) ) ")
          .append("ORDER BY updated_at, created_at DESC;")
          .toString();
      final PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setString(1, search);
      String searchPattern = (search != null) ? "%" + search + "%" : null;
      stmt.setString(2, searchPattern);
      stmt.setString(3, searchPattern);
      final ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        productBean = new Product();
        productBean.setProductId(rs.getInt("product_id"));
        productBean.setCategory(rs.getString("category"));
        productBean.setName(rs.getString("name"));
        productBean.setDescription(rs.getString("description"));
        productBean.setPrice(rs.getFloat("price"));
        productBean.setActive(rs.getBoolean("is_active"));
        productBean.setImagePath(rs.getString("image_path"));
        productBean.setCreatedAt(rs.getTimestamp("created_at"));
        productBean.setUpdatedAt(rs.getTimestamp("updated_at"));
        productArr.add(productBean);
      }

    } catch (Exception e) {
      System.out.print("Products DB Error:" + e);
    } finally {
      conn.close();
    }

    return productArr;
  }

  public static Product getProductById(int productId) throws SQLException {
    Connection conn = null;
    Product productBean = null;

    try {
      conn = DBConn.getConnection();

      // Build sql query
      final String sql = new StringBuilder()
          .append("SELECT p.*, c.name as category ")
          .append("FROM product p, category c ")
          .append("WHERE p.category_id = c.category_id AND p.product_id = ?;")
          .toString();
      final PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setInt(1, productId);
      final ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        productBean = new Product();
        productBean.setProductId(rs.getInt("product_id"));
        productBean.setCategory(rs.getString("category"));
        productBean.setName(rs.getString("name"));
        productBean.setDescription(rs.getString("description"));
        productBean.setPrice(rs.getFloat("price"));
        productBean.setActive(rs.getBoolean("is_active"));
        productBean.setImagePath(rs.getString("image_path"));
        productBean.setCreatedAt(rs.getTimestamp("created_at"));
        productBean.setUpdatedAt(rs.getTimestamp("updated_at"));
      }

    } catch (Exception e) {
      System.out.print("Product DB Error:" + e);
    } finally {
      conn.close();
    }

    return productBean;
  }
}
