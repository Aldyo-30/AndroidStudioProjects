# Implementasi UI Berdasarkan Wireframe

Tujuannya adalah memperbarui UI `ExploreFragment` agar sesuai dengan desain wireframe yang diberikan, termasuk header baru, filter, dan tata letak bottom sheet yang diperbarui.

## Perubahan yang Diusulkan

### UI (Layout)

#### [MODIFY] [fragment_explore.xml](file:///C:/Users/felly/AndroidStudioProjects/TAS_Mobile/app/src/main/res/layout/fragment_explore.xml)
- Menambahkan header di bagian atas dengan teks "Peta Wisata Salatiga" dan Spinner filter.
- Memperbarui `bottom_sheet` (Kartu Pratinjau Cepat):
    - Menyusun ulang elemen: Badge, Nama, Alamat, dan Rating.
    - Menghapus label kategori (tidak ada di wireframe).
    - Menambahkan dua tombol di bagian bawah: **Lihat Detail** dan **Simpan Favorit**.

### Logika (Code)

#### [MODIFY] [ExploreFragment.kt](file:///C:/Users/felly/AndroidStudioProjects/TAS_Mobile/app/src/main/java/com/example/tas_mobile/ui/fragments/ExploreFragment.kt)
- Memperbarui binding untuk header baru (spinner filter).
- Memperbarui fungsi `showSpotDetail` untuk menghubungkan tombol **Lihat Detail** dan **Simpan Favorit**.
- Memindahkan logika bookmark ke tombol **Simpan Favorit**.

## Rencana Verifikasi

### Manual Verification
- Menjalankan aplikasi dan memastikan header muncul dengan benar.
- Menekan marker pada peta dan memverifikasi tampilan bottom sheet baru.
- Mengetes tombol "Simpan Favorit" untuk memastikan data tersimpan di Firebase.
- Mengetes tombol "Lihat Detail" (akan menampilkan Toast sebagai placeholder).
- Memastikan filter berfungsi dengan tata letak baru.
