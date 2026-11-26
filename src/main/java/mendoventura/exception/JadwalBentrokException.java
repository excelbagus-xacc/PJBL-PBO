/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mendoventura.exception;

/**
 *
 * @author MyBook Hype AMD
 */
public class JadwalBentrokException extends Exception {
    private final String kodeAlat;

    public JadwalBentrokException(String kodeAlat, String pesan) {
        super(pesan);
        this.kodeAlat = kodeAlat;
    }

    public String getKodeAlat() {
        return kodeAlat;
    }
}
