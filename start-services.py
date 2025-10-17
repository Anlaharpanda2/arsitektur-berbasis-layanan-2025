import streamlit as st
import subprocess
import os
import platform

# === Konfigurasi dasar ===
BASE_PATH = r"C:\Users\anlah\OneDrive\Documents\SEMESTER 5\Arsitektur Berbasis Layanan\arsitektur-berbasis-layanan-2025"

services = [
    {"name": "Eureka Server", "path": "eureka"},
    {"name": "Produk", "path": r"Marketplace\Produk"},
    {"name": "Pelanggan", "path": r"Marketplace\Pelanggan"},
    {"name": "Order", "path": r"Marketplace\Order"},
    {"name": "Buku", "path": r"Perpustakaan\Buku"},
    {"name": "Anggota", "path": r"Perpustakaan\anggota"},
    {"name": "Pengembalian", "path": r"Perpustakaan\Pengembalian"},
    {"name": "Peminjaman", "path": r"Perpustakaan\Peminjaman"},
    {"name": "API Gateway Marketplace", "path": r"Marketplace\api-gateway"},
    {"name": "API Gateway Perpustakaan", "path": r"Perpustakaan\api-gateway"},
]

# === UI Streamlit ===
st.set_page_config(page_title="Service Launcher", layout="centered")

st.title("🧩 Service Launcher Dashboard")
st.write(f"📁 **Base Path:** `{BASE_PATH}`")
st.write("Pilih service yang ingin dijalankan kemudian klik **Jalankan Service**.")

# Checkbox untuk setiap service
selected_services = []
for svc in services:
    if st.checkbox(f"{svc['name']}"):
        selected_services.append(svc)

# Tombol Jalankan
if st.button("🚀 Jalankan Service"):
    if not selected_services:
        st.warning("Silakan pilih minimal satu service untuk dijalankan.")
    else:
        for service in selected_services:
            service_name = service["name"]
            service_path = os.path.join(BASE_PATH, service["path"])

            if not os.path.isdir(service_path):
                st.error(f"❌ Folder tidak ditemukan untuk '{service_name}' → {service_path}")
                continue

            st.info(f"Menjalankan **{service_name}** ...")

            if platform.system() == "Windows":
                inner_command = f'cd /d "{service_path}" && mvnw.cmd spring-boot:run'
                full_command = f'start "{service_name}" cmd /k "{inner_command}"'
                subprocess.Popen(full_command, shell=True)
            else:
                command_for_shell = f'cd "{service_path}" && ./mvnw spring-boot:run'
                try:
                    subprocess.Popen(
                        ['xterm', '-T', service_name, '-e', f'bash -c "{command_for_shell}; exec bash"']
                    )
                except FileNotFoundError:
                    st.error(f"⚠️ Tidak dapat menjalankan '{service_name}'. Pastikan 'xterm' sudah terinstal.")

        st.success("✅ Semua perintah startup telah dijalankan.")

# Footer
st.markdown("---")
st.caption("Dibuat dengan ❤️ menggunakan Streamlit.")
