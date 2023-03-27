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
    private String boPhan;
    private LocalDate DOB;
    private boolean gender;

    public User() {
        uID = 0;
    }

    public User(int uID, String accID, String ten, String hoLot, String doiTuong, LocalDate ngayBD, LocalDate ngayHetHan, String Email, String DiaChi, String SDT, String boPhan, LocalDate dob, boolean g) {
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
        this.boPhan = boPhan;
        this.DOB = dob;
        this.gender = g;
    }

    public String getTen() {
        return hoLot + " " + ten;
    }

    public String getHoLot() {
        return hoLot;
    }

    public String getDoiTuong() {
        switch (doiTuong) {
            case "GV":
                return "Giảng viên";
            case "SV":
                return "Sinh viên";
            default:
                return "Viên chức";
        }

    }

    public String getCardDateInfo() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedngayBD = ngayBD.format(dateTimeFormatter);
        String formattedngayHetHan = ngayHetHan.format(dateTimeFormatter);
        return formattedngayBD + "->" + formattedngayHetHan ;
    }

    public String getDOB() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDOB = DOB.format(dateTimeFormatter);  //
        return formattedDOB;
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

    public String getBoPhan() {
        return boPhan;
    }

    public int getuID() {
        return uID;
    }

    public String getAccID() {
        return accID;
    }

    public String GendertoString() {
        if (gender) {
            return "nam";
        }
        return "nữ";
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public void setDiaChi(String DiaChi) {
        this.DiaChi = DiaChi;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }
    
}
