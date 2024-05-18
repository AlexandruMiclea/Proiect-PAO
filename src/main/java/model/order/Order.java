package model.order;

//import model.location.Franchise;
import model.people.Person;
import model.people.PersonType;

public class Order {
    private static Long orderCount = 1L;
    private final Long orderID = orderCount;
    private OrderType orderType;
    private OrderStatus orderStatus;
    private Person deliveryDriver;
    private final Person client;

    //Franchise franchise;

    public Order(OrderType orderType, Person client) {
        assert (client.getPersonType() == PersonType.CLIENT);
        this.orderType = orderType;
        this.orderStatus = OrderStatus.RECEIVED;
        this.client = client;
        orderCount += 1;
    }

    public void assignDeliveryDriver(Person person){
        if (orderType == OrderType.DELIVERY && person.getPersonType() == PersonType.DELIVERY_DRIVER){
            this.deliveryDriver = person;
        } else {
            if (orderType != OrderType.DELIVERY){
                System.out.println("ERROR: assigned delivery driver to wrong order type.");
            }
            if (person.getPersonType() != PersonType.DELIVERY_DRIVER){
                System.out.println("ERROR: assigned wrong type of person to order.");
            }
        }
    }

    public Long getOrderID() {
        return orderID;
    }

    @Override
    public String toString() {
        String ans = "Order no: " +  orderID + orderStatus + ". Type of order: " + orderType + ". Order placed by " + client.toString() + ". Type of order: " + orderType + ".";
        if (deliveryDriver != null && orderType == OrderType.DELIVERY) {
            ans += " Order will be delivered by: " + deliveryDriver.toString() + ".";
        }
        return ans;
    }
}
