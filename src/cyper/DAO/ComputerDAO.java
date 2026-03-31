package cyper.DAO;

import cyper.Models.Computer;
import cyper.util.DBContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ComputerDAO {
    public static ComputerDAO instance;
    public static List<Computer> computers = new ArrayList<Computer>();
    public static ComputerDAO getInstance() {
        if (instance == null) {
            instance = new ComputerDAO();
        }
        return null;
    }
    public static void getComputer(){
        try {
            computers.clear();
            Connection conn = DBContext.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from computers");
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                Computer computer = new Computer();
                computer.setID(rs.getInt("id"));
                computer.setName(rs.getString("name"));
                computer.setZone(rs.getString("zone"));
                computer.setText(rs.getString("specs"));
                computer.setPrice_per_hour(rs.getDouble("price_per_hour"));
                computer.setStatus(rs.getString("status"));
                computers.add(computer);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public static boolean insert(Computer pc){
        try {
            Connection conn = DBContext.getConnection();
            PreparedStatement pst = conn.prepareStatement("insert into computers(name, zone,specs, price_per_hour, status) values(?, ?, ?,?, 'available')");
            pst.setString(1, pc.getName());
            pst.setString(2, pc.getZone());
            pst.setString(3, pc.getText());
            pst.setDouble(4,pc.getPrice_per_hour());
            int check = pst.executeUpdate();
            if(check>0){
                return true;
            }else {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static Computer getComputerById(int id) {
        String sql = "select * from computers where id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Computer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("zone"),
                        rs.getString("specs"),
                        rs.getDouble("price_per_hour"),
                        rs.getString("status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean updateComputer(Computer c) {
        String sql = "update computers set name=?, zone=?, specs=?, price_per_hour=? where id=?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getZone());
            ps.setString(3, c.getText());
            ps.setDouble(4, c.getPrice_per_hour());
            ps.setInt(5, c.getID());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean deleteComputer(int id) {
        String sql = "delete from computers where id=?";
        try {
            Connection conn = DBContext.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public List<Computer> getAvailablePCs() {
        List<Computer> list = new ArrayList<>();
        String sql = "select * from computers where status = 'available'";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Computer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("zone"),
                        rs.getString("specs"),
                        rs.getShort("price_per_hour"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
