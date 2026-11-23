/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mendoventura.payment;

/**
 *
 * @author HP
 */

public class BayarTransfer implements Pembayaran {
    private String namaBank;

    public BayarTransfer(String namaBank) {
        this.namaBank = namaBank;
    }

    @Override
    public boolean prosesPembayaran(double jumlah) {
        System.out.println("Pembayaran transfer via " + namaBank + " sebesar Rp" + jumlah + " terverifikasi.");
        return true;
    }
}
