package com.example.proyekakhirpam;

public class Post {
    private String nama;
    private String deskripsi;
    private int imageRes;
    private int reactSmile;
    private int reactHaru;
    private int reactHug;

    public Post(String nama, String deskripsi, int imageRes, int reactSmile, int reactHaru, int reactHug) {
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.imageRes = imageRes;
        this.reactSmile = reactSmile;
        this.reactHaru = reactHaru;
        this.reactHug = reactHug;
    }

    public String getNama() {
        return nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public int getImageRes() {
        return imageRes;
    }

    public int getReactSmile() {
        return reactSmile;
    }

    public int getReactHaru() {
        return reactHaru;
    }

    public int getReactHug() {
        return reactHug;
    }
}
