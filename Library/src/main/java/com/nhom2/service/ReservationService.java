/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.service;

import com.nhom2.library.Utils;
import com.nhom2.pojo.PhieuDat;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 *
 * @author CamHa
 */
public class ReservationService {

    public Date convertToDate(LocalDateTime dateToConvert) {
        return (Date) java.util.Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    public boolean createReservationCard(int idSach, int idUser, PhieuDat p) throws SQLException {
        try (Connection conn = Utils.getConn()) {
            conn.setAutoCommit(false);
            String sql = "INSERT INTO phieudat(idphieudat, ngaydat, sach_idSach, docgia_id) VALUES(?, ?, ?, ?)"; 
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, p.getIdPhieuDat());
            stm.setDate(2, this.convertToDate(p.getNgayDat()));
            stm.setInt(3, p.getIdSach());
            stm.setInt(4, p.getIdDocGia());

            int r = stm.executeUpdate();

            if (r > 0) {
                sql = "update sach set tinhtrang = 1 where idSach = ? ";
                PreparedStatement stm1 = conn.prepareCall(sql);

                stm1.setInt(1, idSach);
                
                 stm1.executeUpdate();
            }

            try {
                conn.commit();
                return true;
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                return false;
            }
        }
    }
}
