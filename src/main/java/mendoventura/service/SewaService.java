package mendoventura.service;

import mendoventura.denda.DendaKehilangan;
import mendoventura.denda.DendaKerusakan;
import mendoventura.denda.DendaTerlambat;
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
import java.util.List;
import java.util.Map;

public class SewaService {
    private static final double TARIF_DENDA_TERLAMBAT_PER_HARI = 25000.0;
    private static final int PENGALI_DENDA_HILANG = 5;

    private final ArrayList<Sewa> daftarSewa;
    private final Ketersediaan ketersediaan;
    private int urutanSewa = 1;

    public SewaService(ArrayList<Sewa> daftarSewa) {
        this.daftarSewa = (daftarSewa != null) ? daftarSewa : new ArrayList<>();
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
    ) {
        if (pelanggan == null) {
            throw new IllegalArgumentException("Data pelanggan tidak boleh kosong.");
        }
        if (tanggalMulai == null || tanggalSelesai == null || !tanggalSelesai.isAfter(tanggalMulai)) {
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
            if (!ketersediaan.tersedia(alat, jumlah, tanggalMulai, tanggalSelesai)) {
                throw new IllegalStateException("Stok alat " + alat.getNama() + " tidak mencukupi pada tanggal tersebut.");
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

    public void verifikasiReservasi(Sewa sewa, Admin admin, boolean disetujui) {
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
            throw new IllegalStateException("Penyerahan alat hanya dapat dilakukan pada transaksi berstatus DIKONFIRMASI.");
        }

        sewa.ubahStatus(StatusSewa.DIPERSIAPKAN);
        sewa.ubahStatus(StatusSewa.DISEWA);
    }

    public double prosesPengembalian(
            Sewa sewa,
            LocalDate tanggalAktual,
            Map<ItemSewa, Kondisi> kondisiPerItem,
            Map<ItemSewa, Double> biayaPerbaikanPerItem
    ) {
        if (sewa == null) {
            throw new IllegalArgumentException("Data transaksi sewa tidak ditemukan.");
        }
        if (sewa.getStatus() != StatusSewa.DISEWA) {
            throw new IllegalStateException("Transaksi belum berada dalam status DISEWA.");
        }
        if (tanggalAktual == null) {
            throw new IllegalArgumentException("Tanggal aktual pengembalian tidak boleh kosong.");
        }
        if (kondisiPerItem == null) {
            throw new IllegalArgumentException("Data pemeriksaan kondisi alat tidak boleh kosong.");
        }

        sewa.ubahStatus(StatusSewa.DIKEMBALIKAN);
        sewa.ubahStatus(StatusSewa.MEMERIKSA_KONDISI);

        long hariTerlambat = sewa.hitungHariTerlambat(tanggalAktual);
        sewa.catatPengembalian(tanggalAktual);

        double totalDendaBaru = 0.0;
        if (hariTerlambat > 0) {
            double dendaTelat = new DendaTerlambat(TARIF_DENDA_TERLAMBAT_PER_HARI).hitung(hariTerlambat);
            sewa.tambahDenda(dendaTelat);
            totalDendaBaru += dendaTelat;
        }

        for (ItemSewa item : sewa.getDaftarItem()) {
            Kondisi kondisi = kondisiPerItem.get(item);
            if (kondisi == null) {
                throw new IllegalArgumentException("Kondisi untuk item " + item.getPeralatan().getNama() + " belum ditentukan.");
            }

            if (kondisi == Kondisi.BAIK) {
                item.catatKondisi("Masuk", Kondisi.BAIK, "Kondisi fisik aman dan lengkap");
            } else if (kondisi == Kondisi.RUSAK) {
                double biayaPerbaikan = (biayaPerbaikanPerItem != null && biayaPerbaikanPerItem.get(item) != null)
                        ? biayaPerbaikanPerItem.get(item)
                        : 0.0;
                if (biayaPerbaikan < 0) {
                    throw new IllegalArgumentException("Biaya perbaikan tidak boleh negatif.");
                }
                item.catatKondisi("Masuk", Kondisi.RUSAK, "Terdapat kerusakan pada alat");
                double dendaRusak = new DendaKerusakan().hitung(biayaPerbaikan);
                sewa.tambahDenda(dendaRusak);
                totalDendaBaru += dendaRusak;
            } else if (kondisi == Kondisi.HILANG) {
                item.catatKondisi("Masuk", Kondisi.HILANG, "Alat tidak dikembalikan/hilang");
                double nilaiGanti = item.getPeralatan().getHargaPerHari() * item.getJumlah() * PENGALI_DENDA_HILANG;
                double dendaHilang = new DendaKehilangan().hitung(nilaiGanti);
                sewa.tambahDenda(dendaHilang);
                totalDendaBaru += dendaHilang;
            } else {
                throw new IllegalArgumentException("Kondisi alat tidak valid: " + kondisi);
            }
        }

        sewa.ubahStatus(StatusSewa.SELESAI);
        return totalDendaBaru;
    }
}