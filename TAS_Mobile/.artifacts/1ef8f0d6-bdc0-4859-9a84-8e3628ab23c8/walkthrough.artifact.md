# Walkthrough - Implementasi UI Wireframe

Saya telah memperbarui UI `ExploreFragment` agar sesuai dengan rancangan wireframe yang Anda berikan.

## Perubahan Utama

### 1. Header Peta
Menambahkan header di bagian atas yang berisi judul **"Peta Wisata Salatiga"** dan dropdown **Filter** yang lebih terintegrasi dalam satu baris.

### 2. Kartu Pratinjau Cepat (Bottom Sheet)
Memperbarui tata letak saat marker ditekan:
- **Badge**: Menampilkan kategori "WISATA RESMI" atau "SPOT WARGA" di bagian paling atas.
- **Informasi**: Menyusun Nama, Alamat, dan Rating sesuai urutan di wireframe.
- **Tombol Aksi**: Menambahkan tombol **Lihat Detail** (dengan gaya outline) dan **Simpan Favorit** (dengan icon bintang) di bagian bawah kartu.

### 3. Internasionalisasi (Strings)
Memindahkan semua teks statis ke `strings.xml` untuk praktik pengembangan yang lebih baik.

## Hasil Verifikasi
- [x] Build Gradle: **Berhasil**
- [x] Layout XML: **Tervalidasi**
- [x] Logika Klik: **Tersambung**

## Cara Mengetes
1. Jalankan aplikasi.
2. Di tab **Eksplorasi**, Anda akan melihat header baru di bagian atas.
3. Klik salah satu marker di peta.
4. Bottom sheet akan muncul dengan desain kartu yang baru.
5. Coba tekan tombol **Simpan Favorit** untuk memverifikasi penyimpanan ke Firebase.
