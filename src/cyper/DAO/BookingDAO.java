package cyper.DAO;

import cyper.Models.Booking;
import cyper.util.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class BookingDAO {
    // 1. Khách hàng đặt máy (Tạo yêu cầu chờ duyệt)
    public boolean createBooking(Booking b) {
        String sql = "INSERT INTO bookings (user_id, computer_id, start_time, status) VALUES (?, ?, NOW(), 'pending')";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, b.getUserId());
            ps.setInt(2, b.getComputerId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // 2. Nhân viên Duyệt máy (QUAN TRỌNG: Dùng Transaction)
    public boolean approveBooking(int bookingId, int computerId) {
        String sqlApprove = "UPDATE bookings SET status = 'active', start_time = NOW() WHERE id = ?";
        String sqlOccupy = "UPDATE computers SET status = 'occupied' WHERE id = ?";
        Connection conn = null;
        try {
            conn = DBContext.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps1 = conn.prepareStatement(sqlApprove);
                 PreparedStatement ps2 = conn.prepareStatement(sqlOccupy)) {
                ps1.setInt(1, bookingId);
                ps1.executeUpdate();
                ps2.setInt(1, computerId);
                ps2.executeUpdate();
                conn.commit();
                return true;
            }
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }
    // 3. Lấy danh sách các yêu cầu đang chờ (Dùng cho Staff)
    public List<Booking> getPendingBookings() {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE status = 'pending'";
        try (Connection conn = DBContext.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Booking(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("computer_id"),
                        rs.getTimestamp("start_time"),
                        rs.getTimestamp("end_time"),
                        rs.getDouble("total_amount"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    public Booking getBookingWithPrice(int userId) {
        String sql = "SELECT b.*, c.price_per_hour FROM bookings b " +
                "JOIN computers c ON b.computer_id = c.id " +
                "WHERE b.user_id = ? AND b.status = 'active' LIMIT 1";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Booking b = new Booking();
                b.setId(rs.getInt("id"));
                b.setComputerId(rs.getInt("computer_id"));
                b.setStartTime(rs.getTimestamp("start_time"));
                // Lưu tạm price_per_hour vào một biến hoặc dùng trực tiếp trong Service
                return b;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
    public boolean checkout(int bookingId, int computerId, double totalAmount) {
        String sqlBooking = "UPDATE bookings SET status = 'completed', end_time = NOW(), total_amount = ? WHERE id = ?";
        String sqlComputer = "UPDATE computers SET status = 'available' WHERE id = ?";

        Connection conn = null;
        try {
            conn = DBContext.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps1 = conn.prepareStatement(sqlBooking);
                 PreparedStatement ps2 = conn.prepareStatement(sqlComputer)) {

                ps1.setDouble(1, totalAmount);
                ps1.setInt(2, bookingId);
                ps1.executeUpdate();

                ps2.setInt(1, computerId);
                ps2.executeUpdate();

                conn.commit();
                return true;
            }
        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (Exception ex) {}
            return false;
        }
    }
    public int getActiveBookingId(int userId) {
        String sql = "SELECT id FROM bookings WHERE user_id = ? AND status = 'active' LIMIT 1";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) { e.printStackTrace(); }
            return -1;
        }
}
