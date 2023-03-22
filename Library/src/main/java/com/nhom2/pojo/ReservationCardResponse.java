/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.pojo;

import java.time.LocalDateTime;

/**
 *
 * @author CamHa
 */
public class ReservationCardResponse {
    private String  idPhieuDat;
    private int idSach;
    private String tenSach;
    private String TinhTrang;
    private LocalDateTime ngayDat;
    private String HoLotTen;

    public ReservationCardResponse() {
    }

    public ReservationCardResponse(String idPhieuDat, int idSach, String tenSach, String TinhTrang, LocalDateTime ngayDat, String HoLotTen) {
        this.idPhieuDat = idPhieuDat;
        this.idSach = idSach;
        this.tenSach = tenSach;
        this.TinhTrang = TinhTrang;
        this.ngayDat = ngayDat;
        this.HoLotTen = HoLotTen;
    }

    

    public String getIdPhieuDat() {
        return idPhieuDat;
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

    public LocalDateTime getNgayDat() {
        return ngayDat;
    }

    public String getHoLotTen() {
        return HoLotTen;
    }

  
    
    
}
