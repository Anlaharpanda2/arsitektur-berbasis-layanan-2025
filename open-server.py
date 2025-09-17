from selenium import webdriver
from selenium.webdriver.firefox.service import Service
from selenium.webdriver.firefox.options import Options
from selenium.webdriver.common.by import By
import time
import sys

def open_servers():
    """
    Membuka Firefox dengan multiple tab untuk server yang sudah ditentukan
    """
    servers = [
        "http://localhost:8761", # Eureka Server
        "http://localhost:9000", # API Gateway Marketplace
        "http://localhost:9001", # API Gateway Perpustakaan
        "http://localhost:8081/api/produk", 
        "http://localhost:8082/api/pelanggan",
        "http://localhost:8083/api/order",
        "http://localhost:8084/api/buku",
        "http://localhost:8085/api/anggota",
        "http://localhost:8086/api/pengembalian",
        "http://localhost:8087/api/peminjaman",

    ]
    
    print(f"Membuka {len(servers)} server...")
    for i, server in enumerate(servers, 1):
        print(f"{i}. {server}")
    
    driver = None
    try:
        firefox_options = Options()
        firefox_options.add_argument('--no-sandbox')
        firefox_options.add_argument('--disable-dev-shm-usage')
        
        print("\nMembuka Firefox...")
        driver = webdriver.Firefox(options=firefox_options)
        driver.set_window_size(1200, 800)
        print(f"Membuka tab 1: {servers[0]}")
        driver.get(servers[0])
        time.sleep(3)
        for i, server in enumerate(servers[1:], 2):
            print(f"Membuka tab {i}: {server}")
            try:
                driver.execute_script("window.open('', '_blank');")
                driver.switch_to.window(driver.window_handles[-1])
                driver.get(server)
                time.sleep(2)
            except Exception as e:
                print(f"Warning: Gagal membuka {server} - {e}")
                continue
        print(f"\nBerhasil! Firefox terbuka dengan {len(driver.window_handles)} tab")
        print("\n=== Tab yang terbuka ===")
        for i, server in enumerate(servers[:len(driver.window_handles)], 1):
            print(f"Tab {i}: {server}")
        
        print(f"\nBrowser akan tetap terbuka...")
        print("Untuk menutup browser, tekan Ctrl+C atau tutup window Firefox secara manual")
        try:
            while True:
                if len(driver.window_handles) == 0:
                    print("Browser ditutup oleh user.")
                    break
                time.sleep(5) 
        except KeyboardInterrupt:
            print("\nMenutup browser...")
        except Exception as e:
            print(f"Browser error: {e}")
    
    except Exception as e:
        print(f"Error saat membuka browser: {e}")
        print("\nTroubleshooting:")
        print("1. Pastikan Firefox sudah terinstall")
        print("2. Install geckodriver:")
        print("   - Download: https://github.com/mozilla/geckodriver/releases")
        print("   - Extract ke folder yang ada di PATH")
        print("3. Update selenium: pip install --upgrade selenium")
    
    finally:
        if driver:
            try:
                driver.quit()
                print("Driver berhasil ditutup.")
            except:
                print("Driver sudah ditutup.")

if __name__ == "__main__":
    open_servers()