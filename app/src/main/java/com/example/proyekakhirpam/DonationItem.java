package com.example.proyekakhirpam;

import java.util.Date;

public class DonationItem {
    private String judul;
    private String deskripsiDonasi;
    private String namaDonatur;
    private String nominalDonasi;
    private Date tanggalSelesai;
    private int gambar;
    private String jenisDonasi;

    public DonationItem(int gambar, String judul, String deskripsi, String namaDonatur, String nominalDonasi, Date tanggalSelesai) {
        this.gambar = gambar;
        this.judul = judul;
        this.namaDonatur = namaDonatur;
        this.nominalDonasi = nominalDonasi;
        this.deskripsiDonasi = deskripsi;
        this.tanggalSelesai = tanggalSelesai;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDeskripsiDonasi() {
        return deskripsiDonasi;
    }

    public void setDeskripsiDonasi(String deskripsi) {
        this.deskripsiDonasi = deskripsi;
    }

    public String getNamaDonatur() {
        return namaDonatur;
    }

    public void setNamaDonatur(String namaDonatur) {
        this.namaDonatur = namaDonatur;
    }

    public String getNominalDonasi() {
        return nominalDonasi;
    }

    public void setNominalDonasi(String nominalDonasi) {
        this.nominalDonasi = nominalDonasi;
    }

    public int getGambar() {
        return gambar;
    }

    public void setGambar(int gambar) {
        this.gambar = gambar;
    }

    public Date getTanggalSelesai() {
        return tanggalSelesai;
    }
    public void setTanggalSelesai(Date tanggalSelesai) {
        this.tanggalSelesai = tanggalSelesai;
    }
}
