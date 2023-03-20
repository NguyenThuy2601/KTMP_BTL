/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.service;

import com.nhom2.library.Utils;
import com.nhom2.pojo.PhieuMuon;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

    public boolean confirmReturningBook(PhieuMuon p) throws SQLException {
        try (Connection conn = Utils.getConn()) {
            conn.setAutoCommit(false);
            String sql = "update phieumuon set tinhtrang = 1 where idphieumuon = ?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, p.getIdPhieuMuon());
            int r = stm.executeUpdate();
            if (r > 0) {
                sql = "update sach set tinhtrang = 0 where idSach = ?";
                PreparedStatement stm1 = conn.prepareCall(sql);
                stm1.setInt(1, p.getIdSach());
                stm1.executeUpdate();
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

    public long calcDayGap(LocalDate borrowingDate) {
        return DAYS.between(borrowingDate, LocalDate.now());
    }

}
