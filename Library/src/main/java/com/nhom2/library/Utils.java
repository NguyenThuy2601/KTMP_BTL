/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.library;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author CamHa
 */
public class Utils {
    static {
        try {
            // B1 Nap driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static Connection getConn() throws SQLException {
        //Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ktpm_btl", "root", "1234");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ktpm_btl", "root", "Admin@123");
        return conn;
    }
    
}
