package cyper.DAO;

import cyper.Models.Order;
import cyper.Models.OrderDetail;
import cyper.util.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    // 1. Tạo hóa đơn gọi món (Dùng Transaction nối 2 bảng)
    public boolean createOrder(Order order, List<OrderDetail> details) {
        String sqlOrder = "INSERT INTO orders (user_id, booking_id, total_price, status) VALUES (?, ?, ?, 'pending')";
        String sqlDetail = "INSERT INTO order_details (order_id, food_id, quantity, price_at_order) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DBContext.getConnection();
            conn.setAutoCommit(false);
            int generatedOrderId = -1;
            try (PreparedStatement psOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS)) {
                psOrder.setInt(1, order.getUserId());
                // Nếu khách không ngồi máy (mua tại quầy) thì bookingId có thể là null
                if (order.getBookingId() > 0) {
                    psOrder.setInt(2, order.getBookingId());
                } else {
                    psOrder.setNull(2, Types.INTEGER);
                }
                psOrder.setDouble(3, order.getTotalPrice());
                psOrder.executeUpdate();

                ResultSet rs = psOrder.getGeneratedKeys();
                if (rs.next()) {
                    generatedOrderId = rs.getInt(1);
                }
            }
            // Bước B: Chèn danh sách món ăn vào bảng trung gian order_details
            if (generatedOrderId != -1) {
                try (PreparedStatement psDetail = conn.prepareStatement(sqlDetail)) {
                    for (OrderDetail d : details) {
                        psDetail.setInt(1, generatedOrderId);
                        psDetail.setInt(2, d.getFoodId()); // food_id chuẩn DB
                        psDetail.setInt(3, d.getQuantity());
                        psDetail.setDouble(4, d.getPriceAtOrder());
                        psDetail.addBatch();
                    }
                    psDetail.executeBatch();
                }
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
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
    public List<Order> getPendingOrders() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, c.name AS pc_name FROM orders o " +
                "JOIN bookings b ON o.booking_id = b.id " +
                "JOIN computers c ON b.computer_id = c.id " +
                "WHERE o.status = 'pending'";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getInt("id"));
                o.setUserId(rs.getInt("user_id"));
                o.setBookingId(rs.getInt("booking_id"));
                o.setTotalPrice(rs.getDouble("total_price"));
                o.setStatus(rs.getString("status"));
                o.setOrderDate(rs.getTimestamp("order_date"));
                list.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    // 3. Cập nhật trạng thái đơn hàng (Đã giao món/Đã thanh toán)
    public boolean updateOrderStatus(int orderId, String status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }
    public static double getTotalFoodMoney(int bookingId) {
        String sql = "SELECT SUM(total_price) as total FROM orders WHERE booking_id = ? AND status IN ('delivered', 'paid')";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("total");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
