package cyper.Service;

import cyper.DAO.BookingDAO;
import cyper.DAO.ComputerDAO;
import cyper.DAO.OrderDAO;
import cyper.DAO.UserDAO;
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
        // 1. Tìm phiên chơi đang active của Quinn
        Booking b = bookingDAO.getBookingWithPrice(userId);
        if (b == null) return "Không có phiên chơi nào đang hoạt động.";
        // 2. Tính tiền máy (Sử dụng giá từ DB thay vì fix cứng 10000)
        long startTime = b.getStartTime().getTime();
        long endTime = System.currentTimeMillis();
        // Tính số giờ chơi (double để có số lẻ phút)
        double diffHours = (endTime - startTime) / (1000.0 * 60 * 60);
        if (diffHours < 0.1) diffHours = 0.1; // Tối thiểu 6 phút
        // Giả sử bạn đã JOIN lấy được giá máy trong hàm getBookingWithPrice
        double pricePerHour = b.getTotalAmount() > 0 ? b.getTotalAmount() : 10000;
        double computerMoney = diffHours * pricePerHour;

        // 3. Tính tiền đồ ăn (LƯU Ý: Phải dùng instance orderDAO, không gọi trực tiếp từ Class)
        // Sửa OrderDAO.getTotalFoodMoney -> orderDAO.getTotalFoodMoney
        double foodMoney = OrderDAO.getTotalFoodMoney(b.getId());

        double finalTotal = computerMoney + foodMoney;

        // 4. LOGIC VÍ ĐIỆN TỬ: Kiểm tra và trừ tiền
        UserService userService = new UserService(); // Khởi tạo service ví
        double currentBalance = new UserDAO().getBalance(userId);

        if (currentBalance < finalTotal) {
            return String.format("THẤT BẠI: Số dư không đủ!\n" +
                    "- Tổng cần trả: %,.0f VNĐ\n" +
                    "- Hiện có: %,.0f VNĐ\n" +
                    "Vui lòng nạp thêm tiền để trả máy.", finalTotal, currentBalance);
        }
        if (userService.processPayment(userId, finalTotal)) {
            if (bookingDAO.checkout(b.getId(), b.getComputerId(), finalTotal)) {
                return String.format("=== HÓA ĐƠN THANH TOÁN ===\n" +
                                "- Tiền máy: %,.0f VNĐ (%.1f giờ)\n" +
                                "- Tiền F&B: %,.0f VNĐ\n" +
                                "- TỔNG CỘNG: %,.0f VNĐ\n" +
                                "Số dư còn lại: %,.0f VNĐ",
                        computerMoney, diffHours, foodMoney, finalTotal, (currentBalance - finalTotal));
            }
        }
        return "Lỗi hệ thống khi xử lý thanh toán!";
    }
}
