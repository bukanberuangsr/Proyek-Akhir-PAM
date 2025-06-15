package com.example.proyekakhirpam;

import java.util.Date;

public class DonationItem {
    private String id;
    private String judul;
    private String deskripsiDonasi;
    private String namaDonatur;
    private String nominalDonasi;
    private Date tanggalSelesai;
    private String gambar_url;
    private String jenisDonasi;

    public DonationItem(String id, String gambar, String judul, String deskripsi, String namaDonatur, String nominalDonasi, Date tanggalSelesai) {
        this.id = id;
        this.gambar_url = gambar;
        this.judul = judul;
        this.namaDonatur = namaDonatur;
        this.nominalDonasi = nominalDonasi;
        this.deskripsiDonasi = deskripsi;
        this.tanggalSelesai = tanggalSelesai;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJenisDonasi() {
        return jenisDonasi;
    }

    public void setJenisDonasi(String jenisDonasi) {
        this.jenisDonasi = jenisDonasi;
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

    public String getGambar_url() {
        return gambar_url;
    }

    public void setGambar_url(String gambar_url) {
        this.gambar_url = gambar_url;
    }

    public Date getTanggalSelesai() {
        return tanggalSelesai;
    }
    public void setTanggalSelesai(Date tanggalSelesai) {
        this.tanggalSelesai = tanggalSelesai;
    }
}
