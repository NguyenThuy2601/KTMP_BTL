/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.pojo;

import java.time.LocalDate;

/**
 *
 * @author CamHa
 */
public class Sach {
    private int idSach;
    private String Ten;
    private int namXB;
    private String noiXB;
    private LocalDate ngayNhap;
    private String moTa;
    private int idDanhMuc;
    private int idViTri;

    public Sach() {
    }

    public Sach(int idSach, String Ten, int namXB, String noiXB, LocalDate ngayNhap, String moTa, int idDanhMuc, int idViTri) {
        this.idSach = idSach;
        this.Ten = Ten;
        this.namXB = namXB;
        this.noiXB = noiXB;
        this.ngayNhap = ngayNhap;
        this.moTa = moTa;
        this.idDanhMuc = idDanhMuc;
        this.idViTri = idViTri;
    }

    public int getIdSach() {
        return idSach;
    }

    public String getTen() {
        return Ten;
    }

    public int getNamXB() {
        return namXB;
    }

    public String getNoiXB() {
        return noiXB;
    }

    public LocalDate getNgayNhap() {
        return ngayNhap;
    }

    public String getMoTa() {
        return moTa;
    }

    public int getIdDanhMuc() {
        return idDanhMuc;
    }

    public int getIdViTri() {
        return idViTri;
    }

}
