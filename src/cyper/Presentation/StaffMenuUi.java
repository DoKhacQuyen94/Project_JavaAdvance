package cyper.Presentation;

import cyper.Models.Booking;
import cyper.Models.Order;
import cyper.Service.StaffService;

import java.util.List;
import java.util.Scanner;

public class StaffMenuUi {
    private static Scanner sc = new Scanner(System.in);
    private static StaffService staffService = new StaffService();
    public static void showMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(20) + " MENU NHÂN VIÊN " + "=".repeat(20));
            System.out.println("1. Duyệt yêu cầu thuê máy (Pending Bookings)");
            System.out.println("2. Duyệt đơn gọi món F&B (Pending Orders)");
            System.out.println("0. Đăng xuất");
            System.out.print("Chọn chức năng: ");
            String choice = sc.nextLine();
            switch (choice) {
                case "1": handleApproveBooking(); break;
                case "2": handleApproveOrder(); break;
                case "0": return;
                default: System.out.println(">>> Lỗi: Lựa chọn không hợp lệ!");
            }
        }
    }
    private static void handleApproveBooking() {
        List<Booking> list = staffService.getPendingBookings();
        if (list.isEmpty()) {
            System.out.println(">>> Thông báo: Không có yêu cầu thuê máy nào đang chờ.");
            return;
        }
        System.out.println("\n" + "-".repeat(50));
        System.out.printf("| %-5s | %-10s | %-10s | %-10s |\n", "ID", "User ID", "Máy ID", "Trạng thái");
        System.out.println("-".repeat(50));
        for (Booking b : list) {
            System.out.printf("| %-5d | %-10d | %-10d | %-10s |\n",
                    b.getId(), b.getUserId(), b.getComputerId(), b.getStatus());
        }
        System.out.print("\nNhập ID Booking muốn DUYỆT (hoặc 0 để quay lại): ");
        int bId = Integer.parseInt(sc.nextLine());
        if (bId == 0) return;
        int cId = -1;
        for (Booking b : list) {
            if (b.getId() == bId) {
                cId = b.getComputerId();
                break;
            }
        }
        if (cId != -1) {
            String result = staffService.approveMachineRequest(bId, cId);
            System.out.println(">>> " + result);
        } else {
            System.out.println(">>> Lỗi: Không tìm thấy ID Booking này!");
        }
    }
    private static void handleApproveOrder() {
        List<Order> list = staffService.getPendingFoodOrders();
        if (list.isEmpty()) {
            System.out.println(">>> Thông báo: Không có đơn gọi món nào đang chờ.");
            return;
        }
        System.out.println("\n" + "-".repeat(60));
        System.out.printf("| %-5s | %-10s | %-10s | %-15s | %-15s |\n","ID", "Order_ID", "Booking ID", "Tổng tiền", "Thời gian");
        System.out.println("-".repeat(60));
        for (Order o : list) {
            System.out.printf("| %-5d | %-10d | %-10d | %-15.0f | %-15s |\n",
                    o.getId(), o.getBookingId(), o.getTotalPrice(), o.getOrderDate());
        }
        System.out.print("\nNhập ID Order đã giao xong (hoặc 0 để quay lại): ");
        int oId = Integer.parseInt(sc.nextLine());
        if (oId == 0) return;
        String result = staffService.confirmOrderDelivered(oId);
        System.out.println(">>> " + result);
    }
}
