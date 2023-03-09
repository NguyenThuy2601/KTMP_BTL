/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author CamHa
 */
public class Utils {
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    public static Connection getConn() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/ktpm_btl", "root", "1234");
        return conn;
    }
    
}
