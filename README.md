# StockOrderEngine
Stock Order Engine to match the orders

# Real-Time Stock Trading Engine
This project implements a real-time **Stock Trading Engine** in Java that efficiently matches **Buy** and **Sell** orders using a lock-free data structure.

## Features
Efficient order matching with O(n) time complexity.  
Uses AtomicReference for thread safety and lock-free concurrency.  
Supports up to 1,024 stocks** (tickers) with dynamic order entries.  
Handles race conditions effectively with atomic operations.  
Implements robust error handling for invalid or incomplete data.  

---

##  How It Works
The engine handles the following:

1. **Add Orders:**  
   - `addOrder(String orderType, String tickerSymbol, int quantity, int price)`
   - Orders are added in sorted order for efficient matching:
     - **BUY orders** → Sorted in **descending** order (highest price first).
     - **SELL orders** → Sorted in **ascending** order (lowest price first).

2. **Match Orders:**  
   - `matchOrder()` efficiently matches BUY and SELL orders with **O(n)** complexity.

3. **Display Orders:**  
   - `displayOrders()` shows the active BUY and SELL orders.

---

## Prerequisites
Ensure you have the following installed on your system:

- **Java JDK 11** or higher
- **IDE** (e.g., IntelliJ IDEA, VS Code, Eclipse) for coding convenience

---

## Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/tarunreddy35/StockOrderEngine.git
   cd StockOrderEngine
Compile the project:
javac TradingEngine.java

Run the project:
java TradingEngine


====Usage====
==Adding Orders==
Use the addOrder() method to place orders.
Example:
orderBook.addOrder("BUY", "AAPL", 50, 200);
orderBook.addOrder("SELL", "GOOGL", 30, 150);

==Matching Orders ==
Call the matchOrder() method to match eligible orders.
Example:
orderBook.matchOrder();

Displaying Orders
Use displayOrders() to view active orders.
Example:
orderBook.displayOrders();

Example Output

== Orders Before Matching ==
BUY | TICKER3 | 50 @ $450
SELL | TICKER3 | 40 @ $400
BUY | TICKER2 | 30 @ $500
SELL | TICKER2 | 20 @ $490

== Matching Orders ==
Matched: 40 shares of TICKER3 at $400
Matched: 20 shares of TICKER2 at $490

===== Orders After Matching ===========
BUY | TICKER3 | 10 @ $450
BUY | TICKER2 | 10 @ $500

================================================================================================ Testing =================================================================================================
Run the TradingEngine class to automatically simulate orders with random values.
The program will print the list of orders before and after matching
