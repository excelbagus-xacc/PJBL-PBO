/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mendoventura.model;

/**
 *
 * @author HP
 */

public class Admin extends Pengguna {
    public Admin(String id, String nama, String password) { super(id, nama, password); }
    public void verifikasiSewa(Sewa sewa) {
        if (sewa.getStatus() == StatusSewa.MENUNGGU_VERIFIKASI) {
            sewa.ubahStatus(StatusSewa.DIKONFIRMASI);
            System.out.println("Reservasi berhasil diverifikasi oleh admin.");
        } else System.out.println("Reservasi tidak dalam status menunggu verifikasi.");
    }
}
