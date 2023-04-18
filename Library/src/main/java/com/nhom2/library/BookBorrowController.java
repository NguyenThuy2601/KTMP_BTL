/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.library;

import com.nhom2.AppUtils.MessageBox;
import com.nhom2.pojo.BookResponse;
import com.nhom2.pojo.BorrowCardResponse;
import com.nhom2.pojo.PhieuMuon;
import com.nhom2.pojo.User;
import com.nhom2.service.BorrowBookService;
import com.nhom2.service.CheckEXPService;
import com.nhom2.service.CheckNumBorrowBooksService;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author ADMIN
 */
public class BookBorrowController implements Initializable {

    @FXML
    private TextField txtMaDG;
    @FXML
    private TextField txtMaSach1;
    @FXML
    private TextField txtMaSach2;
    @FXML
    private TextField txtMaSach3;
    @FXML
    private TextField txtMaSach4;
    @FXML
    private TextField txtMaSach5;
    @FXML
    private Button btXacNhan;

    User u;
    int preUID;
    BorrowBookService s;
    CheckEXPService cEXP;
    CheckNumBorrowBooksService cNum;

    List<Integer> ids = new ArrayList<>();
    List<Integer> bookID = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        s = new BorrowBookService();
        cEXP = new CheckEXPService();
        cNum = new CheckNumBorrowBooksService();
        preUID = 0;

        try (Connection conn = Utils.getConn()) {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT id FROM docgia");
            while (rs.next()) {
                int id = rs.getInt("id");
                ids.add(id);
            }

            ResultSet rs1 = stm.executeQuery("SELECT idsach_copies FROM sach_copies");
            while (rs1.next()) {
                int id = rs1.getInt("idsach_copies");
                bookID.add(id);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BookBorrowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setLoginUser(User uLogin) {
        this.u = uLogin;
    }

    @FXML
    public void backBtnlick(ActionEvent evt) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("primary.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            PrimaryController c = fxmlLoader.getController();
            c.setLoginUser(u);
            c.authorization();
            Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e) {
            return;
        }
    }

    @FXML
    public void confirmClick(ActionEvent event) throws SQLException {
        List<String> checks = new ArrayList<>();

        if (txtMaDG.getText().isBlank()) {
            MessageBox.getBox("Thông báo", "Vui lòng nhập vào mã độc giả", Alert.AlertType.INFORMATION).show();
        } else {
            if (!txtMaDG.getText().matches("\\d+")) {
                MessageBox.getBox("Thông báo", "Vui lòng nhập vào một số ở ô mã độc giả!", Alert.AlertType.INFORMATION).show();
                txtMaDG.clear();
            } else {
                try {
                    boolean flag = false;

                    for (Integer i : ids) {
                        if (Integer.parseInt(this.txtMaDG.getText()) == i) {
                            flag = true;
                            break;
                        }
                    }

                    if (flag) {
                        boolean f = false; //Check hạn thẻ độc giả
                        boolean f1 = false; //Check sách đã trả hết chưa?      
                        boolean f2 = false; //Check số lượng mượn max 5?

                        List<PhieuMuon> p = new ArrayList<>();

                        if (txtMaSach1.getText().isBlank() && txtMaSach2.getText().isBlank() && txtMaSach3.getText().isBlank()
                                && txtMaSach4.getText().isBlank() && txtMaSach5.getText().isBlank()) {
                            MessageBox.getBox("Thông báo", "Vui lòng nhập vào mã sách cần mượn", Alert.AlertType.INFORMATION).show();
                        } else {
                            List<String> text = new ArrayList<>();
                            text.add(txtMaSach1.getText());
                            text.add(txtMaSach2.getText());
                            text.add(txtMaSach3.getText());
                            text.add(txtMaSach4.getText());
                            text.add(txtMaSach5.getText());

                            boolean checkInputNum = true;
                            for (String i : text) {
                                if (!i.isBlank()) {
                                    if (!i.matches("\\d+")) {
                                        checkInputNum = false;
                                        MessageBox.getBox("Thông báo", "Vui lòng nhập số ở các ô mã sách mượn!", Alert.AlertType.INFORMATION).show();
                                        break;
                                    }
                                }
                            }
                            if (checkInputNum) {
                                String c1 = "";
                                //Check hạn thẻ độc giả
                                if (!cEXP.checkEXP(Integer.parseInt(this.txtMaDG.getText()))) {
                                    c1 += "Thẻ độc giả đã hết hạn\n";
                                } else {
                                    f = true; //Thẻ còn hạn
                                }

                                //Check sách đã trả hết chưa?
                                if (!cNum.checkNumBorrowBooks(Integer.parseInt(this.txtMaDG.getText()))) {
                                    c1 += "Độc giả chưa trả hết sách đang mượn\n";
                                } else {
                                    f1 = true; //Trả hết sách rồi
                                }

                                //Check số lượng mượn max5
                                if (!cNum.checkMaxBorrowBooks(Integer.parseInt(txtMaDG.getText()))) {
                                    checks.add("Độc giả đã mượn đủ 5 cuốn!\n");
                                } else {
                                    f2 = true; //chưa đủ 5
                                }

                                if (f && f1 && f2) {

                                    //tạo mảng check lỗi num > 5cuon
                                    List<Integer> checkE = new ArrayList<>(); //1:thành công; 0:lỗi                            

                                    int count = 0;

                                    for (String i : text) {
                                        boolean checkIDBook = false;
                                        boolean flagB = true; //Check sách có được đặt chưa?
                                        boolean flagB1 = true; //Check sách có đang được mượn ko?
                                        if (!i.isBlank()) {
                                            for (int j : bookID) {
                                                if (Integer.parseInt(i) == j) {
                                                    checkIDBook = true;
                                                    break;
                                                }
                                            }
                                            if (!checkIDBook) {
                                                checks.add("Không tìm thấy mã sách " + i + "\n");
                                            }

                                            //Check sách có được đặt chưa?
                                            for (int j : s.checkAvailableBook()) {
                                                if (Integer.parseInt(i) == j) {
                                                    flagB = false;
                                                    break;
                                                }
                                            }
                                            if (!flagB) {
                                                checks.add("Mã sách " + i + " đã được đặt không thể mượn" + "\n");
                                            }

                                            //Check sách có đang được mượn không
                                            for (int j : s.checkNotAvailableBook()) {
                                                if (Integer.parseInt(i) == j) {
                                                    flagB1 = false;
                                                    break;
                                                }
                                            }
                                            if (!flagB1) {
                                                checks.add("Mã sách " + i + " đang được mượn!" + "\n");
                                            }

                                            //Thỏa hết điều kiện -> Tạo phiếu mượn
                                            if (checkIDBook && flagB && flagB1) {
                                                PhieuMuon pm = new PhieuMuon(Integer.parseInt(i), LocalDate.now(), Integer.parseInt(this.txtMaDG.getText()));
                                                p.add(pm);
                                                checkE.add(1);
                                                count += 1;
                                            } else {
                                                checkE.add(0);
                                            }
                                        }
                                    }

                                    int soSachCoTheMuon = cNum.countBorrowBooks(Integer.parseInt(this.txtMaDG.getText()));
                                    if (count > soSachCoTheMuon) {
                                        Alert a = new Alert(Alert.AlertType.INFORMATION);
                                        a.setTitle("Thông báo");
                                        a.setHeaderText("Chỉ mượn được thêm " + soSachCoTheMuon + " cuốn!");
                                        a.showAndWait().ifPresent((ButtonType res) -> {
                                            if (res == ButtonType.OK) {
                                                txtMaSach1.clear();
                                                txtMaSach2.clear();
                                                txtMaSach3.clear();
                                                txtMaSach4.clear();
                                                txtMaSach5.clear();
                                                p.clear();
                                                checks.clear();
                                                checkE.clear();
                                            }
                                        });
                                    } else {
                                        if (!p.isEmpty()) {
                                            boolean cE = false; //kiểm tra có lỗi ko
                                            for (int i : checkE) {
                                                if (i == 0) {
                                                    cE = true;//có lỗi
                                                }
                                                break;
                                            }
                                            if (cE) {
                                                Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                                                a.setTitle("Thông báo");
                                                a.setHeaderText("Có sách không đủ điều kiện mượn, bạn có muốn tiếp tục mượn sách đủ điều kiện?");
                                                //Thông báo chi tiết sách không đủ điều kiện
                                                // Constructing HashSet using listWithDuplicateElements
                                                Set<String> set = new HashSet<String>(checks);
                                                // Constructing listWithoutDuplicateElements using set
                                                List<String> listC = new ArrayList<String>(set);
                                                String i = "";
                                                for (String item : listC) {
                                                    i += item;
                                                }
                                                a.setContentText(i);
                                                a.showAndWait().ifPresent((ButtonType res) -> {
                                                    if (res == ButtonType.OK) {
                                                        for (PhieuMuon item : p) {
                                                            try {
                                                                if (s.addBorrowCard(item)) {
                                                                    txtMaSach1.clear();
                                                                    txtMaSach2.clear();
                                                                    txtMaSach3.clear();
                                                                    txtMaSach4.clear();
                                                                    txtMaSach5.clear();
                                                                    //txtMaDG.clear();
                                                                } else {
                                                                    MessageBox.getBox("Thông báo", "Mượn không thành công", Alert.AlertType.INFORMATION).show();
                                                                }
                                                            } catch (SQLException ex) {
                                                                Logger.getLogger(BookBorrowController.class.getName()).log(Level.SEVERE, null, ex);
                                                            }
                                                        }

                                                        try {
                                                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("thongtinphieu(sauxn).fxml"));
                                                            Parent root1 = (Parent) fxmlLoader.load();
                                                            ThongTinPMController c = fxmlLoader.getController();
                                                            c.setLoginUser(u);
                                                            List<String> ids = new ArrayList<>();
                                                            for (PhieuMuon j : p) {
                                                                //c.setMaPM(i.getIdPhieuMuon());
                                                                ids.add(j.getIdPhieuMuon());
                                                            }
                                                            c.setIDs(ids);
                                                            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                                            stage.setScene(new Scene(root1));
                                                            stage.show();
                                                        } catch (Exception e) {
                                                            System.out.print(e.getMessage());
                                                        }

                                                    } else if (res == ButtonType.CANCEL) {
                                                        txtMaSach1.clear();
                                                        txtMaSach2.clear();
                                                        txtMaSach3.clear();
                                                        txtMaSach4.clear();
                                                        txtMaSach5.clear();
                                                        //txtMaDG.clear();
                                                        checks.clear();
                                                        p.clear();
                                                    }
                                                });
                                            } else {
                                                Alert a = new Alert(Alert.AlertType.INFORMATION);
                                                a.setTitle("Thông báo");
                                                a.setHeaderText("Mượn sách thành công!");
                                                a.showAndWait().ifPresent((ButtonType res) -> {
                                                    if (res == ButtonType.OK) {
                                                        for (PhieuMuon item : p) {
                                                            try {
                                                                if (s.addBorrowCard(item)) {
                                                                    txtMaSach1.clear();
                                                                    txtMaSach2.clear();
                                                                    txtMaSach3.clear();
                                                                    txtMaSach4.clear();
                                                                    txtMaSach5.clear();
                                                                    //txtMaDG.clear();
                                                                } else {
                                                                    MessageBox.getBox("Thông báo", "Mượn không thành công", Alert.AlertType.INFORMATION).show();
                                                                }
                                                            } catch (SQLException ex) {
                                                                Logger.getLogger(BookBorrowController.class.getName()).log(Level.SEVERE, null, ex);
                                                            }
                                                        }

                                                        try {
                                                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("thongtinphieu(sauxn).fxml"));
                                                            Parent root1 = (Parent) fxmlLoader.load();
                                                            ThongTinPMController c = fxmlLoader.getController();
                                                            c.setLoginUser(u);
                                                            List<String> ids = new ArrayList<>();
                                                            for (PhieuMuon i : p) {
                                                                //c.setMaPM(i.getIdPhieuMuon());
                                                                ids.add(i.getIdPhieuMuon());
                                                            }
                                                            c.setIDs(ids);
                                                            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                                            stage.setScene(new Scene(root1));
                                                            stage.show();
                                                        } catch (Exception e) {
                                                            System.out.print(e.getMessage());
                                                        }
                                                    }
                                                });
                                            }
                                        } else {
                                            Alert a = new Alert(Alert.AlertType.INFORMATION);
                                            a.setTitle("Thông báo");
                                            a.setHeaderText("Không đủ điều kiện mượn!");

                                            // Constructing HashSet using listWithDuplicateElements
                                            Set<String> set = new HashSet<String>(checks);
                                            // Constructing listWithoutDuplicateElements using set
                                            List<String> listC = new ArrayList<String>(set);
                                            String i = "";
                                            for (String item : listC) {
                                                i += item;
                                            }
                                            a.setContentText(i);
                                            a.showAndWait().ifPresent((ButtonType res) -> {
                                                if (res == ButtonType.OK) {
                                                    txtMaSach1.clear();
                                                    txtMaSach2.clear();
                                                    txtMaSach3.clear();
                                                    txtMaSach4.clear();
                                                    txtMaSach5.clear();
                                                    //txtMaDG.clear();
                                                    checks.clear();
                                                }
                                            });
                                        }
                                    }
                                } else {
                                    MessageBox.getBox("Thông báo", c1, Alert.AlertType.INFORMATION).show();
                                }
                            }
                        }
                    } else {
                        MessageBox.getBox("Thông báo", "Không tìm thấy mã độc giả", Alert.AlertType.INFORMATION).show();
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(BookBorrowController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
