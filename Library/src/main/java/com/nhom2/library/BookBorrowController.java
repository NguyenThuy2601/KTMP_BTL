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
import com.nhom2.service.BorrowCardViewService;
import com.nhom2.service.CheckEXPService;
import com.nhom2.service.CheckNumBorrowBooksService;
import com.nhom2.service.ConfirmBorrowService;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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

    //Thông tin sau xuất phiếu
    @FXML
    TextField txtMaPhieu;
    @FXML
    TextField txtMaSach;
    @FXML
    TextField txtMaDocGia;
    @FXML
    TextField txtNgayMuon;
    @FXML
    TextField txtTinhTrang;
    @FXML
    TextField txtTenDG;
    @FXML
    TableView<BorrowCardResponse> borrowCardList;

    User u;
    int preUID;
    BorrowBookService s;
    ConfirmBorrowService cs;
    CheckEXPService cEXP;
    CheckNumBorrowBooksService cNum;
    //BorrowCardViewService b;

    List<Integer> ids = new ArrayList<>();
    List<Integer> bookID = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        s = new BorrowBookService();
        cs = new ConfirmBorrowService();
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

        //Thong tin sau xac nhan
//        b = new BorrowCardViewService();
//
//        txtMaPhieu.setEditable(false);
//        txtMaSach.setEditable(false);
//        txtMaDocGia.setEditable(false);
//        txtNgayMuon.setEditable(false);
//        txtTinhTrang.setEditable(false);
//        txtTenDG.setEditable(false);
//        borrowCardList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
//        loadTableColumns();

    }

    public void setLoginUser(User uLogin) {
        this.u = uLogin;
    }

    private void loadTableColumns() {
        TableColumn colCardID = new TableColumn("Mã phiếu mượn");
        colCardID.setCellValueFactory(new PropertyValueFactory("idPhieuMuon"));

        TableColumn colBookID = new TableColumn("Mã sách mượn");
        colBookID.setCellValueFactory(new PropertyValueFactory("idSach"));

        TableColumn colBookName = new TableColumn("Tên đầu sách");
        colBookName.setCellValueFactory(new PropertyValueFactory("tenSach"));
        colBookName.setPrefWidth(200);

        TableColumn colDGID = new TableColumn("Mã độc giả");
        colDGID.setCellValueFactory(new PropertyValueFactory("idUser"));

        TableColumn colDGName = new TableColumn("Họ tên độc giả");
        colDGName.setCellValueFactory(new PropertyValueFactory("HoLotTen"));
        colBookName.setPrefWidth(200);

        TableColumn colStatus = new TableColumn("Tình trạng phiếu");
        colStatus.setCellValueFactory(new PropertyValueFactory("TinhTrang"));

        TableColumn colBorrowDate = new TableColumn("Ngày mượn");
        colBorrowDate.setCellValueFactory(new PropertyValueFactory("ngayMuon"));

        this.borrowCardList.getColumns().addAll(colCardID, colBookID, colBookName, colDGID, colDGName, colStatus, colBorrowDate);
    }

//    private void loadTableData() throws SQLException {
//        List<BorrowCardResponse> card = b.getBorrowCard(u.getuID());
//
//        this.borrowCardList.getItems().clear();
//        this.borrowCardList.setItems(FXCollections.observableList(card));
//
//    }

    @FXML
    public void confirmBorrowBookClick(ActionEvent event) throws SQLException {

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

                    //Check hạn thẻ độc giả
                    if (!cEXP.checkEXP(Integer.parseInt(this.txtMaDG.getText()))) {
                        MessageBox.getBox("Thông báo", "Thẻ độc giả đã hết hạn", Alert.AlertType.INFORMATION).show();
                    } else {
                        f = true; //Thẻ còn hạn
                    }

                    //Check sách đã trả hết chưa?
                    if (!cNum.checkNumBorrowBooks(Integer.parseInt(this.txtMaDG.getText()))) {
                        MessageBox.getBox("Thông báo", "Độc giả chưa trả hết sách đang mượn", Alert.AlertType.INFORMATION).show();
                    } else {
                        f1 = true; //Trả hết sách rồi
                    }

                    if (txtMaSach1.getText().isBlank() && txtMaSach2.getText().isBlank() && txtMaSach3.getText().isBlank()
                            && txtMaSach4.getText().isBlank() && txtMaSach5.getText().isBlank()) {
                        MessageBox.getBox("Thông báo", "Vui lòng nhập vào mã sách cần mượn", Alert.AlertType.INFORMATION).show();
                    } else {
                        boolean flagB = false; //Check sách có được đặt chưa?
                        boolean flagB1 = false; //Check sách có đang được mượn ko?
                        //Check sách đã trả hết chưa?

                        if (!txtMaSach1.getText().isEmpty()) {
                            boolean checkIDBook = false;
                            for (int i : bookID) {
                                if (Integer.parseInt(this.txtMaSach1.getText()) == i) {
                                    checkIDBook = true;
                                    break;
                                }
                            }
                            if (!checkIDBook) {
                                MessageBox.getBox("Thông báo", "Không tìm thấy mã sách 1", Alert.AlertType.INFORMATION).show();
                            }

                            //Check sách có được đặt chưa?
                            for (int i : s.checkAvailableBook()) {
                                if (Integer.parseInt(this.txtMaSach1.getText()) != i) {
                                    flagB = true;
                                    break;
                                }
                            }
                            if (!flagB) {
                                MessageBox.getBox("Thông báo", "Mã sách 1 đã được đặt không thể mượn", Alert.AlertType.INFORMATION).show();
                            }

                            //Check sách có đang được mượn không
                            for (int i : s.checkNotAvailableBook()) {
                                if (Integer.parseInt(this.txtMaSach1.getText()) != i) {
                                    flagB1 = true;
                                    break;
                                }
                            }
                            if (!flagB1) {
                                MessageBox.getBox("Thông báo", "Mã sách 1 đang được mượn!", Alert.AlertType.INFORMATION).show();
                            }

                            //Thỏa hết điều kiện -> Tạo phiếu mượn
                            if (flagB && flagB1 && f && f1) {
                                PhieuMuon p = new PhieuMuon(Integer.parseInt(this.txtMaSach1.getText()), LocalDate.now(), Integer.parseInt(this.txtMaDG.getText()));
                                try {
                                    if (s.addBorrowCard(p)) {
                                        check1 = true;
                                        //MessageBox.getBox("Thông báo", "Mượn sách thành công", Alert.AlertType.INFORMATION).show();
                                        txtMaSach1.clear();
                                    } else {
                                        MessageBox.getBox("Thông báo", "Mã sách 1 mượn không thành công", Alert.AlertType.INFORMATION).show();
                                    }
                                } catch (SQLException ex) {
                                    MessageBox.getBox("Thông báo", "Đã có lỗi xảy ra!", Alert.AlertType.INFORMATION).show();
                                    Logger.getLogger(BookBorrowController.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            } else {
                                MessageBox.getBox("Thông báo", "Mã sách 1 không đủ điều kiện để mượn", Alert.AlertType.INFORMATION).show();
                            }
                        }

                        if (!txtMaSach2.getText().isEmpty()) {
                            boolean checkIDBook = false;
                            for (int i : bookID) {
                                if (Integer.parseInt(this.txtMaSach2.getText()) == i) {
                                    checkIDBook = true;
                                    break;
                                }
                            }
                            if (!checkIDBook) {
                                MessageBox.getBox("Thông báo", "Không tìm thấy mã sách 2", Alert.AlertType.INFORMATION).show();
                            }

                            //Check sách có được đặt chưa?
                            for (int i : s.checkAvailableBook()) {
                                if (Integer.parseInt(this.txtMaSach2.getText()) != i) {
                                    flagB = true;
                                    break;
                                }
                            }
                            if (!flagB) {
                                MessageBox.getBox("Thông báo", "Mã sách 2 đã được đặt không thể mượn", Alert.AlertType.INFORMATION).show();
                            }

                            //Check sách có đang được mượn không
                            for (int i : s.checkNotAvailableBook()) {
                                if (Integer.parseInt(this.txtMaSach2.getText()) != i) {
                                    flagB1 = true;
                                    break;
                                }
                            }
                            if (!flagB1) {
                                MessageBox.getBox("Thông báo", "Mã sách 2 đang được mượn!", Alert.AlertType.INFORMATION).show();
                            }

                            //Thỏa hết điều kiện -> Tạo phiếu mượn
                            if (flagB && flagB1 && f && f1) {
                                PhieuMuon p = new PhieuMuon(Integer.parseInt(this.txtMaSach2.getText()), LocalDate.now(), Integer.parseInt(this.txtMaDG.getText()));
                                try {
                                    if (s.addBorrowCard(p)) {
                                        check2 = true;
                                        //MessageBox.getBox("Thông báo", "Mượn sách thành công", Alert.AlertType.INFORMATION).show();
                                        txtMaSach2.clear();
                                    } else {
                                        MessageBox.getBox("Thông báo", "Mã sách 2 mượn không thành công", Alert.AlertType.INFORMATION).show();
                                    }
                                } catch (SQLException ex) {
                                    MessageBox.getBox("Thông báo", "Đã có lỗi xảy ra!", Alert.AlertType.INFORMATION).show();
                                    Logger.getLogger(BookBorrowController.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            } else {
                                MessageBox.getBox("Thông báo", "Mã sách 2 không đủ điều kiện để mượn", Alert.AlertType.INFORMATION).show();
                            }
                        }

                        if (!txtMaSach3.getText().isEmpty()) {
                            boolean checkIDBook = false;
                            for (int i : bookID) {
                                if (Integer.parseInt(this.txtMaSach3.getText()) == i) {
                                    checkIDBook = true;
                                    break;
                                }
                            }
                            if (!checkIDBook) {
                                MessageBox.getBox("Thông báo", "Không tìm thấy mã sách 3", Alert.AlertType.INFORMATION).show();
                            }

                            //Check sách có được đặt chưa?
                            for (int i : s.checkAvailableBook()) {
                                if (Integer.parseInt(this.txtMaSach3.getText()) != i) {
                                    flagB = true;
                                    break;
                                }
                            }
                            if (!flagB) {
                                MessageBox.getBox("Thông báo", "Mã sách 3 đã được đặt không thể mượn", Alert.AlertType.INFORMATION).show();
                            }

                            //Check sách có đang được mượn không
                            for (int i : s.checkNotAvailableBook()) {
                                if (Integer.parseInt(this.txtMaSach3.getText()) != i) {
                                    flagB1 = true;
                                    break;
                                }
                            }
                            if (!flagB1) {
                                MessageBox.getBox("Thông báo", "Mã sách 3 đang được mượn!", Alert.AlertType.INFORMATION).show();
                            }

                            //Thỏa hết điều kiện -> Tạo phiếu mượn
                            if (flagB && flagB1 && f && f1) {
                                PhieuMuon p = new PhieuMuon(Integer.parseInt(this.txtMaSach3.getText()), LocalDate.now(), Integer.parseInt(this.txtMaDG.getText()));
                                try {
                                    if (s.addBorrowCard(p)) {
                                        check3 = true;
                                        //MessageBox.getBox("Thông báo", "Mượn sách thành công", Alert.AlertType.INFORMATION).show();
                                        txtMaSach3.clear();
                                    } else {
                                        MessageBox.getBox("Thông báo", "Mã sách 3 mượn không thành công", Alert.AlertType.INFORMATION).show();
                                    }
                                } catch (SQLException ex) {
                                    MessageBox.getBox("Thông báo", "Đã có lỗi xảy ra!", Alert.AlertType.INFORMATION).show();
                                    Logger.getLogger(BookBorrowController.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            } else {
                                MessageBox.getBox("Thông báo", "Mã sách 3 không đủ điều kiện để mượn", Alert.AlertType.INFORMATION).show();
                            }
                        }

                        if (!txtMaSach4.getText().isEmpty()) {
                            boolean checkIDBook = false;
                            for (int i : bookID) {
                                if (Integer.parseInt(this.txtMaSach4.getText()) == i) {
                                    checkIDBook = true;
                                    break;
                                }
                            }
                            if (!checkIDBook) {
                                MessageBox.getBox("Thông báo", "Không tìm thấy mã sách 4", Alert.AlertType.INFORMATION).show();
                            }

                            //Check sách có được đặt chưa?
                            for (int i : s.checkAvailableBook()) {
                                if (Integer.parseInt(this.txtMaSach4.getText()) != i) {
                                    flagB = true;
                                    break;
                                }
                            }
                            if (!flagB) {
                                MessageBox.getBox("Thông báo", "Mã sách 4 đã được đặt không thể mượn", Alert.AlertType.INFORMATION).show();
                            }

                            //Check sách có đang được mượn không
                            for (int i : s.checkNotAvailableBook()) {
                                if (Integer.parseInt(this.txtMaSach4.getText()) != i) {
                                    flagB1 = true;
                                    break;
                                }
                            }
                            if (!flagB1) {
                                MessageBox.getBox("Thông báo", "Mã sách 4 đang được mượn!", Alert.AlertType.INFORMATION).show();
                            }

                            //Thỏa hết điều kiện -> Tạo phiếu mượn
                            if (flagB && flagB1 && f && f1) {
                                PhieuMuon p = new PhieuMuon(Integer.parseInt(this.txtMaSach4.getText()), LocalDate.now(), Integer.parseInt(this.txtMaDG.getText()));
                                try {
                                    if (s.addBorrowCard(p)) {
                                        check4 = true;
                                        //MessageBox.getBox("Thông báo", "Mượn sách thành công", Alert.AlertType.INFORMATION).show();
                                        txtMaSach4.clear();
                                    } else {
                                        MessageBox.getBox("Thông báo", "Mã sách 4 mượn không thành công", Alert.AlertType.INFORMATION).show();
                                    }
                                } catch (SQLException ex) {
                                    MessageBox.getBox("Thông báo", "Đã có lỗi xảy ra!", Alert.AlertType.INFORMATION).show();
                                    Logger.getLogger(BookBorrowController.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            } else {
                                MessageBox.getBox("Thông báo", "Mã sách 4 không đủ điều kiện để mượn", Alert.AlertType.INFORMATION).show();
                            }
                        }

                        if (!txtMaSach5.getText().isEmpty()) {
                            boolean checkIDBook = false;
                            for (int i : bookID) {
                                if (Integer.parseInt(this.txtMaSach5.getText()) == i) {
                                    checkIDBook = true;
                                    break;
                                }
                            }
                            if (!checkIDBook) {
                                MessageBox.getBox("Thông báo", "Không tìm thấy mã sách 5", Alert.AlertType.INFORMATION).show();
                            }

                            //Check sách có được đặt chưa?
                            for (int i : s.checkAvailableBook()) {
                                if (Integer.parseInt(this.txtMaSach5.getText()) != i) {
                                    flagB = true;
                                    break;
                                }
                            }
                            if (!flagB) {
                                MessageBox.getBox("Thông báo", "Mã sách 5 đã được đặt không thể mượn", Alert.AlertType.INFORMATION).show();
                            }

                            //Check sách có đang được mượn không
                            for (int i : s.checkNotAvailableBook()) {
                                if (Integer.parseInt(this.txtMaSach5.getText()) != i) {
                                    flagB1 = true;
                                    break;
                                }
                            }
                            if (!flagB1) {
                                MessageBox.getBox("Thông báo", "Mã sách 5 đang được mượn!", Alert.AlertType.INFORMATION).show();
                            }

                            //Thỏa hết điều kiện -> Tạo phiếu mượn
                            if (flagB && flagB1 && f && f1) {
                                PhieuMuon p = new PhieuMuon(Integer.parseInt(this.txtMaSach5.getText()), LocalDate.now(), Integer.parseInt(this.txtMaDG.getText()));
                                try {
                                    if (s.addBorrowCard(p)) {
                                        check5 = true;
                                        //MessageBox.getBox("Thông báo", "Mượn sách thành công", Alert.AlertType.INFORMATION).show();
                                        txtMaSach5.clear();
                                    } else {
                                        MessageBox.getBox("Thông báo", "Mã sách 5 mượn không thành công", Alert.AlertType.INFORMATION).show();
                                    }
                                } catch (SQLException ex) {
                                    MessageBox.getBox("Thông báo", "Đã có lỗi xảy ra!", Alert.AlertType.INFORMATION).show();
                                    Logger.getLogger(BookBorrowController.class.getName()).log(Level.SEVERE, null, ex);
                                }

                            } else {
                                MessageBox.getBox("Thông báo", "Mã sách 5 không đủ điều kiện để mượn", Alert.AlertType.INFORMATION).show();
                            }
                        }
                    }

                    if (check1 || check2 || check3 || check4 || check5) {
                        MessageBox.getBox("Thông báo", "Mượn sách thành công", Alert.AlertType.INFORMATION).show();
                    }
                } else {
                    MessageBox.getBox("Thông báo", "Không tìm thấy mã độc giả", Alert.AlertType.INFORMATION).show();
                }
            } catch (SQLException ex) {
                Logger.getLogger(BookBorrowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

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

//    @FXML
//    public void tbCardListClick(MouseEvent evt) {
//        txtMaPhieu.setText(borrowCardList.getSelectionModel().getSelectedItem().getIdPhieuMuon());
//        txtMaSach.setText(Integer.toString(borrowCardList.getSelectionModel().getSelectedItem().getIdSach()));
//        txtNgayMuon.setText(borrowCardList.getSelectionModel().getSelectedItem().getNgayMuon());
//        txtMaDocGia.setText(Integer.toString(borrowCardList.getSelectionModel().getSelectedItem().getIdUser()));
//        txtTinhTrang.setText(borrowCardList.getSelectionModel().getSelectedItem().getTinhTrang());
//        txtTenDG.setText(borrowCardList.getSelectionModel().getSelectedItem().getHoLotTen());
//    }

//    @FXML
//    public void chuyenFormClick(MouseEvent evt) {
//        try {
//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("thongtinphieu(sauxn).fxml"));
//            Parent root1 = (Parent) fxmlLoader.load();
//            BookBorrowController c = fxmlLoader.getController();
//            c.setLoginUser(u);
//            Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
//            stage.setScene(new Scene(root1));
//            stage.show();
//        } catch (Exception e) {
//            System.out.print(e.getMessage());
//        }
//    }
}
