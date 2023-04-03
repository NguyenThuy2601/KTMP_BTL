
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
    @CsvFileSource(resources = "/year.csv")
    public void checkYearFormat(String input, String expected){
        Assertions.assertEquals(expected, Boolean.toString(s.checkYearFormat(input)));
    }
    
    public void testParseYearNull(){       
        Assertions.assertEquals(0, s.parseYear(null));
    }
    
    public void testParseYearBlank(){       
        Assertions.assertEquals(0, s.parseYear(""));
    }

    public void testParseYearSuccess(){       
        Assertions.assertEquals(1999, s.parseYear("1999"));
        
    }
    
    public void testcheckNullSelectedComboBoxItemIsNull(){
        Assertions.assertEquals(0, s.checkNullSelectedComboBoxItem(null));
    }
    
    public void testcheckNullSelectedComboBoxItemIsNotNull(){
        DanhMuc c = new DanhMuc(1, "sách CNTT");
        Assertions.assertEquals(1, s.checkNullSelectedComboBoxItem(c));
    }
    
    @ParameterizedTest
    @CsvFileSource(resources = "/findData.csv", numLinesToSkip = 1, emptyValue = " ")
    public void testFindBook(String inputBname, String inputAname, String inputYear, String inputCateID, String expected) throws SQLException{
         List<BookResponse> list = s.getBooks();
         DanhMuc c = null;
         if(inputCateID.equals("null") == false)
             c = new DanhMuc(Integer.parseInt(inputCateID), "test case");            
         List<BookResponse> l = s.findBook(list, inputBname.equals("null")? null:inputBname,
                            inputAname.equals("null")? null:inputAname,
                            s.parseYear(inputYear.equals("null")? null:inputYear),
                            s.checkNullSelectedComboBoxItem(c));
         boolean flag = l.isEmpty();
         Assertions.assertEquals(expected, Boolean.toString(flag));
    }
   
}
