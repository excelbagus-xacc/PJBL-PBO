/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mendoventura.model;

/**
 *
 * @author HP
 */

public class Pelanggan extends Pengguna {
    private String kontak;

    public Pelanggan(String id, String nama, String password, String kontak) {
        super(id, nama, password);
        setKontak(kontak);
    }

    public String getKontak() {
        return kontak;
    }

    public void setKontak(String kontak) {
        if (kontak == null || kontak.isBlank()) {
            throw new IllegalArgumentException("Nomor kontak pelanggan wajib diisi");
        }
        this.kontak = kontak;
    }

    @Override
    public void tampilkanInfo() {
        super.tampilkanInfo();
        System.out.println("Kontak   : " + kontak);
    }
}
