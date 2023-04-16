/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.pojo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 *
 * @author CamHa
 */
public class PhieuMuon {
    private String  idPhieuMuon;
    private int idSach;
    private int TinhTrang;
    private LocalDate ngayMuon;
    private LocalDate ngayTra;
    private int idDocGia;

    {
        String temp [] = UUID.randomUUID().toString().split("-");
        idPhieuMuon = temp[2] + "-" + temp[3];
    }
    
    public PhieuMuon() {
        this.idPhieuMuon = "none";
    }

    public PhieuMuon(String idPhieuMuon, int idSach, int TinhTrang, LocalDate ngayMuon, int idDocGia) {
        this.idPhieuMuon = idPhieuMuon;
        this.idSach = idSach;
        this.TinhTrang = TinhTrang;
        this.ngayMuon = ngayMuon;
        this.idDocGia = idDocGia;
    }
    //consructer co ngay tra

    public PhieuMuon(int idSach, int TinhTrang, LocalDate ngayMuon, LocalDate ngayTra, int idDocGia) {
        this.idSach = idSach;
        this.TinhTrang = TinhTrang;
        this.ngayMuon = ngayMuon;
        this.ngayTra = ngayTra;
        this.idDocGia = idDocGia;
    }
    
    
    
    public PhieuMuon(LocalDate ngayMuon, int idDocGia) {
        this.ngayMuon = ngayMuon;
        this.idDocGia = idDocGia;
        this.TinhTrang = -1;
    }
    
    public PhieuMuon(int idSach, LocalDate ngayMuon, int idDocGia) {
        this.idSach = idSach;
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

    public LocalDate getNgayMuon() {
        return ngayMuon;
    }

    public String getNgayMuonToString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedngayMuon = ngayMuon.format(dateTimeFormatter);  //
        return formattedngayMuon;
    }
    
    public int getIdDocGia() {
        return idDocGia;
    }

    public LocalDate getNgayTra() {
        return ngayTra;
    }
    
    public String getNgayTraToString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedngayTra = ngayTra.format(dateTimeFormatter);  //
        return formattedngayTra;
    }
}
