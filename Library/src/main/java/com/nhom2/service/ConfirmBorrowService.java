/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.service;

import com.nhom2.library.Utils;
import com.nhom2.pojo.PhieuMuon;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author ADMIN
 */
public class ConfirmBorrowService {

    //Xác nhận cho mượn trực tiếp
    public boolean confirmBorrow() throws SQLException {
        try (Connection conn = Utils.getConn()) {
            CheckEXPService exp = new CheckEXPService();
            CheckNumBorrowBooksService num = new CheckNumBorrowBooksService();
            conn.setAutoCommit(false);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("select * from phieumuon where idphieumuon = ?");
            while (rs.next()) {
                //String id = rs.getString("idphieumuon");
                int idDG = rs.getInt("docgia_id");
                if (exp.checkEXP(idDG) && num.checkNumBorrowBooks(idDG) && !num.checkMaxBorrowBooks(idDG)) {
                    return true;
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

    //Xác nhận cho mượn từ phiếu đặt
    public void confirmBorrowToReservation(String id) throws SQLException {
        try (Connection conn = Utils.getConn()) {
            id = null;

        }
    }
}
