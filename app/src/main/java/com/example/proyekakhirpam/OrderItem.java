package com.example.proyekakhirpam;

public class OrderItem {
    String doc_id;   // simpan id dokumen Firestore jika perlu pembatalan
    String makanan_id;
    String nama_makanan;
    String nama_donor;
    String image_url;
    int jumlah;
    int total_harga;

    public OrderItem() {
    }

    //Constructor
    public OrderItem(String makanan_id, String nama_makanan, String nama_donor, String image_url, int jumlah, int total_harga) {
        this.makanan_id = makanan_id;
        this.nama_makanan = nama_makanan;
        this.nama_donor = nama_donor;
        this.image_url = image_url;
        this.jumlah = jumlah;
        this.total_harga = total_harga;
    }

    public String getNamaMakanan() {
        return nama_makanan;
    }

    public String getNamaRestoran() {
        return nama_donor;
    }

    public int getJumlah() {
        return jumlah;
    }

    public int getTotalHarga() {
        return total_harga;
    }

    public String getMakananId() {
        return makanan_id;
    }

    public String getImageURL() {
        return image_url;
    }

    public String getDocumentId() {
        return doc_id;
    }
}