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
public class PhieuDat {

    private String  idPhieuDat ;
    private int idSach;
    private LocalDateTime ngayDat;
    private int idDocGia;
    
    {
        String temp [] = UUID.randomUUID().toString().split("-");
        idPhieuDat = temp[2] + "-" + temp[3];
    }

    public PhieuDat() {
    }

    public PhieuDat(int idSach, LocalDateTime ngayDat, int idDocGia) {
        this.idSach = idSach;
        this.ngayDat = ngayDat;
        this.idDocGia = idDocGia;
    }
    
    public PhieuDat(LocalDateTime ngayDat, int idDocGia) {
        this.ngayDat = ngayDat;
        this.idDocGia = idDocGia;
    }

    public String getIdPhieuDat() {
        return idPhieuDat;
    }

    public int getIdSach() {
        return idSach;
    }

    public LocalDateTime getNgayDat() {
        return ngayDat;
    }

    public int getIdDocGia() {
        return idDocGia;
    }
    
    
    

}
