/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.service;

import com.nhom2.pojo.User;
import com.nhom2.library.Utils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.time.LocalDate;

/**
 *
 * @author CamHa
 */
public class LoginService {

    public boolean checkEmail(String username) throws SQLException, ClassNotFoundException {
        try (Connection conn = Utils.getConn()) {
            String sql = "SELECT * FROM taikhoan where username = ?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, username);

            ResultSet rs = stm.executeQuery();

            if (rs.next() == false) {
                return false;
            } else {
                return true;
            }
        }
    }

    public String hashPassword(String passwordToHash) {
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Add password bytes to digest
            md.update(passwordToHash.getBytes());

            // Get the hash's bytes
            byte[] bytes = md.digest();

            // This bytes[] has bytes in decimal format. Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            // Get complete hashed password in hex format
            generatedPassword = sb.toString();
            return generatedPassword;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean checkPassword(String username, String password) throws SQLException, ClassNotFoundException {
        String pass = null;
        try (Connection conn = Utils.getConn()) {

            
            String sql = "SELECT password FROM taikhoan where username = ?";

            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, username);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                pass = rs.getString("password");
            }
            
            if(this.hashPassword(password).equalsIgnoreCase(pass) )
                return true;
            else
                return false;
        }
    }
    
    public String getAccID(String username) throws SQLException, ClassNotFoundException{
        String ID =  null;
        try (Connection conn = Utils.getConn()) {

            
            String sql = "SELECT idTaiKhoan FROM taikhoan where username = ?";

            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, username);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ID = rs.getString("idTaiKhoan");
            }
            
            
        }
        return ID;
    }
    
    public User setUser(String accountID) throws SQLException, ClassNotFoundException {
        
        try (Connection conn = Utils.getConn()) {
            
            User u;
            String sql = "SELECT * FROM docgia where TaiKhoan_id = ?";

            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, accountID);

            ResultSet rs = stm.executeQuery();

               u= new User(rs.getInt("id"), rs.getString("TaiKhoan_id"), 
                            rs.getString("Ten"),rs.getString("HoLot"), rs.getString("DoiTuong"), 
                            rs.getDate("NgayBD").toLocalDate(), rs.getDate("NgayHetHan").toLocalDate(), 
                            rs.getString("Email"), rs.getString("DiaChi"), rs.getString("SDT"),
                            rs.getString("bophan_id"), rs.getDate("DOB").toLocalDate() );

            return u;
        }
        
    }
}
