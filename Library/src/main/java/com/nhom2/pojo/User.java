/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.pojo;
import java.time.LocalDate;
/**
 *
 * @author CamHa
 */
public class User {
    private int uID;
    private String accID;
    private String ten;
    private String hoLot;
    private String doiTuong;
    private LocalDate ngayBD;
    private LocalDate ngayHetHan;
    private String Email;
    private String DiaChi;
    private String SDT;
    private String boPhan_id;
    private LocalDate DOB;

    
    public User() {
        
    }

    public User(int uID, String accID, String ten, String hoLot, String doiTuong, LocalDate ngayBD, LocalDate ngayHetHan, String Email, String DiaChi, String SDT, String boPhan_id, LocalDate dob) {
        this.uID = uID;
        this.accID = accID;
        this.ten = ten;
        this.hoLot = hoLot;
        this.doiTuong = doiTuong;
        this.ngayBD = ngayBD;
        this.ngayHetHan = ngayHetHan;
        this.Email = Email;
        this.DiaChi = DiaChi;
        this.SDT = SDT;
        this.boPhan_id = boPhan_id;
        this.DOB = dob;
    }
    
    public String getTen() {
        return ten;
    }

    public String getHoLot() {
        return hoLot;
    }

    public String getDoiTuong() {
        return doiTuong;
    }

    public LocalDate getNgayBD() {
        return ngayBD;
    }

     public LocalDate getDOB() {
        return DOB;
    }
    
    public LocalDate getNgayHetHan() {
        return ngayHetHan;
    }

    public String getEmail() {
        return Email;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public String getSDT() {
        return SDT;
    }

    public String getBoPhan_id() {
        return boPhan_id;
    }

    public int getuID() {
        return uID;
    }

    
}
