package mendoventura.ui;

import mendoventura.model.Admin;
import mendoventura.model.Carrier;
import mendoventura.model.ItemSewa;
import mendoventura.model.KomporLapangan;
import mendoventura.model.Kondisi;
import mendoventura.model.Laporan;
import mendoventura.model.Pelanggan;
import mendoventura.model.Pemilik;
import mendoventura.model.Peralatan;
import mendoventura.model.Sewa;
import mendoventura.model.SleepingBag;
import mendoventura.model.Tenda;
import mendoventura.payment.BayarTransfer;
import mendoventura.payment.BayarTunai;
import mendoventura.payment.Pembayaran;
import mendoventura.repository.Katalog;
import mendoventura.service.SewaService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final Scanner input = new Scanner(System.in);
    private static final Katalog katalog = new Katalog(new ArrayList<>());
    private static final SewaService sewaService = new SewaService(new ArrayList<>());

    public static void main(String[] args) {
        isiKatalog();

        Admin admin = new Admin("A001", "Admin Toko", "123");
        Pemilik pemilik = new Pemilik("O001", "Pemilik Toko", "123");

        int pilihan;
        do {
            System.out.println("\n========================================");
            System.out.println("         WELCOME TO MENDOVENTURA          ");
            System.out.println("========================================");
            System.out.println("1. Lihat Katalog dan Ketersediaan Alat");
            System.out.println("2. Buat Reservasi Sewa Baru");
            System.out.println("3. Verifikasi Reservasi (Admin)");
            System.out.println("4. Persiapan & Penyerahan Alat (Admin)");
            System.out.println("5. Pengembalian & Pemeriksaan Kondisi (Admin)");
            System.out.println("6. Riwayat Penyewaan");
            System.out.println("7. Laporan Pendapatan & Sewa (Pemilik)");
            System.out.println("8. Kelola Data Alat (Admin)");
            System.out.println("0. Keluar Program");
            System.out.println("----------------------------------------");
            System.out.print("Silakan pilih menu (0-8): ");

            pilihan = bacaInt();

            switch (pilihan) {
                case 1:
                    tampilkanKatalog();
                    break;
                case 2:
                    buatReservasiBaru();
                    break;
                case 3:
                    verifikasi(admin);
                    break;
                case 4:
                    prosesPenyerahan();
                    break;
                case 5:
                    prosesPengembalian();
                    break;
                case 6:
                    tampilkanRiwayat();
                    break;
                case 7:
                    System.out.println("\n--- LAPORAN PEMILIK: " + pemilik.getNama() + " ---");
                    new Laporan(new ArrayList<>(sewaService.getDaftarSewa())).tampilkan();
                    break;
                case 8:
                    kelolaAlat();
                    break;
                case 0:
                    System.out.println("\nTerima kasih telah menggunakan layanan MENDOVENTURA. Salam Booyah!");
                    break;
                default:
                    System.out.println("Pilihan menu tidak valid. Silakan coba lagi.");
            }
        } while (pilihan != 0);

        input.close();
    }

    private static int bacaInt() {
        while (!input.hasNextInt()) {
            System.out.print("Input salah. Masukkan angka dengan benar: ");
            input.next();
        }
        int n = input.nextInt();
        input.nextLine();
        return n;
    }

    private static double bacaDouble() {
        while (!input.hasNextDouble()) {
            System.out.print("Input salah. Masukkan angka desimal dengan benar: ");
            input.next();
        }
        double n = input.nextDouble();
        input.nextLine();
        return n;
    }

    private static void isiKatalog() {
        katalog.tambahPeralatan(new Tenda("T001", "Tenda Dome 4P", 40000, 5, 4));
        katalog.tambahPeralatan(new Carrier("C001", "Carrier 60L", 25000, 8, 60));
        katalog.tambahPeralatan(new SleepingBag("S001", "Sleeping Bag Polar", 15000, 10, "Polar"));
        katalog.tambahPeralatan(new KomporLapangan("K001", "Kompor Portable Gas", 20000, 4, "Gas"));
    }

    private static void tampilkanKatalog() {
        System.out.println("\n========== KATALOG ALAT OUTDOOR ==========");
        int i = 1;
        for (Peralatan alat : katalog.getDaftarPeralatan()) {
            if (alat.isAktif()) {
                System.out.print(i + ". ");
                alat.tampilkanInfo();
                alat.spesifikasi();
                System.out.println("----------------------------------------");
            }
            i++;
        }
    }

    private static void buatReservasiBaru() {
        try {
            System.out.println("\n--- FORMULIR RESERVASI SEWA ---");
            System.out.print("Masukkan nama penyewa : ");
            String namaPelanggan = input.nextLine();
            System.out.print("Masukkan nomor telepon/kontak: ");
            String kontak = input.nextLine();

            String kodePelanggan = "P" + String.format("%03d", sewaService.getDaftarSewa().size() + 1);
            Pelanggan pelanggan = new Pelanggan(kodePelanggan, namaPelanggan, "123456", kontak);

            System.out.print("Tanggal mulai sewa (YYYY-MM-DD): ");
            LocalDate mulai = LocalDate.parse(input.nextLine());

            System.out.print("Tanggal selesai/kembali (YYYY-MM-DD): ");
            LocalDate selesai = LocalDate.parse(input.nextLine());

            Map<Peralatan, Integer> keranjangItem = new LinkedHashMap<>();
            boolean tambahLagi = true;
            while (tambahLagi) {
                tampilkanKatalog();
                System.out.print("Pilih nomor alat yang ingin disewa: ");
                int nomor = bacaInt();

                if (nomor < 1 || nomor > katalog.jumlahPeralatan()) {
                    System.out.println("Nomor alat tidak terdaftar.");
                } else {
                    Peralatan alat = katalog.getPeralatan(nomor - 1);
                    if (!alat.isAktif()) {
                        System.out.println("Alat sedang tidak aktif.");
                    } else {
                        System.out.print("Masukkan jumlah unit yang disewa: ");
                        int jumlah = bacaInt();
                        if (jumlah <= 0) {
                            System.out.println("Jumlah unit harus lebih dari 0.");
                        } else {
                            keranjangItem.put(alat, keranjangItem.getOrDefault(alat, 0) + jumlah);
                            System.out.println("Berhasil menambahkan " + alat.getNama() + " (" + jumlah + " unit) ke keranjang.");
                        }
                    }
                }

                System.out.print("Ingin tambah alat lain? (y/n): ");
                tambahLagi = input.nextLine().equalsIgnoreCase("y");
            }

            if (keranjangItem.isEmpty()) {
                System.out.println("Reservasi dibatalkan karena belum ada alat yang dipilih.");
                return;
            }

            System.out.println("\n--- METODE PEMBAYARAN ---");
            System.out.println("1. Tunai");
            System.out.println("2. Transfer / QRIS");
            System.out.print("Pilih metode pembayaran: ");
            int metode = bacaInt();

            Pembayaran pembayaran;
            String bukti;
            if (metode == 1) {
                pembayaran = new BayarTunai();
                bukti = "Tunai";
            } else if (metode == 2) {
                System.out.print("Masukkan Nama Bank / E-Wallet (BCA/BRI/ShopeePay/Gopay): ");
                String bank = input.nextLine();
                if (bank.trim().isEmpty()) {
                    System.out.println("Nama bank/e-wallet tidak boleh kosong. Reservasi dibatalkan.");
                    return;
                }
                pembayaran = new BayarTransfer(bank);
                bukti = "Transfer - " + bank.trim();
            } else {
                System.out.println("Metode pembayaran tidak valid. Reservasi dibatalkan.");
                return;
            }

            System.out.print("Jaminan identitas (KTP, Kartu siswa/mahasiswa): ");
            String jaminan = input.nextLine();

            Sewa sewa = sewaService.buatReservasi(pelanggan, mulai, selesai, keranjangItem, pembayaran, bukti, jaminan);

            System.out.println("\nReservasi berhasil dibuat!");
            System.out.println("Kode Sewa           : " + sewa.getKode());
            System.out.println("Status Saat Ini     : " + sewa.getStatus());
            System.out.println("Metode Pembayaran   : " + sewa.getBuktiBayar());
            System.out.println("Total Estimasi Biaya: Rp " + sewa.hitungSubtotal());

        } catch (DateTimeParseException e) {
            System.out.println("Format tanggal tidak valid. Mohon gunakan format YYYY-MM-DD.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Gagal membuat reservasi: " + e.getMessage());
        }
    }

    private static Sewa pilihSewa() {
        List<Sewa> daftar = sewaService.getDaftarSewa();
        if (daftar.isEmpty()) {
            System.out.println("\nBelum ada transaksi penyewaan yang tercatat.");
            return null;
        }

        System.out.println("\n--- DAFTAR TRANSAKSI SEWA ---");
        for (int i = 0; i < daftar.size(); i++) {
            Sewa s = daftar.get(i);
            System.out.println((i + 1) + ". Kode: " + s.getKode() + " | Pelanggan: " + s.getPelanggan().getNama() + " | Status: " + s.getStatus());
        }

        System.out.print("Pilih nomor transaksi: ");
        int n = bacaInt();

        if (n < 1 || n > daftar.size()) {
            System.out.println("Nomor transaksi tidak valid.");
            return null;
        }
        return daftar.get(n - 1);
    }

    private static void verifikasi(Admin admin) {
        Sewa s = pilihSewa();
        if (s == null) return;

        System.out.println("\n--- DETAIL VERIFIKASI ---");
        System.out.println("Pelanggan       : " + s.getPelanggan().getNama());
        System.out.println("Bukti Pembayaran: " + s.getBuktiBayar());
        System.out.println("Jaminan         : " + s.getJaminan());
        System.out.print("Apakah valid dan ingin disetujui? (y/n): ");
        boolean disetujui = input.nextLine().equalsIgnoreCase("y");

        try {
            sewaService.verifikasiReservasi(s, admin, disetujui);
            if (disetujui) {
                System.out.println("Verifikasi reservasi telah dikonfirmasi oleh Admin.");
            } else {
                System.out.println("Verifikasi reservasi ditolak.");
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Gagal memverifikasi reservasi: " + e.getMessage());
        }
    }

    private static void prosesPenyerahan() {
        Sewa s = pilihSewa();
        if (s == null) return;

        try {
            System.out.println("\nMemproses persiapan dan penyerahan peralatan...");
            sewaService.serahkanAlat(s);
            System.out.println("Alat telah berhasil diserahkan kepada " + s.getPelanggan().getNama() + ". Status berubah menjadi DISEWA.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Gagal menyerahkan alat: " + e.getMessage());
        }
    }

    private static void prosesPengembalian() {
        Sewa s = pilihSewa();
        if (s == null) return;

        try {
            System.out.print("Masukkan tanggal aktual pengembalian (YYYY-MM-DD): ");
            LocalDate aktual = LocalDate.parse(input.nextLine());

            Map<ItemSewa, Kondisi> kondisiPerItem = new LinkedHashMap<>();
            Map<ItemSewa, Double> biayaPerbaikanPerItem = new LinkedHashMap<>();

            System.out.println("\n--- PEMERIKSAAN KONDISI ALAT ---");
            for (ItemSewa item : s.getDaftarItem()) {
                System.out.println("\nAlat: " + item.getPeralatan().getNama() + " (Jumlah: " + item.getJumlah() + ")");
                System.out.println("Pilih Kondisi: 1. Baik (Normal) | 2. Rusak | 3. Hilang");
                System.out.print("Masukkan pilihan kondisi (1/2/3): ");
                int k = bacaInt();

                if (k == 1) {
                    kondisiPerItem.put(item, Kondisi.BAIK);
                } else if (k == 2) {
                    kondisiPerItem.put(item, Kondisi.RUSAK);
                    System.out.print("Masukkan biaya perbaikan alat: Rp ");
                    biayaPerbaikanPerItem.put(item, bacaDouble());
                } else if (k == 3) {
                    kondisiPerItem.put(item, Kondisi.HILANG);
                } else {
                    System.out.println("Pilihan kondisi tidak valid. Proses pemeriksaan dibatalkan.");
                    return;
                }
            }

            double totalDenda = sewaService.prosesPengembalian(s, aktual, kondisiPerItem, biayaPerbaikanPerItem);
            System.out.println("\nProses pengembalian dan pemeriksaan selesai!");
            if (totalDenda > 0) {
                System.out.println("Total Denda Dikenakan: Rp " + totalDenda);
            } else {
                System.out.println("Tidak ada denda (tepat waktu & kondisi baik).");
            }
            s.tampilkan();

        } catch (DateTimeParseException e) {
            System.out.println("Format tanggal tidak valid. Gunakan format YYYY-MM-DD.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Gagal memproses pengembalian: " + e.getMessage());
        }
    }

    private static void tampilkanRiwayat() {
        List<Sewa> daftar = sewaService.getDaftarSewa();
        if (daftar.isEmpty()) {
            System.out.println("\n[Informasi] Belum ada riwayat penyewaan.");
        } else {
            System.out.println("\n========== SEMUA RIWAYAT PENYEWAAN ==========");
            for (Sewa s : daftar) {
                s.tampilkan();
                System.out.println("---------------------------------------------");
            }
        }
    }

    private static void kelolaAlat() {
        try {
            System.out.println("\n--- KELOLA DATA KATALOG ALAT ---");
            System.out.println("1. Tambah Alat Baru");
            System.out.println("2. Ubah / Update Stok Alat");
            System.out.println("3. Nonaktifkan Alat");
            System.out.print("Pilih menu kelola (1-3): ");
            int p = bacaInt();

            if (p == 1) {
                System.out.println("Jenis Alat: 1. Tenda | 2. Carrier | 3. Sleeping Bag | 4. Kompor Lapangan");
                System.out.print("Pilih jenis (1-4): ");
                int j = bacaInt();

                System.out.print("Masukkan Kode Alat (contoh T002): ");
                String kode = input.nextLine();
                System.out.print("Masukkan Nama Alat: ");
                String nama = input.nextLine();
                System.out.print("Harga Sewa per Hari (Rp): ");
                double harga = bacaDouble();
                System.out.print("Jumlah Stok Awal: ");
                int stok = bacaInt();

                if (j == 1) katalog.tambahPeralatan(new Tenda(kode, nama, harga, stok, 4));
                else if (j == 2) katalog.tambahPeralatan(new Carrier(kode, nama, harga, stok, 60));
                else if (j == 3) katalog.tambahPeralatan(new SleepingBag(kode, nama, harga, stok, "Standar"));
                else if (j == 4) katalog.tambahPeralatan(new KomporLapangan(kode, nama, harga, stok, "Gas"));
                else {
                    System.out.println("Jenis alat tidak valid.");
                    return;
                }
                System.out.println("Alat baru berhasil ditambahkan ke katalog.");

            } else {
                tampilkanKatalog();
                System.out.print("Pilih nomor urut alat yang ingin dikelola: ");
                int n = bacaInt();

                if (n < 1 || n > katalog.jumlahPeralatan()) {
                    System.out.println("Nomor alat tidak valid.");
                    return;
                }

                Peralatan a = katalog.getPeralatan(n - 1);
                if (p == 2) {
                    System.out.print("Masukkan perubahan stok (angka positif menambah, negatif mengurangi): ");
                    a.ubahStok(bacaInt());
                    System.out.println("Stok berhasil diperbarui.");
                } else if (p == 3) {
                    a.nonaktifkan();
                    System.out.println("Alat berhasil dinonaktifkan dari katalog.");
                } else {
                    System.out.println("Pilihan menu kelola tidak valid.");
                }
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Gagal mengelola alat: " + e.getMessage());
        }
    }
}