/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.library;

import com.nhom2.AppUtils.MessageBox;
import com.nhom2.pojo.BorrowCardResponse;
import com.nhom2.pojo.PhieuMuon;
import com.nhom2.pojo.User;
import com.nhom2.service.BookReturningService;
import com.nhom2.service.CheckService;
import java.net.URL;
import java.sql.SQLException;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author CamHa
 */
public class BookReturnController implements Initializable {

    @FXML
    Button findBtn;
    @FXML
    Button confirmBtn;
    @FXML
    TextField cardIDTxt;
    @FXML
    TextField cardIDInfoTxt;
    @FXML
    TextField dateInfoTxt;
    @FXML
    TextField bookIDInfoTxt;
    @FXML
    TextField uIDInfoTxt;
    @FXML
    TextField uNameInfoTxt;
    @FXML
    TextField bookNameInfoTxt;
    @FXML
    Label fineLbl;
    @FXML
    Label statusLbl;
    @FXML
    Label fineTotalLbl;

    BookReturningService s;
    CheckService cs;
    User u;
    int preUID;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        s = new BookReturningService();
        cs = new CheckService();
        preUID = 0;

        confirmBtn.setDisable(true);

        cardIDInfoTxt.setEditable(false);
        dateInfoTxt.setEditable(false);
        bookIDInfoTxt.setEditable(false);
        uIDInfoTxt.setEditable(false);
        uNameInfoTxt.setEditable(false);
        bookNameInfoTxt.setEditable(false);

        fineLbl.setText("0 đ");
        fineTotalLbl.setText("0 đ");
        statusLbl.setText("");
    }

    public void setLoginUser(User uLogin) {
        this.u = uLogin;
    }

    @FXML
    public void findBtnlick(ActionEvent evt) {
        if (cardIDTxt.getText().isBlank()) {
            MessageBox.getBox("Thông báo", "Vui lòng nhập vào mã phiếu mượn", Alert.AlertType.INFORMATION).show();
        } else {
            try {
                BorrowCardResponse p = s.getBorrowingCardInfo(cardIDTxt.getText().trim());
                if (p.getIdPhieuMuon().equals("none")) {
                    MessageBox.getBox("Thông báo", "Mã phiếu mượn không tồn tại", Alert.AlertType.INFORMATION).show();
                } else {
                    if (s.calcDayGap(p.getNgayMuonOriginalForm()) > 30) {
                        cs.updateBorrowingCardWithID(cardIDTxt.getText().trim());
                        p = s.getBorrowingCardInfo(cardIDTxt.getText().trim());
                    }

                    int fine = s.calcFee(s.calcDayGap(p.getNgayMuonOriginalForm()));

                    cardIDInfoTxt.setText(p.getIdPhieuMuon());
                    dateInfoTxt.setText(p.getNgayMuon());
                    bookIDInfoTxt.setText(Integer.toString(p.getIdSach()));
                    uIDInfoTxt.setText(Integer.toString(p.getIdUser()));
                    uNameInfoTxt.setText(p.getHoLotTen());
                    fineLbl.setText(Integer.toString(fine) + " đ");
                    statusLbl.setText(p.getTinhTrang());
                    bookNameInfoTxt.setText(p.getTenSach());

                    if (p.getTinhTrangOriginalForm() == 1) {
                        confirmBtn.setDisable(true);
                    } else {
                        confirmBtn.setDisable(false);
                        if(Integer.parseInt(uIDInfoTxt.getText()) != preUID){
                            fineTotalLbl.setText(fineLbl.getText());
                            preUID = Integer.parseInt(uIDInfoTxt.getText());
                        }
                        else{
                            String total = s.calcTotalFine(fineLbl.getText(), fineTotalLbl.getText());
                            fineTotalLbl.setText(total + " đ");
                        }
                            
                    }

                }
            } catch (SQLException ex) {
                Logger.getLogger(BookReturnController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    @FXML
    public void confirmBtnlick(ActionEvent evt) {
        try {
            if (s.confirmReturningBook(cardIDInfoTxt.getText(), Integer.parseInt(bookIDInfoTxt.getText()))) {
                MessageBox.getBox("Thông báo", "Xác nhận trả sách thành công", Alert.AlertType.INFORMATION).show();
                cardIDInfoTxt.setText("");
                dateInfoTxt.setText("");
                bookIDInfoTxt.setText("");
                uIDInfoTxt.setText("");
                uNameInfoTxt.setText("");
                bookNameInfoTxt.setText("");
                fineLbl.setText("0 đ");
                statusLbl.setText("");
                confirmBtn.setDisable(true);
                
            } else {
                MessageBox.getBox("Thông báo", "Xác nhận trả sách thất bại", Alert.AlertType.INFORMATION).show();
            }
        } catch (SQLException ex) {
            MessageBox.getBox("Thông báo", "Đã có lỗi xáy ra", Alert.AlertType.INFORMATION).show();
            Logger.getLogger(BookReturnController.class.getName()).log(Level.SEVERE, null, ex);
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
}
