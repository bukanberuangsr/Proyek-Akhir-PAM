package com.example.proyekakhirpam;

public class Teman {
    private String nama;
    private String deskripsi;
    private int gambar;

    public Teman(String nama, String deskripsi, int gambar) {
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.gambar = gambar;
    }

    public String getNama() {
        return nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public int getGambar() {
        return gambar;
    }
}
