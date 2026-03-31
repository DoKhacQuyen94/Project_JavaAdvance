package cyper.Models;

public class Computer {
    private static int ID;
    private static String Name;
    private static String zone;
    private static String text;
    private static double price_per_hour;
    private static String status;

    public Computer() {
    }

    public Computer(int ID, String name, String zone, String text, double price_per_hour, String status) {
        this.ID = ID;
        this.Name = name;
        this.zone = zone;
        this.text = text;
        this.price_per_hour = price_per_hour;
        this.status = status;
    }

    public static int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public static String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public static String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public static String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public static double getPrice_per_hour() {
        return price_per_hour;
    }

    public void setPrice_per_hour(double price_per_hour) {
        this.price_per_hour = price_per_hour;
    }

    public static String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public static void displayComputer(){
        System.out.println("Computer ID: " + getID());
        System.out.println("Computer Name: " + getName());
        System.out.println("Computer zone: " + getZone());
        System.out.println("Computer text: " + getText());
        System.out.println("Computer price_per_hour: " + getPrice_per_hour());
        System.out.println("Computer status: " + getStatus());
    }
}

