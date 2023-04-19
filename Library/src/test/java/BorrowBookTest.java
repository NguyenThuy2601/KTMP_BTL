
import com.nhom2.library.Utils;
import com.nhom2.pojo.BorrowCardResponse;
import com.nhom2.pojo.PhieuMuon;
import com.nhom2.service.BorrowBookService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import static java.time.LocalDate.now;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author ADMIN
 */
public class BorrowBookTest {

    public static Connection conn;
    public static BorrowBookService s;

    @BeforeAll
    public static void beforeAll() throws SQLException {
        try {
            conn = Utils.getConn();
        } catch (SQLException ex) {
            Logger.getLogger(LoginTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        s = new BorrowBookService();
    }

    @AfterAll
    public static void afterAll() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(LoginTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Test
    public void testAddSuccessful() {
        PhieuMuon p = new PhieuMuon(LocalDate.now(), 8);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String id = p.getIdPhieuMuon();
        try {
            boolean actual = s.addBorrowCard(p);
            Assertions.assertTrue(actual);

            String sql = "SELECT * FROM phieumuon WHERE idphieumuon=?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, id);

            ResultSet rs = stm.executeQuery();
            PhieuMuon p1 = new PhieuMuon();
            while (rs.next()) {
                p1 = new PhieuMuon(rs.getString("idphieumuon"),
                        rs.getInt("sach_idSach1"), rs.getInt("tinhtrang"),
                        rs.getDate("ngaymuon").toLocalDate(), rs.getInt("docgia_id"));
            }
            Assertions.assertEquals(id, p1.getIdPhieuMuon());
            Assertions.assertEquals(p.getNgayMuon().format(fmt), p1.getNgayMuon().format(fmt));
            Assertions.assertEquals(p.getTinhTrang(), p1.getTinhTrang());
            Assertions.assertEquals(p.getIdDocGia(), p1.getIdDocGia());
            Assertions.assertEquals(p.getIdSach(), p1.getIdSach());
        } catch (SQLException ex) {
            Logger.getLogger(BorrowBookTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void getPM() {
        try {
            List<String> id = new ArrayList<>();
            id.add("a1");
            id.add("a2");
            List<BorrowCardResponse> list = s.getCards(id);
            Assertions.assertTrue(!list.isEmpty());
        } catch (SQLException ex) {
            Logger.getLogger(BorrowBookTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void checkSachDaDuocDat() {
        try {
            List<Integer> list = s.checkAvailableBook();
            Assertions.assertFalse(!list.isEmpty());
            System.out.println(list.get(0));
        } catch (SQLException ex) {
            Logger.getLogger(CheckNumBorrowBooksTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void checkSachDaDuocDat1() {
        try {
            List<Integer> list = s.checkAvailableBook();
            System.out.print(list.get(0));
            Assertions.assertTrue(!list.isEmpty());
        } catch (SQLException ex) {
            Logger.getLogger(CheckNumBorrowBooksTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void checkSachDangMuon() {
        try {
            List<Integer> list = s.checkNotAvailableBook();
            System.out.print(list.get(0));
            Assertions.assertTrue(!list.isEmpty());
        } catch (SQLException ex) {
            Logger.getLogger(CheckNumBorrowBooksTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
