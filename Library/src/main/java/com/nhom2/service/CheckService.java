/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.service;

import com.nhom2.library.Utils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author CamHa
 */
public class CheckService {

    public boolean checkReservationCard() throws SQLException {
        List<Integer> bookcopiesIDList = new ArrayList<>();
        List<Integer> bookIDList = new ArrayList<>();
        List<String> reservationCardIDList = new ArrayList<>();
        String sql;
        try (Connection conn = Utils.getConn()) {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("select phieudat.idphieudat,phieudat.id_sach, idDauSach\n"
                    + "from ktpm_btl.phieudat, ktpm_btl.sach_copies\n"
                    + "where hour(timediff(now(), phieudat.ngaydat)) > 48 and phieudat.TinhTrang = -1\n"
                    + "	and phieudat.id_sach = idsach_copies;");
            while (rs.next()) {
                bookcopiesIDList.add(rs.getInt("id_sach"));
                bookIDList.add(rs.getInt("idDauSach"));
                reservationCardIDList.add(rs.getString("phieudat.idphieudat"));
            }
            conn.setAutoCommit(false);

            if (!reservationCardIDList.isEmpty()) {
                for (int i = 0; i < reservationCardIDList.size(); i++) {
                    sql = "update phieudat set TinhTrang = 0 where hour(timediff(now(), phieudat.ngaydat)) > 48;";
                    stm.executeUpdate(sql);
                }

            }

            if (!bookIDList.isEmpty() && !bookcopiesIDList.isEmpty()) {

                sql = "update sach_copies set TinhTrang = 0 where idsach_copies = ? ";
                PreparedStatement stm1 = conn.prepareCall(sql);
                for (int i = 0; i < bookcopiesIDList.size(); i++) {
                    stm1.setInt(1, bookcopiesIDList.get(i));
                    stm1.executeUpdate();
                }

                sql = "update sach set SoLuong = SoLuong + 1 where idSach = ? ";
                PreparedStatement stm2 = conn.prepareCall(sql);
                for (int i = 0; i < bookIDList.size(); i++) {
                    stm2.setInt(1, bookIDList.get(i));
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
            return true;
        }
    }

    public boolean checkBorrowingCard() throws SQLException {
        try (Connection conn = Utils.getConn()) {
            String sql = "update phieumuon set tinhtrang = 0 where tinhtrang = -1 and   DATEDIFF( now(),phieumuon.ngaymuon) > 30";
            Statement stm = conn.createStatement();
            int r = stm.executeUpdate(sql);
            return r > 0;
        }
    }

}
