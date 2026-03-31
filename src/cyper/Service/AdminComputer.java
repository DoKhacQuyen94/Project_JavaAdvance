package cyper.Service;

import cyper.DAO.ComputerDAO;
import cyper.Models.Computer;

import java.util.List;
import java.util.Scanner;

public class AdminComputer {
    static Scanner sc = new Scanner(System.in);
    public static void displayComputerTable() {
        List<Computer> list = ComputerDAO.computers;
        System.out.println("--------------------------------------------------------------------------------------------------------------------");
        System.out.printf("| %-3s | %-20s | %-10s | %-20s | %-8s | %-20s | \n", "ID", "Tên máy", "Phòng", "Mô tả", "Giá tiền/ giờ","Trạng thái");
        System.out.println("--------------------------------------------------------------------------------------------------------------------");
        for (Computer f : list) {
            System.out.printf("| %-3d | %-20s | %-10s | %-20s | %-8.2f | %-20s |\n",
                    f.getID(), f.getName(), f.getZone(), f.getText(), f.getPrice_per_hour(),f.getStatus());
        }
        System.out.println("---------------------------------------------------------------------------");
    }
    public static void addComputer(){
        String name ="";
        String text ="";
        String zone = "";
        Integer price_per_hour;
        boolean check = false;
        while (true){
            System.out.print("Nhập tên máy trạm: ");
            name = sc.next();
            if (name.isEmpty()) {
                System.out.println("Không được để trống");
            } else if (name.isBlank()) {
                System.out.println("Không được có khoảng trắng");
            } else {
                break;
            }
        }
        while (true){
            System.out.print("Nhập Khu vực (1: standard || 2: vip || 3.stream): ");
            int select = sc.nextInt();
            if(select == 1){
                zone = "standard";
                break;
            }else if(select == 2){
                zone = "vip";
                break;
            }else if(select == 3){
                zone = "stream";
                break;
            }else{
                System.out.println("Nhập lựa chọn sai vui lòng chọn lại!");
            }
        }
        while (true){
            System.out.print("Nhập mô tả: ");
            text = sc.next();
            if (text.isEmpty()) {
                System.out.println("Không đưc để trống");
            }else {
                break;
            }
        }

        while (true){
            sc.nextLine();
            System.out.print("Nhập giá tiền/ giờ: ");
            String price = sc.next();
            try {
                price_per_hour= Integer.parseInt(price);
                break;
            }catch (NumberFormatException e){
                System.out.println("Cần nhập số nha");
            }

        }
        Computer computerNew = new Computer(0,name,zone,text,price_per_hour,"available");
        try {
            check = ComputerDAO.insert(computerNew);
        }catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
        if(check){
            System.out.println("Thêm máy thành công");
        }else {
            System.out.println("Thêm máy thất bại");
        }
    }
    public static void updateComputer(){
        // sửa
        System.out.print("Nhập ID máy cần sửa: ");
        int id = Integer.parseInt(sc.nextLine());
        Computer oldComp = ComputerDAO.getComputerById(id);
        if (oldComp == null) {
            System.out.println(">>> Lỗi: Không tìm thấy máy có ID này!");
            return;
        }
        System.out.println("--- THÔNG TIN CŨ ---");
        System.out.println("Tên: " + oldComp.getName() + " | Khu vực: " + oldComp.getZone() + " | Giá: " + oldComp.getPrice_per_hour());
        System.out.println("--- NHẬP THÔNG TIN MỚI (Để trống nếu không muốn đổi) ---");
        System.out.print("Tên mới: ");
        String newName = sc.nextLine();
        if (newName.isEmpty()) newName = oldComp.getName();
        System.out.print("Khu vực mới (STANDARD/VIP/STREAM): ");
        String newZone = sc.nextLine().toUpperCase();
        if (newZone.isEmpty()) newZone = oldComp.getZone();
        System.out.print("Giá tiền mới: ");
        String priceInput = sc.nextLine();
        double newPrice = priceInput.isEmpty() ? oldComp.getPrice_per_hour() : Double.parseDouble(priceInput);
        oldComp.setName(newName);
        oldComp.setZone(newZone);
        oldComp.setPrice_per_hour(newPrice);

        if (ComputerDAO.updateComputer(oldComp)) {
            System.out.println(">>> Cập nhật máy thành công!");
        } else {
            System.out.println(">>> Cập nhật thất bại!");
        }
    }
    public static void deleteComputer(){
        String conform = "";
        int idDelete = 0;
        System.out.print("Nhập id máy cần xóa: ");
        try{
            idDelete = Integer.parseInt(sc.nextLine());
        }catch (NumberFormatException e){
            System.out.println(e.getMessage());
        }
        Computer checkComputer = ComputerDAO.getComputerById(idDelete);
        if (checkComputer == null) {
            System.out.println(">>> Lỗi: Không tìm thấy máy có ID này!");
            return;
        }else {
            System.out.print("Bạn có xác nhận xóa máy có id: "+idDelete +" không(y/n): ");
            conform = sc.nextLine();
            if (!conform.isEmpty()){
                if(conform.equals("y")){
                    if(ComputerDAO.deleteComputer(idDelete)){
                        System.out.println(">> Xóa máy thành công");

                    }else {
                        System.out.println(">> Xóa máy thất bại");
                    }
                }else {
                    System.out.println(">> Bạn đã hủy xóa");
                }
            }else{
                System.out.println(">> Bạn đã hủy xóa vì không xác nhận");
            }
        }

    }
}