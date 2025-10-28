import streamlit as st
import subprocess
import os
import platform

# === Konfigurasi dasar ===
# Path untuk Deepin 25
BASE_PATH = "/media/Anla/DATA/Project/SEMESTER5/matkul-abl/arsitektur-berbasis-layanan-2025"

services = [
    {"name": "Eureka Server", "path": "eureka"},
    {"name": "Produk", "path": "Marketplace/Produk"},
    {"name": "Pelanggan", "path": "Marketplace/Pelanggan"},
    {"name": "Order", "path": "Marketplace/Order"},
    {"name": "Buku", "path": "Perpustakaan/Buku"},
    {"name": "Anggota", "path": "Perpustakaan/anggota"},
    {"name": "Pengembalian", "path": "Perpustakaan/Pengembalian"},
    {"name": "Peminjaman", "path": "Perpustakaan/Peminjaman"},
    {"name": "API Gateway Marketplace", "path": "Marketplace/api-gateway"},
    {"name": "API Gateway Perpustakaan", "path": "Perpustakaan/api-gateway"},
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
                # Linux: Gunakan deepin-terminal (terminal bawaan Deepin)
                command_for_shell = f'cd "{service_path}" && ./mvnw spring-boot:run'
                
                # Coba deepin-terminal dulu, lalu xterm, lalu gnome-terminal sebagai fallback
                terminal_options = [
                    ['deepin-terminal', '-e', f'bash -c "{command_for_shell}; exec bash"'],
                    ['xterm', '-T', service_name, '-e', f'bash -c "{command_for_shell}; exec bash"'],
                    ['gnome-terminal', '--', 'bash', '-c', f'{command_for_shell}; exec bash'],
                    ['x-terminal-emulator', '-e', f'bash -c "{command_for_shell}; exec bash"']
                ]
                
                terminal_launched = False
                for terminal_cmd in terminal_options:
                    try:
                        subprocess.Popen(terminal_cmd)
                        terminal_launched = True
                        break
                    except FileNotFoundError:
                        continue
                
                if not terminal_launched:
                    st.error(f"⚠️ Tidak dapat menjalankan '{service_name}'. Install terminal: sudo apt install deepin-terminal")
        
        st.success("✅ Semua perintah startup telah dijalankan.")

# Footer
st.markdown("---")
st.caption("Dibuat dengan ❤️ menggunakan Streamlit.")