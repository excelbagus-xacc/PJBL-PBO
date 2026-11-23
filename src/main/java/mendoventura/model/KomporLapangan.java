/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mendoventura.model;

/**
 *
 * @author HP
 */

public class KomporLapangan extends Peralatan {
    private String jenisBahanBakar;

    public KomporLapangan(String kode, String nama, double harga, int stok,
                          String jenisBahanBakar) {
        super(kode, nama, "Kompor Lapangan", harga, stok);
        this.jenisBahanBakar = jenisBahanBakar;
    }

    @Override
    public void spesifikasi() {
        System.out.println("Bahan bakar kompor: " + jenisBahanBakar + ".");
    }
}
