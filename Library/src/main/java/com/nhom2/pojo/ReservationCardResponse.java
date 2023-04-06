/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.pojo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author CamHa
 */
public class ReservationCardResponse {
    private String  idPhieuDat;
    private int idSach;
    private String tenSach;
    private int TinhTrang;
    private LocalDateTime ngayDat;
    private String HoLotTen;
    private int docgiaID;

//    public ReservationCardResponse() {
//    }
    
    public ReservationCardResponse() {
        this.idPhieuDat = "none";
    }

    public ReservationCardResponse(String idPhieuDat, int idSach, String tenSach, int TinhTrang, LocalDateTime ngayDat, String HoLotTen, int uID) {
        this.idPhieuDat = idPhieuDat;
        this.idSach = idSach;
        this.tenSach = tenSach;
        this.TinhTrang = TinhTrang;
        this.ngayDat = ngayDat;
        this.HoLotTen = HoLotTen;
        docgiaID = uID;
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

    public int getTinhTrangOriginalForm() {
        return TinhTrang;
    }
    
    public String getTinhTrang() {
        switch(TinhTrang)
        {
            case 1:
                return "Đã được xác nhận mượn";
            case 0:
                return "Phiếu bị hủy";
            default:
                 return "Phiếu còn hạn";
        }
    }

    public int getDocgiaID() {
        return docgiaID;
    }

    public LocalDateTime getNgayDatOriginalForm() {
        return ngayDat;
    }

    public String getNgayDat() {
        DateTimeFormatter fmt3 = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String d = ngayDat.format(fmt3);
        return d;
    }

    public String getHoLotTen() {
        return HoLotTen;
    }

  
    
    
}
