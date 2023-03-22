/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.pojo;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

/**
 *
 * @author CamHa
 */
public class BookResponse {

    private int idSach;
    private String Ten;
    private int namXB;
    private String noiXB;
    private LocalDate ngayNhap;
    private String moTa;
    private boolean tinhTrang;
    private String TenTG;
    private String tenDM;
    private String ViTri;

    public BookResponse() {
    }

    public BookResponse(int idSach, String Ten, int namXB, String noiXB, LocalDate ngayNhap, String moTa, boolean tinhTrang, String TenTG, String tenDM, String ViTri) {
        this.idSach = idSach;
        this.Ten = Ten;
        this.namXB = namXB;
        this.noiXB = noiXB;
        this.ngayNhap = ngayNhap;
        this.moTa = moTa;
        this.tinhTrang = tinhTrang;
        this.TenTG = TenTG;
        this.tenDM = tenDM;
        this.ViTri = ViTri;
    }

    public int getIdSach() {
        return idSach;
    }

    public String getTen() {
        ByteBuffer buffer = StandardCharsets.UTF_8.encode(this.Ten);
        String utf8EncodedString = StandardCharsets.UTF_8.decode(buffer).toString();
        return utf8EncodedString;
    }

    public int getNamXB() {
        return namXB;
    }

    public boolean getTinhTrang() {
        return tinhTrang;
    }

    public String getTenTG() {
        return TenTG;
    }

    public String getTenDM() {
        return tenDM;
    }

    public String getViTri() {
        return ViTri;
    }

}
