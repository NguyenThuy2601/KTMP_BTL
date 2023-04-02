/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.service;

import com.nhom2.library.Utils;
import com.nhom2.pojo.PhieuDat;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author CamHa
 */
public class CheckService {

    public boolean checkReservationnCardEXP(LocalDateTime fromDate) {
        if (Duration.between(fromDate, LocalDateTime.now()).toHours() > 48) {
            return false;
        }
        return true;
    }

    public boolean checkReservationCard() throws SQLException {
        List<PhieuDat> reservationCardIDList = new ArrayList<>();
        String sql;
        try (Connection conn = Utils.getConn()) {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("select phieudat.*\n"
                    + "from ktpm_btl.phieudat\n"
                    + "where phieudat.TinhTrang = -1");
            while (rs.next()) {
                PhieuDat p = new PhieuDat(rs.getString("idphieudat"),
                        rs.getInt("id_sach"),
                        rs.getInt("TinhTrang"),
                        rs.getTimestamp("ngaydat").toLocalDateTime(),
                         rs.getInt("docgia_id"));
                reservationCardIDList.add(p);
            }
            conn.setAutoCommit(false);

            if (!reservationCardIDList.isEmpty()) {
                sql = "update phieudat set TinhTrang = 0 where phieudat.idphieudat = ?";
                PreparedStatement stm4 = conn.prepareCall(sql);
                for (int i = 0; i < reservationCardIDList.size(); i++) {
                    if (checkReservationnCardEXP(reservationCardIDList.get(i).getNgayDat()) == false) {
                        stm4.setString(1, reservationCardIDList.get(i).getIdPhieuDat());
                        stm4.executeUpdate();
                    }
                }
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

    public boolean checkBorrowingCard() throws SQLException {
        try (Connection conn = Utils.getConn()) {
            conn.setAutoCommit(false);
            String sql = "update phieumuon set tinhtrang = 0 where tinhtrang = -1 and   DATEDIFF( now(),phieumuon.ngaymuon) > 30";
            Statement stm = conn.createStatement();
            int r = stm.executeUpdate(sql);
            try {
                conn.commit();
                return true;
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                return false;
            }
        }
    }
    
    public boolean updateBorrowingCardWithID(String cardID) throws SQLException {
        try (Connection conn = Utils.getConn()) {
            conn.setAutoCommit(false);
            String sql = "update phieumuon set tinhtrang = 0 where idphieumuon = ?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, cardID);
            int r = stm.executeUpdate();
            return r > 0;
             
        }
    }
}
