#!/bin/bash

echo "🔨 Building All Services for ELK Stack Integration..."
echo ""

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Build Buku Service
echo -e "${BLUE}[1/5] Building Buku Service...${NC}"
cd Buku && ./mvnw clean package -DskipTests && cd ..
echo -e "${GREEN}✓ Buku Service built successfully${NC}"
echo ""

# Build Anggota Service
echo -e "${BLUE}[2/5] Building Anggota Service...${NC}"
cd anggota && ./mvnw clean package -DskipTests && cd ..
echo -e "${GREEN}✓ Anggota Service built successfully${NC}"
echo ""

# Build Pengembalian Service
echo -e "${BLUE}[3/5] Building Pengembalian Service...${NC}"
cd Pengembalian && ./mvnw clean package -DskipTests && cd ..
echo -e "${GREEN}✓ Pengembalian Service built successfully${NC}"
echo ""

# Build Peminjaman Service
echo -e "${BLUE}[4/5] Building Peminjaman Service...${NC}"
cd Peminjaman && ./mvnw clean package -DskipTests && cd ..
echo -e "${GREEN}✓ Peminjaman Service built successfully${NC}"
echo ""

# Build API Gateway
echo -e "${BLUE}[5/5] Building API Gateway...${NC}"
cd api-gateway && ./mvnw clean package -DskipTests && cd ..
echo -e "${GREEN}✓ API Gateway built successfully${NC}"
echo ""

echo -e "${GREEN}🎉 All services built successfully!${NC}"
echo ""
echo "Next steps:"
echo "1. Run: docker-compose up -d"
echo "2. Wait 2-3 minutes for all services to start"
echo "3. Access Kibana at http://localhost:5601"
echo ""
