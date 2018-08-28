package com.example.syluanit.myapplication.Model;

public class GheNgoi {
    private int hinhAnh;
    private String viTri;
    private int trangThai;

    public GheNgoi(int hinhAnh, String viTri) {
        this.hinhAnh = hinhAnh;
        this.viTri = viTri;
    }

    public GheNgoi(int hinhAnh, String viTri, int trangThai) {
        this.hinhAnh = hinhAnh;
        this.viTri = viTri;
        this.trangThai = trangThai;
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