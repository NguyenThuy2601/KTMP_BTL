
import com.nhom2.library.Utils;
import com.nhom2.service.CheckService;
import java.sql.Connection;
import java.sql.SQLException;
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
    public void checkReservationCard() {
        try {
            boolean Assert = s.checkReservationCard();
            Assertions.assertTrue(Assert);
        } catch (SQLException ex) {
            Logger.getLogger(CheckTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Test 
    public void checkBorrowingCard() {
        try {
            boolean Assert = s.checkReservationCard();
            Assertions.assertTrue(Assert);
        } catch (SQLException ex) {
            Logger.getLogger(CheckTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
