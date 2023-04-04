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
public class CheckEXPService {

    public boolean checkEXP(int id) throws SQLException {
        int exp = 0;
        boolean flag = false;
        try (Connection conn = Utils.getConn()) {
            conn.setAutoCommit(false);
            //Truy van
            //Statement stm = conn.createStatement();
            // Truy van lay du lieu --> select
            String sql = "SELECT DATEDIFF(NgayHetHan,now()) AS 'EXP' FROM docgia WHERE id = ?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                exp = rs.getInt("EXP");
                if (exp > 0) flag = true;
            }

            try {
                conn.commit();
                return flag;
//                if (exp > 0) {
//                    return true;
//                } else {
//                    return false;
//                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                return flag;
            }
        }
    }
}
