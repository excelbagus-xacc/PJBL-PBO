/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mendoventura.model;

/**
 *
 * @author HP
 */

public class SleepingBag extends Peralatan {
    private String bahan;

    public SleepingBag(String kode, String nama, double harga, int stok, String bahan) {
        super(kode, nama, "Sleeping Bag", harga, stok);
        this.bahan = bahan;
    }

    @Override
    public void spesifikasi() {
        System.out.println("Bahan sleeping bag: " + bahan + ".");
    }
}
