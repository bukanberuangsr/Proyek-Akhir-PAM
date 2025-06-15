package com.example.proyekakhirpam;

public class Postingan {
    private String nama;
    private String deskripsi;
    private String imageUrl;
    private String postinganId;

    public Postingan() {
        // Diperlukan oleh Firestore
    }

    public Postingan(String nama, String deskripsi, String imageUrl, String postinganId) {
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.imageUrl = imageUrl;
        this.postinganId = postinganId;
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

    public String getPostinganId() {
        return postinganId;
    }

    public void setPostinganId(String postinganId) {
        this.postinganId = postinganId;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
