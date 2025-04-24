package com.example.proyekakhirpam;

public class Post {
    private String nama;
    private String deskripsi;
    private int imageRes;
    private int smile;
    private int haru;
    private int hug;

    public Post(String nama, String deskripsi, int imageRes, int smile, int haru, int hug) {
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.imageRes = imageRes;
        this.smile = smile;
        this.haru = haru;
        this.hug = hug;
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
        return smile;
    }

    public int getReactHaru() {
        return haru;
    }

    public int getReactHug() {
        return hug;
    }
}
