package mendoventura;

import mendoventura.denda.DendaTerlambat;
import mendoventura.model.*;
import mendoventura.service.Ketersediaan;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;

class MendoventuraTest {

    // TEST DENDA
    @Test
    void dendaTerlambatBerhasil() {
        DendaTerlambat denda = new DendaTerlambat(5000);
        assertEquals(15000, denda.hitung(3));
    }

    @Test
    void dendaTanpaKeterlambatan() {
        DendaTerlambat denda = new DendaTerlambat(5000);
        assertEquals(0, denda.hitung(0));
    }

    @Test
    void dendaHariNegatifDitolak() {
        DendaTerlambat denda = new DendaTerlambat(5000);
        assertThrows(IllegalArgumentException.class, () -> denda.hitung(-1));
    }

    // TEST KETERSEDIAAN
    @Test
    void alatTersediaJikaStokCukup() {
        ArrayList<Sewa> daftarSewa = new ArrayList<>();
        Ketersediaan cek = new Ketersediaan(daftarSewa);
        Tenda tenda = new Tenda("T001", "Tenda Dome", 50000, 3, 4);
        LocalDate mulai = LocalDate.of(2026, 12, 1);
        LocalDate selesai = LocalDate.of(2026, 12, 3);
        assertTrue(cek.tersedia(tenda, 2, mulai, selesai));
    }

    @Test
    void alatTidakTersediaJikaJumlahMelebihiStok() {
        ArrayList<Sewa> daftarSewa = new ArrayList<>();
        Ketersediaan cek = new Ketersediaan(daftarSewa);
        Tenda tenda = new Tenda("T001", "Tenda Dome", 50000, 2, 4);
        LocalDate mulai = LocalDate.of(2026, 12, 1);
        LocalDate selesai = LocalDate.of(2026, 12, 3);
        assertFalse(cek.tersedia(tenda, 3, mulai, selesai));
    }

    @Test
    void jumlahSewaTidakValidDitolak() {
        ArrayList<Sewa> daftarSewa = new ArrayList<>();
        Ketersediaan cek = new Ketersediaan(daftarSewa);
        Tenda tenda = new Tenda("T001", "Tenda Dome", 50000, 3, 4);
        LocalDate mulai = LocalDate.of(2026, 12, 1);
        LocalDate selesai = LocalDate.of(2026, 12, 3);
        assertFalse(cek.tersedia(tenda, 0, mulai, selesai));
    }

    @Test
    void tanggalSewaTidakValidDitolak() {
        ArrayList<Sewa> daftarSewa = new ArrayList<>();
        Ketersediaan cek = new Ketersediaan(daftarSewa);
        Tenda tenda = new Tenda("T001", "Tenda Dome", 50000, 3, 4);
        LocalDate mulai = LocalDate.of(2026, 12, 3);
        LocalDate selesai = LocalDate.of(2026, 12, 1);
        assertFalse(cek.tersedia(tenda, 1, mulai, selesai));
    }
}

