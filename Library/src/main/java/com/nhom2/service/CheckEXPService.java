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
public class CheckEXPService {

    public boolean checkEXP() throws SQLException {
        int exp = 0;
        try (Connection conn = Utils.getConn()) {
            conn.setAutoCommit(false);
            //Truy van
            Statement stm = conn.createStatement();
            // Truy van lay du lieu --> select
            ResultSet rs = stm.executeQuery("SELECT DATEDIFF(d.NgayHetHan,now()) AS 'EXP'\n"
                    + "FROM phieumuon p, docgia d\n"
                    + "WHERE p.docgia_id = d.id and p.idphieumuon = ?");
            if (rs.next()) {
                exp = rs.getInt("EXP");
            }
            if (exp > 0) {
                try {
                    conn.commit();
                    return true;
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                    return false;
                }
            }
        }
        return false;
    }
}
