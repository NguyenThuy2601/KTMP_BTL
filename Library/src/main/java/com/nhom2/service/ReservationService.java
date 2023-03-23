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

    public boolean createReservationCard(int idSach, int idUser, PhieuDat p) throws SQLException {
        try (Connection conn = Utils.getConn()) {

            DateTimeFormatter fmt3 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String d = p.getNgayDat().format(fmt3);
            String t[] = d.split(" ");

            conn.setAutoCommit(false);
            String sql = "INSERT INTO phieudat(idphieudat, ngaydat, sach_idSach, docgia_id, TinhTrang) VALUES(?, TIMESTAMP(?, ?) , ?, ?, ?)";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, p.getIdPhieuDat());
            stm.setString(2, t[0]);
            stm.setString(3, t[1]);
            stm.setInt(4, p.getIdSach());
            stm.setInt(5, p.getIdDocGia());
            stm.setInt(6, p.getTinhTrang());

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

    public List<ReservationCardResponse> getReservationCard(int uID) throws SQLException {
        List<ReservationCardResponse> ReservationCardList = new ArrayList<>();
        try (Connection conn = Utils.getConn()) {
            String sql = "select phieudat.idphieudat, sach.idSach, sach.Ten, phieudat.ngaydat\n"
                    + "			, (\n"
                    + "					CASE \n"
                    + "						WHEN phieudat.TinhTrang = -1 THEN \"còn hạn\"\n"
                    + "						WHEN phieudat.TinhTrang = 0 THEN \"phiếu bị hủy\"\n"
                    + "						WHEN  phieudat.TinhTrang = 1 THEN \"đã nhận\"\n"
                    + "					END) AS TinhTrang\n"
                    + "            , concat(docgia.HoLot,  \" \" ,docgia.Ten) as TacGia\n"
                    + "from ktpm_btl.sach, ktpm_btl.phieudat, ktpm_btl.docgia\n"
                    + "where sach.idSach = phieudat.sach_idSach and phieudat.docgia_id = docgia.id and phieudat.docgia_id = ?";

            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1, uID);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                ReservationCardResponse c = new ReservationCardResponse(rs.getString("idphieudat"),
                        rs.getInt("idSach"),
                        rs.getNString("Ten"),
                        rs.getString("TinhTrang"),
                        rs.getTimestamp("ngaydat").toLocalDateTime(),
                        rs.getNString("TacGia"));
                ReservationCardList.add(c);
            }
        }
        return ReservationCardList;
    }

}
