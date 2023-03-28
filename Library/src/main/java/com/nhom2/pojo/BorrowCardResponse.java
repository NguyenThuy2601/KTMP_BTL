/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.pojo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author CamHa
 */
public class BorrowCardResponse {
    private String  idPhieuMuon;
    private int idSach;
    private String tenSach;
    private String TinhTrang;
    private boolean TinhTrangBool;
    private LocalDate ngayMuon;
    private int idUser;
    private String HoLotTen;

    public BorrowCardResponse() {
        this.idPhieuMuon = "none";
    }

    public BorrowCardResponse(String idPhieuMuon, int idSach, String tenSach, boolean TinhTrangBool, LocalDate ngayMuon, String HoLotTen, int uID) {
        this.idPhieuMuon = idPhieuMuon;
        this.idSach = idSach;
        this.tenSach = tenSach;
        this.TinhTrangBool = TinhTrangBool;       
        this.ngayMuon = ngayMuon;
        this.HoLotTen = HoLotTen;
        idUser = uID;
    }
    
     public BorrowCardResponse(String idPhieuMuon, int idSach, String tenSach, String TinhTrang, LocalDate ngayMuon, String HoLotTen) {
        this.idPhieuMuon = idPhieuMuon;
        this.idSach = idSach;
        this.tenSach = tenSach;
        this.TinhTrang = TinhTrang;
        this.ngayMuon = ngayMuon;
        this.HoLotTen = HoLotTen;
    }

    public String getIdPhieuMuon() {
        return idPhieuMuon;
    }

    public int getIdSach() {
        return idSach;
    }

    public String getTenSach() {
        return tenSach;
    }

    public String getTinhTrang() {
        return TinhTrang;
    }

    public String getNgayMuonToString() {
         DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedNgayMuon = ngayMuon.format(dateTimeFormatter);  //
        return formattedNgayMuon;
    }

    public String getHoLotTen() {
        return HoLotTen;
    }
    
    public LocalDate getNgayMuon() {
        return ngayMuon;
    }

    public boolean isTinhTrangBool() {
        return TinhTrangBool;
    }

    public int getIdUser() {
        return idUser;
    }
    
    
    
}
