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

public class Laporan {
    private ArrayList<Sewa> daftarSewa;
    public Laporan(ArrayList<Sewa> daftarSewa) { this.daftarSewa = daftarSewa; }

    public void tampilkan() {
        double pendapatanSewa = 0;
        double pendapatanDenda = 0;
        System.out.println("\n===== LAPORAN PENYEWAAN MENDOVENTURA =====");
        if (daftarSewa.isEmpty()) { System.out.println("Belum ada transaksi."); return; }
        for (Sewa sewa : daftarSewa) {
            sewa.tampilkan();
            if (sewa.getStatus() == StatusSewa.SELESAI) {
                pendapatanSewa += sewa.hitungSubtotal();
                pendapatanDenda += sewa.getTotalDenda();
            }
        }
        System.out.println("Total pendapatan sewa: Rp" + pendapatanSewa);
        System.out.println("Total pendapatan denda: Rp" + pendapatanDenda);
        System.out.println("Total pendapatan: Rp" + (pendapatanSewa + pendapatanDenda));
    }
}
