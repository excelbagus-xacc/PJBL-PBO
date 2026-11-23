/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mendoventura.payment;

/**
 *
 * @author HP
 */

public class BayarTunai implements Pembayaran {
    @Override
    public boolean prosesPembayaran(double jumlah) {
        System.out.println("Pembayaran tunai sebesar Rp" + jumlah + " diterima di kasir.");
        return true;
    }
}
