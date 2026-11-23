/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mendoventura.model;

/**
 *
 * @author HP
 */

public abstract class Peralatan {
    private String kode;
    private String nama;
    private String kategori;
    private double hargaPerHari;
    private int stok;
    private boolean aktif;

    public Peralatan(String kode, String nama, String kategori,
                     double hargaPerHari, int stok) {
        if (kode == null || kode.isBlank() || nama == null || nama.isBlank()) {
            throw new IllegalArgumentException("Kode dan nama peralatan wajib diisi");
        }
        if (hargaPerHari <= 0) {
            throw new IllegalArgumentException("Harga sewa per hari harus lebih dari 0");
        }
        if (stok < 0) {
            throw new IllegalArgumentException("Stok awal tidak boleh negatif");
        }
        this.kode = kode;
        this.nama = nama;
        this.kategori = kategori;
        this.hargaPerHari = hargaPerHari;
        this.stok = stok;
        this.aktif = true;
    }

    public String getKode() { return kode; }
    public String getNama() { return nama; }
    public String getKategori() { return kategori; }
    public double getHargaPerHari() { return hargaPerHari; }
    public int getStok() { return stok; }
    public boolean isAktif() { return aktif; }

    public boolean ubahStok(int perubahan) {
        if (stok + perubahan < 0) {
            throw new IllegalStateException("Stok " + nama + " tidak mencukupi");
        }
        stok += perubahan;
        return true;
    }

    public void nonaktifkan() { aktif = false; }

    public abstract void spesifikasi();

    public void tampilkanInfo() {
        System.out.println(kode + " | " + nama + " | " + kategori + " | Rp" + hargaPerHari + "/hari | Stok: " + stok);
    }
}
