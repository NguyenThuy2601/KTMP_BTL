
import com.nhom2.library.Utils;
import com.nhom2.pojo.BorrowCardResponse;
import com.nhom2.service.ThongKeService;
import java.sql.Connection;
import java.sql.SQLException;
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
public class ThongKeTest {

    public static Connection conn;
    public static ThongKeService s;

    @BeforeAll
    public static void beforeAll() throws SQLException {
        try {
            conn = Utils.getConn();
        } catch (SQLException ex) {
            Logger.getLogger(LoginTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        s = new ThongKeService();
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
    public void testGetTheoNamSuccessful() throws SQLException {
        try {            
            List<BorrowCardResponse> card = s.getInfo(2023);
            System.out.print(card.get(0).getTenSach());
            Assertions.assertTrue(!card.isEmpty());
        } catch (SQLException ex) {
            Logger.getLogger(ThongKeTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testGetTheoQuy1Successful() throws SQLException {
        try {            
            List<BorrowCardResponse> card = s.getInfoTheoQuy1(2023);
            System.out.print(card.get(0).getTenSach());
            Assertions.assertTrue(!card.isEmpty());
        } catch (SQLException ex) {
            Logger.getLogger(ThongKeTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testGetTheoQuy2Successful() throws SQLException {
        try {            
            List<BorrowCardResponse> card = s.getInfoTheoQuy2(2023);
            System.out.print(card.get(0).getTenSach());
            Assertions.assertTrue(!card.isEmpty());
        } catch (SQLException ex) {
            Logger.getLogger(ThongKeTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
