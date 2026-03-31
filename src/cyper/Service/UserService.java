package cyper.Service;

import cyper.DAO.UserDAO;

public class UserService {
    private static UserDAO userDAO = new UserDAO();
    // Hiển thị số dư định dạng đẹp
    public static void showWallet(int userId) {
        double balance = userDAO.getBalance(userId);
        System.out.printf(">>> Số dư hiện tại của bạn: %,.0f VNĐ\n", balance);
    }
    // Nạp tiền (Dành cho Admin hoặc Staff nạp cho khách)
    public String topUp(int userId, double amount) {
        if (amount <= 0) return "LỖI: Số tiền nạp phải lớn hơn 0!";
        boolean success = userDAO.updateBalance(userId, amount);
        return success ? "SUCCESS: Nạp tiền thành công!" : "LỖI: Nạp tiền thất bại!";
    }

    // Thanh toán (Trừ tiền)
    public boolean processPayment(int userId, double totalCost) {
        double currentBalance = userDAO.getBalance(userId);
        if (currentBalance < totalCost) {
            System.out.println(">>> LỖI: Tài khoản không đủ tiền! Vui lòng nạp thêm.");
            return false;
        }
        return userDAO.updateBalance(userId, -totalCost); // Trừ tiền (số âm)
    }
}
