/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.library;

import com.nhom2.AppUtils.MessageBox;
import com.nhom2.pojo.User;
import com.nhom2.service.LoginService;
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
 * @author CamHa
 */
public class LoginController implements Initializable {

    @FXML
    Button exitBtn;
    @FXML
    Button loginBtn;
    @FXML
    TextField txtUname;
    @FXML
    TextField txtPass;

    LoginService s;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        s = new LoginService();

    }

    @FXML
    public void exitBtnlick(ActionEvent evt) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("primary.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e) {
            return;
        }
    }

    @FXML
    public void loginBtnClick(ActionEvent evt) {

        if (txtUname.getText().isEmpty() || txtPass.getText().isEmpty()) {
            MessageBox.getBox("Thông báo", "Vui lòng điền đầy đủ thông tin", Alert.AlertType.INFORMATION).show();
        } else {
            String uName = txtUname.getText();
            String pass = txtPass.getText();
            try {
                if (s.checkEmail(uName) == false) {
                    MessageBox.getBox("Thông báo", "Không tìm thấy tài khoản", Alert.AlertType.INFORMATION).show();
                } else {
                    if (s.checkPassword(uName, pass) == false) {
                        MessageBox.getBox("Thông báo", "Sai password", Alert.AlertType.INFORMATION).show();
                    } else {
                        String accID = s.getAccID(uName);
                        User u = s.setUser(accID);
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
            } catch (SQLException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
