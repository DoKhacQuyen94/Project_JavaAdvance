package cyper.Service;

import cyper.DAO.FoodDAO;
import cyper.Models.Food;
import java.util.List;
import java.util.Scanner;


public class AdminFoodMenu {
    static Scanner sc = new Scanner(System.in);
    public static void displayFoodTable() {
        FoodDAO foodDAO;
        List<Food> list = FoodDAO.getAllFoods();
        System.out.println("---------------------------------------------------------------------------");
        System.out.printf("| %-3s | %-20s | %-10s | %-8s | %-20s |\n", "ID", "Tên món", "Giá", "Tồn kho", "Mô tả");
        System.out.println("---------------------------------------------------------------------------");
        for (Food f : list) {
            System.out.printf("| %-3d | %-20s | %-10.2f | %-8d | %-20s |\n",
                    f.getId(), f.getName(), f.getPrice(), f.getStock(), f.getDescription());
        }
        System.out.println("---------------------------------------------------------------------------");
    }
    public static void addFoodMenu() {
        System.out.print("Nhập tên món: ");
        String name = sc.nextLine();
        System.out.print("Nhập giá bán: ");
        double price = Double.parseDouble(sc.nextLine());
        System.out.print("Nhập số lượng tồn kho: ");
        int stock = Integer.parseInt(sc.nextLine());
        System.out.print("Mô tả: ");
        String desc = sc.nextLine();

        if (FoodDAO.addFood(new Food(0, name, price, stock, desc))) {
            System.out.println(">>> Thêm món thành công!");
        } else {
            System.out.println(">>> Lỗi dữ liệu đầu vào!");
        }
    }
    public static void deleteFoodMenu() {
        System.out.print("Nhập ID món cần xóa: ");
        int id = Integer.parseInt(sc.nextLine());

        Food f = FoodDAO.getFoodById(id);
        if (f == null) {
            System.out.println(">>> Lỗi: ID không tồn tại!");
            return;
        }

        System.out.print("Bạn có chắc chắn muốn xóa món '" + f.getName() + "'? (Y/N): ");
        String confirm = sc.nextLine();
        if (confirm.equalsIgnoreCase("Y")) {
            if (FoodDAO.deleteFood(id)) {
                System.out.println(">>> Đã xóa thành công!");
            }
        } else {
            System.out.println(">>> Đã hủy thao tác xóa.");
        }
    }
    public static void editFood() {
        System.out.print("\nNhập ID món cần sửa: ");
        int id = Integer.parseInt(sc.nextLine());
        Food old = FoodDAO.getFoodById(id);

        if (old == null) {
            System.out.println(">>> LỖI: ID không tồn tại!");
            return;
        }

        System.out.println("Đang sửa: " + old.getName());
        System.out.print("Giá mới (Hiện tại: " + old.getPrice() + "): ");
        String pStr = sc.nextLine();
        if (!pStr.isEmpty()) old.setPrice(Double.parseDouble(pStr));

        System.out.print("Tồn kho mới (Hiện tại: " + old.getStock() + "): ");
        String sStr = sc.nextLine();
        if (!sStr.isEmpty()) old.setStock(Integer.parseInt(sStr));

        if (FoodDAO.updateFood(old)) {
            System.out.println(">>> THÀNH CÔNG: Đã cập nhật thông tin.");
        }
    }
}
