/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.service;

import com.nhom2.library.Utils;
import com.nhom2.pojo.BookResponse;
import com.nhom2.pojo.Sach;
import com.nhom2.pojo.DanhMuc;
import com.nhom2.pojo.Sach_TacGia;
import com.nhom2.pojo.TacGia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author CamHa
 */
public class HomepageService {

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

    public List<BookResponse> getBooks() throws SQLException {
        List<BookResponse> bookList = new ArrayList<>();

        try (Connection conn = Utils.getConn()) {
            // B3 Thuc thi truy van
            Statement stm = conn.createStatement();

            // + Truy van lay du lieu: select
            ResultSet rs = stm.executeQuery("select sach.idSach, sach.Ten, sach.NamXB ,GROUP_CONCAT(DISTINCT tacgia.HoLot, \" \" ,tacgia.Ten) as \"TenTacGia\" ,\n"
                    + "		danhmuc.ten as \"TenDM\", vitri.TenViTri, sach.NgayNhap, sach.NoiXB, sach.MoTa,\n"
                    + "        sach.SoLuong, sach.danhmuc_iddanhmuc\n"
                    + "from ktpm_btl.sach, ktpm_btl.tg_sach, ktpm_btl.tacgia, ktpm_btl.danhmuc, ktpm_btl.vitri\n"
                    + "where sach.idSach = tg_sach.idSach and tg_sach.idTacGia = tacgia.idtacgia "
                    + "and sach.danhmuc_iddanhmuc = danhmuc.iddanhmuc "
                    + "AND sach.vitri_idvitri = vitri.idvitri\n"
                    + "GROUP BY sach.idSach;");
            while (rs.next()) {
                BookResponse b = new BookResponse(rs.getInt("idSach"),
                        rs.getNString("Ten"), rs.getInt("NamXB"),
                        rs.getString("NoiXB"), rs.getDate("NgayNhap").toLocalDate(),
                        rs.getNString("MoTa"),
                        rs.getInt("SoLuong"),
                        rs.getNString("TenTacGia"),
                        rs.getNString("TenDM"),
                        rs.getNString("TenViTri"),
                            rs.getInt("danhmuc_iddanhmuc"));

                bookList.add(b);
            }
        }
        return bookList;
    }

    public List<BookResponse> findBook(List<BookResponse> l, String bName, String aName, int namXB, int idDanhMuc) {
        if (bName != null) {
            for (int i = 0; i < l.size(); i++) {
                if (l.get(i).getTen().contains(bName) == false) {
                    l.remove(i);
                    i--;
                }
            }
        }
        if (aName != null) {
            for (int i = 0; i < l.size(); i++) {
                if (l.get(i).getTenTG().contains(aName) == false) {
                    l.remove(i);
                    i--;
                }
            }
        }
        if (namXB > 0) {
            for (int i = 0; i < l.size(); i++) {
                if (l.get(i).getNamXB() != namXB) {
                    l.remove(i);
                    i--;
                }
            }
        }
        if (idDanhMuc != 0) {
            for (int i = 0; i < l.size(); i++) {
                if (l.get(i).getIdDM() != idDanhMuc) {
                    l.remove(i);
                    i--;
                }
            }
        }
        return l;
    }

    public boolean checkYearFormat(String year) {
        if (year == null || year.isEmpty() || year.isBlank()) {
            return true;
        } else {

            try {
                int intValue = Integer.parseInt(year.trim());
                if(intValue > 0)
                    return true;
                else
                    return false;

            } catch (NumberFormatException e) {
                return false;
            }
        }
    }
    
    public int parseYear(String year){
        if (year == null || year.isEmpty() || year.isBlank()) 
            return 0;
        else
            return Integer.parseInt(year.trim());
    }
    
    public int checkNullSelectedComboBoxItem(DanhMuc c){
        if(c == null)
            return 0;
        else
            return c.getIdDanhMuc();
    }
}
