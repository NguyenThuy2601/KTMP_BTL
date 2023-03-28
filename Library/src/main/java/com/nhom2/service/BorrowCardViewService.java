/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.service;

import com.nhom2.library.Utils;
import com.nhom2.pojo.BorrowCardResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author CamHa
 */
public class BorrowCardViewService {

    public List<BorrowCardResponse> getBorrowCard(int uID) throws SQLException {
        List<BorrowCardResponse> list = new ArrayList<>();
        try (Connection conn = Utils.getConn()) {
            String sql = "select phieumuon.idphieumuon, phieumuon.sach_idSach1, sach.Ten, phieumuon.ngaymuon,\n"
                    + "(\n"
                    + "CASE \n"
                    + "WHEN phieumuon.tinhtrang = -1 THEN \"phiếu còn hạn\"\n"
                    + "WHEN phieumuon.tinhtrang = 0 THEN \"phiếu quá hạn\"\n"
                    + "WHEN  phieumuon.tinhtrang = 1 THEN \"đã trả sách\"\n"
                    + "END) AS TinhTrang,\n"
                    + "concat(docgia.HoLot, \" \" ,docgia.Ten) as DocGia\n"
                    + "from ktpm_btl.sach_copies, ktpm_btl.phieumuon, ktpm_btl.docgia, ktpm_btl.sach\n"
                    + "where idsach_copies = phieumuon.sach_idSach1 and phieumuon.docgia_id = docgia.id \n"
                    + "and sach.idSach = sach_copies.idDauSach and phieumuon.docgia_id = ?";

            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1, uID);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                BorrowCardResponse c = new BorrowCardResponse(rs.getString("idphieumuon"),
                                                            rs.getInt("sach_idSach1"),
                                                            rs.getNString("Ten"),
                                                            rs.getNString("TinhTrang"),
                                                            rs.getDate("ngaymuon").toLocalDate(),
                                                            rs.getNString("DocGia"));
                list.add(c);
            }
            return list;
        }

    }
}
