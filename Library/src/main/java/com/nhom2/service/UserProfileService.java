/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.service;

import com.nhom2.library.Utils;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

    public int checkPhoneFormat(String sdt) {
        if (sdt == null || sdt.isEmpty() || sdt.isBlank()) {
            return 1;
        } else {
            if ((sdt.length() >= 1 && sdt.length() < 10) || sdt.length() > 10) {
                return 0;
            } else {
                try {
                    int intValue = Integer.parseInt(sdt.trim());
                    return 1;

                } catch (NumberFormatException e) {
                    return -1;
                }
            }
        }
    }
    public boolean checkEmailFormat(String email)
    {
        if(email == null || email.isEmpty() || email.isBlank())
            return true;
        else
        {
            return email.contains("@");
        }
    }
}
