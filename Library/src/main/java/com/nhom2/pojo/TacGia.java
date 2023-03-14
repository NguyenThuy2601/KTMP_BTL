/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.pojo;

/**
 *
 * @author CamHa
 */
public class TacGia {
    private int idTacGia;
    private String HoLot;
    private String Ten;

    public TacGia() {
    }

    public TacGia(int idTacGia, String HoLot, String Ten) {
        this.idTacGia = idTacGia;
        this.HoLot = HoLot;
        this.Ten = Ten;
    }

    

    public String getTen() {
        return Ten;
    }

    
    public String toString() {
        return (this.HoLot + this.Ten);
    }

    public String getHoLot() {
        return HoLot;
    }
}
