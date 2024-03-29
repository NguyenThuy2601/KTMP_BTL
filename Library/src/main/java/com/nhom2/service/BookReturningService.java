/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.service;

import com.nhom2.library.Utils;
import com.nhom2.pojo.BorrowCardResponse;
import com.nhom2.pojo.PhieuMuon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import static java.time.temporal.ChronoUnit.DAYS;

/**
 *
 * @author CamHa
 */
public class BookReturningService {

    public int calcFee(int day) {
        if (day > 30) {
            return 5000 * (day - 30);
        }
        return 0;
    }

    public boolean confirmReturningBook(String idPhieuMuon, int idSach) throws SQLException {
        try (Connection conn = Utils.getConn()) {
            String sql = "update phieumuon set tinhtrang = 1, ngaytra = now() where idphieumuon = ?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, idPhieuMuon);
            int r = stm.executeUpdate();
            return r > 0;
        }
    }

    public int calcDayGap(LocalDate borrowingDate) {
        return (int)DAYS.between(borrowingDate, LocalDate.now());
    }

    public BorrowCardResponse getBorrowingCardInfo(String idPhieuMuon) throws SQLException {
        try (Connection conn = Utils.getConn()) {
            BorrowCardResponse p = new BorrowCardResponse();
            String sql = "select phieumuon.*, concat(docgia.HoLot, \" \", docgia.Ten) as Name , sach.Ten\n"
                    + "from phieumuon, docgia, sach, sach_copies\n"
                    + "where phieumuon.docgia_id = docgia.id"
                    + " and phieumuon.sach_idSach1 = sach_copies.idsach_copies "
                    + "and sach.idSach = sach_copies.idDauSach and idphieumuon = ?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, idPhieuMuon);
            // + Truy van lay du lieu: select
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                p = new BorrowCardResponse(rs.getString("idphieumuon"),
                        rs.getInt("sach_idSach1"),
                        rs.getNString("Ten"), 
                        rs.getInt("tinhtrang"),
                        rs.getDate("ngaymuon").toLocalDate(),
                        rs.getNString("Name"),
                        rs.getInt("docgia_id"));
            }
            return p;
        }
    }
    
    public int getFineTotalFromLabel(String text){
        String temp [] = text.split(" ");
        return Integer.parseInt(temp[0]);
    }
    
     public String calcTotalFine(String text1,String text2){
        int a = getFineTotalFromLabel(text1);
        int b = getFineTotalFromLabel(text2);
        return Integer.toString((a+b));
    }

}
