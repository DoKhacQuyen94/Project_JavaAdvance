package cyper.Service;

import cyper.DAO.UserDAO;
import cyper.Models.User;
import cyper.Presentation.AuthMenuUi;

import java.util.Scanner;

public class Auth {
    public static String username;
    public static String password;
    static Scanner sc = new Scanner(System.in);
    public static User Login(){
        while(true){
            while(true){
                System.out.print("Nhập tên tài khoản: ");
                username = sc.nextLine();
                if (username.isBlank()){
                    System.out.println("UserName không có khoảng trắng hoặc để trống");
                }else {
                    break;
                }
            }
            while(true){
                System.out.print("Nhập mật khẩu: ");
                password = sc.nextLine();
                if (password.isBlank()){
                    System.out.println("Password không có khoảng trắng hoặc để trống");
                }else {
                    break;
                }
            }

            User user = UserDAO.login(username.trim(), password.trim());
            if (user == null) {
                System.out.println("Đăng nhập không thành công sai tài khoản mật khẩu!!");
            }else {
                System.out.println("Đăng nhập thành công");
//                            User.display();
                return user;
            }
        }
    }
    public static void Register(){
        boolean isSuccess = false;
        System.out.print("Nhập Username: ");
        String username = sc.nextLine();
        String pass = "";
        while (true) {
            System.out.print("Nhập Password (min 6 ký tự): ");
            pass = sc.nextLine();
            if (pass.length() >= 6) break;
            System.out.println("Lỗi: Mật khẩu quá ngắn!");
        }
        System.out.print("Nhập Họ và tên: ");
        String name = sc.nextLine();
        System.out.print("Nhập Số điện thoại: ");
        String phone = sc.nextLine();
        User newUser = new User(0, username, pass, name, phone, "customer", 0.0);
        try{
            isSuccess = UserDAO.register(newUser);

        }catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
        if (isSuccess) {
            System.out.println(">>> Đăng ký thành công! Bạn có thể đăng nhập ngay.");
        } else {
            System.out.println(">>> Đăng ký thất bại (Tên đăng nhập có thể đã tồn tại).");
        }
    }
}
