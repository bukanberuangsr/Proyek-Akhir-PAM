package com.example.proyekakhirpam;

import com.google.firebase.Timestamp;

public class FoodItem {
    private String id;
    private String nama_makanan;
    private String donor_nama;
    private int jumlah;
    private Timestamp tanggal_expired;
    private int harga;
    private String gambar_url;
    private boolean isAvailable;
    private String donor_id;

    public FoodItem() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama_makanan() {
        return nama_makanan;
    }

    public void setNama_makanan(String nama_makanan) {
        this.nama_makanan = nama_makanan;
    }

    public String getDonor_nama() {
        return donor_nama;
    }

    public void setDonor_nama(String donor_nama) {
        this.donor_nama = donor_nama;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public Timestamp getTanggal_expired() {
        return tanggal_expired;
    }

    public void setTanggal_expired(Timestamp tanggal_expired) {
        this.tanggal_expired = tanggal_expired;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public String getGambar_url() {
        return gambar_url;
    }

    public void setGambar_url(String gambar_url) {
        this.gambar_url = gambar_url;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getDonor_id() {
        return donor_id;
    }

    public void setDonor_id(String donor_id) {
        this.donor_id = donor_id;
    }
}