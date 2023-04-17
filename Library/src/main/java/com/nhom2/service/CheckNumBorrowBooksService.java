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

/**
 *
 * @author ADMIN
 */
public class CheckNumBorrowBooksService {

    //Check đã trả hết sách chưa
    public boolean checkNumBorrowBooks(int id) throws SQLException {
        boolean flag = true; //sách đã trả hết
        try (Connection conn = Utils.getConn()) {
            conn.setAutoCommit(false);
            //Truy van
            String sql = "SELECT COUNT(idphieumuon) AS 'SoLuong'\n"
                    + "FROM phieumuon\n"
                    + "WHERE docgia_id = ? and tinhtrang != 1 and ngaymuon != date(now())";
            //Statement stm = conn.createStatement();
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1, id);
            // Truy van lay du lieu --> select            
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                int num = rs.getInt("SoLuong");
                if (num > 0) {
                    flag = false;
                }
            }

            try {
                conn.commit();
                return flag;
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                return flag;
            }
        }
    }

    //Check mượn tối đa 5 cuốn, quá 5 cuốn -> báo
    public boolean checkMaxBorrowBooks(int id) throws SQLException {
        boolean flag = true; //không quá 5 cuốn mượn
        try (Connection conn = Utils.getConn()) {
            conn.setAutoCommit(false);
            //Truy van
            String sql = "SELECT COUNT(idphieumuon) AS 'SoLuong' \n"
                    + "FROM phieumuon \n"
                    + "WHERE docgia_id = ? and ngaymuon = date(now()) and tinhtrang != 1";
            //Statement stm = conn.createStatement();
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1, id);
            // Truy van lay du lieu --> select
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                int n = rs.getInt("SoLuong");
                if (n >= 5) {
                    flag = false; //true: quá 5 cuốn
                }
            }

            try {
                conn.commit();
                return flag;
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                return flag;
            }
        }
    }

    //Đếm có bao nhiêu sách đang được mượn trong ngày
    public int countBorrowBooks(int id) throws SQLException {
        int count = 0;
        try (Connection conn = Utils.getConn()) {
            conn.setAutoCommit(false);
            //Truy van
            String sql = "SELECT COUNT(idphieumuon) AS 'SoLuong' \n"
                    + "FROM phieumuon \n"
                    + "WHERE docgia_id = ? and ngaymuon = date(now()) and tinhtrang != 1";
            //Statement stm = conn.createStatement();
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1, id);
            // Truy van lay du lieu --> select
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                count = rs.getInt("SoLuong");
            }
        }
        return (5 - count);
    }

}
