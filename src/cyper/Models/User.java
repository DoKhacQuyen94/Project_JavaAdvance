package cyper.Models;

public class User {
    private static int  ID;
    private static String USERNAME;
    private String Password;
    private static String FULLNAME;
    private static String PHONE;
    private static String ROLE;
    private static double Balance;

    public User() {
    }

    public User(int ID, String USERNAME, String password, String FULLNAME, String PHONE, String ROLE, double balance) {
        this.ID = ID;
        this.USERNAME = USERNAME;
        this.Password = password;
        this.FULLNAME = FULLNAME;
        this.PHONE = PHONE;
        this.ROLE = ROLE;
        this.Balance = balance;
    }

    public static int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public static String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public static String getFULLNAME() {
        return FULLNAME;
    }

    public void setFULLNAME(String FULLNAME) {
        this.FULLNAME = FULLNAME;
    }

    public static String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }

    public static String getROLE() {
        return ROLE;
    }

    public void setROLE(String ROLE) {
        this.ROLE = ROLE;
    }

    public static double getBalance() {
        return Balance;
    }

    public void setBalance(double balance) {
        Balance = balance;
    }

    public static void display() {
        System.out.println("ID: " + getID());
        System.out.println("USERNAME: " + getUSERNAME());
        System.out.println("FULLNAME: " + getFULLNAME());
        System.out.println("PHONE: " + getPHONE());
        System.out.println("ROLE: " + getROLE());
        System.out.println("Balance: " + getBalance());

    }
}
