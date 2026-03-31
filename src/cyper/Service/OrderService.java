package cyper.Service;

import cyper.DAO.FoodDAO;
import cyper.DAO.OrderDAO;
import cyper.Models.Food;
import cyper.Models.Order;
import cyper.Models.OrderDetail;

import java.util.List;

public class OrderService {
    private OrderDAO orderDAO = new OrderDAO();
    private FoodDAO foodDAO = new FoodDAO();
    public String placeOrder(int userId, int bookingId, List<OrderDetail> cart) {
        if (cart.isEmpty()) return "LỖI: Giỏ hàng trống!";
        double totalPrice = 0;
        for (OrderDetail item : cart) {
            Food f = foodDAO.getFoodById(item.getFoodId());
            if (f.getStock() < item.getQuantity()) {
                return "LỖI: Món " + f.getName() + " không đủ số lượng trong kho!";
            }
            totalPrice += item.getPriceAtOrder() * item.getQuantity();
        }
        Order order = new Order(0, userId, bookingId, null, totalPrice, "pending");
        boolean success = orderDAO.createOrder(order, cart);
        return success ? "SUCCESS: Đơn hàng đã được gửi đi." : "LỖI: Đặt món thất bại!";
    }
}
