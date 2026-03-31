package cyper;

import cyper.Models.User;
import cyper.Presentation.AdminFoodMenuUi;
import cyper.Presentation.AuthMenuUi;
import cyper.Presentation.CustomerMenuUi;
import cyper.Presentation.StaffMenuUi;

import java.util.Scanner;

import static cyper.Presentation.AdminComputerMenuUi.AdminManagerComputer;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        User user;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            user = AuthMenuUi.auth();
            if (user == null){
                System.out.println("Bạn đã thoát");
                return;
            }
            if(user.getROLE().equals("admin")){
                do{
                    System.out.println("=====Admin manager=====");
                    System.out.println("1. Quản lý máy trạm");
                    System.out.println("2. Quản lý Menu F&B");
                    System.out.println("3. Thoát");
                    System.out.print("Nhập lựa chọn của bạn: ");
                    choice = sc.nextInt();
                    sc.nextLine();
                    switch(choice){
                        case 1: AdminManagerComputer(); break;
                        case 2: AdminFoodMenuUi.showMenu(); break;
                        case 0:
                            System.out.println("Bạn đã thoát khỏi quản lý");break;
                        default:
                            System.out.println("Lựa chọn không hợp lệ");
                            break;
                    }
                }while (choice!=0);
            }else if(user.getROLE().equals("customer")){
                CustomerMenuUi.showMenu(user);
            }else if(user.getROLE().equals("staff")){
                StaffMenuUi.showMenu();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
