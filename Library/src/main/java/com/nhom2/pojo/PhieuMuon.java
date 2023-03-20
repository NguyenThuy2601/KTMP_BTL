/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.pojo;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 *
 * @author CamHa
 */
public class PhieuMuon {
    private String  idPhieuMuon;
    private int idSach;
    private int TinhTrang;
    private LocalDateTime ngayMuon;
    private int idDocGia;

    {
        String temp [] = UUID.randomUUID().toString().split("-");
        idPhieuMuon = temp[2] + "-" + temp[3];
    }
    
    public PhieuMuon() {
    }

    public PhieuMuon(String idPhieuMuon, int idSach, int TinhTrang, LocalDateTime ngayMuon, int idDocGia) {
        this.idPhieuMuon = idPhieuMuon;
        this.idSach = idSach;
        this.TinhTrang = TinhTrang;
        this.ngayMuon = ngayMuon;
        this.idDocGia = idDocGia;
    }
    
    public PhieuMuon(LocalDateTime ngayMuon, int idDocGia) {
        this.ngayMuon = ngayMuon;
        this.idDocGia = idDocGia;
        this.TinhTrang = -1;
    }

    public String getIdPhieuMuon() {
        return idPhieuMuon;
    }

    public int getIdSach() {
        return idSach;
    }

    public int getTinhTrang() {
        return TinhTrang;
    }

    public LocalDateTime getNgayMuon() {
        return ngayMuon;
    }

    public int getIdDocGia() {
        return idDocGia;
    }
    
    
}
