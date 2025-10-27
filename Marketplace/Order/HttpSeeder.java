import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

/**
 * HTTP-based Order Seeder (Requires running server)
 * Compile: javac HttpSeeder.java
 * Run: java HttpSeeder
 */
public class HttpSeeder {
    
    private static final String API_URL = "http://localhost:8083/api/order";
    
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
        System.out.println("Starting HTTP Order Seeder...");
        System.out.println("Target API: " + API_URL);
        
        Random random = new Random();
        int successCount = 0;
        int errorCount = 0;
        long startTime = System.currentTimeMillis();
        
        for (int i = 1; i <= 50000; i++) {
            try {
                String customerId = CUSTOMER_IDS[random.nextInt(CUSTOMER_IDS.length)];
                String productId = PRODUCT_IDS[random.nextInt(PRODUCT_IDS.length)];
                int quantity = QUANTITIES[random.nextInt(QUANTITIES.length)];
                double price = PRICES[random.nextInt(PRICES.length)];
                double total = price * quantity;
                
                String jsonPayload = String.format(
                    "{\"pelangganId\":\"%s\",\"productId\":\"%s\",\"jumlah\":%d,\"total\":%.2f}",
                    customerId, productId, quantity, total
                );
                
                String response = sendPostRequest(API_URL, jsonPayload);
                
                if (response != null && response.contains("Order")) {
                    successCount++;
                    if (i % 50 == 0) {
                        System.out.println("Created " + i + " orders - " + response);
                    }
                } else {
                    errorCount++;
                    System.err.println("Error on request " + i + ": " + response);
                }
                
                // Small delay
                Thread.sleep(50);
                
            } catch (Exception e) {
                errorCount++;
                System.err.println("Exception on request " + i + ": " + e.getMessage());
            }
        }
        
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        
        System.out.println("\nHTTP Seeder completed in " + executionTime + " ms!");
        System.out.println("Successfully created: " + successCount + " orders");
        System.out.println("Errors: " + errorCount + " requests");
    }
    
    private static String sendPostRequest(String apiUrl, String jsonPayload) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        // Set request method and headers
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
        
        // Send request body
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonPayload.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        
        // Read response
        int responseCode = conn.getResponseCode();
        StringBuilder response = new StringBuilder();
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                responseCode >= 200 && responseCode < 300 ? conn.getInputStream() : conn.getErrorStream(),
                "utf-8"))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
        
        conn.disconnect();
        return response.toString();
    }
}