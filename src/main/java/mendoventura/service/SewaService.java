package mendoventura.service;

import mendoventura.denda.DendaKehilangan;
import mendoventura.denda.DendaKerusakan;
import mendoventura.denda.DendaTerlambat;
import mendoventura.exception.JadwalBentrokException;
import mendoventura.exception.StokTidakCukupException;
import mendoventura.model.Admin;
import mendoventura.model.ItemSewa;
import mendoventura.model.Kondisi;
import mendoventura.model.Pelanggan;
import mendoventura.model.Peralatan;
import mendoventura.model.Sewa;
import mendoventura.model.StatusSewa;
import mendoventura.payment.Pembayaran;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SewaService {

    private static final double TARIF_DENDA_TERLAMBAT_PER_HARI = 25000.0;
    private static final int PENGALI_DENDA_HILANG = 5;

    private final ArrayList<Sewa> daftarSewa;
    private final Ketersediaan ketersediaan;
    private int urutanSewa = 1;

    public SewaService(ArrayList<Sewa> daftarSewa) {
        this.daftarSewa = (daftarSewa != null)
                ? daftarSewa : new ArrayList<>();
        this.ketersediaan = new Ketersediaan(this.daftarSewa);
    }

    public List<Sewa> getDaftarSewa() {
        return Collections.unmodifiableList(daftarSewa);
    }

    public Sewa buatReservasi(
            Pelanggan pelanggan,
            LocalDate tanggalMulai,
            LocalDate tanggalSelesai,
            Map<Peralatan, Integer> keranjangItem,
            Pembayaran pembayaran,
            String buktiBayar,
            String jaminan
    ) throws StokTidakCukupException, JadwalBentrokException {
        if (pelanggan == null) {
            throw new IllegalArgumentException("Data pelanggan tidak boleh kosong.");
        }
        if (tanggalMulai == null || tanggalSelesai == null
                || !tanggalSelesai.isAfter(tanggalMulai)) {
            throw new IllegalArgumentException("Tanggal kembali harus setelah tanggal mulai sewa.");
        }
        if (keranjangItem == null || keranjangItem.isEmpty()) {
            throw new IllegalArgumentException("Minimal harus ada 1 alat yang dipilih untuk disewa.");
        }
        if (pembayaran == null) {
            throw new IllegalArgumentException("Metode pembayaran tidak boleh kosong.");
        }
        if (jaminan == null || jaminan.trim().isEmpty()) {
            throw new IllegalArgumentException("Jaminan identitas wajib diisi.");
        }

        String kodeSewa = "S" + String.format("%03d", urutanSewa++);
        Sewa sewa = new Sewa(kodeSewa, pelanggan, tanggalMulai, tanggalSelesai);

        for (Map.Entry<Peralatan, Integer> entry : keranjangItem.entrySet()) {
            Peralatan alat = entry.getKey();
            Integer jumlah = entry.getValue();

            if (alat == null || jumlah == null || jumlah <= 0) {
                throw new IllegalArgumentException("Data alat atau jumlah unit tidak valid.");
            }
            if (jumlah > alat.getStok()) {
                throw new StokTidakCukupException(alat.getKode(), jumlah, alat.getStok());
            }
            if (!ketersediaan.tersedia(alat, jumlah, tanggalMulai, tanggalSelesai)) {
                throw new JadwalBentrokException(
                        alat.getKode(),
                        "Jadwal sewa alat " + alat.getNama() + " bentrok pada tanggal tersebut."
                );
            }

            sewa.tambahItem(alat, jumlah);
        }

        double totalBayar = sewa.hitungSubtotal();

        if (!pembayaran.prosesPembayaran(totalBayar)) {
            throw new IllegalStateException("Proses pembayaran gagal.");
        }

        sewa.unggahBukti(buktiBayar, jaminan.trim());
        daftarSewa.add(sewa);

        return sewa;
    }

    public void verifikasiReservasi(
            Sewa sewa,
            Admin admin,
            boolean disetujui
    ) {
        if (sewa == null) {
            throw new IllegalArgumentException("Data transaksi sewa tidak ditemukan.");
        }

        if (disetujui) {
            if (admin == null) {
                throw new IllegalArgumentException("Admin wajib diisi untuk menyetujui reservasi.");
            }
            admin.verifikasiSewa(sewa);
        } else {
            sewa.ubahStatus(StatusSewa.DITOLAK);
        }
    }

    public void serahkanAlat(Sewa sewa) {
        if (sewa == null) {
            throw new IllegalArgumentException("Data transaksi sewa tidak ditemukan.");
        }
        if (sewa.getStatus() != StatusSewa.DIKONFIRMASI) {
            throw new IllegalStateException(
                    "Penyerahan alat hanya dapat dilakukan pada transaksi berstatus DIKONFIRMASI."
            );
        }

        sewa.ubahStatus(StatusSewa.DIPERSIAPKAN);
        sewa.ubahStatus(StatusSewa.DISEWA);
    }

    public double prosesPengembalian(
            Sewa sewa,
            LocalDate tanggalAktual,
            Map<ItemSewa, Kondisi> kondisiPerItem,
            Map<ItemSewa, Double> biayaPerbaikanPerItem
    ) throws StokTidakCukupException {

        // 1. Validasi transaksi
        if (sewa == null) {
            throw new IllegalArgumentException("Data transaksi sewa tidak ditemukan.");
        }

        if (sewa.getStatus() != StatusSewa.DISEWA) {
            throw new IllegalStateException("Transaksi belum berada dalam status DISEWA.");
        }

        // 2. Validasi tanggal pengembalian
        if (tanggalAktual == null) {
            throw new IllegalArgumentException("Tanggal aktual pengembalian tidak boleh kosong.");
        }

        if (tanggalAktual.isBefore(sewa.getTanggalSewa())) {
            throw new IllegalArgumentException(
                    "Tanggal pengembalian tidak boleh sebelum tanggal mulai sewa."
            );
        }

        // 3. Validasi data kondisi alat
        if (kondisiPerItem == null) {
            throw new IllegalArgumentException(
                    "Data pemeriksaan kondisi alat tidak boleh kosong."
            );
        }

        // Menyimpan jumlah alat yang hilang berdasarkan peralatan
        Map<Peralatan, Integer> jumlahAlatHilang = new HashMap<>();

        // Menyimpan denda yang akan dikenakan
        double totalDendaBaru = 0.0;

        // 4. Menghitung denda keterlambatan
        long hariTerlambat = sewa.hitungHariTerlambat(tanggalAktual);

        if (hariTerlambat > 0) {
            totalDendaBaru += new DendaTerlambat(
                    TARIF_DENDA_TERLAMBAT_PER_HARI
            ).hitung(hariTerlambat);
        }

        // 5. Validasi seluruh kondisi dan biaya sebelum mengubah transaksi
        for (ItemSewa item : sewa.getDaftarItem()) {

            Kondisi kondisi = kondisiPerItem.get(item);

            if (kondisi == null) {
                throw new IllegalArgumentException(
                        "Kondisi untuk item "
                        + item.getPeralatan().getNama()
                        + " belum ditentukan."
                );
            }

            if (kondisi == Kondisi.BAIK) {
                continue;
            }

            if (kondisi == Kondisi.RUSAK) {

                double biayaPerbaikan =
                        (biayaPerbaikanPerItem != null
                        && biayaPerbaikanPerItem.get(item) != null)
                        ? biayaPerbaikanPerItem.get(item)
                        : 0.0;

                if (!Double.isFinite(biayaPerbaikan)
                        || biayaPerbaikan < 0) {
                    throw new IllegalArgumentException(
                            "Biaya perbaikan harus berupa angka yang valid dan tidak boleh negatif."
                    );
                }

                totalDendaBaru += new DendaKerusakan()
                        .hitung(biayaPerbaikan);

            } else if (kondisi == Kondisi.HILANG) {

                double nilaiGanti =
                        item.getPeralatan().getHargaPerHari()
                        * item.getJumlah()
                        * PENGALI_DENDA_HILANG;

                totalDendaBaru += new DendaKehilangan()
                        .hitung(nilaiGanti);

                jumlahAlatHilang.merge(
                        item.getPeralatan(),
                        item.getJumlah(),
                        Integer::sum
                );

            } else {
                throw new IllegalArgumentException(
                        "Kondisi alat tidak valid: " + kondisi
                );
            }
        }

        // 6. Memastikan stok cukup untuk mencatat seluruh alat yang hilang
        for (Map.Entry<Peralatan, Integer> entry
                : jumlahAlatHilang.entrySet()) {

            Peralatan alat = entry.getKey();
            int jumlahHilang = entry.getValue();

            if (alat.getStok() < jumlahHilang) {
                throw new StokTidakCukupException(alat.getKode(), jumlahHilang, alat.getStok());
            }
        }

        // 7. Mengubah status setelah seluruh validasi berhasil
        sewa.ubahStatus(StatusSewa.DIKEMBALIKAN);
        sewa.ubahStatus(StatusSewa.MEMERIKSA_KONDISI);

        // 8. Mencatat tanggal pengembalian
        sewa.catatPengembalian(tanggalAktual);

        // 9. Mencatat kondisi setiap alat
        for (ItemSewa item : sewa.getDaftarItem()) {

            Kondisi kondisi = kondisiPerItem.get(item);

            if (kondisi == Kondisi.BAIK) {

                item.catatKondisi(
                        "Masuk",
                        Kondisi.BAIK,
                        "Kondisi fisik aman dan lengkap"
                );

            } else if (kondisi == Kondisi.RUSAK) {

                item.catatKondisi(
                        "Masuk",
                        Kondisi.RUSAK,
                        "Terdapat kerusakan pada alat"
                );

            } else if (kondisi == Kondisi.HILANG) {

                item.catatKondisi(
                        "Masuk",
                        Kondisi.HILANG,
                        "Alat tidak dikembalikan/hilang"
                );
            }
        }

        // 10. Mengurangi stok peralatan yang hilang
        for (Map.Entry<Peralatan, Integer> entry
                : jumlahAlatHilang.entrySet()) {

            entry.getKey().ubahStok(-entry.getValue());
        }

        // 11. Menambahkan total denda ke transaksi
        sewa.tambahDenda(totalDendaBaru);

        // 12. Menyelesaikan transaksi
        sewa.ubahStatus(StatusSewa.SELESAI);

        return totalDendaBaru;
    }
}