package cyper.Presentation;

import cyper.Service.AdminFoodMenu;

import java.util.Scanner;

public class AdminFoodMenuUi {
    private static final Scanner sc = new Scanner(System.in);
    public static void showMenu() {
        while (true) {
            System.out.println("\n===== QUẢN LÝ DỊCH VỤ F&B =====");
            System.out.println("1. Hiển thị danh mục đồ ăn/thức uống");
            System.out.println("2. Thêm dịch vụ mới");
            System.out.println("3. Sửa thông tin (Giá/Tồn kho)");
            System.out.println("4. Xóa dịch vụ");
            System.out.println("0. Quay lại Menu chính");
            System.out.print("Chọn chức năng: ");

            String choice = sc.nextLine();
            switch (choice) {
                case "1": AdminFoodMenu.displayFoodTable(); break;
                case "2": AdminFoodMenu.addFoodMenu(); break;
                case "3": AdminFoodMenu.editFood(); break;
                case "4": AdminFoodMenu.deleteFoodMenu(); break;
                case "0": return;
                default: System.out.println("Lỗi: Lựa chọn không hợp lệ!");
            }
        }
    }
}