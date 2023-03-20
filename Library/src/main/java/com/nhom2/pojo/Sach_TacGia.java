/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.pojo;

import java.util.List;

/**
 *
 * @author CamHa
 */
public class Sach_TacGia {
    private int idSach;
    private List<TacGia> TacGia;

    public Sach_TacGia() {
    }

    public Sach_TacGia(int idSach) {
        this.idSach = idSach;
    }

    public int getIdSach() {
        return idSach;
    }

   public void setTacGia(TacGia tg){
       TacGia.add(tg);
   }

    public List<TacGia> getTacGia() {
        return TacGia;
    }
   
       
}
