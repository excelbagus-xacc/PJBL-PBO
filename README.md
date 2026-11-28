# Mendoventura - Sistem Penyewaan Alat Outdoor (v4)

Aplikasi berbasis Java CLI (Maven) untuk mengelola inventaris peralatan outdoor, reservasi penyewaan, verifikasi admin, pembayaran, hingga perhitungan denda keterlambatan, kerusakan, dan kehilangan.

## Fitur & Pembaruan v4
- **Manajemen Exception Kustom (`mendoventura.exception`):** Penambahan penanganan error spesifik seperti `StokTidakCukupException` dan `JadwalBentrokException`.
- **Arsitektur Repository Generik (`mendoventura.repository`):** Penerapan kelas generik `Penyimpanan<T>` untuk pengelolaan data yang lebih fleksibel.
- **Peningkatan Validasi & Logika Bisnis:** Perbaikan validasi tanggal pengembalian, pengecekan biaya perbaikan, serta pembaruan logika pengurangan stok untuk alat yang hilang.

## Struktur Package
- `mendoventura.model`: Entitas domain (`Pelanggan`, `Admin`, `Pemilik`, `Peralatan`, `Tenda`, `SleepingBag`, `Carrier`, `KomporLapangan`, `Sewa`, `ItemSewa`, `StatusSewa`, `Kondisi`, `Laporan`).
- `mendoventura.service`: Logika bisnis utama (`SewaService`, `Ketersediaan`).
- `mendoventura.payment`: Implementasi metode pembayaran (`Pembayaran`, `BayarTunai`, `BayarTransfer`).
- `mendoventura.denda`: Implementasi perhitungan denda (`AturanDenda`, `DendaTerlambat`, `DendaKerusakan`, `DendaKehilangan`).
- `mendoventura.repository`: Penyimpanan data in-memory menggunakan kelas generik (`Katalog`, `Penyimpanan`).
- `mendoventura.exception`: Kumpulan custom exception (`StokTidakCukupException`, `JadwalBentrokException`).
- `mendoventura.ui`: Antarmuka pengguna berbasis konsol/CLI (`Main`).

## Cara Menjalankan
1. **Kompilasi & Jalankan Aplikasi:**
   ```bash
   mvn compile exec:java