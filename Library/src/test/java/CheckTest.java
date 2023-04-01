
import com.nhom2.library.Utils;
import com.nhom2.pojo.PhieuMuon;
import com.nhom2.service.BookReturningService;
import com.nhom2.service.CheckService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.*;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author CamHa
 */
public class CheckTest {
    private static Connection conn;
    private static CheckService s;
    @BeforeAll
    public static void beforeAll() throws ClassNotFoundException {
        try {
            conn = Utils.getConn();
        } catch (SQLException ex) {
            Logger.getLogger(LoginTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        s= new CheckService();
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
    public void checkReservationCardCommitSuccess() {
        try {
            boolean Assert = s.checkReservationCard();
            Assertions.assertTrue(Assert);
        } catch (SQLException ex) {
            Logger.getLogger(CheckTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    
     @Test 
    public void checkBorrowingCardCommitSuccess() {
        try {
            boolean Assert = s.checkBorrowingCard();
            Assertions.assertTrue(Assert);
        } catch (SQLException ex) {
            Logger.getLogger(CheckTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Test 
    public void checkUpdateBorrowCardWithCardID() {
        try {
            BookReturningService bs = new BookReturningService();
            String sql = "select * from phieumuon where idphieumuon = ?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, "a7");
            ResultSet r = stm.executeQuery();
            PhieuMuon p = null;
            while(r.next()){
                p = new PhieuMuon(r.getString("idphieumuon"),
                        r.getInt("sach_idSach1"),
                        r.getInt("tinhtrang"),
                        r.getDate("ngaymuon").toLocalDate(),
                        r.getInt("docgia_id"));
            }
            if(bs.calcDayGap(p.getNgayMuon()) > 30)
            {
                boolean Assert = s.updateBorrowingCardWithID("a7");
                Assertions.assertTrue(Assert);
            }
            else
            {
                boolean failSignal = true;
                Assertions.assertFalse(failSignal);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CheckTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Test 
    public void checkUpdateBorrowCardWithCardID2() {
        try {
            BookReturningService bs = new BookReturningService();
            String sql = "select * from phieumuon where idphieumuon = ?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, "a2");
            ResultSet r = stm.executeQuery();
            PhieuMuon p = null;
            while(r.next()){
                p = new PhieuMuon(r.getString("idphieumuon"),
                        r.getInt("sach_idSach1"),
                        r.getInt("tinhtrang"),
                        r.getDate("ngaymuon").toLocalDate(),
                        r.getInt("docgia_id"));
            }
            if(bs.calcDayGap(p.getNgayMuon()) > 30)
            {
                boolean Assert = s.updateBorrowingCardWithID("a2");
                boolean failSignal = true;
                Assertions.assertFalse(failSignal);
            }
            else
            {
                boolean passSignal = true;
                 Assertions.assertTrue(passSignal);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CheckTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
