
import com.nhom2.library.Utils;
import com.nhom2.pojo.User;
import com.nhom2.service.LoginService;
import com.nhom2.service.UserProfileService;
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
public class UserProfileTest {

    private static Connection conn;
    private static UserProfileService s;

    @BeforeAll
    public static void beforeAll() throws ClassNotFoundException {
        try {
            conn = Utils.getConn();
        } catch (SQLException ex) {
            Logger.getLogger(LoginTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        s = new UserProfileService();
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
    public void testUpdateAddnewInfo() {
        try {
            boolean Assert = s.updateInfo(2, "toi@gmail.com", "NK", "090909");
            Assertions.assertTrue(Assert);
            LoginService s1 = new LoginService();
            try {
                User u = s1.setUser("DG002");
                Assertions.assertEquals("toi@gmail.com", u.getEmail());  
                Assertions.assertEquals("NK", u.getDiaChi());  
                Assertions.assertEquals("090909", u.getSDT());  
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(UserProfileTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(UserProfileTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testUpdateInfo() {
        try {
            boolean Assert = s.updateInfo(2, "2051052140@gmail.com", "Nhà Bè", "090909");
            Assertions.assertTrue(Assert);
            LoginService s1 = new LoginService();
            try {
                User u = s1.setUser("DG002");
                Assertions.assertEquals("2051052140@gmail.com", u.getEmail());  
                Assertions.assertEquals("Nhà Bè", u.getDiaChi());  
                Assertions.assertEquals("090909", u.getSDT());  
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(UserProfileTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(UserProfileTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testUpdateDeleteInfo() {
         try {
            boolean Assert = s.updateInfo(2, "", "", "");
            Assertions.assertTrue(Assert);
            LoginService s1 = new LoginService();
            try {
                User u = s1.setUser("DG002");
                Assertions.assertEquals("", u.getEmail());  
                Assertions.assertEquals("", u.getDiaChi());  
                Assertions.assertEquals("", u.getSDT());  
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(UserProfileTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(UserProfileTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
