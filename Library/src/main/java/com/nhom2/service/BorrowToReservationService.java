/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.service;

import com.nhom2.library.Utils;
import com.nhom2.pojo.ReservationCardResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class BorrowToReservationService {
    //Mượn thành công -> đổi tình trạng phiếu đặt
    public boolean confirmBorrowToReservationBook(String idPhieuDat) throws SQLException {
        try (Connection conn = Utils.getConn()) {
            String sql = "UPDATE phieudat SET TinhTrang = 1 WHERE idphieudat = ?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, idPhieuDat);
            int r = stm.executeUpdate();
            return r > 0;
        }
    }

    public ReservationCardResponse getReservationCard(String id) throws SQLException {
        try (Connection conn = Utils.getConn()) {
            ReservationCardResponse p = new ReservationCardResponse();
            String sql = "select p.idphieudat, p.ngaydat, p.id_sach, s.Ten, p.tinhtrang, p.docgia_id, \n"
                    + "concat(d.HoLot,  \" \" , d.Ten) as \"TenDocGia\" \n"
                    + "FROM ktpm_btl.phieudat p, ktpm_btl.docgia d, ktpm_btl.sach s, ktpm_btl.sach_copies c \n"
                    + "WHERE p.docgia_id = d.id and p.id_sach = c.idsach_copies and s.idSach = c.idDauSach and p.idphieudat = ?\n";

            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, id);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                p = new ReservationCardResponse(rs.getString("idphieudat"),
                        rs.getInt("id_sach"),
                        rs.getNString("Ten"),
                        rs.getInt("TinhTrang"),
                        rs.getTimestamp("ngaydat").toLocalDateTime(),
                        rs.getNString("TenDocGia"),
                        rs.getInt("docgia_id"));
            }
            return p;
        }
    }
    
    public boolean updateReservationCardWithID(String id) throws SQLException {
        try (Connection conn = Utils.getConn()) {
            conn.setAutoCommit(false);
            String sql = "UPDATE phieudat SET TinhTrang = 0 WHERE idphieudat = ?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, id);
            int r = stm.executeUpdate();
            return r > 0;
             
        }
    }
}
