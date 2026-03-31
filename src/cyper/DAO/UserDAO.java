package cyper.DAO;

import cyper.Models.User;
import cyper.util.DBContext;
import cyper.util.HashUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public static User login(String username, String password) {
        try{
            Connection con = DBContext.getConnection();
            PreparedStatement pst = con.prepareStatement("SELECT * FROM users WHERE username = ?");
            pst.setString(1,username);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                if(HashUtil.checkPassword(password,rs.getString("password"))) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("full_name"),
                            rs.getString("phone"),
                            rs.getString("role"),
                            rs.getDouble("balance")
                    );
                }else {
                    return null;
                }

            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }


    public static boolean register(User user) throws IllegalArgumentException {
        try {
            Connection con = DBContext.getConnection();
            PreparedStatement pst = con.prepareStatement("insert into users (username, password, full_name, phone, role, balance) values (?,?,?,?,?,?);");
            pst.setString(1, User.getUSERNAME());
            pst.setString(2, HashUtil.HashUtil(user.getPassword()));
            pst.setString(3, User.getFULLNAME());
            pst.setString(4, User.getPHONE());
            pst.setString(5, User.getROLE());
            pst.setDouble(6, User.getBalance());
            int rowAffected = pst.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public static boolean updateBalance(int userId, double amount) {
        // amount > 0 là nạp tiền, amount < 0 là trừ tiền
        String sql = "UPDATE users SET balance = balance + ? WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, amount);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public double getBalance(int userId) {
        String sql = "SELECT balance FROM users WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("balance");
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }
}
