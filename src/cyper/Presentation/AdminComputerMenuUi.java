package cyper.Presentation;

import cyper.DAO.ComputerDAO;
import java.util.Scanner;
import static cyper.Service.AdminComputer.*;

public class AdminComputerMenuUi {
    static Scanner sc = new Scanner(System.in);
    static  int choice =0;

    public static void AdminManagerComputer(){
        ComputerDAO computer = ComputerDAO.getInstance();
        while(true){
            System.out.println("=============Menu Admin Manager Computer==============");
            System.out.println("1. Hiển thị danh sách máy trạm");
            System.out.println("2. Thêm máy trạm");
            System.out.println("3. Sửa thông tin máy");
            System.out.println("4. Xoá máy trạm");
            System.out.println("0. Quay lại menu chính");
            System.out.print("Nhập lựa chọn của bạn: ");
            choice = sc.nextInt();
            sc.nextLine();
            switch(choice){
                case 1:
                    ComputerDAO.getComputer();
                    displayComputerTable();
                    break;
                case 2:
                    addComputer();
                    break;
                case 3:
                    updateComputer();
                    break;
                case 4:
                    deleteComputer();
                    // xóa
                    break;
                case 0: return;
                default:
                    System.out.println("Lựa chọn của bạn không hợp lệ");
                    break;
            }
        }
    }
}
