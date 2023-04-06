/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.library;

import com.nhom2.AppUtils.MessageBox;
import com.nhom2.pojo.BorrowCardResponse;
import com.nhom2.pojo.PhieuMuon;
import com.nhom2.pojo.ReservationCardResponse;
import com.nhom2.pojo.User;
import com.nhom2.service.BorrowBookService;
import com.nhom2.service.BorrowToReservationService;
import com.nhom2.service.CheckEXPService;
import com.nhom2.service.CheckNumBorrowBooksService;
import com.nhom2.service.CheckService;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author ADMIN
 */
public class ConfirmToReservationController implements Initializable {

    @FXML
    TextField txtMaPhieu;
    @FXML
    TextField txtMaPhieuDat;
    @FXML
    TextField txtMaSach;
    @FXML
    TextField txtMaDocGia;
    @FXML
    TextField txtNgayDat;
    @FXML
    TextField txtTinhTrang;
    @FXML
    TextField txtTenDG;
    @FXML
    Button btTim;
    @FXML
    Button btXacNhan;

    User u;
    BorrowBookService b;
    BorrowToReservationService r;
    CheckService c;
    CheckEXPService cEXP;
    CheckNumBorrowBooksService cNum;

    PhieuMuon p1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        b = new BorrowBookService();
        r = new BorrowToReservationService();
        c = new CheckService();
        cEXP = new CheckEXPService();
        cNum = new CheckNumBorrowBooksService();

        txtMaPhieuDat.setEditable(false);
        txtMaSach.setEditable(false);
        txtMaDocGia.setEditable(false);
        txtNgayDat.setEditable(false);
        txtTinhTrang.setEditable(false);
        txtTenDG.setEditable(false);
    }

    public void setLoginUser(User uLogin) {
        this.u = uLogin;
    }

    @FXML
    public void timClick(ActionEvent evt) {
        if (txtMaPhieu.getText().isBlank()) {
            MessageBox.getBox("Thông báo", "Vui lòng nhập vào mã phiếu đặt", Alert.AlertType.INFORMATION).show();
        } else {
            try {
                ReservationCardResponse p = r.getReservationCard(txtMaPhieu.getText().trim());
                if (p.getIdPhieuDat().equals("none")) {
                    MessageBox.getBox("Thông báo", "Mã phiếu đặt không tồn tại", Alert.AlertType.INFORMATION).show();
                } else {
                    if (!c.checkReservationnCardEXP(p.getNgayDatOriginalForm())) {
                        if (r.updateReservationCardWithID(txtMaPhieu.getText().trim())) {
                            p = r.getReservationCard(txtMaPhieu.getText().trim());
                        }
                    }
                    txtMaPhieuDat.setText(p.getIdPhieuDat());
                    txtNgayDat.setText(p.getNgayDat());
                    txtMaSach.setText(Integer.toString(p.getIdSach()));
                    txtMaDocGia.setText(Integer.toString(p.getDocgiaID()));
                    txtTenDG.setText(p.getHoLotTen());
                    txtTinhTrang.setText(p.getTinhTrang());

                    if (p.getTinhTrangOriginalForm() == -1) {
                        p1 = new PhieuMuon(LocalDate.now(), Integer.parseInt(txtMaDocGia.getText()));
                        btXacNhan.setDisable(false);
                    } else {
                        btXacNhan.setDisable(true);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(ConfirmToReservationController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    @FXML
    public void confirmClick(ActionEvent evt) throws SQLException {
        List<String> checks = new ArrayList<>();
        boolean f = false; //Check hạn thẻ độc giả
        boolean f1 = false; //Check sách đã trả hết chưa?
        boolean flagB = false; //Check sách có đang được mượn quá 5 cuốn ko?

        //Check hạn thẻ độc giả
        if (!cEXP.checkEXP(Integer.parseInt(this.txtMaDocGia.getText()))) {
            checks.add("Thẻ độc giả đã hết hạn\n");
        } else {
            f = true; //Thẻ còn hạn
        }

        //Check sách đã trả hết chưa?
        if (!cNum.checkNumBorrowBooks(Integer.parseInt(this.txtMaDocGia.getText()))) {
            checks.add("Độc giả chưa trả hết sách đang mượn\n");
        } else {
            f1 = true; //Trả hết sách rồi
        }

        //Check số lượng sách mượn
        if (!cNum.checkMaxBorrowBooks(Integer.parseInt(this.txtMaDocGia.getText()))) {
            checks.add("Độc giả đã mượn đủ 5 cuốn, không thể mượn thêm!\n");
        } else {
            flagB = true;
        }

        if (f && f1 && flagB) {
            try {
                if (b.addBorrowCard(p1)) {
                    MessageBox.getBox("Thông báo", "Xác nhận mượn sách thành công", Alert.AlertType.INFORMATION).show();
                    txtMaPhieu.setText("");
                    txtMaPhieuDat.setText("");
                    txtMaDocGia.setText("");
                    txtMaSach.setText("");
                    txtNgayDat.setText("");
                    txtTenDG.setText("");
                    txtTinhTrang.setText("0 đ");
                    btXacNhan.setDisable(true);

                } else {
                    MessageBox.getBox("Thông báo", "Xác nhận mượn sách thất bại", Alert.AlertType.INFORMATION).show();
                }
            } catch (SQLException ex) {
                MessageBox.getBox("Thông báo", "Đã có lỗi xáy ra", Alert.AlertType.INFORMATION).show();
                Logger.getLogger(ConfirmToReservationController.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("thongtinphieu(sauxn).fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                ThongTinPMController c = fxmlLoader.getController();
                c.setLoginUser(u);
                c.setMaDG(Integer.parseInt(this.txtMaDocGia.getText()));
                Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root1));
                stage.show();
            } catch (Exception e) {
                System.out.print(e.getMessage());
            }
        } else {
            
            String i = "Không đủ điều kiện mượn:\n\n";
            for (String item : checks) {
                i += item;
            }
            MessageBox.getBox("Thông báo", i, Alert.AlertType.INFORMATION).show();
        }
    }

    @FXML
    public void backBtnlick(ActionEvent evt
    ) {
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
}
