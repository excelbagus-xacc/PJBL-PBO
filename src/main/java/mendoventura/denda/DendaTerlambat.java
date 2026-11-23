/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mendoventura.denda;

/**
 *
 * @author HP
 */

public class DendaTerlambat implements AturanDenda {
    private double dendaPerHari;

    public DendaTerlambat(double dendaPerHari) {
        this.dendaPerHari = dendaPerHari;
    }

    @Override
    public double hitung(double hariTerlambat) {
        return hariTerlambat * dendaPerHari;
    }
}
