import java.util.concurrent.atomic.AtomicReference;
import java.util.Random;

// Node class representing a Stock Order
class StockOrderNode {
    String orderType;
    String tickerSymbol;
    int quantity;
    int price;
    StockOrderNode next;

    StockOrderNode(String orderType, String tickerSymbol, int quantity, int price) {
        this.orderType = orderType;
        this.tickerSymbol = tickerSymbol;
        this.quantity = quantity;
        this.price = price;
        this.next = null;
    }
}

// Order Book Class
class OrderBook {
    private AtomicReference<StockOrderNode> buyHead = new AtomicReference<>(null);
    private AtomicReference<StockOrderNode> sellHead = new AtomicReference<>(null);

    // Add Order to the Book
    public void addOrder(String orderType, String tickerSymbol, int quantity, int price) {
        if (quantity <= 0) {
            System.out.println("Invalid quantity. Order rejected.");
            return;
        }

        StockOrderNode newOrder = new StockOrderNode(orderType, tickerSymbol, quantity, price);
        if (orderType.equals("BUY")) {
            addToList(buyHead, newOrder, true); // Add to BUY orders (Descending order)
        } else if (orderType.equals("SELL")) {
            addToList(sellHead, newOrder, false); // Add to SELL orders (Ascending order)
        }
    }

    // Add Node to List (with sorting logic)
    private void addToList(AtomicReference<StockOrderNode> head, StockOrderNode newOrder, boolean isBuy) {
        StockOrderNode current = head.get();

        // Insert at the head if list is empty or newOrder fits best at the start
        if (current == null || (isBuy && newOrder.price >= current.price) || (!isBuy && newOrder.price <= current.price)) {
            newOrder.next = current;
            head.set(newOrder);  // Atomic insertion
            return;
        }

        // Insertion in sorted order
        StockOrderNode temp = current;
        while (temp.next != null && 
               ((isBuy && temp.next.price > newOrder.price) || 
                (!isBuy && temp.next.price < newOrder.price))) {
            temp = temp.next;
        }
        newOrder.next = temp.next;
        temp.next = newOrder;
    }

    // Match Orders (O(n) Complexity)
    public void matchOrder() {
        StockOrderNode buyCurrent = buyHead.get();
        StockOrderNode sellCurrent = sellHead.get();

        while (buyCurrent != null && sellCurrent != null) {
            if (buyCurrent.price >= sellCurrent.price) {
                int matchedQuantity = Math.min(buyCurrent.quantity, sellCurrent.quantity);

                System.out.println("Matched: " + matchedQuantity + " shares of " +
                        buyCurrent.tickerSymbol + " at $" + sellCurrent.price);

                // Adjust quantities or remove nodes
                buyCurrent.quantity -= matchedQuantity;
                sellCurrent.quantity -= matchedQuantity;

                if (buyCurrent.quantity == 0) {
                    buyHead.set(buyCurrent.next);  // Remove empty Buy order
                    buyCurrent = buyCurrent.next;
                } else {
                    buyCurrent = buyCurrent;  // Continue with the same Buy node
                }

                if (sellCurrent.quantity == 0) {
                    sellHead.set(sellCurrent.next);  // Remove empty Sell order
                    sellCurrent = sellCurrent.next;
                } else {
                    sellCurrent = sellCurrent;  // Continue with the same Sell node
                }
            } else {
                buyCurrent = buyCurrent.next;
                sellCurrent = sellCurrent.next;
            }
        }
    }

    // Display Orders
    public void displayOrders() {
        System.out.println("BUY Orders:");
        displayList(buyHead.get());

        System.out.println("SELL Orders:");
        displayList(sellHead.get());
    }

    private void displayList(StockOrderNode head) {
        if (head == null) {
            System.out.println("No orders available.");
            return;
        }

        StockOrderNode current = head;
        while (current != null) {
            System.out.println(current.orderType + " | " +
                    current.tickerSymbol + " | " +
                    current.quantity + " @ $" +
                    current.price);
            current = current.next;
        }
    }
}

// Trading Engine Class
public class TradingEngine {
    public static void main(String[] args) {
        OrderBook orderBook = new OrderBook();
        Random random = new Random();

        // Simulate Random Orders
        for (int i = 0; i < 10; i++) {
            String orderType = random.nextBoolean() ? "BUY" : "SELL";
            String tickerSymbol = "TICKER" + random.nextInt(5);
            int quantity = random.nextInt(100) + 1;
            int price = random.nextInt(500) + 1;

            orderBook.addOrder(orderType, tickerSymbol, quantity, price);
        }

        // Display Orders before Matching
        System.out.println("== Orders Before Matching ==");
        orderBook.displayOrders();

        // Match Orders
        System.out.println("\n== Matching Orders ==");
        orderBook.matchOrder();

        // Display Remaining Orders
        System.out.println("\n== Orders After Matching ==");
        orderBook.displayOrders();
    }
}
