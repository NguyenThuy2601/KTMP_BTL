/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.service;

import com.nhom2.library.Utils;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author ADMIN
 */
public class CheckNumBorrowBooksService {

    public boolean checkNumBorrowBooks() throws SQLException {
        int num = 0;
        try (Connection conn = Utils.getConn()) {
            //Truy van
            Statement stm = conn.createStatement();
            // Truy van lay du lieu --> select
            ResultSet rs = stm.executeQuery("SELECT COUNT(idphieumuon) AS 'SoLuong'\n"
                    + "FROM phieumuon\n"
                    + "WHERE docgia_id = ? and tinhtrang != 1");
            while (rs.next()) {
                num = rs.getInt("SoLuong");
            }
            conn.setAutoCommit(false);
            try {
                conn.commit();
                if (num < 5) {
                    return true;
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                return false;
            }
        }
        return false;
    }

}
