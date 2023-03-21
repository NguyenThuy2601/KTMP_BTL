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
        List<Integer> bookIDList = new ArrayList<>();
        try (Connection conn = Utils.getConn()) {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("select phieudat.sach_idSach\n"
                    + "from ktpm_btl.phieudat \n"
                    + "where hour(timediff(now(), phieudat.ngaydat)) > 48 and TinhTrang = -1;");
            while (rs.next()) {
                bookIDList.add(rs.getInt("sach_idSach"));
            }
            conn.setAutoCommit(false);
          
            
            String sql = "update phieudat set TinhTrang = 0 where hour(timediff(now(), phieudat.ngaydat)) > 48;";
            stm.executeUpdate(sql);

            if (!bookIDList.isEmpty()) {

                sql = "update ktpm_btl.sach set tinhtrang = 0 where idSach = ? ";
                PreparedStatement stm1 = conn.prepareCall(sql);
                for (int i = 0; i < bookIDList.size(); i++) {
                    stm1.setString(1, Integer.toString(bookIDList.get(i)));
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
            return true;
        }
    }
    
    public boolean checkBorrowingCard() throws SQLException {
        try (Connection conn = Utils.getConn()) {
            String sql = "update phieumuon set tinhtrang = 0 where tinhtrang = -1 and  day(timediff(now(), phieumuon.ngaymuon)) > 30";
            Statement stm = conn.createStatement();
            int r = stm.executeUpdate(sql);
            return r > 0;
        }
    }
}
