/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mendoventura.repository;

import mendoventura.model.Peralatan;

/**
 *
 * @author HP
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Katalog mengelola referensi objek Peralatan yang dibuat dari luar.
 * Peralatan tetap dapat hidup dan digunakan tanpa objek Katalog (agregasi).
 */
public class Katalog {
    private final ArrayList<Peralatan> daftarPeralatan;

    public Katalog(ArrayList<Peralatan> daftarPeralatan) {
        if (daftarPeralatan == null) {
            throw new IllegalArgumentException("Daftar peralatan tidak boleh null.");
        }
        this.daftarPeralatan = daftarPeralatan;
    }

    public void tambahPeralatan(Peralatan peralatan) {
        if (peralatan != null) {
            daftarPeralatan.add(peralatan);
        }
    }

    public List<Peralatan> getDaftarPeralatan() {
        return Collections.unmodifiableList(daftarPeralatan);
    }

    public Peralatan getPeralatan(int index) {
        return daftarPeralatan.get(index);
    }

    public int jumlahPeralatan() {
        return daftarPeralatan.size();
    }
}
