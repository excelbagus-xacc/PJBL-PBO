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
            System.out.println("Stok tidak boleh negatif.");
            return false;
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
