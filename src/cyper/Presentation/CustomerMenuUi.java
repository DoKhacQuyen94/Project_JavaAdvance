package cyper.Presentation;

import cyper.DAO.BookingDAO;
import cyper.DAO.ComputerDAO;
import cyper.DAO.FoodDAO;
import cyper.Models.Computer;
import cyper.Models.Food;
import cyper.Models.OrderDetail;
import cyper.Models.User;
import cyper.Service.AdminFoodMenu;
import cyper.Service.BookingService;
import cyper.Service.OrderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CustomerMenuUi {
    private static Scanner sc = new Scanner(System.in);
    private static BookingService bookingService = new BookingService();
    private static OrderService orderService = new OrderService();
    private static ComputerDAO compDAO = new ComputerDAO();
    private static FoodDAO foodDAO = new FoodDAO();
    private static User currentUser;

    public static void showMenu(User user) {
        currentUser = user;
        while (true) {
            System.out.println("\n======= MENU KHÁCH HÀNG: " + user.getUSERNAME().toUpperCase() + " =======");
            System.out.println("1. Xem danh sách máy trống");
            System.out.println("2. Đặt máy chơi (Gửi yêu cầu duyệt)");
            System.out.println("3. Gọi đồ ăn & Thức uống");
            System.out.println("0. Đăng xuất");
            System.out.print("Lựa chọn của bạn: ");
            String choice = sc.nextLine();
            switch (choice) {
                case "1": showAvailablePCs(); break;
                case "2": handleBooking(); break;
                case "3": handleOrderFood(); break;
                case "0": return;
                default: System.out.println(">>> Lỗi: Lựa chọn không hợp lệ!");
            }
        }
    }
    private static void showAvailablePCs() {
        List<Computer> list = compDAO.getAvailablePCs();
        System.out.println("\n" + "=".repeat(60));
        System.out.printf("| %-4s | %-15s | %-10s | %-10s |\n", "ID", "Tên máy", "Khu vực", "Giá/Giờ");
        System.out.println("-".repeat(60));
        for (Computer c : list) {
            System.out.printf("| %-4d | %-15s | %-10s | %-10.0f |\n",
                    c.getID(), c.getName(), c.getZone(), c.getPrice_per_hour());
        }
        System.out.println("=".repeat(60));
    }
    // 2. Logic Đặt máy (Thông qua Service để check trạng thái)
    private static void handleBooking() {
        showAvailablePCs();
        System.out.print("Nhập ID máy bạn muốn ngồi: ");
        try {
            int pcId = Integer.parseInt(sc.nextLine());
            // Gọi Service để xử lý nghiệp vụ
            String result = bookingService.startBooking(currentUser.getID(), pcId);
            System.out.println(">>> " + result);
        } catch (NumberFormatException e) {
            System.out.println(">>> Lỗi: ID máy phải là số!");
        }
    }
    // 3. Logic Gọi món (Giỏ hàng trung gian)
    private static void handleOrderFood() {
        int bId = new BookingDAO().getActiveBookingId(currentUser.getID());
        if (bId == -1) {
            System.out.println(">>> Lỗi: Bạn chưa có phiên chơi nào được kích hoạt!");
            System.out.println(">>> Vui lòng đặt máy và chờ nhân viên duyệt trước khi gọi món.");
            return;
        }
        System.out.println(">>> Hệ thống ghi nhận bạn đang ngồi máy. Đang chuẩn bị menu...");
        List<OrderDetail> cart = new ArrayList<>();
        double tempTotal = 0;

        while (true) {
            new AdminFoodMenu().displayFoodTable();
            System.out.print("Nhập ID món (0 để CHỐT ĐƠN): ");
            int fId = Integer.parseInt(sc.nextLine());
            if (fId == 0) break;

            Food f = foodDAO.getFoodById(fId);
            if (f == null) {
                System.out.println(">>> Lỗi: Món không tồn tại!");
                continue;
            }
            System.out.print("Nhập số lượng: ");
            int qty = Integer.parseInt(sc.nextLine());

            // Lưu vào danh sách chi tiết (Tầng trung gian)
            OrderDetail item = new OrderDetail(0, 0, fId, qty, f.getPrice());
            cart.add(item);
            tempTotal += f.getPrice() * qty;
            System.out.println(">>> Đã thêm " + qty + " " + f.getName() + " vào giỏ hàng.");
        }
        if (!cart.isEmpty()) {
            System.out.println("\n--- HÓA ĐƠN DỰ KIẾN ---");
            System.out.printf("Tổng tiền đồ ăn: %,.0f VNĐ\n", tempTotal);
            System.out.print("Xác nhận đặt món? (y/n): ");
            if (sc.nextLine().equalsIgnoreCase("y")) {
                // Đẩy sang Service để kiểm tra kho và lưu DB
                String result = orderService.placeOrder(currentUser.getID(), bId, cart);
                System.out.println(">>> " + result);
            }
        }
    }

}
