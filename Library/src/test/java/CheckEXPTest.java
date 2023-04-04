
import com.nhom2.library.Utils;
import com.nhom2.service.CheckEXPService;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author ADMIN
 */
public class CheckEXPTest {

    private static Connection conn;
    private static CheckEXPService s;

    @BeforeAll
    public static void beforeAll() throws SQLException {
        try {
            conn = Utils.getConn();
        } catch (SQLException ex) {
            Logger.getLogger(LoginTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        s = new CheckEXPService();
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
    public void checkTheDGConEXP() {
        //id = 8;
        //boolean expected = true;
        try {
            boolean actual = s.checkEXP(8);
            //Assertions.assertEquals(expected, actual);
            Assertions.assertTrue(actual);
        } catch (SQLException ex) {
            Logger.getLogger(CheckEXPTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void checkTheDGKhongConEXP() {
        boolean expected = false;
        try {
            boolean actual = s.checkEXP(1);
            Assertions.assertEquals(expected, actual);
            //Assertions.assertTrue(actual);
        } catch (SQLException ex) {
            Logger.getLogger(CheckEXPTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void checkTheDGKhongConEXP1() {        
        try {
            boolean actual = s.checkEXP(1);
            Assertions.assertTrue(actual);
        } catch (SQLException ex) {
            Logger.getLogger(CheckEXPTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
