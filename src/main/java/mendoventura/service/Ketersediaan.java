/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mendoventura.service;

import mendoventura.model.ItemSewa;
import mendoventura.model.Peralatan;
import mendoventura.model.Sewa;
import mendoventura.model.StatusSewa;

/**
 *
 * @author HP
 */

import java.util.ArrayList;
import java.time.LocalDate;

public class Ketersediaan {
    private ArrayList<Sewa> daftarSewa;
    public Ketersediaan(ArrayList<Sewa> daftarSewa) { this.daftarSewa = daftarSewa; }

    public int hitungStokTersedia(Peralatan alat, LocalDate mulai, LocalDate selesai) {
        int terpakai = 0;
        for (Sewa sewa : daftarSewa) {
            StatusSewa status = sewa.getStatus();
            boolean aktif = status != StatusSewa.DITOLAK && status != StatusSewa.DIBATALKAN
                    && status != StatusSewa.SELESAI;
            if (aktif && sewa.tanggalBentrok(mulai, selesai)) {
                for (ItemSewa item : sewa.getDaftarItem())
                    if (item.getPeralatan() == alat) terpakai += item.getJumlah();
            }
        }
        return alat.getStok() - terpakai;
    }

    public boolean tersedia(Peralatan alat, int jumlah, LocalDate mulai, LocalDate selesai) {
        if (!alat.isAktif() || jumlah <= 0 || !selesai.isAfter(mulai)) return false;
        return hitungStokTersedia(alat, mulai, selesai) >= jumlah;
    }
}
