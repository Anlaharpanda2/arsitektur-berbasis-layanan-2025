# Docker Setup Documentation

## Overview
This project contains multiple microservices organized into different domains:
- **Marketplace**: E-commerce services (Produk, Pelanggan, Order)
- **GenericService**: CQRS pattern implementation (Command & Query services)
- **Perpustakaan**: Library management services (Buku, Anggota, Peminjaman, Pengembalian)

## Prerequisites
- Docker Engine 20.10+
- Docker Compose 2.0+
- Maven 3.8+ (for building JAR files)
- Java 17+

## Project Structure
```
.
├── docker-compose.yaml              # Root compose file (all services)
├── Marketplace/
│   ├── docker-compose.yaml          # Marketplace services only
│   ├── Produk/Dockerfile
│   ├── Pelanggan/Dockerfile
│   ├── Order/Dockerfile
│   └── api-gateway/Dockerfile
├── GenericService/
│   ├── docker-compose.yaml          # CQRS services only
│   ├── command-service/Dockerfile
│   └── query-service/Dockerfile
├── Perpustakaan/
│   ├── docker-compose.yaml          # Library services only
│   ├── Buku/Dockerfile
│   ├── anggota/Dockerfile
│   ├── Peminjaman/Dockerfile
│   ├── Pengembalian/Dockerfile
│   └── api-gateway/Dockerfile
└── eureka/Dockerfile                # Service discovery
```

## Building Services

### Build All Services
Before running Docker, build all JAR files:

```bash
# Build Marketplace services
cd Marketplace/Produk && mvn clean package -DskipTests && cd ../..
cd Marketplace/Pelanggan && mvn clean package -DskipTests && cd ../..
cd Marketplace/Order && mvn clean package -DskipTests && cd ../..
cd Marketplace/api-gateway && mvn clean package -DskipTests && cd ../..

# Build GenericService
cd GenericService/command-service && mvn clean package -DskipTests && cd ../..
cd GenericService/query-service && mvn clean package -DskipTests && cd ../..

# Build Perpustakaan services
cd Perpustakaan/Buku && mvn clean package -DskipTests && cd ../..
cd Perpustakaan/anggota && mvn clean package -DskipTests && cd ../..
cd Perpustakaan/Peminjaman && mvn clean package -DskipTests && cd ../..
cd Perpustakaan/Pengembalian && mvn clean package -DskipTests && cd ../..
cd Perpustakaan/api-gateway && mvn clean package -DskipTests && cd ../..

# Build Eureka server
cd eureka && mvn clean package -DskipTests && cd ..
```

### Build Script
Alternatively, use this bash script to build all services:

```bash
#!/bin/bash
echo "Building all services..."

services=(
  "Marketplace/Produk"
  "Marketplace/Pelanggan"
  "Marketplace/Order"
  "Marketplace/api-gateway"
  "GenericService/command-service"
  "GenericService/query-service"
  "Perpustakaan/Buku"
  "Perpustakaan/anggota"
  "Perpustakaan/Peminjaman"
  "Perpustakaan/Pengembalian"
  "Perpustakaan/api-gateway"
  "eureka"
)

for service in "${services[@]}"; do
  echo "Building $service..."
  cd "$service" && mvn clean package -DskipTests && cd - > /dev/null
done

echo "All services built successfully!"
```

## Running Services

### Option 1: Run All Services (Recommended for Development)
```bash
# From project root
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down
```

### Option 2: Run Specific Domain Services

#### Marketplace Only
```bash
cd Marketplace
docker-compose up -d
docker-compose logs -f
```

#### GenericService (CQRS) Only
```bash
cd GenericService
docker-compose up -d
docker-compose logs -f
```

#### Perpustakaan Only
```bash
cd Perpustakaan
docker-compose up -d
docker-compose logs -f
```

## Service Ports

### Infrastructure Services
- **Eureka Server**: http://localhost:8761
- **RabbitMQ Management (Marketplace)**: http://localhost:15672 (admin/password)
- **RabbitMQ Management (Generic)**: http://localhost:15674 (guest/guest)
- **RabbitMQ Management (Perpustakaan)**: http://localhost:15675 (guest/guest)
- **MongoDB**: mongodb://localhost:27017

### Marketplace Services
- **Produk Service**: http://localhost:8081
- **Pelanggan Service**: http://localhost:8082
- **Order Service**: http://localhost:8083
- **API Gateway**: http://localhost:9000

### Generic Services (CQRS)
- **Command Service**: http://localhost:8091
- **Query Service**: http://localhost:8092

### Perpustakaan Services
- **Buku Service**: http://localhost:8084
- **Anggota Service**: http://localhost:8085
- **Pengembalian Service**: http://localhost:8086
- **Peminjaman Service**: http://localhost:8087
- **API Gateway**: http://localhost:9001

## Health Checks
All services expose actuator health endpoints:
```bash
curl http://localhost:8081/actuator/health  # Produk
curl http://localhost:8082/actuator/health  # Pelanggan
curl http://localhost:8083/actuator/health  # Order
# ... and so on
```

## Database Access

### H2 Console Access
H2 console is enabled for services with H2 database:
- Produk: http://localhost:8081/h2-console
- Pelanggan: http://localhost:8082/h2-console
- Order: http://localhost:8083/h2-console
- Buku: http://localhost:8084/h2-console
- Anggota: http://localhost:8085/h2-console
- Pengembalian: http://localhost:8086/h2-console
- Peminjaman: http://localhost:8087/h2-console
- Command Service: http://localhost:8091/h2-console

**Connection Settings:**
- JDBC URL: `jdbc:h2:file:/app/data/{service-name}`
- Username: `sa`
- Password: `password`

### MongoDB Access
For Query Service:
```bash
docker exec -it mongodb mongosh
use pelanggan_readdb
db.pelanggan.find()
```

## Troubleshooting

### Services Not Starting
1. Check if JAR files are built:
   ```bash
   find . -name "*.jar" -path "*/target/*"
   ```

2. Check service logs:
   ```bash
   docker-compose logs [service-name]
   ```

3. Verify Eureka registration:
   - Open http://localhost:8761
   - All services should appear after ~30 seconds

### Port Conflicts
If ports are already in use, modify the port mappings in docker-compose.yaml:
```yaml
ports:
  - "NEW_PORT:CONTAINER_PORT"
```

### Memory Issues
If services crash due to memory, adjust Java options:
```yaml
environment:
  JAVA_OPTS: "-Xmx512m -Xms256m"
```

### Rebuilding Services
```bash
# Rebuild specific service
docker-compose build [service-name]

# Rebuild all services
docker-compose build

# Force rebuild without cache
docker-compose build --no-cache
```

## Docker Commands Cheatsheet

```bash
# Start services in background
docker-compose up -d

# Start specific service
docker-compose up -d [service-name]

# View logs
docker-compose logs -f [service-name]

# Stop services
docker-compose stop

# Stop and remove containers
docker-compose down

# Remove volumes (WARNING: deletes data)
docker-compose down -v

# Restart service
docker-compose restart [service-name]

# Check service status
docker-compose ps

# Execute command in container
docker-compose exec [service-name] bash

# View resource usage
docker stats
```

## Network Architecture
All services communicate through a shared Docker network (`microservices-network`), allowing:
- Service-to-service communication using container names
- Eureka service discovery
- RabbitMQ message passing
- Database connections

## Data Persistence
Docker volumes are used for data persistence:
- `produk_data`, `pelanggan_data`, `order_data`: Marketplace H2 databases
- `command_data`: CQRS event store
- `mongodb_data`: Query service read model
- `buku_data`, `anggota_data`, `pengembalian_data`: Library H2 databases

To remove all data:
```bash
docker-compose down -v
```

## Production Considerations
For production deployment, consider:
1. Use PostgreSQL/MySQL instead of H2
2. Configure proper logging (ELK stack)
3. Add monitoring (Prometheus/Grafana)
4. Implement API rate limiting
5. Use production-grade message broker setup
6. Configure proper security (JWT, OAuth2)
7. Use secrets management for credentials
8. Set up container orchestration (Kubernetes)

## Support
For issues or questions, please check:
1. Service logs: `docker-compose logs [service-name]`
2. Eureka dashboard: http://localhost:8761
3. RabbitMQ management UI
4. Container status: `docker-compose ps`
