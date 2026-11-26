package mendoventura.service;

import mendoventura.model.ItemSewa;
import mendoventura.model.Peralatan;
import mendoventura.model.Sewa;
import mendoventura.model.StatusSewa;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class Ketersediaan {

    private final ArrayList<Sewa> daftarSewa;

    public Ketersediaan(ArrayList<Sewa> daftarSewa) {
        this.daftarSewa = (daftarSewa != null)
                ? daftarSewa
                : new ArrayList<>();
    }

    public int hitungStokTersedia(
            Peralatan alat,
            LocalDate mulai,
            LocalDate selesai
    ) {
        if (alat == null || mulai == null || selesai == null
                || !selesai.isAfter(mulai)) {
            throw new IllegalArgumentException(
                    "Data alat atau tanggal tidak valid."
            );
        }

        int terpakai = 0;

        for (Sewa sewa : daftarSewa) {

            if (sewa == null) {
                continue;
            }

            StatusSewa status = sewa.getStatus();

            boolean aktif = status != StatusSewa.DITOLAK
                    && status != StatusSewa.DIBATALKAN
                    && status != StatusSewa.SELESAI;

            if (aktif && sewa.tanggalBentrok(mulai, selesai)) {

                for (ItemSewa item : sewa.getDaftarItem()) {

                    // Membandingkan peralatan berdasarkan kode alat
                    if (Objects.equals(item.getPeralatan(), alat)) {
                        terpakai += item.getJumlah();
                    }
                }
            }
        }

        return alat.getStok() - terpakai;
    }

    public boolean tersedia(
            Peralatan alat,
            int jumlah,
            LocalDate mulai,
            LocalDate selesai
    ) {
        if (alat == null || mulai == null || selesai == null) {
            return false;
        }

        if (!alat.isAktif() || jumlah <= 0
                || !selesai.isAfter(mulai)) {
            return false;
        }

        return hitungStokTersedia(alat, mulai, selesai) >= jumlah;
    }
}
