import subprocess
import os
import time
import sys
import ctypes
from ctypes import wintypes
import win32gui
import win32con
import win32process
import psutil
import re

def is_vscode_child_process(pid):
    """
    Cek apakah proses adalah child dari VSCode
    """
    try:
        proc = psutil.Process(pid)
        current = proc
        for _ in range(5):  # Max 5 level parent
            if current.parent():
                parent = current.parent()
                parent_name = parent.name().lower()
                if 'code.exe' in parent_name or 'vscode' in parent_name:
                    return True
                current = parent
            else:
                break
        return False
    except:
        return False

def is_spring_boot_process(proc):
    """
    Cek apakah proses Java adalah Spring Boot
    """
    try:
        cmdline = proc.cmdline()
        cmdline_str = ' '.join(cmdline).lower()
        
        # Indikator Spring Boot
        spring_indicators = [
            'spring-boot',
            'mvnw',
            'maven',
            '.jar',
            'org.springframework',
            'eureka',
            'server.port',
            'application.yml',
            'application.properties'
        ]
        
        return any(indicator in cmdline_str for indicator in spring_indicators)
    except:
        return False

def find_spring_boot_processes():
    """
    Temukan semua proses Spring Boot yang berjalan
    """
    spring_processes = []
    
    try:
        for proc in psutil.process_iter(['pid', 'name', 'cmdline', 'ppid']):
            try:
                proc_name = proc.info['name'].lower()
                
                # Cari proses Java
                if proc_name in ['java.exe', 'javaw.exe']:
                    # Skip jika ini child dari VSCode
                    if is_vscode_child_process(proc.info['pid']):
                        continue
                        
                    # Cek apakah ini Spring Boot
                    if is_spring_boot_process(proc):
                        cmdline_str = ' '.join(proc.info['cmdline'])
                        
                        # Extract info service
                        service_name = "Unknown Service"
                        port = "Unknown Port"
                        
                        # Cari nama service dari path
                        if 'eureka' in cmdline_str.lower():
                            service_name = "Eureka Server"
                        elif 'api-gateway' in cmdline_str.lower():
                            service_name = "API Gateway"
                        elif 'produk' in cmdline_str.lower():
                            service_name = "Produk Service"
                        elif 'pelanggan' in cmdline_str.lower():
                            service_name = "Pelanggan Service"
                        elif 'order' in cmdline_str.lower():
                            service_name = "Order Service"
                        elif 'buku' in cmdline_str.lower():
                            service_name = "Buku Service"
                        elif 'anggota' in cmdline_str.lower():
                            service_name = "Anggota Service"
                        elif 'peminjaman' in cmdline_str.lower():
                            service_name = "Peminjaman Service"
                        elif 'pengembalian' in cmdline_str.lower():
                            service_name = "Pengembalian Service"
                        
                        # Cari port dari command line
                        port_match = re.search(r'server\.port[=\s]+(\d+)', cmdline_str)
                        if port_match:
                            port = port_match.group(1)
                        else:
                            # Port default berdasarkan service
                            if 'eureka' in cmdline_str.lower():
                                port = "8761"
                            elif 'api-gateway' in cmdline_str.lower():
                                port = "8080"
                        
                        spring_processes.append({
                            'pid': proc.info['pid'],
                            'name': service_name,
                            'port': port,
                            'cmdline': cmdline_str
                        })
                        
            except (psutil.NoSuchProcess, psutil.AccessDenied, psutil.ZombieProcess):
                continue
    except Exception as e:
        print(f"Error scanning processes: {e}")
    
    return spring_processes

def stop_spring_boot_process(pid, service_name):
    """
    Stop Spring Boot process dengan graceful shutdown dulu
    """
    try:
        proc = psutil.Process(pid)
        
        print(f"  🔄 Stopping {service_name} (PID: {pid})...")
        
        # Method 1: Graceful shutdown via SIGTERM (if supported)
        try:
            proc.terminate()
            proc.wait(timeout=10)  # Wait up to 10 seconds
            if not proc.is_running():
                print(f"  ✅ {service_name} stopped gracefully")
                return True
        except psutil.TimeoutExpired:
            print(f"  ⏰ {service_name} didn't stop gracefully, force killing...")
        except:
            pass
        
        # Method 2: Force kill
        try:
            proc.kill()
            proc.wait(timeout=5)
            print(f"  💀 {service_name} force killed")
            return True
        except:
            pass
        
        # Method 3: System taskkill
        try:
            result = subprocess.run(['taskkill', '/f', '/t', '/pid', str(pid)], 
                                  capture_output=True, text=True)
            if result.returncode == 0:
                print(f"  💥 {service_name} killed via taskkill")
                return True
        except:
            pass
        
        print(f"  ❌ Failed to stop {service_name}")
        return False
        
    except psutil.NoSuchProcess:
        print(f"  ✅ {service_name} already stopped")
        return True
    except Exception as e:
        print(f"  ❌ Error stopping {service_name}: {e}")
        return False

def close_console_windows():
    """
    Tutup jendela console yang masih terbuka setelah kill proses
    """
    print("\n🔍 Menutup jendela console yang orphaned...")
    
    closed_count = 0
    
    def enum_windows_proc(hwnd, lParam):
        nonlocal closed_count
        try:
            class_name = win32gui.GetClassName(hwnd)
            if class_name == "ConsoleWindowClass" and win32gui.IsWindowVisible(hwnd):
                window_title = win32gui.GetWindowText(hwnd)
                
                # Cek apakah masih ada proses yang terkait
                try:
                    _, pid = win32process.GetWindowThreadProcessId(hwnd)
                    # Jika proses masih ada dan bukan VSCode, skip dulu
                    if psutil.pid_exists(pid) and not is_vscode_child_process(pid):
                        return True
                except:
                    pass
                
                # Tutup window orphaned
                print(f"  🗑️ Closing orphaned window: '{window_title}'")
                try:
                    win32gui.PostMessage(hwnd, win32con.WM_CLOSE, 0, 0)
                    time.sleep(0.5)
                    
                    if win32gui.IsWindow(hwnd) and win32gui.IsWindowVisible(hwnd):
                        win32gui.EndTask(hwnd, True, True)
                    
                    closed_count += 1
                except:
                    pass
                    
        except:
            pass
        return True
    
    win32gui.EnumWindows(enum_windows_proc, 0)
    print(f"✅ Closed {closed_count} orphaned console windows")

def kill_remaining_cmd_processes():
    """
    Kill sisa proses CMD yang mungkin masih ada
    """
    print("\n🧹 Cleaning up remaining CMD processes...")
    
    killed_count = 0
    
    try:
        for proc in psutil.process_iter(['pid', 'name']):
            try:
                proc_name = proc.info['name'].lower()
                if proc_name in ['cmd.exe', 'conhost.exe']:
                    if not is_vscode_child_process(proc.info['pid']):
                        print(f"  💀 Killing {proc_name} PID: {proc.info['pid']}")
                        subprocess.run(['taskkill', '/f', '/pid', str(proc.info['pid'])], 
                                     capture_output=True)
                        killed_count += 1
            except:
                continue
    except:
        pass
    
    print(f"✅ Killed {killed_count} remaining CMD processes")

def stop_all_spring_boot_servers():
    """
    Fungsi utama untuk menghentikan semua server Spring Boot
    """
    print("="*70)
    print("    🛑 SPRING BOOT SERVER KILLER 🛑")
    print("    (MELINDUNGI TERMINAL VSCODE)")
    print("="*70)
    
    print("🔍 Scanning untuk Spring Boot processes...")
    spring_processes = find_spring_boot_processes()
    
    if not spring_processes:
        print("✅ Tidak ada Spring Boot server yang berjalan!")
        
        # Tapi tetap bersihkan CMD orphaned
        close_console_windows()
        kill_remaining_cmd_processes()
        return
    
    print(f"\n📋 Ditemukan {len(spring_processes)} Spring Boot server:")
    print("="*70)
    
    for proc in spring_processes:
        print(f"🚀 {proc['name']}")
        print(f"   PID: {proc['pid']}")
        print(f"   Port: {proc['port']}")
        print(f"   Command: {proc['cmdline'][:100]}...")
        print()
    
    print("🛑 Menghentikan semua Spring Boot servers...")
    print("="*70)
    
    success_count = 0
    for proc in spring_processes:
        if stop_spring_boot_process(proc['pid'], proc['name']):
            success_count += 1
    
    print(f"\n📊 HASIL:")
    print(f"✅ Berhasil dihentikan: {success_count}/{len(spring_processes)} servers")
    
    # Wait a moment for processes to fully terminate
    print("\n⏳ Menunggu proses selesai...")
    time.sleep(3)
    
    # Close orphaned console windows
    close_console_windows()
    
    # Clean up remaining CMD processes
    kill_remaining_cmd_processes()
    
    # Final check
    print("\n🔍 Final verification...")
    remaining_spring = find_spring_boot_processes()
    
    if not remaining_spring:
        print("🎉 SEMUA SPRING BOOT SERVERS BERHASIL DIHENTIKAN!")
    else:
        print(f"⚠️ Masih ada {len(remaining_spring)} server yang berjalan:")
        for proc in remaining_spring:
            print(f"  - {proc['name']} (PID: {proc['pid']})")
    
    # Check VSCode terminals
    vscode_count = 0
    try:
        for proc in psutil.process_iter(['pid', 'name']):
            if proc.info['name'] and proc.info['name'].lower() == 'cmd.exe':
                if is_vscode_child_process(proc.info['pid']):
                    vscode_count += 1
    except:
        pass
    
    if vscode_count > 0:
        print(f"🔒 {vscode_count} terminal VSCode tetap aman!")

def main():
    # Cek dependencies
    try:
        import win32gui
        import win32con
        import win32process
        import psutil
    except ImportError as e:
        print(f"Error: Module tidak ditemukan - {e}")
        print("Install dengan:")
        print("  pip install pywin32 psutil")
        return
    
    print("🛑 SPRING BOOT SERVER KILLER")
    print("="*40)
    print("🎯 Target: Semua Spring Boot servers (Java processes)")
    print("🔒 Perlindungi: Terminal VSCode")
    print("💀 Metode: Graceful → Force Kill → Cleanup")
    print()
    print("⚠️  WARNING: Ini akan menghentikan SEMUA server Spring Boot!")
    print("   Termasuk Eureka, API Gateway, Microservices, dll.")
    print()
    
    response = input("Lanjutkan menghentikan semua Spring Boot servers? (y/N): ").strip().lower()
    
    if response in ['y', 'yes']:
        stop_all_spring_boot_servers()
    else:
        print("❌ Dibatalkan")

if __name__ == "__main__":
    main()