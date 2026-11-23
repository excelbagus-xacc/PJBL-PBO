/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mendoventura.model;

/**
 *
 * @author HP
 */

public class Tenda extends Peralatan {
    private int kapasitasOrang;

    public Tenda(String kode, String nama, double harga, int stok, int kapasitasOrang) {
        super(kode, nama, "Tenda", harga, stok);
        this.kapasitasOrang = kapasitasOrang;
    }

    @Override
    public void spesifikasi() {
        System.out.println("Kapasitas tenda: " + kapasitasOrang + " orang.");
    }
}
