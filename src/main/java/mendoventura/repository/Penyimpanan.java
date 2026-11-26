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
import java.util.Collections;
import java.util.List;

public class Penyimpanan<T> {
    private final List<T> daftar;

    public Penyimpanan() {
        this.daftar = new ArrayList<>();
    }

    public Penyimpanan(List<T> daftarAwal) {
        if (daftarAwal == null) {
            throw new IllegalArgumentException("Daftar data tidak boleh null.");
        }
        this.daftar = daftarAwal;
    }

    public void tambah(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Data tidak boleh kosong.");
        }
        daftar.add(item);
    }

    public List<T> semua() {
        return Collections.unmodifiableList(daftar);
    }

    public T ambil(int index) {
        return daftar.get(index);
    }

    public int jumlah() {
        return daftar.size();
    }
}
