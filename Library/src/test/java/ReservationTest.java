/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import com.nhom2.library.Utils;
import com.nhom2.pojo.PhieuDat;
import com.nhom2.service.ReservationService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.*;
import java.sql.Date;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author CamHa
 */
public class ReservationTest {

    private static Connection conn;
    private static ReservationService s;

    @BeforeAll
    public static void beforeAll() throws ClassNotFoundException {
        try {
            conn = Utils.getConn();
        } catch (SQLException ex) {
            Logger.getLogger(LoginTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        s = new ReservationService();
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
    public void testCreateReservationCard() {

        try {
            PhieuDat p = new PhieuDat(1, LocalDateTime.now(), 1);
            DateTimeFormatter fmt3 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            
            String id = p.getIdPhieuDat();
            boolean Assert = s.createReservationCard(1, 1, p);
            Assertions.assertTrue(Assert);
            String sql = "select * from phieudat where idphieudat = ?";
            PreparedStatement stm = conn.prepareCall(sql);
            stm.setString(1, id);
            ResultSet rs = stm.executeQuery();
            PhieuDat p2 = new PhieuDat();
            while (rs.next()) {
                p2 = new PhieuDat(rs.getString("idphieudat"), rs.getInt("sach_idSach"),
                        rs.getInt("TinhTrang"), rs.getTimestamp("ngaydat").toLocalDateTime(), rs.getInt("docgia_id"));

            }
            Assertions.assertEquals(id, p2.getIdPhieuDat());
            Assertions.assertEquals(p.getIdDocGia(), p2.getIdDocGia());
            Assertions.assertEquals(p.getIdSach(), p2.getIdSach());
            Assertions.assertEquals(p.getTinhTrang(), p2.getTinhTrang());
            Assertions.assertEquals(p.getNgayDat().format(fmt3), p2.getNgayDat().format(fmt3));
        } catch (SQLException ex) {
            Logger.getLogger(ReservationTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
