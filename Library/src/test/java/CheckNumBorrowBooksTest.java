
import com.nhom2.library.Utils;
import com.nhom2.service.CheckNumBorrowBooksService;
import java.sql.Connection;
import java.sql.SQLException;
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
public class CheckNumBorrowBooksTest {
    private static Connection conn;
    private static CheckNumBorrowBooksService s;
    
    @BeforeAll
    public static void beforeAll() throws SQLException {
        try {
            conn = Utils.getConn();
        } catch (SQLException ex) {
            Logger.getLogger(LoginTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        s = new CheckNumBorrowBooksService();
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
    //Test sách mượn chưa trả hết
    public void checkSachNotSuccessful() {
        try {
            boolean Assert = s.checkNumBorrowBooks(8);
            Assertions.assertTrue(Assert);
        } catch (SQLException ex) {
            Logger.getLogger(CheckNumBorrowBooksTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    //Test sách mượn đã trả hết
    public void checkSachSuccessful() {
        try {
            boolean Assert = s.checkNumBorrowBooks(2);
            Assertions.assertTrue(Assert);
        } catch (SQLException ex) {
            Logger.getLogger(CheckNumBorrowBooksTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    //Test số lượng sách không quá 5
    public void checkSLSachSuccessful() {
        try {
            boolean Assert = s.checkMaxBorrowBooks(2);
            Assertions.assertTrue(Assert);
        } catch (SQLException ex) {
            Logger.getLogger(CheckNumBorrowBooksTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
