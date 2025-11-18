# Quick Start Guide

## Step 1: Build All Services

Run the build script to compile all microservices:

```bash
./build-all.sh
```

Or build manually:
```bash
# Build each service
cd eureka && mvn clean package -DskipTests && cd ..
cd Marketplace/Produk && mvn clean package -DskipTests && cd ../..
cd Marketplace/Pelanggan && mvn clean package -DskipTests && cd ../..
cd Marketplace/Order && mvn clean package -DskipTests && cd ../..
cd Marketplace/api-gateway && mvn clean package -DskipTests && cd ../..
cd GenericService/command-service && mvn clean package -DskipTests && cd ../..
cd GenericService/query-service && mvn clean package -DskipTests && cd ../..
cd Perpustakaan/Buku && mvn clean package -DskipTests && cd ../..
cd Perpustakaan/anggota && mvn clean package -DskipTests && cd ../..
cd Perpustakaan/Peminjaman && mvn clean package -DskipTests && cd ../..
cd Perpustakaan/Pengembalian && mvn clean package -DskipTests && cd ../..
cd Perpustakaan/api-gateway && mvn clean package -DskipTests && cd ../..
```

## Step 2: Run Services

### Option A: Run All Services
```bash
docker-compose up -d
```

### Option B: Run Specific Domain

**Marketplace Services:**
```bash
cd Marketplace
docker-compose up -d
```

**Generic Services (CQRS):**
```bash
cd GenericService
docker-compose up -d
```

**Perpustakaan Services:**
```bash
cd Perpustakaan
docker-compose up -d
```

## Step 3: Verify Services

Check Eureka Dashboard:
```bash
http://localhost:8761
```

Check service health:
```bash
curl http://localhost:8081/actuator/health  # Produk
curl http://localhost:8082/actuator/health  # Pelanggan
curl http://localhost:8083/actuator/health  # Order
curl http://localhost:8084/actuator/health  # Buku
curl http://localhost:8085/actuator/health  # Anggota
curl http://localhost:8086/actuator/health  # Pengembalian
curl http://localhost:8087/actuator/health  # Peminjaman
curl http://localhost:8091/actuator/health  # Command Service
curl http://localhost:8092/actuator/health  # Query Service
```

## Step 4: Access Services

### API Gateways
- Marketplace Gateway: http://localhost:9000
- Perpustakaan Gateway: http://localhost:9001

### Individual Services
- Produk: http://localhost:8081
- Pelanggan: http://localhost:8082
- Order: http://localhost:8083
- Buku: http://localhost:8084
- Anggota: http://localhost:8085
- Pengembalian: http://localhost:8086
- Peminjaman: http://localhost:8087
- Command Service: http://localhost:8091
- Query Service: http://localhost:8092

### Management UIs
- Eureka: http://localhost:8761
- RabbitMQ (Marketplace): http://localhost:15672 (admin/password)
- RabbitMQ (Generic): http://localhost:15674 (guest/guest)
- RabbitMQ (Perpustakaan): http://localhost:15675 (guest/guest)

### Database Consoles (H2)
Access H2 console at: http://localhost:{service-port}/h2-console

**Connection settings:**
- JDBC URL: `jdbc:h2:file:/app/data/{service-name}`
- Username: `sa`
- Password: `password`

## Step 5: View Logs

```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f produk-service

# Last 100 lines
docker-compose logs --tail=100 -f
```

## Step 6: Stop Services

```bash
# Stop all services
docker-compose down

# Stop and remove volumes (deletes data)
docker-compose down -v
```

## Troubleshooting

### Services not starting?
1. Check if JAR files exist:
   ```bash
   find . -name "*.jar" -path "*/target/*"
   ```

2. Check logs:
   ```bash
   docker-compose logs [service-name]
   ```

3. Rebuild:
   ```bash
   docker-compose build --no-cache
   docker-compose up -d
   ```

### Port already in use?
Find and stop the process:
```bash
sudo lsof -i :8761  # Check who's using port 8761
sudo kill -9 <PID>  # Kill the process
```

### Service not registered in Eureka?
- Wait 30-60 seconds for registration
- Check Eureka at http://localhost:8761
- Verify service logs for connection errors

## Common Commands

```bash
# Restart a service
docker-compose restart produk-service

# Rebuild a service
docker-compose build produk-service
docker-compose up -d produk-service

# Execute command in container
docker-compose exec produk-service bash

# View container stats
docker stats

# Clean up everything
docker-compose down -v
docker system prune -a
```

## Service Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    Eureka Server (8761)                  │
└─────────────────────────────────────────────────────────┘
                            │
           ┌────────────────┼────────────────┐
           │                │                │
    ┌──────▼──────┐  ┌──────▼──────┐  ┌──────▼──────┐
    │ Marketplace │  │   Generic   │  │ Perpustakaan │
    │             │  │    (CQRS)   │  │             │
    │ ┌─────────┐ │  │ ┌─────────┐ │  │ ┌─────────┐ │
    │ │ Produk  │ │  │ │ Command │ │  │ │  Buku   │ │
    │ │ (8081)  │ │  │ │ (8091)  │ │  │ │ (8084)  │ │
    │ └─────────┘ │  │ └─────────┘ │  │ └─────────┘ │
    │             │  │      │      │  │             │
    │ ┌─────────┐ │  │ ┌─────────┐ │  │ ┌─────────┐ │
    │ │Pelanggan│ │  │ │  Query  │ │  │ │ Anggota │ │
    │ │ (8082)  │ │  │ │ (8092)  │ │  │ │ (8085)  │ │
    │ └─────────┘ │  │ └─────────┘ │  │ └─────────┘ │
    │             │  │             │  │             │
    │ ┌─────────┐ │  │  MongoDB   │  │ ┌─────────┐ │
    │ │  Order  │ │  │  (27017)   │  │ │Peminjaman│
    │ │ (8083)  │ │  │             │  │ │ (8087)  │ │
    │ └─────────┘ │  │  RabbitMQ  │  │ └─────────┘ │
    │             │  │  (5674)    │  │             │
    │  RabbitMQ  │  └─────────────┘  │ ┌─────────┐ │
    │   (5672)   │                   │ │Pengembali│
    │             │                   │ │ (8086)  │ │
    │ ┌─────────┐ │                   │ └─────────┘ │
    │ │ Gateway │ │                   │             │
    │ │ (9000)  │ │                   │  RabbitMQ  │
    │ └─────────┘ │                   │  (5675)    │
    └─────────────┘                   │             │
                                      │ ┌─────────┐ │
                                      │ │ Gateway │ │
                                      │ │ (9001)  │ │
                                      │ └─────────┘ │
                                      └─────────────┘
```

For more details, see [DOCKER_SETUP.md](DOCKER_SETUP.md)
