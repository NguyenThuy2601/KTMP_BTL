/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.library;

import com.nhom2.AppUtils.MessageBox;
import com.nhom2.pojo.BorrowCardResponse;
import com.nhom2.pojo.User;
import com.nhom2.service.BorrowBookService;
import com.nhom2.service.ConfirmBorrowService;
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
    ConfirmBorrowService cs;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        s = new BorrowBookService();
        cs = new ConfirmBorrowService();
        preUID = 0;

    }

    public void setLoginUser(User uLogin) {
        this.u = uLogin;
    }

    @FXML
    public void confirmBorrowBookClick(ActionEvent event) {
        if (txtMaDG.getText().isBlank()) {
            MessageBox.getBox("Thông báo", "Vui lòng nhập vào mã độc giả", Alert.AlertType.INFORMATION).show();
        } else {
            int idDG = Integer.parseInt(this.txtMaDG.getText());
            int idSach1 = Integer.parseInt(this.txtMaSach1.getText());
            int idSach2 = Integer.parseInt(this.txtMaSach2.getText());
            int idSach3 = Integer.parseInt(this.txtMaSach3.getText());
            int idSach4 = Integer.parseInt(this.txtMaSach4.getText());
            int idSach5 = Integer.parseInt(this.txtMaSach5.getText());
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
