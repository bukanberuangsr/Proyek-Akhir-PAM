package com.example.proyekakhirpam;

public class FoodItem {
    String namaMakanan;
    String namaRestoran;
    int jumlah;
    String tanggal;
    int harga;
    int image;

    public FoodItem(String namaMakanan, String namaRestoran, int jumlah, String tanggal, int harga, int image) {
        this.namaMakanan = namaMakanan;
        this.namaRestoran = namaRestoran;
        this.jumlah = jumlah;
        this.tanggal = tanggal;
        this.harga = harga;
        this.image = image;
    }

    public String getNamaMakanan() {
        return namaMakanan; }
    public String getNamaRestoran() {
        return namaRestoran; }
    public int getJumlah() {
        return jumlah; }
    public String getTanggal() {
        return tanggal; }
    public int getHarga() {
        return harga; }
    public int getImageId() {
        return image;
    }
}
