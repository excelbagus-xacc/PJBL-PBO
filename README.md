# Mendoventura - Sistem Penyewaan Alat Outdoor (v3)

Aplikasi berbasis Java CLI (Maven) untuk mengelola inventaris peralatan outdoor, reservasi penyewaan, verifikasi admin, pembayaran, hingga perhitungan denda keterlambatan, kerusakan, dan kehilangan.

## Struktur Package
* `mendoventura.model`: Entitas domain (`Pelanggan`, `Admin`, `Pemilik`, `Peralatan`, `Tenda`, `SleepingBag`, `Carrier`, `KomporLapangan`, `Sewa`, `ItemSewa`, `StatusSewa`, `Kondisi`, `Laporan`).
* `mendoventura.service`: Logika bisnis utama (`SewaService`, `Ketersediaan`).
* `mendoventura.payment`: Implementasi metode pembayaran (`Pembayaran`, `BayarTunai`, `BayarTransfer`).
* `mendoventura.denda`: Implementasi perhitungan denda (`AturanDenda`, `DendaTerlambat`, `DendaKerusakan`, `DendaKehilangan`).
* `mendoventura.repository`: Penyimpanan data in-memory (`Katalog`).
* `mendoventura.ui`: Antarmuka pengguna berbasis konsol/CLI (`Main`).

## Cara Menjalankan
1. **Kompilasi & Jalankan Aplikasi:**
   ```bash
   mvn compile exec:java

   