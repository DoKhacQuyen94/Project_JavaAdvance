package cyper.Service;

import cyper.DAO.BookingDAO;
import cyper.DAO.ComputerDAO;
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
}
