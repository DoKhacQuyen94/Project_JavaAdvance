package cyper.Service;

import cyper.DAO.BookingDAO;
import cyper.DAO.ComputerDAO;
import cyper.DAO.OrderDAO;
import cyper.Models.Booking;
import cyper.Models.Order;

import java.util.List;

public class StaffService {
    private BookingDAO bookingDAO = new BookingDAO();
    private OrderDAO orderDAO = new OrderDAO();
    private ComputerDAO computerDAO = new ComputerDAO();
    public List<Booking> getPendingBookings() {
        // Gọi DAO để lấy các booking có status = 'pending'
        return bookingDAO.getPendingBookings();
    }
    public String approveMachineRequest(int bookingId, int computerId) {
        // Kiểm tra xem máy đó hiện tại có thực sự trống không (Double check)
        var pc = computerDAO.getComputerById(computerId);
        if (pc == null || !pc.getStatus().equals("available")) {
            return "LỖI: Máy này hiện không sẵn sàng để duyệt!";
        }
        // Gọi hàm approve trong DAO (Hàm có sử dụng Transaction)
        boolean success = bookingDAO.approveBooking(bookingId, computerId);

        if (success) {
            return "SUCCESS: Đã kích hoạt máy " + pc.getName() + ". Khách có thể đăng nhập.";
        } else {
            return "LỖI: Không thể cập nhật trạng thái. Vui lòng kiểm tra kết nối DB.";
        }
    }
    public List<Order> getPendingFoodOrders() {
        return orderDAO.getPendingOrders();
    }
    public String confirmOrderDelivered(int orderId) {
        // Cập nhật trạng thái đơn hàng sang 'delivered'
        boolean success = orderDAO.updateOrderStatus(orderId, "delivered");

        if (success) {
            return "SUCCESS: Đã xác nhận giao món cho đơn hàng #" + orderId;
        } else {
            return "LỖI: Thao tác thất bại!";
        }
    }
}
