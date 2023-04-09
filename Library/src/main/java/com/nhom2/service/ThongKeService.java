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
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class ThongKeService {

    //Tổng số sách hiện có trong thư viện
    public int totalBookInLib() throws SQLException {
        int sum = 0;
        try (Connection conn = Utils.getConn()) {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT SUM(SoLuong) AS 'TongSL' FROM sach");

            if (rs.next()) {
                sum = rs.getInt("TongSL");
            }

            return sum;
        }
    }

    //Thống kê Theo Năm
    //Tổng số sách đang mượn
    public int totalBorrowBook(int year) throws SQLException {
        int sum = 0;
        try (Connection conn = Utils.getConn()) {
            String sql = "SELECT Count(DISTINCT sach_idSach1) AS 'TongSL' \n"
                    + "FROM phieumuon \n"
                    + "WHERE tinhtrang != 1 AND Year(ngaymuon) = ?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1, year);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                sum = rs.getInt("TongSL");
            }

            return sum;
        }
    }

    //Tổng số phiếu mượn
    public int totalBorrowCard(int year) throws SQLException {
        try (Connection conn = Utils.getConn()) {
            int sum = 0;
            String sql = "SELECT COUNT(idphieumuon) AS 'TongSL' FROM phieumuon WHERE Year(ngaymuon) = ?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1, year);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                sum = rs.getInt("TongSL");
            }

            return sum;
        }
    }

    //Tổng số phiếu mượn quá hạn
    public int totalBorrowCardQuaHan(int year) throws SQLException {
        try (Connection conn = Utils.getConn()) {
            int sum = 0;
            String sql = "SELECT COUNT(idphieumuon) AS 'TongSL' FROM phieumuon WHERE tinhtrang = 0 And Year(ngaymuon) = ?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1, year);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                sum = rs.getInt("TongSL");
            }

            return sum;
        }
    }

    //Số lượng sách đã trả
    public int totalBookBack(int year) throws SQLException {
        int sum = 0;
        try (Connection conn = Utils.getConn()) {
            String sql = "SELECT COUNT(idphieumuon) AS 'TongSL' FROM phieumuon WHERE tinhtrang = 1 AND Year(ngaymuon) = ?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1, year);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                sum = rs.getInt("TongSL");
            }

            return sum;
        }
    }

    //Số lượng sách chưa trả
    public int totalBookNotBackYet(int year) throws SQLException {
        try (Connection conn = Utils.getConn()) {
            int sum = 0;

            String sql = "SELECT COUNT(idphieumuon) AS 'TongSL' FROM phieumuon WHERE tinhtrang != 1 AND Year(ngaymuon) = ?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1, year);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                sum = rs.getInt("TongSL");
            }

            return sum;
        }
    }

    //Tên sách chưa trả + Tên người mượn
    public List<BorrowCardResponse> getInfo(int year) throws SQLException {
        List<BorrowCardResponse> cards = new ArrayList<>();
        try (Connection conn = Utils.getConn()) {
            String sql = "SELECT  p.idphieumuon, s.Ten, concat(d.HoLot,  \" \" , d.Ten) as \"TenDocGia\", p.ngaymuon, \n"
                    + "p.sach_idSach1, p.tinhtrang, p.docgia_id \n"
                    + "FROM ktpm_btl.phieumuon p, ktpm_btl.docgia d, ktpm_btl.sach s, ktpm_btl.sach_copies c \n"
                    + "WHERE p.tinhtrang != 1 AND year(p.ngaymuon) = ? AND p.sach_idSach1 = c.idsach_copies \n"
                    + "AND s.idSach = c.idDauSach AND p.docgia_id = d.id";

            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1, year);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                BorrowCardResponse b = new BorrowCardResponse(rs.getString("idphieumuon"),
                        rs.getInt("sach_idSach1"),
                        rs.getNString("Ten"),
                        rs.getInt("tinhtrang"),
                        rs.getDate("ngaymuon").toLocalDate(),
                        rs.getNString("TenDocGia"),
                        rs.getInt("docgia_id"));
                cards.add(b);
            }
        }
        return cards;
    }

    //Thống kê Theo Quý
    //Quí 1 : từ tháng 1 -> hết tháng 3
    //Tổng số sách đang mượn
    public int totalBorrowBookQuy1(int year) throws SQLException {
        int sum = 0;
        try (Connection conn = Utils.getConn()) {
            String sql = "SELECT Count(DISTINCT sach_idSach1) AS 'TongSL' \n"
                    + "FROM phieumuon \n"
                    + "WHERE tinhtrang != 1 AND Year(ngaymuon) = ? AND ngaymuon between '" + year + "-01-01' and '" + year + "-03-31'";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1, year);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                sum = rs.getInt("TongSL");
            }

            return sum;
        }
    }

    //Số lượng sách đã trả
    public int totalBookBackTheoQuy1(int year) throws SQLException {
        try (Connection conn = Utils.getConn()) {
            int sum = 0;

            String sql = "SELECT COUNT(idphieumuon) AS 'TongSL' FROM phieumuon WHERE tinhtrang = 1 \n"
                    + "AND ngaymuon between '" + year + "-01-01' and '" + year + "-03-31'";
            PreparedStatement stm = conn.prepareCall(sql);
            //stm.setInt(1, year);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                sum = rs.getInt("TongSL");
            }

            return sum;
        }
    }

    //Số lượng sách chưa trả
    public int totalBookNotBackYetTheoQuy1(int year) throws SQLException {
        try (Connection conn = Utils.getConn()) {
            int sum = 0;

            String sql = "SELECT COUNT(idphieumuon) AS 'TongSL' FROM phieumuon WHERE tinhtrang != 1 \n"
                    + "AND ngaymuon between '" + year + "-01-01' and '" + year + "-03-31'";
            PreparedStatement stm = conn.prepareCall(sql);
            //stm.setInt(1, year);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                sum = rs.getInt("TongSL");
            }

            return sum;
        }
    }

    //Tổng số phiếu mượn
    public int totalBorrowCardQuy1(int year) throws SQLException {
        try (Connection conn = Utils.getConn()) {
            int sum = 0;
            String sql = "SELECT COUNT(idphieumuon) AS 'TongSL' FROM phieumuon WHERE Year(ngaymuon) = ? \n"
                    + "AND ngaymuon between '" + year + "-01-01' and '" + year + "-03-31'";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1, year);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                sum = rs.getInt("TongSL");
            }

            return sum;
        }
    }

    //Tổng số phiếu mượn quá hạn
    public int totalBorrowCardQuaHanQuy1(int year) throws SQLException {
        try (Connection conn = Utils.getConn()) {
            int sum = 0;
            String sql = "SELECT COUNT(idphieumuon) AS 'TongSL' FROM phieumuon WHERE tinhtrang = 0 And Year(ngaymuon) = ?\n"
                    + "AND ngaymuon between '" + year + "-01-01' and '" + year + "-03-31'";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1, year);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                sum = rs.getInt("TongSL");
            }

            return sum;
        }
    }

    //Tên sách chưa trả + Tên người mượn
    public List<BorrowCardResponse> getInfoTheoQuy1(int year) throws SQLException {
        List<BorrowCardResponse> cards = new ArrayList<>();
        try (Connection conn = Utils.getConn()) {
            String sql = "SELECT  s.Ten, concat(d.HoLot,  \" \" , d.Ten) as \"TenDocGia\", p.tinhtrang, p.idphieumuon, p.docgia_id, p.sach_idSach1, p.ngaymuon \n"
                    + "FROM ktpm_btl.phieumuon p, ktpm_btl.docgia d, ktpm_btl.sach s, ktpm_btl.sach_copies c \n"
                    + "WHERE p.tinhtrang != 1 AND p.sach_idSach1 = c.idsach_copies \n"
                    + "AND s.idSach = c.idDauSach AND p.docgia_id = d.id \n"
                    + "AND p.ngaymuon between '" + year + "-01-01' and '" + year + "-03-31'";

            PreparedStatement stm = conn.prepareCall(sql);
            //stm.setInt(1, year);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                BorrowCardResponse b = new BorrowCardResponse(rs.getString("idphieumuon"),
                        rs.getInt("sach_idSach1"),
                        rs.getNString("Ten"),
                        rs.getInt("tinhtrang"),
                        rs.getDate("ngaymuon").toLocalDate(),
                        rs.getNString("TenDocGia"),
                        rs.getInt("docgia_id"));
                cards.add(b);
            }
        }
        return cards;
    }

    //Quí 2 : từ tháng 4 -> hết tháng 6
    //Tổng số sách đang mượn
    public int totalBorrowBookQuy2(int year) throws SQLException {
        int sum = 0;
        try (Connection conn = Utils.getConn()) {
            String sql = "SELECT Count(DISTINCT sach_idSach1) AS 'TongSL' \n"
                    + "FROM phieumuon \n"
                    + "WHERE tinhtrang != 1 AND Year(ngaymuon) = ? AND ngaymuon between '" + year + "-04-01' and '" + year + "-06-30'";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1, year);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                sum = rs.getInt("TongSL");
            }

            return sum;
        }
    }

    //Tổng số phiếu mượn
    public int totalBorrowCardQuy2(int year) throws SQLException {
        try (Connection conn = Utils.getConn()) {
            int sum = 0;
            String sql = "SELECT COUNT(idphieumuon) AS 'TongSL' FROM phieumuon WHERE Year(ngaymuon) = ? \n"
                    + "AND ngaymuon between '" + year + "-04-01' and '" + year + "-06-30'";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1, year);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                sum = rs.getInt("TongSL");
            }

            return sum;
        }
    }

    //Tổng số phiếu mượn quá hạn
    public int totalBorrowCardQuaHanQuy2(int year) throws SQLException {
        try (Connection conn = Utils.getConn()) {
            int sum = 0;
            String sql = "SELECT COUNT(idphieumuon) AS 'TongSL' FROM phieumuon WHERE tinhtrang = 0 And Year(ngaymuon) = ?\n"
                    + "AND ngaymuon between '" + year + "-04-01' and '" + year + "-06-30'";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1, year);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                sum = rs.getInt("TongSL");
            }

            return sum;
        }
    }

    //Số lượng sách đã trả
    public int totalBookBackTheoQuy2(int year) throws SQLException {
        try (Connection conn = Utils.getConn()) {
            int sum = 0;

            String sql = "SELECT COUNT(idphieumuon) AS 'TongSL' FROM phieumuon WHERE tinhtrang = 1 \n"
                    + "AND ngaymuon between '" + year + "-04-01' and '" + year + "-06-30'";
            PreparedStatement stm = conn.prepareCall(sql);
            //stm.setInt(1, year);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                sum = rs.getInt("TongSL");
            }

            return sum;
        }
    }

    //Số lượng sách chưa trả
    public int totalBookNotBackYetTheoQuy2(int year) throws SQLException {
        try (Connection conn = Utils.getConn()) {
            int sum = 0;

            String sql = "SELECT COUNT(idphieumuon) AS 'TongSL' FROM phieumuon WHERE tinhtrang != 1 \n"
                    + "AND ngaymuon between '" + year + "-04-01' and '" + year + "-06-30'";
            PreparedStatement stm = conn.prepareCall(sql);
            //stm.setInt(1, year);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                sum = rs.getInt("TongSL");
            }

            return sum;
        }
    }

    //Tên sách chưa trả + Tên người mượn
    public List<BorrowCardResponse> getInfoTheoQuy2(int year) throws SQLException {
        List<BorrowCardResponse> cards = new ArrayList<>();
        try (Connection conn = Utils.getConn()) {
            String sql = "SELECT s.Ten, concat(d.HoLot,  \" \" , d.Ten) as \"TenDocGia\", p.tinhtrang, p.idphieumuon, p.docgia_id, p.sach_idSach1, p.ngaymuon \n"
                    + "FROM ktpm_btl.phieumuon p, ktpm_btl.docgia d, ktpm_btl.sach s, ktpm_btl.sach_copies c \n"
                    + "WHERE p.tinhtrang != 1 AND p.sach_idSach1 = c.idsach_copies \n"
                    + "AND s.idSach = c.idDauSach AND p.docgia_id = d.id \n"
                    + "AND p.ngaymuon between '" + year + "-04-01' and '" + year + "-06-30'";

            PreparedStatement stm = conn.prepareCall(sql);
            //stm.setInt(1, year);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                BorrowCardResponse b = new BorrowCardResponse(rs.getString("idphieumuon"),
                        rs.getInt("sach_idSach1"),
                        rs.getNString("Ten"),
                        rs.getInt("tinhtrang"),
                        rs.getDate("ngaymuon").toLocalDate(),
                        rs.getNString("TenDocGia"),
                        rs.getInt("docgia_id"));
                cards.add(b);
            }
        }
        return cards;
    }

    //Quí 3 : từ tháng 7 -> hết tháng 9
    //Tổng số sách đang mượn
    public int totalBorrowBookQuy3(int year) throws SQLException {
        int sum = 0;
        try (Connection conn = Utils.getConn()) {
            String sql = "SELECT Count(DISTINCT sach_idSach1) AS 'TongSL' \n"
                    + "FROM phieumuon \n"
                    + "WHERE tinhtrang != 1 AND Year(ngaymuon) = ? AND ngaymuon between '" + year + "-07-01' and '" + year + "-09-30'";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1, year);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                sum = rs.getInt("TongSL");
            }

            return sum;
        }
    }

    //Tổng số phiếu mượn
    public int totalBorrowCardQuy3(int year) throws SQLException {
        try (Connection conn = Utils.getConn()) {
            int sum = 0;
            String sql = "SELECT COUNT(idphieumuon) AS 'TongSL' FROM phieumuon WHERE Year(ngaymuon) = ? \n"
                    + "AND ngaymuon between '" + year + "-07-01' and '" + year + "-09-30'";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1, year);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                sum = rs.getInt("TongSL");
            }

            return sum;
        }
    }

    //Tổng số phiếu mượn quá hạn
    public int totalBorrowCardQuaHanQuy3(int year) throws SQLException {
        try (Connection conn = Utils.getConn()) {
            int sum = 0;
            String sql = "SELECT COUNT(idphieumuon) AS 'TongSL' FROM phieumuon WHERE tinhtrang = 0 And Year(ngaymuon) = ?\n"
                    + "AND ngaymuon between '" + year + "-07-01' and '" + year + "-09-30'";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1, year);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                sum = rs.getInt("TongSL");
            }

            return sum;
        }
    }

    //Số lượng sách đã trả
    public int totalBookBackTheoQuy3(int year) throws SQLException {
        try (Connection conn = Utils.getConn()) {
            int sum = 0;

            String sql = "SELECT COUNT(idphieumuon) AS 'TongSL' FROM phieumuon WHERE tinhtrang = 1 \n"
                    + "AND ngaymuon between '" + year + "-07-01' and '" + year + "-09-30'";
            PreparedStatement stm = conn.prepareCall(sql);
            //stm.setInt(1, year);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                sum = rs.getInt("TongSL");
            }

            return sum;
        }
    }

    //Số lượng sách chưa trả
    public int totalBookNotBackYetTheoQuy3(int year) throws SQLException {
        try (Connection conn = Utils.getConn()) {
            int sum = 0;

            String sql = "SELECT COUNT(idphieumuon) AS 'TongSL' FROM phieumuon WHERE tinhtrang != 1 \n"
                    + "AND ngaymuon between '" + year + "-07-01' and '" + year + "-09-30'";
            PreparedStatement stm = conn.prepareCall(sql);
            //stm.setInt(1, year);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                sum = rs.getInt("TongSL");
            }

            return sum;
        }
    }

    //Tên sách chưa trả + Tên người mượn
    public List<BorrowCardResponse> getInfoTheoQuy3(int year) throws SQLException {
        List<BorrowCardResponse> cards = new ArrayList<>();
        try (Connection conn = Utils.getConn()) {
            String sql = "SELECT s.Ten, concat(d.HoLot,  \" \" , d.Ten) as \"TenDocGia\", p.tinhtrang, p.idphieumuon, p.docgia_id, p.sach_idSach1, p.ngaymuon \n"
                    + "FROM ktpm_btl.phieumuon p, ktpm_btl.docgia d, ktpm_btl.sach s, ktpm_btl.sach_copies c \n"
                    + "WHERE p.tinhtrang != 1 AND p.sach_idSach1 = c.idsach_copies \n"
                    + "AND s.idSach = c.idDauSach AND p.docgia_id = d.id \n"
                    + "AND p.ngaymuon between '" + year + "-07-01' and '" + year + "-09-30'";

            PreparedStatement stm = conn.prepareCall(sql);
            //stm.setInt(1, year);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                BorrowCardResponse b = new BorrowCardResponse(rs.getString("idphieumuon"),
                        rs.getInt("sach_idSach1"),
                        rs.getNString("Ten"),
                        rs.getInt("tinhtrang"),
                        rs.getDate("ngaymuon").toLocalDate(),
                        rs.getNString("TenDocGia"),
                        rs.getInt("docgia_id"));
                cards.add(b);
            }
        }
        return cards;
    }

    //Quí 4 : từ tháng 10 -> hết tháng 12
    //Tổng số sách đang mượn
    public int totalBorrowBookQuy4(int year) throws SQLException {
        int sum = 0;
        try (Connection conn = Utils.getConn()) {
            String sql = "SELECT Count(DISTINCT sach_idSach1) AS 'TongSL' \n"
                    + "FROM phieumuon \n"
                    + "WHERE tinhtrang != 1 AND Year(ngaymuon) = ? AND ngaymuon between '" + year + "-10-01' and '" + year + "-12-31'";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1, year);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                sum = rs.getInt("TongSL");
            }

            return sum;
        }
    }

    //Tổng số phiếu mượn
    public int totalBorrowCardQuy4(int year) throws SQLException {
        try (Connection conn = Utils.getConn()) {
            int sum = 0;
            String sql = "SELECT COUNT(idphieumuon) AS 'TongSL' FROM phieumuon WHERE Year(ngaymuon) = ? \n"
                    + "AND ngaymuon between '" + year + "-10-01' and '" + year + "-12-31'";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1, year);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                sum = rs.getInt("TongSL");
            }

            return sum;
        }
    }

    //Tổng số phiếu mượn quá hạn
    public int totalBorrowCardQuaHanQuy4(int year) throws SQLException {
        try (Connection conn = Utils.getConn()) {
            int sum = 0;
            String sql = "SELECT COUNT(idphieumuon) AS 'TongSL' FROM phieumuon WHERE tinhtrang = 0 And Year(ngaymuon) = ?\n"
                    + "AND ngaymuon between '" + year + "-10-01' and '" + year + "-12-31'";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setInt(1, year);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                sum = rs.getInt("TongSL");
            }

            return sum;
        }
    }

    //Số lượng sách đã trả
    public int totalBookBackTheoQuy4(int year) throws SQLException {
        try (Connection conn = Utils.getConn()) {
            int sum = 0;

            String sql = "SELECT COUNT(idphieumuon) AS 'TongSL' FROM phieumuon WHERE tinhtrang = 1 \n"
                    + "AND ngaymuon between '" + year + "-10-01' and '" + year + "-12-31'";
            PreparedStatement stm = conn.prepareCall(sql);
            //stm.setInt(1, year);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                sum = rs.getInt("TongSL");
            }

            return sum;
        }
    }

    //Số lượng sách chưa trả
    public int totalBookNotBackYetTheoQuy4(int year) throws SQLException {
        try (Connection conn = Utils.getConn()) {
            int sum = 0;

            String sql = "SELECT COUNT(idphieumuon) AS 'TongSL' FROM phieumuon WHERE tinhtrang != 1 \n"
                    + "AND ngaymuon between '" + year + "-10-01' and '" + year + "-12-31'";
            PreparedStatement stm = conn.prepareCall(sql);
            //stm.setInt(1, year);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                sum = rs.getInt("TongSL");
            }

            return sum;
        }
    }

    //Tên sách chưa trả + Tên người mượn
    public List<BorrowCardResponse> getInfoTheoQuy4(int year) throws SQLException {
        List<BorrowCardResponse> cards = new ArrayList<>();
        try (Connection conn = Utils.getConn()) {
            String sql = "SELECT s.Ten, concat(d.HoLot,  \" \" , d.Ten) as \"TenDocGia\", p.tinhtrang, p.idphieumuon, p.docgia_id, p.sach_idSach1, p.ngaymuon \n"
                    + "FROM ktpm_btl.phieumuon p, ktpm_btl.docgia d, ktpm_btl.sach s, ktpm_btl.sach_copies c \n"
                    + "WHERE p.tinhtrang != 1 AND p.sach_idSach1 = c.idsach_copies \n"
                    + "AND s.idSach = c.idDauSach AND p.docgia_id = d.id \n"
                    + "AND p.ngaymuon between '" + year + "-10-01' and '" + year + "-12-31'";

            PreparedStatement stm = conn.prepareCall(sql);
            //stm.setInt(1, year);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                BorrowCardResponse b = new BorrowCardResponse(rs.getString("idphieumuon"),
                        rs.getInt("sach_idSach1"),
                        rs.getNString("Ten"),
                        rs.getInt("tinhtrang"),
                        rs.getDate("ngaymuon").toLocalDate(),
                        rs.getNString("TenDocGia"),
                        rs.getInt("docgia_id"));
                cards.add(b);
            }
        }
        return cards;
    }
}
