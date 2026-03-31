package cyper.Presentation;


import cyper.Models.User;
import cyper.Service.Auth;

import java.util.Scanner;

public class AuthMenuUi {
    static Scanner sc = new Scanner(System.in);
    public static User auth(){
        int choice = 0;
        while (true){
            System.out.println("=============Menu Cyper Gamming==============");
            System.out.println("1.Đăng Nhập");
            System.out.println("2.Đăng Ký");
            System.out.println("3.Thoát");
            System.out.print("Vui Lòng Nhập lựa chọn của bạn: ");
            choice = sc.nextInt();
            sc.nextLine();
            switch (choice){
                case 1:
                    User user = Auth.Login();
                    if(user != null){
                        return user;
                    }
                    break;
                case 2:
                    Auth.Register();
                    break;
                case 3:
                    return null;
                default:
                    System.out.println("Lựa chọn sai vui lòng chọn lại!!");
                    break;
            }
        }
    }

}
