package com.example.proyekakhirpam;

public class Postingan {
    private String nama;
    private String deskripsi;
    private String imageUrl;

    public Postingan() {
        // Diperlukan oleh Firestore
    }

    public Postingan(String nama, String deskripsi, String imageUrl, int smile, int haru, int hug) {
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.imageUrl = imageUrl;
    }

    public String getNama() {
        return nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
