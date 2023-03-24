
import com.nhom2.library.Utils;
import com.nhom2.pojo.PhieuMuon;
import com.nhom2.service.ConfirmBorrowService;
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
public class ConfirmBorrowTest {
    private static Connection conn;
    private static ConfirmBorrowService s;
    
    @BeforeAll
    public static void beforeAll() throws SQLException {
        try {
            conn = Utils.getConn();
        } catch (SQLException ex) {
            Logger.getLogger(LoginTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        s = new ConfirmBorrowService();
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
    public void confirmBorrow() {
        String id = "4";
        try {
            boolean actual = s.confirmBorrow(id);
            Assertions.assertFalse(actual);
        } catch (SQLException ex) {
            Logger.getLogger(ConfirmBorrowTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
