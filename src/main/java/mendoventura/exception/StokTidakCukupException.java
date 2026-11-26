/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mendoventura.exception;

/**
 *
 * @author MyBook Hype AMD
 */
public class StokTidakCukupException extends Exception {
    private final String kodeAlat;
    private final int diminta;
    private final int tersedia;

    public StokTidakCukupException(String kodeAlat, int diminta, int tersedia) {
        super("Stok alat " + kodeAlat + " tidak cukup: diminta " + diminta + ", tersedia " + tersedia + ".");
        this.kodeAlat = kodeAlat;
        this.diminta = diminta;
        this.tersedia = tersedia;
    }

    public String getKodeAlat() {
        return kodeAlat;
    }

    public int getDiminta() {
        return diminta;
    }

    public int getTersedia() {
        return tersedia;
    }
}