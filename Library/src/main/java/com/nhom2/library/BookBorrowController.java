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
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
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
    public void confirmBorrowBookClick(ActionEvent event) throws SQLException {
        List<String> checks = new ArrayList<>();

        if (txtMaDG.getText().isBlank()) {
            MessageBox.getBox("Thông báo", "Vui lòng nhập vào mã độc giả", Alert.AlertType.INFORMATION).show();
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
                    //Check confirm thành công
                    boolean check1 = false;
                    boolean check2 = false;
                    boolean check3 = false;
                    boolean check4 = false;
                    boolean check5 = false;

                    boolean f = false; //Check hạn thẻ độc giả
                    boolean f1 = false; //Check sách đã trả hết chưa?      
                    boolean f2 = false; //Check số lượng mượn max 5?

                    List<PhieuMuon> p = new ArrayList<>();

                    if (txtMaSach1.getText().isBlank() && txtMaSach2.getText().isBlank() && txtMaSach3.getText().isBlank()
                            && txtMaSach4.getText().isBlank() && txtMaSach5.getText().isBlank()) {
                        MessageBox.getBox("Thông báo", "Vui lòng nhập vào mã sách cần mượn", Alert.AlertType.INFORMATION).show();
                    } else {
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

                        if (f && f1) {
                            if (!txtMaSach1.getText().isEmpty()) {
                                boolean checkIDBook = false;
                                boolean flagB = true; //Check sách có được đặt chưa?
                                boolean flagB1 = true; //Check sách có đang được mượn ko?
                                for (int i : bookID) {
                                    if (Integer.parseInt(this.txtMaSach1.getText()) == i) {
                                        checkIDBook = true;
                                        break;
                                    }
                                }
                                if (!checkIDBook) {
                                    checks.add("Không tìm thấy mã sách " + txtMaSach1.getText() + "\n");
                                }

                                //Check sách có được đặt chưa?
                                for (int i : s.checkAvailableBook()) {
                                    if (Integer.parseInt(this.txtMaSach1.getText()) == i) {
                                        flagB = false;
                                        break;
                                    }
                                }
                                if (!flagB) {
                                    checks.add("Mã sách " + txtMaSach1.getText() + " đã được đặt không thể mượn");
                                }

                                //Check sách có đang được mượn không
                                for (int i : s.checkNotAvailableBook()) {
                                    if (Integer.parseInt(this.txtMaSach1.getText()) == i) {
                                        flagB1 = false;
                                        break;
                                    }
                                }
                                if (!flagB1) {
                                    checks.add("Mã sách " + txtMaSach1.getText() + " đang được mượn!" + "\n");
                                }

                                //Check số lượng mượn max5
                                if (!cNum.checkMaxBorrowBooks(Integer.parseInt(this.txtMaDG.getText()))) {
                                    checks.add("Độc giả đã mượn đủ 5 cuốn!\n");
                                } else {
                                    f2 = true; //chưa đủ 5
                                }

                                //Thỏa hết điều kiện -> Tạo phiếu mượn
                                if (flagB && flagB1 && f2) {
                                    check1 = true;
                                    PhieuMuon p1 = new PhieuMuon(Integer.parseInt(this.txtMaSach1.getText()), LocalDate.now(), Integer.parseInt(this.txtMaDG.getText()));

                                    p.add(p1);

                                } else {
                                    check1 = false;
                                }
                            } else {
                                check1 = true;
                            }

                            if (!txtMaSach2.getText().isEmpty()) {
                                boolean checkIDBook = false;
                                boolean flagB = true; //Check sách có được đặt chưa?
                                boolean flagB1 = true; //Check sách có đang được mượn ko?
                                for (int i : bookID) {
                                    if (Integer.parseInt(this.txtMaSach2.getText()) == i) {
                                        checkIDBook = true;
                                        break;
                                    }
                                }
                                if (!checkIDBook) {
                                    checks.add("Không tìm thấy mã sách " + txtMaSach2.getText() + "\n");
                                }

                                //Check sách có được đặt chưa?
                                for (int i : s.checkAvailableBook()) {
                                    if (Integer.parseInt(this.txtMaSach2.getText()) == i) {
                                        flagB = false;
                                        break;
                                    }
                                }
                                if (!flagB) {
                                    checks.add("Mã sách " + txtMaSach2.getText() + " đã được đặt không thể mượn" + "\n");
                                }

                                //Check sách có đang được mượn không
                                for (int i : s.checkNotAvailableBook()) {
                                    if (Integer.parseInt(this.txtMaSach2.getText()) == i) {
                                        flagB1 = false;
                                        break;
                                    }
                                }
                                if (!flagB1) {
                                    checks.add("Mã sách " + txtMaSach2.getText() + " đang được mượn!" + "\n");
                                }

                                //Check số lượng mượn max5
                                if (!cNum.checkMaxBorrowBooks(Integer.parseInt(this.txtMaDG.getText()))) {
                                    checks.add("Độc giả đã mượn đủ 5 cuốn!\n");
                                } else {
                                    f2 = true; //chưa đủ 5
                                }

                                //Thỏa hết điều kiện -> Tạo phiếu mượn
                                if (flagB && flagB1 && f2) {
                                    check2 = true;
                                    PhieuMuon p2 = new PhieuMuon(Integer.parseInt(this.txtMaSach2.getText()), LocalDate.now(), Integer.parseInt(this.txtMaDG.getText()));

                                    p.add(p2);
                                } else {
                                    check2 = false;
                                }
                            } else {
                                check2 = true;
                            }

                            if (!txtMaSach3.getText().isEmpty()) {
                                boolean checkIDBook = false;
                                boolean flagB = true; //Check sách có được đặt chưa?
                                boolean flagB1 = true; //Check sách có đang được mượn ko?
                                for (int i : bookID) {
                                    if (Integer.parseInt(this.txtMaSach3.getText()) == i) {
                                        checkIDBook = true;
                                        break;
                                    }
                                }
                                if (!checkIDBook) {
                                    checks.add("Không tìm thấy mã sách " + txtMaSach3.getText() + "\n");
                                }

                                //Check sách có được đặt chưa?
                                for (int i : s.checkAvailableBook()) {
                                    if (Integer.parseInt(this.txtMaSach3.getText()) == i) {
                                        flagB = false;
                                        break;
                                    }
                                }
                                if (!flagB) {
                                    checks.add("Mã sách " + txtMaSach3.getText() + " đã được đặt không thể mượn" + "\n");
                                }

                                //Check sách có đang được mượn không
                                for (int i : s.checkNotAvailableBook()) {
                                    if (Integer.parseInt(this.txtMaSach3.getText()) == i) {
                                        flagB1 = false;
                                        break;
                                    }
                                }
                                if (!flagB1) {
                                    checks.add("Mã sách " + txtMaSach3.getText() + " đang được mượn!" + "\n");
                                }

                                //Check số lượng mượn max5
                                if (!cNum.checkMaxBorrowBooks(Integer.parseInt(this.txtMaDG.getText()))) {
                                    checks.add("Độc giả đã mượn đủ 5 cuốn!\n");
                                } else {
                                    f2 = true; //chưa đủ 5
                                }

                                //Thỏa hết điều kiện -> Tạo phiếu mượn
                                if (flagB && flagB1 && f2) {
                                    check3 = true;
                                    PhieuMuon p3 = new PhieuMuon(Integer.parseInt(this.txtMaSach3.getText()), LocalDate.now(), Integer.parseInt(this.txtMaDG.getText()));

                                    p.add(p3);
                                } else {
                                    check3 = false;
                                }
                            } else {
                                check3 = true;
                            }

                            if (!txtMaSach4.getText().isEmpty()) {
                                boolean checkIDBook = false;
                                boolean flagB = true; //Check sách có được đặt chưa?
                                boolean flagB1 = true; //Check sách có đang được mượn ko?
                                for (int i : bookID) {
                                    if (Integer.parseInt(this.txtMaSach4.getText()) == i) {
                                        checkIDBook = true;
                                        break;
                                    }
                                }
                                if (!checkIDBook) {
                                    checks.add("Không tìm thấy mã sách " + txtMaSach4.getText() + "\n");
                                }

                                //Check sách có được đặt chưa?
                                for (int i : s.checkAvailableBook()) {
                                    if (Integer.parseInt(this.txtMaSach4.getText()) == i) {
                                        flagB = false;
                                        break;
                                    }
                                }
                                if (!flagB) {
                                    checks.add("Mã sách " + txtMaSach4.getText() + " đã được đặt không thể mượn" + "\n");
                                }

                                //Check sách có đang được mượn không
                                for (int i : s.checkNotAvailableBook()) {
                                    if (Integer.parseInt(this.txtMaSach4.getText()) == i) {
                                        flagB1 = false;
                                        break;
                                    }
                                }
                                if (!flagB1) {
                                    checks.add("Mã sách " + txtMaSach4.getText() + " đang được mượn!" + "\n");
                                }

                                //Check số lượng mượn max5
                                if (!cNum.checkMaxBorrowBooks(Integer.parseInt(this.txtMaDG.getText()))) {
                                    checks.add("Độc giả đã mượn đủ 5 cuốn!\n");
                                } else {
                                    f2 = true; //chưa đủ 5
                                }

                                //Thỏa hết điều kiện -> Tạo phiếu mượn
                                if (flagB && flagB1 && f2) {
                                    check4 = true;
                                    PhieuMuon p4 = new PhieuMuon(Integer.parseInt(this.txtMaSach4.getText()), LocalDate.now(), Integer.parseInt(this.txtMaDG.getText()));

                                    p.add(p4);
                                } else {
                                    check4 = false;
                                }
                            } else {
                                check4 = true;
                            }

                            if (!txtMaSach5.getText().isEmpty()) {
                                boolean checkIDBook = false;
                                boolean flagB = true; //Check sách có được đặt chưa?
                                boolean flagB1 = true; //Check sách có đang được mượn ko?
                                for (int i : bookID) {
                                    if (Integer.parseInt(this.txtMaSach5.getText()) == i) {
                                        checkIDBook = true;
                                        break;
                                    }
                                }
                                if (!checkIDBook) {
                                    checks.add("Không tìm thấy mã sách " + txtMaSach5.getText() + "\n");
                                }

                                //Check sách có được đặt chưa?
                                for (int i : s.checkAvailableBook()) {
                                    if (Integer.parseInt(this.txtMaSach5.getText()) == i) {
                                        flagB = false;
                                        break;
                                    }
                                }
                                if (!flagB) {
                                    checks.add("Mã sách " + txtMaSach5.getText() + " đã được đặt không thể mượn" + "\n");
                                }

                                //Check sách có đang được mượn không
                                for (int i : s.checkNotAvailableBook()) {
                                    if (Integer.parseInt(this.txtMaSach5.getText()) == i) {
                                        flagB1 = false;
                                        break;
                                    }
                                }
                                if (!flagB1) {
                                    checks.add("Mã sách " + txtMaSach5.getText() + " đang được mượn!" + "\n");
                                }

                                //Check số lượng mượn max5
                                if (!cNum.checkMaxBorrowBooks(Integer.parseInt(this.txtMaDG.getText()))) {
                                    checks.add("Độc giả đã mượn đủ 5 cuốn!\n");
                                } else {
                                    f2 = true; //chưa đủ 5
                                }

                                //Thỏa hết điều kiện -> Tạo phiếu mượn
                                if (flagB && flagB1 && f2) {
                                    check5 = true;
                                    PhieuMuon p5 = new PhieuMuon(Integer.parseInt(this.txtMaSach5.getText()), LocalDate.now(), Integer.parseInt(this.txtMaDG.getText()));

                                    p.add(p5);
                                } else {
                                    check5 = false;
                                }
                            } else {
                                check5 = true;
                            }

                            //Đủ điều kiện
                            if (check1 && check2 && check3 && check4 && check5) {
                                btXacNhan.setOnAction(evt -> {
                                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                                    a.setTitle("Thông báo");
                                    a.setHeaderText("Mượn sách thành công!");
                                    a.showAndWait().ifPresent(res -> {
                                        if (res == ButtonType.OK) {
                                            try {
                                                for (PhieuMuon item : p) {
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
                                                }
                                            } catch (SQLException ex) {
                                                MessageBox.getBox("Thông báo", "Đã có lỗi xảy ra!", Alert.AlertType.INFORMATION).show();
                                                Logger.getLogger(BookBorrowController.class.getName()).log(Level.SEVERE, null, ex);
                                            }

                                            try {
                                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("thongtinphieu(sauxn).fxml"));
                                                Parent root1 = (Parent) fxmlLoader.load();
                                                ThongTinPMController c = fxmlLoader.getController();
                                                c.setLoginUser(u);
                                                c.setMaDG(Integer.parseInt(this.txtMaDG.getText()));
                                                Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
                                                stage.setScene(new Scene(root1));
                                                stage.show();
                                            } catch (Exception e) {
                                                System.out.print(e.getMessage());
                                            }
                                        }
                                    });
                                });
                                //MessageBox.getBox("Thông báo", "Không có sách nào đủ điều kiện mượn!", Alert.AlertType.INFORMATION).show();
                            } else {
                                btXacNhan.setOnAction(evt -> {
                                    Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                                    a.setTitle("Thông báo");
                                    a.setHeaderText("Không đủ điều kiện mượn, bạn muốn tiếp tục?");
//                            VBox dialogPaneContent = new VBox();
//                            Label label = new Label("Chi tiết:");

                                    // Constructing HashSet using listWithDuplicateElements
                                    Set<String> set = new HashSet<String>(checks);
                                    // Constructing listWithoutDuplicateElements using set
                                    List<String> listC = new ArrayList<String>(set);
                                    String i = "";
                                    for (String item : listC) {
                                        i += item;
                                    }

//                            TextArea textArea = new TextArea();
//                            textArea.setText(i);
//                            dialogPaneContent.getChildren().addAll(label, textArea);
//                            a.getDialogPane().setContent(dialogPaneContent);
                                    a.setContentText(i);
                                    a.showAndWait().ifPresent(res -> {
                                        if (res == ButtonType.OK) {
                                            try {
                                                for (PhieuMuon item : p) {
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
                                                }

                                            } catch (SQLException ex) {
                                                MessageBox.getBox("Thông báo", "Đã có lỗi xảy ra!", Alert.AlertType.INFORMATION).show();
                                                Logger.getLogger(BookBorrowController.class.getName()).log(Level.SEVERE, null, ex);
                                            }

                                            try {
                                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("thongtinphieu(sauxn).fxml"));
                                                Parent root1 = (Parent) fxmlLoader.load();
                                                ThongTinPMController c = fxmlLoader.getController();
                                                c.setLoginUser(u);
                                                c.setMaDG(Integer.parseInt(this.txtMaDG.getText()));
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
                                        }
                                    });
                                });
                            }
                        } else {
                            MessageBox.getBox("Thông báo", c1, Alert.AlertType.INFORMATION).show();
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
