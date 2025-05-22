package com.example.proyekakhirpam;

public class OrderItem {
    String namaMakanan;
    String namaRestoran;
    int jumlah;
    String tanggal;
    int totalHarga;
    int image;

    public OrderItem(String namaMakanan, String namaRestoran, int jumlah, String tanggal, int totalHarga, int image) {
        this.namaMakanan = namaMakanan;
        this.namaRestoran = namaRestoran;
        this.jumlah = jumlah;
        this.tanggal = tanggal;
        this.totalHarga = totalHarga;
        this.image = image;
    }

    // Getter (atau bisa pakai public variable kalau mau simple)
    public String getNamaMakanan() {
        return namaMakanan; }
    public String getNamaRestoran() {
        return namaRestoran; }
    public int getJumlah() {
        return jumlah; }
    public String getTanggal() {
        return tanggal; }
    public int getTotalHarga() {
        return totalHarga; }
    public int getImageId() {
        return image;
    }
}
