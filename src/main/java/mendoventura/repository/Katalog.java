/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mendoventura.repository;

/**
 *
 * @author MyBook Hype AMD
 */

import java.util.ArrayList;
import java.util.List;
import mendoventura.model.Peralatan;

public class Katalog {
    private final Penyimpanan<Peralatan> daftarPeralatan;

    public Katalog() {
        this.daftarPeralatan = new Penyimpanan<>();
    }

    public Katalog(ArrayList<Peralatan> daftarPeralatan) {
        this.daftarPeralatan = new Penyimpanan<>(daftarPeralatan);
    }

    public void tambahPeralatan(Peralatan peralatan) {
        if (peralatan != null) {
            daftarPeralatan.tambah(peralatan);
        }
    }

    public List<Peralatan> getDaftarPeralatan() {
        return daftarPeralatan.semua();
    }

    public Peralatan getPeralatan(int index) {
        return daftarPeralatan.ambil(index);
    }

    public int jumlahPeralatan() {
        return daftarPeralatan.jumlah();
    }
}