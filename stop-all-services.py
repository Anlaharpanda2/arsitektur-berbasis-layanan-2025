import subprocess
import os
import platform

BASE_PATH = r"C:\Users\anlah\OneDrive\Documents\SEMESTER 5\Arsitektur Berbasis Layanan\arsitektur-berbasis-layanan-2025"

# Saya mengaktifkan kembali semua service untuk pengujian
services = [
    {
        "name": "Eureka Server",
        "path": "eureka"
    },
    # # marketplace services
    # {
    #     "name": "produk",
    #     "path": r"Marketplace\Produk"
    # },
    # {
    #     "name": "Pelanggan",
    #     "path": r"Marketplace\Pelanggan"
    # },
    # {
    #     "name": "Order",
    #     "path": r"Marketplace\Order"
    # },
    # # perpustakaan services
    # {
    #     "name": "Buku",
    #     "path": r"Perpustakaan\Buku"
    # },
    # {
    #     "name": "Anggota",
    #     "path": r"Perpustakaan\anggota"
    # },
    # {
    #     "name": "Pengembalian",
    #     "path": r"Perpustakaan\Pengembalian"
    # },
    # {
    #     "name": "Peminjaman",
    #     "path": r"Perpustakaan\Peminjaman"
    # },
    # # api gateway
    # {
    #     "name": "API Gateway",
    #     "path": "api-gateway"
    # },
]

print("="*50)
print(f"Base project path: {BASE_PATH}")
print("="*50)
print("\nStarting All Microservices...\n")
print("A new command window will open for each service.")
print("="*50 + "\n")

for service in services:
    service_name = service["name"]
    service_path = os.path.join(BASE_PATH, service["path"])

    if not os.path.isdir(service_path):
        print(f"--> WARNING: Directory not found for '{service_name}', skipping: {service_path}")
        continue

    print(f"--> Starting '{service_name}'...")
    
    if platform.system() == "Windows":
        # Perintah internal yang akan dijalankan di dalam cmd baru
        inner_command = f'cd /d "{service_path}" && mvnw.cmd spring-boot:run'
        
        # <--- INI BAGIAN YANG DIPERBAIKI
        # Gunakan 'start' untuk membuka cmd baru dan jalankan perintah di dalamnya.
        # 'shell=True' penting agar perintah 'start' bisa dikenali.
        # Argumen pertama setelah 'start' adalah judul jendela.
        full_command = f'start "{service_name}" cmd /k "{inner_command}"'
        subprocess.Popen(full_command, shell=True)

    else: # Untuk Linux atau macOS
        command_for_shell = f'cd "{service_path}" && ./mvnw spring-boot:run'
        try:
            subprocess.Popen(['xterm', '-T', service_name, '-e', f'bash -c "{command_for_shell}; exec bash"'])
        except FileNotFoundError:
            print(f"--> ERROR: Could not start '{service_name}'. Please ensure 'xterm' is installed or modify the script for your terminal.")

print("\nAll service startup commands have been issued.")