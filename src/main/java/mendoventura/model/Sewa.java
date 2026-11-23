/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mendoventura.model;

/**
 *
 * @author HP
 */

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Sewa {
    private String kode;
    private Pelanggan pelanggan;
    private LocalDate tanggalSewa;
    private LocalDate tanggalKembali;
    private LocalDate tanggalAktualKembali;
    private ArrayList<ItemSewa> daftarItem;
    private StatusSewa status;
    private String buktiBayar;
    private String jaminan;
    private double totalDenda;

    public Sewa(String kode, Pelanggan pelanggan, LocalDate tanggalSewa, LocalDate tanggalKembali) {
        this.kode = kode;
        this.pelanggan = pelanggan;
        this.tanggalSewa = tanggalSewa;
        this.tanggalKembali = tanggalKembali;
        this.daftarItem = new ArrayList<>();
        this.status = StatusSewa.MENUNGGU_VERIFIKASI;
        this.totalDenda = 0;
    }

    public void tambahItem(Peralatan alat, int jumlah) { daftarItem.add(new ItemSewa(alat, jumlah)); }
    public ArrayList<ItemSewa> getDaftarItem() { return daftarItem; }
    public StatusSewa getStatus() { return status; }
    public void ubahStatus(StatusSewa status) { this.status = status; }
    public String getKode() { return kode; }
    public Pelanggan getPelanggan() { return pelanggan; }
    public LocalDate getTanggalSewa() { return tanggalSewa; }
    public LocalDate getTanggalKembali() { return tanggalKembali; }
    public String getBuktiBayar() { return buktiBayar; }
    public String getJaminan() { return jaminan; }
    public double getTotalDenda() { return totalDenda; }
    public void unggahBukti(String buktiBayar, String jaminan) {
        this.buktiBayar = buktiBayar;
        this.jaminan = jaminan;
    }
    public void catatPengembalian(LocalDate tanggalAktual) { this.tanggalAktualKembali = tanggalAktual; }
    public void tambahDenda(double denda) { if (denda > 0) totalDenda += denda; }

    public boolean tanggalBentrok(LocalDate mulai, LocalDate selesai) {
        return tanggalSewa.isBefore(selesai) && tanggalKembali.isAfter(mulai);
    }
    public int hitungLamaHari() {
        return (int) Math.max(1, ChronoUnit.DAYS.between(tanggalSewa, tanggalKembali));
    }
    public double hitungSubtotal() {
        double total = 0;
        for (ItemSewa item : daftarItem) total += item.getSubtotal(hitungLamaHari());
        return total;
    }
    public double hitungTotal() { return hitungSubtotal() + totalDenda; }
    public long hitungHariTerlambat(LocalDate tanggalAktualKembali) {
        if (tanggalAktualKembali.isAfter(tanggalKembali))
            return ChronoUnit.DAYS.between(tanggalKembali, tanggalAktualKembali);
        return 0;
    }
    public void tampilkan() {
        System.out.println("\nKode sewa: " + kode);
        System.out.println("Pelanggan: " + pelanggan.getNama());
        System.out.println("Tanggal: " + tanggalSewa + " sampai " + tanggalKembali);
        System.out.println("Status: " + status);
        System.out.println("Bukti bayar: " + buktiBayar + " | Jaminan: " + jaminan);
        for (ItemSewa item : daftarItem) item.tampilkan();
        System.out.println("Biaya sewa: Rp" + hitungSubtotal());
        System.out.println("Total denda: Rp" + totalDenda);
    }
}
