/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mendoventura.model;

/**
 *
 * @author HP
 */

public abstract class Pengguna {
    private String id;
    private String nama;
    private String password;

    public Pengguna(String id, String nama, String password) {
        this.id = id;
        this.nama = nama;
        this.password = password;
    }

    public String getId() { return id; }
    public String getNama() { return nama; }

    public boolean login(String password) {
        return this.password.equals(password);
    }

    public void tampilkanInfo() {
        System.out.println("ID: " + id + " | Nama: " + nama);
    }
}
