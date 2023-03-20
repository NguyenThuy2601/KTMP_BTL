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

/**
 *
 * @author CamHa
 */
public class BookReturningService {
    public boolean checkExpiredCard(String cardID) throws SQLException{
        try (Connection conn = Utils.getConn()) {

            String sql = "SELECT day(timediff(now(), phieumuon.ngaymuon)) as day FROM phieumuon where idphieumuon = ?";

            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, cardID);

            ResultSet rs = stm.executeQuery();
            int day = 0;
            while (rs.next()) {
                day = rs.getInt("day");
            }
            return day > 30;
        }
    }
    
    public int calFee(){
        return 0;
    }
}
