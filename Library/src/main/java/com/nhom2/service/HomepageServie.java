/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.service;

import com.nhom2.library.Utils;
import com.nhom2.pojo.Book;
import com.nhom2.pojo.DanhMuc;
import com.nhom2.pojo.Sach_TacGia;
import com.nhom2.pojo.TacGia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author CamHa
 */
public class HomepageServie {

    public List<DanhMuc> getDanhMucSach() throws SQLException {
        List<DanhMuc> DanhMucList = new ArrayList<>();
        try (Connection conn = Utils.getConn()) {
            // B3 Thuc thi truy van
            Statement stm = conn.createStatement();

            // + Truy van lay du lieu: select
            ResultSet rs = stm.executeQuery("SELECT * FROM danhmuc");
            while (rs.next()) {
                DanhMuc c = new DanhMuc(rs.getInt("iddanhmuc"), rs.getString("ten"));
                DanhMucList.add(c);
            }
        }
        return DanhMucList;
    }

    public List<Sach_TacGia> getTacGia(List<Book> bookList) throws SQLException {
        List<Sach_TacGia> s_tgList = new ArrayList<>();
        try (Connection conn = Utils.getConn()) {
            // B3 Thuc thi truy van
            String sql = "select tg.idtacgia, tg.HoLot, tg.Ten"
                    + "FROM ktpm_btl.tg_sach cb, ktpm_btl.tacgia tg"
                    + "where cb.idTacGia = tg.idTacGia and cb.idSach = ?";
            for (int i = 0; i < bookList.size(); i++) {
                PreparedStatement stm = conn.prepareCall(sql);
                stm.setString(1, Integer.toString(bookList.get(i).getIdSach()));
                ResultSet rs = stm.executeQuery();

                while (rs.next()) {
                    TacGia tg = new TacGia(rs.getInt("idtacgia"), rs.getNString("HoLot"), rs.getNString("Ten"));
                    s_tgList.get(i).setTacGia(tg);
                }
            }
            return s_tgList;
        }

    }

    public List<Book> getBooks() throws SQLException {
        List<Book> bookList = new ArrayList<>();

        try (Connection conn = Utils.getConn()) {
            // B3 Thuc thi truy van
            Statement stm = conn.createStatement();

            // + Truy van lay du lieu: select
            ResultSet rs = stm.executeQuery("SELECT * FROM sach");
            while (rs.next()) {
                Book b = new Book(rs.getInt("idSach"), rs.getNString("Ten"), rs.getInt("NamXB"),
                        rs.getNString("NoiXB"), rs.getDate("NgayNhap").toLocalDate(), rs.getString("MoTa"),
                        rs.getBoolean("TinhTrang"), rs.getInt("danhmuc_iddanhmuc"),
                        rs.getInt("vitri_idvitri"));

                bookList.add(b);
            }
        }
        return bookList;
    }

    public List<Integer> findBooksByAuthorName(String aFname, String aLname) throws SQLException {
        List<Integer> bookIDList = new ArrayList<>();
        try (Connection conn = Utils.getConn()) {
            String sql = "select cb.idSach from tacgia tg, tg_sach tgs where tg.HoLot like %?% and tg.Ten like %?% ";

            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, aLname);
            stm.setString(2, aFname);

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                bookIDList.add(rs.getInt("idSach"));
            }
        }
        return bookIDList;
    }

    public List<Integer> findBooksByBookName(String bName) throws SQLException {
        List<Integer> bookIDList = new ArrayList<>();
        try (Connection conn = Utils.getConn()) {
            String sql = "select idSach from sach where Ten like %?% ";

            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, bName);

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                bookIDList.add(rs.getInt("idSach"));
            }
        }
        return bookIDList;
    }
    
    public List<Integer> findBooksByPublishYear(int year) throws SQLException {
        List<Integer> bookIDList = new ArrayList<>();
        try (Connection conn = Utils.getConn()) {
            String sql = "select idSach from sach where NamXB = ? ";

            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, Integer.toString(year));

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                bookIDList.add(rs.getInt("idSach"));
            }
        }
        return bookIDList;
    }
    
    public List<Integer> findBooksByCate(int idCate) throws SQLException {
        List<Integer> bookIDList = new ArrayList<>();
        try (Connection conn = Utils.getConn()) {
            String sql = "select idSach from sach where danhmuc_iddanhmuc = ? ";

            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, Integer.toString(idCate));

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                bookIDList.add(rs.getInt("idSach"));
            }
        }
        return bookIDList;
    }
    
    
}
