package com.anla.Order.seeder;

import com.anla.Order.command.CreateOrderCommand;
import com.anla.Order.service.OrderCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.math.BigDecimal;
import java.util.Random;

/**
 * Standalone Order Seeder Application
 * Run dengan: mvnw exec:java -Dexec.mainClass="com.anla.Order.seeder.OrderSeeder"
 */
@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = "com.anla.Order")
public class OrderSeeder implements CommandLineRunner {

    @Autowired
    private OrderCommandService orderCommandService;

    // Sample data
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
        System.setProperty("spring.profiles.active", "seeder"); 
        SpringApplication.run(OrderSeeder.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("🚀 Starting Order Seeder - Creating 500 orders");
        
        Random random = new Random();
        int successCount = 0;
        int errorCount = 0;
        long startTime = System.currentTimeMillis();
        
        for (int i = 1; i <= 500; i++) {
            try {
                CreateOrderCommand command = new CreateOrderCommand();
                command.setPelangganId(CUSTOMER_IDS[random.nextInt(CUSTOMER_IDS.length)]);
                command.setProductId(PRODUCT_IDS[random.nextInt(PRODUCT_IDS.length)]);
                command.setJumlah(QUANTITIES[random.nextInt(QUANTITIES.length)]);
                
                double price = PRICES[random.nextInt(PRICES.length)];
                double total = price * command.getJumlah();
                command.setTotal(BigDecimal.valueOf(total));
                
                String orderId = orderCommandService.handleCreateOrder(command);
                successCount++;
                
                if (i % 50 == 0) {
                    log.info("✅ Created {} orders, latest ID: {}", i, orderId);
                }
                
                // Small delay to avoid overwhelming the system
                Thread.sleep(10);
                
            } catch (Exception e) {
                errorCount++;
                log.error("❌ Error creating order {}: {}", i, e.getMessage());
            }
        }
        
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        
        log.info("🎉 Seeder completed in {} ms!", executionTime);
        log.info("✅ Successfully created: {} orders", successCount);
        log.info("❌ Errors: {} orders", errorCount);
        
        // Exit the application
        System.exit(0);
    }
}