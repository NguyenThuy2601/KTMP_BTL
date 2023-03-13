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
public class UserProfileService {
    public boolean updateInfo(int uID, String email, String address, String sdt) throws SQLException {
         try (Connection conn = Utils.getConn()) {

            
            String sql = "update docgia set Email = ?, DiaChi = ?, SDT = ? where id = ?";

            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, email);
            stm.setString(2, address);
            stm.setString(3, sdt);
            stm.setString(4, Integer.toString(uID));

            int r = stm.executeUpdate();

            return r > 0;
        }
    }
}
