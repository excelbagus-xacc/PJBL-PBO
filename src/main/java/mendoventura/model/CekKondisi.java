/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mendoventura.model;

/**
 *
 * @author HP
 */

import java.time.LocalDateTime;

public class CekKondisi {
    private String tahap;
    private Kondisi kondisi;
    private String catatan;
    private LocalDateTime waktu;

    public CekKondisi(String tahap, Kondisi kondisi, String catatan) {
        this.tahap = tahap;
        this.kondisi = kondisi;
        this.catatan = catatan;
        this.waktu = LocalDateTime.now();
    }

    public boolean bermasalah() {
        return kondisi != Kondisi.BAIK;
    }

    public void tampilkan() {
        System.out.println(tahap + " | " + kondisi + " | " + catatan + " | " + waktu);
    }
}
