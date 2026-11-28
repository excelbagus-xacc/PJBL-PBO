
package mendoventura;

import mendoventura.denda.DendaTerlambat;
import mendoventura.exception.JadwalBentrokException;
import mendoventura.exception.StokTidakCukupException;
import mendoventura.model.*;
import mendoventura.service.Ketersediaan;
import mendoventura.service.SewaService;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    // TEST REGRESSION

    @Test
    void tanggalKembaliSebelumTanggalSewaDitolak() {
        Pelanggan pelanggan = new Pelanggan(
                "P001", "Aufa", "password123", "08123456789"
        );

        LocalDate mulai = LocalDate.of(2026, 12, 5);
        LocalDate kembali = LocalDate.of(2026, 12, 3);

        assertThrows(IllegalArgumentException.class, () ->
                new Sewa("S001", pelanggan, mulai, kembali)
        );
    }

    @Test
    void biayaPerbaikanNegatifDitolak() throws Exception {
        SewaService service = new SewaService(new ArrayList<>());

        Tenda tenda = new Tenda("T002", "Tenda Camping", 50000, 3, 4);

        Sewa sewa = buatSewaAktif(
                service, tenda, 1,
                LocalDate.of(2026, 12, 1),
                LocalDate.of(2026, 12, 3)
        );

        ItemSewa item = sewa.getDaftarItem().get(0);

        Map<ItemSewa, Kondisi> kondisi = new HashMap<>();
        kondisi.put(item, Kondisi.RUSAK);

        Map<ItemSewa, Double> biaya = new HashMap<>();
        biaya.put(item, -10000.0);

        assertThrows(IllegalArgumentException.class, () ->
                service.prosesPengembalian(
                        sewa,
                        LocalDate.of(2026, 12, 3),
                        kondisi,
                        biaya
                )
        );
    }

    @Test
    void stokBerkurangKetikaAlatHilang() throws Exception {
        SewaService service = new SewaService(new ArrayList<>());

        Tenda tenda = new Tenda("T003", "Tenda Camping", 50000, 3, 4);

        Sewa sewa = buatSewaAktif(
                service, tenda, 2,
                LocalDate.of(2026, 12, 1),
                LocalDate.of(2026, 12, 3)
        );

        ItemSewa item = sewa.getDaftarItem().get(0);

        Map<ItemSewa, Kondisi> kondisi = new HashMap<>();
        kondisi.put(item, Kondisi.HILANG);

        double stokAwal = tenda.getStok();

        service.prosesPengembalian(
                sewa,
                LocalDate.of(2026, 12, 3),
                kondisi,
                new HashMap<>()
        );

        assertEquals(stokAwal - 2, tenda.getStok());
    }

    // TEST EXCEPTION

    @Test
    void stokTidakCukupException() {
        SewaService service = new SewaService(new ArrayList<>());

        Pelanggan pelanggan = new Pelanggan(
                "P004", "Doni", "password123", "08123456789"
        );

        Tenda tenda = new Tenda("T004", "Tenda Camping", 50000, 1, 4);

        Map<Peralatan, Integer> keranjang = new HashMap<>();
        keranjang.put(tenda, 2);

        assertThrows(StokTidakCukupException.class, () ->
                service.buatReservasi(
                        pelanggan,
                        LocalDate.of(2026, 12, 1),
                        LocalDate.of(2026, 12, 3),
                        keranjang,
                        jumlah -> true,
                        "bukti.jpg",
                        "KTP.jpg"
                )
        );
    }

    @Test
    void jadwalBentrokException() throws Exception {
        SewaService service = new SewaService(new ArrayList<>());

        Pelanggan pelanggan = new Pelanggan(
                "P005", "Ema", "password123", "08123456789"
        );

        Tenda tenda = new Tenda("T005", "Tenda Camping", 50000, 1, 4);

        LocalDate mulai = LocalDate.of(2026, 12, 1);
        LocalDate selesai = LocalDate.of(2026, 12, 3);

        // Reservasi pertama
        buatReservasi(service, pelanggan, tenda, 1, mulai, selesai);

        // Reservasi kedua dengan alat dan tanggal yang sama
        Map<Peralatan, Integer> keranjang = new HashMap<>();
        keranjang.put(tenda, 1);

        assertThrows(JadwalBentrokException.class, () ->
                service.buatReservasi(
                        pelanggan,
                        mulai,
                        selesai,
                        keranjang,
                        jumlah -> true,
                        "bukti2.jpg",
                        "KTP.jpg"
                )
        );
    }

    // HELPER MEMBUAT RESERVASI

    private Sewa buatReservasi(
            SewaService service,
            Pelanggan pelanggan,
            Peralatan alat,
            int jumlah,
            LocalDate mulai,
            LocalDate selesai
    ) throws Exception {

        Map<Peralatan, Integer> keranjang = new HashMap<>();
        keranjang.put(alat, jumlah);

        return service.buatReservasi(
                pelanggan,
                mulai,
                selesai,
                keranjang,
                jumlahBayar -> true,
                "bukti.jpg",
                "KTP.jpg"
        );
    }

    // HELPER MENYIAPKAN SEWA YANG SUDAH AKTIF

    private Sewa buatSewaAktif(
            SewaService service,
            Peralatan alat,
            int jumlah,
            LocalDate mulai,
            LocalDate selesai
    ) throws Exception {

        Pelanggan pelanggan = new Pelanggan(
                "P010", "Juns", "password123", "08123456789"
        );

        Sewa sewa = buatReservasi(
                service, pelanggan, alat, jumlah, mulai, selesai
        );

        Admin admin = new Admin("A001", "Admin", "admin123");

        service.verifikasiReservasi(sewa, admin, true);
        service.serahkanAlat(sewa);

        return sewa;
    }
}
