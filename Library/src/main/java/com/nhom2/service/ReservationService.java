/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.service;

import com.nhom2.library.Utils;
import com.nhom2.pojo.BookResponse;
import com.nhom2.pojo.PhieuDat;
import com.nhom2.pojo.ReservationCardResponse;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author CamHa
 */
public class ReservationService {

    public int getAvailableIdSach_Copies(int idsach) throws SQLException {
        try (Connection conn = Utils.getConn()) {
            int Id = 0;

            String sql = "select idsach_copies from sach_copies where idDauSach = ? and TinhTrang = 0 limit 1";

            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1, idsach);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Id = rs.getInt("idsach_copies");
            }

            return Id;
        }
    }

    public boolean createReservationCard(PhieuDat p) throws SQLException {
        try (Connection conn = Utils.getConn()) {

            DateTimeFormatter fmt3 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String d = p.getNgayDat().format(fmt3);
            String t[] = d.split(" ");

            String sql = "INSERT INTO phieudat(idphieudat, ngaydat, id_sach, docgia_id, TinhTrang) VALUES(?, TIMESTAMP(?, ?) , ?, ?, ?)";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, p.getIdPhieuDat());
            stm.setString(2, t[0]);
            stm.setString(3, t[1]);
            stm.setInt(4, p.getIdSach());
            stm.setInt(5, p.getIdDocGia());
            stm.setInt(6, p.getTinhTrang());

            int r = stm.executeUpdate();

            return r > 0;

        }
    }

    public List<ReservationCardResponse> getReservationCard(int uID) throws SQLException {
        List<ReservationCardResponse> ReservationCardList = new ArrayList<>();
        try (Connection conn = Utils.getConn()) {
            String sql = "select phieudat.idphieudat, idsach_copies, sach.Ten, phieudat.ngaydat, phieudat.TinhTrang\n"
                    + " , concat(docgia.HoLot,  \" \" ,docgia.Ten) as DocGia, phieudat.docgia_id \n"
                    + " from ktpm_btl.sach_copies, ktpm_btl.phieudat, ktpm_btl.docgia, ktpm_btl.sach\n"
                    + "where idsach_copies = phieudat.id_sach and phieudat.docgia_id = docgia.id \n"
                    + "and sach.idSach = sach_copies.idDauSach  and phieudat.docgia_id = ?";

            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1, uID);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ReservationCardResponse c = new ReservationCardResponse(rs.getString("idphieudat"),
                        rs.getInt("idsach_copies"),
                        rs.getNString("Ten"),
                        rs.getInt("TinhTrang"),
                        rs.getTimestamp("ngaydat").toLocalDateTime(),
                        rs.getNString("DocGia"),
                        rs.getInt("docgia_id"));
                ReservationCardList.add(c);
            }
        }
        return ReservationCardList;
    }

}
