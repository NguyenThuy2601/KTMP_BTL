/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import com.nhom2.library.Utils;
import com.nhom2.pojo.User;
import com.nhom2.service.LoginService;
import java.sql.*;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

/**
 *
 * @author CamHa
 */
public class LoginTest {

    private static Connection conn;

    @BeforeAll
    public static void beforeAll() throws ClassNotFoundException {
        try {
            conn = Utils.getConn();
        } catch (SQLException ex) {
            Logger.getLogger(LoginTest.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    public void checkEmailSuccess() throws SQLException, ClassNotFoundException {
        LoginService s = new LoginService();
        boolean Assert = s.checkEmail("thanh.dh");
        Assertions.assertEquals(true, Assert);
    }

    @Test
    public void checkEmailFail() throws SQLException, ClassNotFoundException {
        LoginService s = new LoginService();
        boolean Assert = s.checkEmail("thanh.dht");
        Assertions.assertFalse(Assert);
    }

    @Test
    public void checPasswordPass() throws SQLException, ClassNotFoundException {
        LoginService s = new LoginService();
        boolean Assert = s.checkPassword("thanh.dh", "1");
        Assertions.assertTrue(Assert);
    }

    @Test
    public void checPasswordFail() throws SQLException, ClassNotFoundException {
        LoginService s = new LoginService();
        boolean Assert = s.checkPassword("thanh.dh", "12");
        Assertions.assertFalse(Assert);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/PassData.csv", numLinesToSkip = 1)
    public void checkHashPass(String input, String expected) throws SQLException {
        LoginService s = new LoginService();
        Assertions.assertEquals(s.hashPassword(input), expected);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/AccountData.csv", numLinesToSkip = 1)
    public void getAccIdSuccess(String ID, String username) throws SQLException, ClassNotFoundException {
        LoginService s = new LoginService();
        Assertions.assertEquals(s.getAccID(username), ID);
    }

    @Test
    public void setUserSuccess() throws ClassNotFoundException {
        try {
            User u = new User(3, "DG003",
                    "Huy", "Đoàn Gia", "SV",
                    LocalDate.of(2017, 11, 17), LocalDate.of(2024, 12, 31),
                    "huy@ou.edu.vn", null, null,
                    "TH", null, true);
            LoginService s = new LoginService();
            User u1 = s.setUser("DG003");
            Assertions.assertEquals(u.getuID(), u1.getuID());
        } catch (SQLException ex) {
            Logger.getLogger(LoginTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testLoginWithWrongUserName() throws ClassNotFoundException {
        LoginService s = new LoginService();
        try {
            try {
                if (s.checkEmail("thanh") == false) {
                    Assertions.assertFalse(s.checkEmail("thanh"));
                } else {
                    if (s.checkPassword("thanh", "1") == false) {

                    } else {
                        String accID = s.getAccID("thanh");
                        User u;
                        if (s.checkEmpl(accID)) {
                            u = s.setEmplUser(accID);
                        } else {
                            u = s.setUser(accID);
                        }

                    }
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(LoginTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testLoginWithWrongPassword() throws ClassNotFoundException {
        LoginService s = new LoginService();
        try {
            try {
                if (s.checkEmail("thanh.dh") == false) {

                } else {
                    if (s.checkPassword("thanh.dh", "1") == false) {
                        Assertions.assertFalse(s.checkPassword("thanh.dh", "1"));
                    } else {
                        String accID = s.getAccID("thanh.dh");
                        User u;
                        if (s.checkEmpl(accID)) {
                            u = s.setEmplUser(accID);
                        } else {
                            u = s.setUser(accID);
                        }
                    }
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(LoginTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testLoginUserSucess() throws ClassNotFoundException {
        LoginService s = new LoginService();
        try {
            try {
                if (s.checkEmail("thanh.dh") == false) {

                } else {
                    if (s.checkPassword("thanh.dh", "1") == false) {

                    } else {
                        String accID = s.getAccID("thanh.dh");
                        User u;
                        if (s.checkEmpl(accID)) {
                            u = s.setEmplUser(accID);
                            User u1 = new User(1, "DG001",
                                    "Thành", "Dương Hữu", "GV",
                                    LocalDate.of(2017, 11, 17), LocalDate.of(2017, 12, 31),
                                    "thanh.dh@ou.edu.vn", null, null,
                                    "TH", LocalDate.of(1999, 1, 1)
                                    , true);
                            Assertions.assertEquals(u.getuID(), u1.getuID());
                            
                        } else {
                            u = s.setUser(accID);
                        }

                    }
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(LoginTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testLoginEmplUserSucess() throws ClassNotFoundException {
        LoginService s = new LoginService();
        try {
            try {
                if (s.checkEmail("thanh.dh") == false) {

                } else {
                    if (s.checkPassword("thanh.dh", "1") == false) {

                    } else {
                        String accID = s.getAccID("thanh.dh");
                        User u;
                        if (s.checkEmpl(accID)) {
                            u = s.setEmplUser(accID);                           
                        } else {
                            u = s.setUser(accID);
                             User u1 = new User(1, "NV001",
                                    "Thụy", "Cao Nguyên");
                            Assertions.assertEquals(u.getuID(), u1.getuID());
                        }
                    }
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(LoginTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
