import subprocess
import os
import platform
BASE_PATH = r"C:\Users\anlah\OneDrive\Documents\SEMESTER 5\Arsitektur Berbasis Layanan\arsitektur-berbasis-layanan-2025"

services = [
    {
        "name": "Buku",
        "path": r"Perpustakaan\Buku"
    },
    {
        "name": "Anggota",
        "path": r"Perpustakaan\anggota"
    },
    {
        "name": "Pengembalian",
        "path": r"Perpustakaan\Pengembalian"
    },
    {
        "name": "Peminjaman",
        "path": r"Perpustakaan\Peminjaman"
    },
    {
        "name": "API Gateway Perpustakaan",
        "path": r"Perpustakaan\api-gateway"
    },
]
print("="*80)
print("Memulai Pengecekan PMD untuk Semua Layanan Mikro...")
print("="*80)
for service in services:
    service_name = service["name"]
    service_path = os.path.join(BASE_PATH, service["path"])
    if not os.path.isdir(service_path):
        print(f"\n--> PERINGATAN: Direktori tidak ditemukan untuk '{service_name}', dilewati: {service_path}")
        continue
    pom_path = os.path.join(service_path, 'pom.xml')
    if not os.path.isfile(pom_path):
        print(f"\n--> INFO: File pom.xml tidak ditemukan untuk '{service_name}', dilewati.")
        continue
    print(f"\n\n{'='*30} Memeriksa '{service_name}' {'='*30}")
    command = "mvnw.cmd pmd:check" if platform.system() == "Windows" else "./mvnw pmd:check"
    try:
        
        
        process = subprocess.run(
            command,
            cwd=service_path,
            shell=True,
            check=False, 
            capture_output=True, 
            text=True, 
            encoding='utf-8' 
        )
        print(f"--> Menjalankan PMD check di: {service_path}")
        
        if process.stdout:
            print("\n--- STDOUT ---\n")
            print(process.stdout)
        
        if process.stderr:
            print("\n--- STDERR ---\n")
            print(process.stderr)
        if process.returncode == 0:
            print(f"--> SUKSES: Pengecekan PMD untuk '{service_name}' berhasil.")
        else:
            print(f"--> GAGAL: Pengecekan PMD untuk '{service_name}' gagal dengan kode keluar {process.returncode}.")
    except Exception as e:
        print(f"--> ERROR: Terjadi kesalahan saat menjalankan PMD check untuk '{service_name}': {e}")
print("\n\n" + "="*80)
print("Semua pengecekan PMD telah selesai.")
print("="*80)
