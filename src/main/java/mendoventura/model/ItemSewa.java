/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mendoventura.model;

/**
 *
 * @author HP
 */

import java.util.ArrayList;

public class ItemSewa {
    private Peralatan peralatan;
    private int jumlah;
    private double hargaSatuan;
    private ArrayList<CekKondisi> catatanKondisi;

    public ItemSewa(Peralatan peralatan, int jumlah) {
        this.peralatan = peralatan;
        this.jumlah = jumlah;
        this.hargaSatuan = peralatan.getHargaPerHari();
        this.catatanKondisi = new ArrayList<>();
    }

    public Peralatan getPeralatan() { return peralatan; }
    public int getJumlah() { return jumlah; }
    public double getSubtotal(int lamaHari) {
        return hargaSatuan * jumlah * lamaHari;
    }

    public void catatKondisi(String tahap, Kondisi kondisi, String catatan) {
        catatanKondisi.add(new CekKondisi(tahap, kondisi, catatan));
    }

    public void tampilkan() {
        System.out.println(peralatan.getNama() + " x " + jumlah + " @ Rp" + hargaSatuan);
        for (CekKondisi cek : catatanKondisi) cek.tampilkan();
    }
}
