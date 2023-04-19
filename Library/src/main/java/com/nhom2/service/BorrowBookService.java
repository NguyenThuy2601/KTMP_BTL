/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.service;

import com.nhom2.library.Utils;
import com.nhom2.pojo.BorrowCardResponse;
import com.nhom2.pojo.PhieuMuon;
import com.nhom2.pojo.User;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class BorrowBookService {

    public LocalDate convertToLocalDateViaSqlDate(Date dateToConvert) {
        return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
    }

    public boolean addBorrowCard(PhieuMuon p) throws SQLException {
        try (Connection conn = Utils.getConn()) {
            //conn.setAutoCommit(false);
            String sql = "INSERT INTO phieumuon(idphieumuon, ngaymuon, tinhtrang, docgia_id, sach_idSach1) VALUES(?, ? , ?, ?, ?)";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, p.getIdPhieuMuon());
            stm.setDate(2, Date.valueOf(p.getNgayMuon()));

            stm.setInt(3, p.getTinhTrang());
            stm.setInt(4, p.getIdDocGia());
            stm.setInt(5, p.getIdSach());

            int r = stm.executeUpdate();

            return r > 0;
        }
    }

//    public void deleteBorrowCard(String id) throws SQLException {
//        try (Connection conn = Utils.getConn()) {
//            String sql = "DELETE FROM phieumuon WHERE idphieumuon = ?";
//            PreparedStatement stm = conn.prepareCall(sql);
//            stm.setString(1, id);
//            stm.executeUpdate();
//        }
//    }

    public List<BorrowCardResponse> getBorrowCards(int id) throws SQLException {
        List<BorrowCardResponse> borrowCards = new ArrayList<>();
        try (Connection conn = Utils.getConn()) {
            String sql = "SELECT p.idphieumuon, p.ngaymuon, p.sach_idSach1, s.Ten, p.tinhtrang, p.docgia_id, \n"
                    + "concat(d.HoLot,  \" \" , d.Ten) as \"TenDocGia\" \n"
                    + "FROM ktpm_btl.phieumuon p, ktpm_btl.docgia d, ktpm_btl.sach s, ktpm_btl.sach_copies c \n"
                    + "WHERE p.docgia_id = d.id and p.sach_idSach1 = c.idsach_copies and s.idSach = c.idDauSach and p.docgia_id = ?";

            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                BorrowCardResponse b = new BorrowCardResponse(rs.getString("idphieumuon"),
                        rs.getInt("sach_idSach1"),
                        rs.getNString("Ten"),
                        rs.getInt("tinhtrang"),
                        rs.getDate("ngaymuon").toLocalDate(),
                        rs.getNString("TenDocGia"),
                        rs.getInt("docgia_id"));
                borrowCards.add(b);
            }
        }
        return borrowCards;
    }

    public List<BorrowCardResponse> getBorrowCard(String id) throws SQLException {
        List<BorrowCardResponse> borrowCards = new ArrayList<>();
        try (Connection conn = Utils.getConn()) {
            String sql = "SELECT p.idphieumuon, p.ngaymuon, p.sach_idSach1, s.Ten, p.tinhtrang, p.docgia_id, \n"
                    + "concat(d.HoLot,  \" \" , d.Ten) as \"TenDocGia\" \n"
                    + "FROM phieumuon p, docgia d, sach s, sach_copies c \n"
                    + "WHERE p.sach_idSach1 = c.idsach_copies and c.idDauSach = s.idSach and p.docgia_id = d.id and p.idphieumuon = ?";

            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, id);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                BorrowCardResponse b = new BorrowCardResponse(rs.getString("idphieumuon"),
                        rs.getInt("sach_idSach1"),
                        rs.getNString("Ten"),
                        rs.getInt("tinhtrang"),
                        rs.getDate("ngaymuon").toLocalDate(),
                        rs.getNString("TenDocGia"),
                        rs.getInt("docgia_id"));
                borrowCards.add(b);
            }
        }
        return borrowCards;
    }

    public List<BorrowCardResponse> getCards(List<String> id) throws SQLException {
        List<BorrowCardResponse> borrowCards = new ArrayList<>();
        try (Connection conn = Utils.getConn()) {
            String sql = "SELECT p.idphieumuon, p.ngaymuon, p.sach_idSach1, s.Ten, p.tinhtrang, p.docgia_id, \n"
                    + "concat(d.HoLot,  \" \" , d.Ten) as \"TenDocGia\" \n"
                    + "FROM phieumuon p, docgia d, sach s, sach_copies c \n"
                    + "WHERE p.sach_idSach1 = c.idsach_copies and c.idDauSach = s.idSach and p.docgia_id = d.id and p.idphieumuon = ?";

            PreparedStatement stm = conn.prepareCall(sql);
            for (String i : id) {
                stm.setString(1, i);

                ResultSet rs = stm.executeQuery();

                while (rs.next()) {
                    BorrowCardResponse b = new BorrowCardResponse(rs.getString("idphieumuon"),
                            rs.getInt("sach_idSach1"),
                            rs.getNString("Ten"),
                            rs.getInt("tinhtrang"),
                            rs.getDate("ngaymuon").toLocalDate(),
                            rs.getNString("TenDocGia"),
                            rs.getInt("docgia_id"));
                    borrowCards.add(b);
                }
            }
        }
        return borrowCards;
    }

    public List<Integer> checkAvailableBook() throws SQLException {
        try (Connection conn = Utils.getConn()) {
            //id = 0;
            List<Integer> idBookNotAvailable = new ArrayList<>();
            Statement stm = conn.createStatement();
            //conn.setAutoCommit(false);
            ResultSet rs = stm.executeQuery("SELECT * FROM sach_copies WHERE TinhTrang = -1");//sach da duoc dat truoc
            while (rs.next()) {
                int id = rs.getInt("idsach_copies");
                idBookNotAvailable.add(id);
            }
            return idBookNotAvailable;
        }
    }

    //Check sách đang được mượn hay không
    public List<Integer> checkNotAvailableBook() throws SQLException {
        try (Connection conn = Utils.getConn()) {
            //id = 0;
            List<Integer> idBookAvailable = new ArrayList<>();
            Statement stm = conn.createStatement();
            //conn.setAutoCommit(false);
            ResultSet rs = stm.executeQuery("SELECT * FROM sach_copies WHERE TinhTrang = 1");//sach dang duoc muon
            while (rs.next()) {
                int id = rs.getInt("idsach_copies");
                idBookAvailable.add(id);
            }
            return idBookAvailable;
        }
    }

    public int getIdSach_Copies(int idS) throws SQLException {
        try (Connection conn = Utils.getConn()) {
            int id = 0;

            String sql = "SELECT * FROM sach_copies WHERE idsach_copies = ? and TinhTrang = 0 limit 1";

            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1, idS);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                id = rs.getInt("idsach_copies");
            }

            return id;
        }
    }

    public int getIdDG(String id) throws SQLException {
        try (Connection conn = Utils.getConn()) {
            int idDG = 0;

            String sql = "SELECT docgia_id FROM phieumuon WHERE idphieumuon = ?";

            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, id);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                idDG = rs.getInt("docgia_id");
            }

            return idDG;
        }
    }
}
