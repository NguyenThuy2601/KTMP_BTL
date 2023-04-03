
import com.nhom2.library.Utils;
import com.nhom2.pojo.BorrowCardResponse;
import com.nhom2.service.BorrowCardViewService;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
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
public class BorrowCardViewServiceTest {
    private static Connection conn;
    private static BorrowCardViewService s;

    @BeforeAll
    public static void beforeAll() throws ClassNotFoundException {
        try {
            conn = Utils.getConn();
        } catch (SQLException ex) {
            Logger.getLogger(LoginTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        s = new BorrowCardViewService();
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
    public void getBorrowCardFromUserWhoHaveAleastOne()
    {
        try {
            List<BorrowCardResponse> list = s.getBorrowCard(1);
            Assertions.assertTrue(!list.isEmpty());
        } catch (SQLException ex) {
            Logger.getLogger(BorrowCardViewServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void getBorrowCardFromUserWhoHaveNone()
    {
        try {
            List<BorrowCardResponse> list = s.getBorrowCard(4);
            Assertions.assertTrue(list.isEmpty());
        } catch (SQLException ex) {
            Logger.getLogger(BorrowCardViewServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
