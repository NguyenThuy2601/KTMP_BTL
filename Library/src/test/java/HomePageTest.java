
import com.nhom2.library.Utils;
import com.nhom2.pojo.BookResponse;
import com.nhom2.pojo.DanhMuc;
import com.nhom2.pojo.Sach;
import com.nhom2.pojo.Sach_TacGia;
import com.nhom2.service.HomepageService;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.List;
import org.junit.jupiter.api.Assertions;
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
public class HomePageTest {
    private static Connection conn;
    private static HomepageService s;
    @BeforeAll
    public static void beforeAll() throws ClassNotFoundException {
        try {
            conn = Utils.getConn();
        } catch (SQLException ex) {
            Logger.getLogger(LoginTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        s = new HomepageService();
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
    public void testGetDanhMucSach() {
        try {
            List<DanhMuc> list = s.getDanhMucSach();
            boolean Assert = list.isEmpty();
            Assertions.assertEquals(false, Assert);
        } catch (SQLException ex) {
            Logger.getLogger(HomePageTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testBooks() {
        try {
            List<BookResponse> list = s.getBooks();
            boolean Assert = list.isEmpty();
            System.out.print(list.get(0).getTen());
            Assertions.assertEquals(false, Assert);
        } catch (SQLException ex) {
            Logger.getLogger(HomePageTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @ParameterizedTest
    @CsvFileSource(resources = "/year.csv", numLinesToSkip = 1)
    public void checkYearFormat(){
        
    }
    

    
}
