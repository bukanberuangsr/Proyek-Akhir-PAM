package com.example.proyekakhirpam;

public class UserModel {
    private String nama;
    private String tanggalLahir;
    private String bio;

    public UserModel() {
        // Diperlukan oleh Firestore
    }

    public UserModel(String nama, String tanggalLahir, String bio) {
        this.nama = nama;
        this.tanggalLahir = tanggalLahir;
        this.bio = bio;
    }

    public String getNama() {
        return nama;
    }

    public String getTanggalLahir() {
        return tanggalLahir;
    }

    public String getBio() {
        return bio;
    }
}
