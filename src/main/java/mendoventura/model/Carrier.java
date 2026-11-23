/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mendoventura.model;

/**
 *
 * @author HP
 */

public class Carrier extends Peralatan {
    private int kapasitasLiter;

    public Carrier(String kode, String nama, double harga, int stok, int kapasitasLiter) {
        super(kode, nama, "Carrier", harga, stok);
        this.kapasitasLiter = kapasitasLiter;
    }

    @Override
    public void spesifikasi() {
        System.out.println("Kapasitas carrier: " + kapasitasLiter + " liter.");
    }
}
