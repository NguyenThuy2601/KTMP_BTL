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
            return 5000 * day;
        }
        return 0;
    }

    public boolean confirmReturningBook(PhieuMuon p) throws SQLException {
        try (Connection conn = Utils.getConn()) {
            conn.setAutoCommit(false);
            String sql = "update phieumuon set tinhtrang = 1 where idphieumuon = ?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, p.getIdPhieuMuon());
            int r = stm.executeUpdate();
            if (r > 0) {
                sql = "update sach_copies set TinhTrang = 0 where idsach_copies = ?";
                PreparedStatement stm1 = conn.prepareCall(sql);
                stm1.setInt(1, p.getIdSach());
                stm1.executeUpdate();

                sql = "update sach set SoLuong = SoLuong + 1 where idSach = (select idDauSach from sach_copies where idsach_copies = ?)";
                PreparedStatement stm2 = conn.prepareCall(sql);
                stm2.setInt(1, p.getIdSach());
                stm2.executeUpdate();
            }
            try {
                conn.commit();
                return true;
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                return false;
            }
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

}
