package cyper.Models;

public class OrderDetail {
    private int id;
    private int orderId;
    private int foodId;
    private int quantity;
    private double priceAtOrder;

    public OrderDetail() {}

    public OrderDetail(int id, int orderId, int foodId, int quantity, double priceAtOrder) {
        this.id = id;
        this.orderId = orderId;
        this.foodId = foodId;
        this.quantity = quantity;
        this.priceAtOrder = priceAtOrder;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPriceAtOrder() {
        return priceAtOrder;
    }

    public void setPriceAtOrder(double priceAtOrder) {
        this.priceAtOrder = priceAtOrder;
    }
}
