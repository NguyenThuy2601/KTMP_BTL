/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.service;

import com.nhom2.library.Utils;
import com.nhom2.pojo.PhieuMuon;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author ADMIN
 */
public class BorrowBookService {

    public LocalDate convertToLocalDateViaSqlDate(Date dateToConvert) {
        return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
    }

    public boolean addBorrowCard(int idSach, int idUser, PhieuMuon p) throws SQLException {
        try (Connection conn = Utils.getConn()) {
            DateTimeFormatter fmt3 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String d = p.getNgayMuon().format(fmt3);
            String t[] = d.split(" ");

            conn.setAutoCommit(false);
            String sql = "INSERT INTO phieumuon(idphieumuon, ngaymuon, tinhtrang, docgia_id, sach_idSach1) VALUES(?, TIMESTAMP(?, ?) , ?, ?, ?)";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, p.getIdPhieuMuon());
            stm.setString(2, t[0]);
            stm.setString(3, t[1]);
            stm.setInt(4, p.getTinhTrang());
            stm.setInt(5, p.getIdDocGia());
            stm.setInt(6, p.getIdSach());

            int r = stm.executeUpdate();

            if (r > 0) {
                sql = "UPDATE sach_copies SET tinhtrang = 1 WHERE idsach_copies = ?";
                PreparedStatement stm1 = conn.prepareCall(sql);
                stm1.setInt(1, idSach);
                stm1.executeUpdate();

                sql = "UPDATE sach SET SoLuong = (SoLuong - 1) WHERE idSach = (SELECT idSach FROM (\n"
                        + "SELECT idSach FROM sach INNER JOIN sach_copies ON sach.idSach = sach_copies.idDauSach \n"
                        + "WHERE sach_copies.idsach_copies = ?) AS temp)";
                PreparedStatement stm2 = conn.prepareCall(sql);
                stm2.setInt(1, idSach);
                stm2.executeUpdate();
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
