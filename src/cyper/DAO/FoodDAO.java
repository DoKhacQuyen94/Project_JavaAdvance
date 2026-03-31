package cyper.DAO;

import cyper.Models.Food;
import cyper.util.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodDAO {
    public static List<Food> getAllFoods() {
        List<Food> list = new ArrayList<>();
        String sql = "select * from foods";
        try (Connection conn = DBContext.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Food(rs.getInt("id"), rs.getString("name"),
                        rs.getDouble("price"), rs.getInt("stock"),
                        rs.getString("description")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public static boolean addFood(Food f) {
        String sql = "insert into foods (name, price, stock, description) values (?, ?, ?, ?)";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, f.getName());
            ps.setDouble(2, f.getPrice());
            ps.setInt(3, f.getStock());
            ps.setString(4, f.getDescription());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public static boolean deleteFood(int id) {
        String sql = "delete from foods where id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public static Food getFoodById(int id) {
        String sql = "select * from foods where id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Food(rs.getInt("id"), rs.getString("name"),
                        rs.getDouble("price"), rs.getInt("stock"),
                        rs.getString("description"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
    public static boolean updateFood(Food f) {
        String sql = "update foods set name = ?, price = ?, stock = ?, description = ? where id = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, f.getName());
            ps.setDouble(2, f.getPrice());
            ps.setInt(3, f.getStock());
            ps.setString(4, f.getDescription());
            ps.setInt(5, f.getId());
            int rowAffected = ps.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}