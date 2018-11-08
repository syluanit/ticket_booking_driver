package com.example.syluanit.myapplication.Model;

public class GheNgoi {
    private String id;
    private int hinhAnh;
    private int Cua;
    private String viTri;
    private int trangThai;

    public GheNgoi(int hinhAnh, String viTri) {
        this.hinhAnh = hinhAnh;
        this.viTri = viTri;
    }

    public GheNgoi(String id, int hinhAnh, int cua, String viTri, int trangThai) {
        this.id = id;
        this.hinhAnh = hinhAnh;
        Cua = cua;
        this.viTri = viTri;
        this.trangThai = trangThai;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCua() {
        return Cua;
    }

    public void setCua(int cua) {
        Cua = cua;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public int getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(int hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public String getViTri() {
        return viTri;
    }

    public void setViTri(String viTri) {
        this.viTri = viTri;
    }
}
