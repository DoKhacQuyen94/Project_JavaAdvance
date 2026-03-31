package cyper.Service;

import cyper.DAO.BookingDAO;
import cyper.DAO.ComputerDAO;
import cyper.DAO.OrderDAO;
import cyper.Models.Booking;
import cyper.Models.Computer;

public class BookingService {
    private BookingDAO bookingDAO = new BookingDAO();
    private ComputerDAO compDAO = new ComputerDAO();
    public String startBooking(int userId, int computerId) {
        Computer pc = compDAO.getComputerById(computerId);
        if (pc == null || !pc.getStatus().equals("available")) {
            return "LỖI: Máy không sẵn sàng hoặc đang hỏng!";
        }
        Booking b = new Booking(0, userId, computerId, null, null, 0, "pending");
        boolean success = bookingDAO.createBooking(b);
        return success ? "SUCCESS: Đã gửi yêu cầu đặt máy." : "LỖI: Hệ thống bận!";
    }
    public String endSession(int userId) {
        Booking b = bookingDAO.getBookingWithPrice(userId);
        if (b == null) return "Không có phiên chơi nào đang hoạt động.";
        long startTime = b.getStartTime().getTime();
        long endTime = System.currentTimeMillis();
        double diffHours = (endTime - startTime) / (1000.0 * 60 * 60);
        if (diffHours < 0.1) diffHours = 0.1;
        double computerMoney = diffHours * 10000;
        double foodMoney = OrderDAO.getTotalFoodMoney(b.getId());
        double finalTotal = computerMoney + foodMoney;
        if (bookingDAO.checkout(b.getId(), b.getComputerId(), finalTotal)) {
            return String.format("Kết thúc phiên chơi thành công!\n" +
                    "- Tiền máy: %,.0f VNĐ\n" +
                    "- Tiền F&B: %,.0f VNĐ\n" +
                    "- TỔNG CỘNG: %,.0f VNĐ", computerMoney, foodMoney, finalTotal);
        }
        return "Lỗi khi xử lý thanh toán!";
    }
}
