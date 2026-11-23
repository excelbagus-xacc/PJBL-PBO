/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mendoventura.model;


/**
 *
 * @author HP
 */

public class Pemilik extends Pengguna {
    public Pemilik(String id, String nama, String password) {
        super(id, nama, password);
    }

    public void lihatLaporan(Laporan laporan) {
        laporan.tampilkan();
    }
}
