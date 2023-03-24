
import com.nhom2.library.Utils;
import com.nhom2.service.BookReturningService;
import com.nhom2.service.UserProfileService;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author CamHa
 */
public class BookReturnTest {
    private static Connection conn;
    private static BookReturningService s;

    @BeforeAll
    public static void beforeAll() throws ClassNotFoundException {
        try {
            conn = Utils.getConn();
        } catch (SQLException ex) {
            Logger.getLogger(LoginTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        s = new BookReturningService();
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
    
    @ParameterizedTest
    @CsvFileSource(resources = "/CalcFeeData.csv", numLinesToSkip = 1)
    public void checkCalcFee(String input, String expected) throws SQLException {
        Assertions.assertEquals(s.calcFee(Integer.parseInt(input)), Integer.parseInt(expected));
    }
}
