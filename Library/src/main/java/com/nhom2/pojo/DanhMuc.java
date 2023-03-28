/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.pojo;

/**
 *
 * @author CamHa
 */
public class DanhMuc {
    private int idDanhMuc;
    private String Ten;

    public DanhMuc() {
    }

    public DanhMuc(int idDanhMuc, String Ten) {
        this.idDanhMuc = idDanhMuc;
        this.Ten = Ten;
    }

    public String getTen() {
        return Ten;
    }
    
    public String toString(){
        return this.Ten;
    }

    public int getIdDanhMuc() {
        return idDanhMuc;
    }
    
}
