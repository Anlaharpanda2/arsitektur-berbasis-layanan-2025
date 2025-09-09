# Microservices E-Commerce System

Sistem e-commerce berbasis microservices yang terdiri dari 3 layanan utama: Product Service, Pelanggan Service, dan Order Service. Setiap layanan berjalan pada server terpisah menggunakan Java Spring Boot dengan Maven.

## 🏗️ Arsitektur Sistem

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│  Product Service│    │ Pelanggan Service│   │  Order Service  │
│   Port: 8081    │    │   Port: 8082    │    │   Port: 8083    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 📋 Daftar Services

| Service | Port | Endpoint | Deskripsi |
|---------|------|----------|-----------|
| Product Service | 8081 | `/api/products` | Mengelola data produk |
| Pelanggan Service | 8082 | `/api/pelanggan` | Mengelola data pelanggan |
| Order Service | 8083 | `/api/order` | Mengelola data pesanan |

## 🚀 Cara Menjalankan

### Prerequisites
- Java 11 atau lebih tinggi
- Maven 3.6+
- IDE (IntelliJ IDEA/Eclipse) atau Command Line

### Menjalankan Setiap Service

1. **Product Service (Port 8081)**
   ```bash
   cd product-service
   mvn clean install
   mvn spring-boot:run
   ```

2. **Pelanggan Service (Port 8082)**
   ```bash
   cd pelanggan-service
   mvn clean install
   mvn spring-boot:run
   ```

3. **Order Service (Port 8083)**
   ```bash
   cd order-service
   mvn clean install
   mvn spring-boot:run
   ```

### Verifikasi Service Berjalan
- Product Service: http://localhost:8081
- Pelanggan Service: http://localhost:8082
- Order Service: http://localhost:8083

## 📡 API Documentation

### 1. Product Service API

#### Create Product
- **URL**: `http://localhost:8081/api/products`
- **Method**: `POST`
- **Content-Type**: `application/json`

**Request Body:**
```json
{
  "nama": "Beras Premium",
  "satuan": "Kg",
  "harga": "15000"
}
```

#### Get All Products
- **URL**: `http://localhost:8081/api/products`
- **Method**: `GET`

### 2. Pelanggan Service API

#### Create Pelanggan
- **URL**: `http://localhost:8082/api/pelanggan`
- **Method**: `POST`
- **Content-Type**: `application/json`

**Request Body:**
```json
{
  "kode": "C001",
  "nama": "Budi Santoso",
  "alamat": "Jl. Merdeka No. 10, Padang"
}
```

#### Get All Pelanggan
- **URL**: `http://localhost:8082/api/pelanggan`
- **Method**: `GET`

### 3. Order Service API

#### Create Order
- **URL**: `http://localhost:8083/api/order`
- **Method**: `POST`
- **Content-Type**: `application/json`

**Request Body:**
```json
{
  "productId": "1",
  "pelangganId": "1",
  "jumlah": 10,
  "status": "PENDING",
  "Total": 50000.00
}
```

#### Get All Orders
- **URL**: `http://localhost:8083/api/order`
- **Method**: `GET`

## 📮 Template Postman Collection

Copy dan paste JSON berikut ke Postman untuk mengimport collection:

```json
{
  "info": {
    "name": "Microservices E-Commerce",
    "description": "Collection untuk testing microservices e-commerce system",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Product Service",
      "item": [
        {
          "name": "Create Product",
          "request": {
            "method": "POST",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"nama\": \"Beras Premium\",\n  \"satuan\": \"Kg\",\n  \"harga\": \"15000\"\n}"
            },
            "url": {
              "raw": "http://localhost:8081/api/products",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8081",
              "path": ["api", "products"]
            }
          }
        },
        {
          "name": "Get All Products",
          "request": {
            "method": "GET",
            "header": [],
            "url": {
              "raw": "http://localhost:8081/api/products",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8081",
              "path": ["api", "products"]
            }
          }
        }
      ]
    },
    {
      "name": "Pelanggan Service",
      "item": [
        {
          "name": "Create Pelanggan",
          "request": {
            "method": "POST",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"kode\": \"C001\",\n  \"nama\": \"Budi Santoso\",\n  \"alamat\": \"Jl. Merdeka No. 10, Padang\"\n}"
            },
            "url": {
              "raw": "http://localhost:8082/api/pelanggan",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8082",
              "path": ["api", "pelanggan"]
            }
          }
        },
        {
          "name": "Get All Pelanggan",
          "request": {
            "method": "GET",
            "header": [],
            "url": {
              "raw": "http://localhost:8082/api/pelanggan",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8082",
              "path": ["api", "pelanggan"]
            }
          }
        }
      ]
    },
    {
      "name": "Order Service",
      "item": [
        {
          "name": "Create Order",
          "request": {
            "method": "POST",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"productId\": \"1\",\n  \"pelangganId\": \"1\",\n  \"jumlah\": 10,\n  \"status\": \"PENDING\",\n  \"Total\": 50000.00\n}"
            },
            "url": {
              "raw": "http://localhost:8083/api/order",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8083",
              "path": ["api", "order"]
            }
          }
        },
        {
          "name": "Get All Orders",
          "request": {
            "method": "GET",
            "header": [],
            "url": {
              "raw": "http://localhost:8083/api/order",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8083",
              "path": ["api", "order"]
            }
          }
        }
      ]
    }
  ]
}
```

## 📥 Cara Import Collection ke Postman

1. Buka Postman
2. Klik tombol **Import** di pojok kiri atas
3. Pilih tab **Raw text**
4. Copy dan paste JSON collection di atas
5. Klik **Continue** dan kemudian **Import**
6. Collection "Microservices E-Commerce" akan muncul di sidebar kiri

## 🧪 Testing Flow

### Urutan Testing yang Disarankan:

1. **Buat Product** terlebih dahulu
2. **Buat Pelanggan** 
3. **Buat Order** dengan menggunakan `productId` dan `pelangganId` yang sudah dibuat

### Contoh Testing Sequence:

```bash
# 1. Create Product
POST http://localhost:8081/api/products
Response: { "id": 1, "nama": "Beras Premium", ... }

# 2. Create Pelanggan  
POST http://localhost:8082/api/pelanggan
Response: { "id": 1, "kode": "C001", "nama": "Budi Santoso", ... }

# 3. Create Order menggunakan ID dari langkah 1 dan 2
POST http://localhost:8083/api/order
Body: { "productId": "1", "pelangganId": "1", ... }
```

## 🔧 Configuration

Pastikan setiap service menggunakan port yang berbeda di file `application.properties`:

**Product Service (`application.properties`):**
```properties
server.port=8081
spring.application.name=product-service
```

**Pelanggan Service (`application.properties`):**
```properties
server.port=8082
spring.application.name=pelanggan-service
```

**Order Service (`application.properties`):**
```properties
server.port=8083
spring.application.name=order-service
```

## 🐛 Troubleshooting

### Port Already in Use
Jika ada error port sudah digunakan:
```bash
# Cek proses yang menggunakan port
netstat -tulpn | grep :8081
netstat -tulpn | grep :8082
netstat -tulpn | grep :8083

# Kill proses jika diperlukan
kill -9 <PID>
```

### Service Tidak Bisa Diakses
1. Pastikan semua service sudah running
2. Cek log aplikasi untuk error
3. Pastikan tidak ada firewall yang memblokir port
4. Cek konfigurasi database jika menggunakan database

## 📝 Notes

- Pastikan menjalankan service Product dan Pelanggan terlebih dahulu sebelum Order Service
- ID pada request Order harus sesuai dengan ID yang sudah ada di Product dan Pelanggan
- Gunakan environment variables di Postman untuk memudahkan switching between environments (dev, staging, prod)
