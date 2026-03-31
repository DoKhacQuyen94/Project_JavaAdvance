package cyper.Models;

public class Computer {
    private int ID;
    private String Name;
    private String zone;
    private String text;
    private double price_per_hour;
    private String status;

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

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getPrice_per_hour() {
        return price_per_hour;
    }

    public void setPrice_per_hour(double price_per_hour) {
        this.price_per_hour = price_per_hour;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public void displayComputer(){
        System.out.println("Computer ID: " + getID());
        System.out.println("Computer Name: " + getName());
        System.out.println("Computer zone: " + getZone());
        System.out.println("Computer text: " + getText());
        System.out.println("Computer price_per_hour: " + getPrice_per_hour());
        System.out.println("Computer status: " + getStatus());
    }
}

