import java.sql.*;
import java.util.UUID;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * Standalone Database Seeder for Order Events
 * Compile: javac -cp ".:postgresql-42.7.3.jar" DatabaseSeeder.java
 * Run: java -cp ".:postgresql-42.7.3.jar" DatabaseSeeder
 */
public class DatabaseSeeder {
    
    // Database connection details
    private static final String POSTGRES_URL = "jdbc:postgresql://localhost:5432/orderdb";
    private static final String POSTGRES_USER = "postgres";
    private static final String POSTGRES_PASSWORD = "";
    
    // Sample data arrays
    private static final String[] CUSTOMER_IDS = {
        "CUST-001", "CUST-002", "CUST-003", "CUST-004", "CUST-005",
        "CUST-006", "CUST-007", "CUST-008", "CUST-009", "CUST-010"
    };
    
    private static final String[] PRODUCT_IDS = {
        "PROD-001", "PROD-002", "PROD-003", "PROD-004", "PROD-005",
        "PROD-006", "PROD-007", "PROD-008", "PROD-009", "PROD-010"
    };
    
    private static final double[] PRICES = {50000.0, 75000.0, 100000.0, 150000.0, 200000.0};
    private static final int[] QUANTITIES = {1, 2, 3, 5, 10};
    
    public static void main(String[] args) {
        System.out.println("🚀 Starting Order Event Seeder...");
        
        try {
            // Load PostgreSQL driver
            Class.forName("org.postgresql.Driver");
            
            // Connect to database
            Connection conn = DriverManager.getConnection(POSTGRES_URL, POSTGRES_USER, POSTGRES_PASSWORD);
            System.out.println("✅ Connected to PostgreSQL database");
            
            // Create 500 order events
            createOrderEvents(conn, 500);
            
            conn.close();
            System.out.println("✅ Database connection closed");
            
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("🎉 Seeder completed successfully!");
    }
    
    private static void createOrderEvents(Connection conn, int count) throws SQLException {
        String insertEventSQL = """
            INSERT INTO order_events (event_id, aggregate_id, event_type, payload, timestamp) 
            VALUES (?, ?, 'ORDER_CREATED', ?, ?)
        """;
        
        String insertOrderSQL = """
            INSERT INTO orders (order_id, pelanggan_id, product_id, jumlah, tanggal, status, total) 
            VALUES (?, ?, ?, ?, ?, 'PENDING', ?)
        """;
        
        PreparedStatement eventStmt = conn.prepareStatement(insertEventSQL);
        PreparedStatement orderStmt = conn.prepareStatement(insertOrderSQL);
        
        Random random = new Random();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        
        System.out.println("📝 Creating " + count + " order events...");
        
        for (int i = 1; i <= count; i++) {
            String orderId = UUID.randomUUID().toString();
            String customerId = CUSTOMER_IDS[random.nextInt(CUSTOMER_IDS.length)];
            String productId = PRODUCT_IDS[random.nextInt(PRODUCT_IDS.length)];
            int quantity = QUANTITIES[random.nextInt(QUANTITIES.length)];
            double price = PRICES[random.nextInt(PRICES.length)];
            double total = price * quantity;
            LocalDateTime now = LocalDateTime.now();
            
            // Create JSON payload for event
            String payload = String.format("""
                {"orderId":"%s","pelangganId":"%s","productId":"%s","jumlah":%d,"total":%.2f,"tanggal":"%s","status":"PENDING"}
                """, orderId, customerId, productId, quantity, total, now.toString());
            
            // Insert into order_events (Event Store)
            eventStmt.setObject(1, UUID.randomUUID()); // event_id
            eventStmt.setString(2, orderId); // aggregate_id
            eventStmt.setString(3, payload); // payload
            eventStmt.setTimestamp(4, Timestamp.valueOf(now)); // timestamp
            eventStmt.addBatch();
            
            // Insert into orders (Read Model)
            orderStmt.setString(1, orderId);
            orderStmt.setString(2, customerId);
            orderStmt.setString(3, productId);
            orderStmt.setInt(4, quantity);
            orderStmt.setTimestamp(5, Timestamp.valueOf(now));
            orderStmt.setDouble(6, total);
            orderStmt.addBatch();
            
            // Execute batch every 50 records
            if (i % 50 == 0) {
                eventStmt.executeBatch();
                orderStmt.executeBatch();
                System.out.println("✅ Inserted " + i + " records");
            }
        }
        
        // Execute remaining records
        eventStmt.executeBatch();
        orderStmt.executeBatch();
        
        eventStmt.close();
        orderStmt.close();
        
        System.out.println("🎯 Successfully created " + count + " order events!");
    }
}