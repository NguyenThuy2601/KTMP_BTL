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
    private LocalDate ngayMuon;
    private String HoLotTen;

    public BorrowCardResponse() {
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

    public String getNgayMuon() {
         DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedNgayMuon = ngayMuon.format(dateTimeFormatter);  //
        return formattedNgayMuon;
    }

    public String getHoLotTen() {
        return HoLotTen;
    }
    
    
}
