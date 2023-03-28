/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.library;

import com.nhom2.AppUtils.MessageBox;
import com.nhom2.pojo.User;
import com.nhom2.service.UserProfileService;
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
public class ProfileController implements Initializable {

    @FXML
    Label userIDInfo;
    @FXML
    Label userNameInfo;
    @FXML
    Label userGenderInfo;
    @FXML
    Label userDOBInfo;
    @FXML
    Label userMemberRoleInfo;
    @FXML
    Label userMemberInfo;
    @FXML
    Label userCardDateInfo;
    @FXML
    TextField userEmailInfo;
    @FXML
    TextField userAdressInfo;
    @FXML
    TextField userPhoneNumInfo;
    @FXML
    Button backBtn;

    User u;
    UserProfileService s;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        s = new UserProfileService();
    }

    public void setLoginUser(User uLogin) {
        this.u = uLogin;
    }

    public void loadLoginUserInfo() {
        userIDInfo.setText(Integer.toString(u.getuID()));
        userNameInfo.setText(u.getTen());
        userGenderInfo.setText(u.GendertoString());
        userDOBInfo.setText(u.getDOB());
        userMemberRoleInfo.setText(u.getDoiTuong());
        userMemberInfo.setText(u.getBoPhan());
        userCardDateInfo.setText(u.getCardDateInfo());
        userEmailInfo.setText(u.getEmail());
        userAdressInfo.setText(u.getDiaChi());
        userPhoneNumInfo.setText(u.getSDT());

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
    public void saveInfoBtnclick(ActionEvent evt) {
        if (s.checkPhoneFormat(userPhoneNumInfo.getText()) == 0) {
            MessageBox.getBox("Thông báo", "Độ dài không phù hợp", Alert.AlertType.INFORMATION).show();
        } else {
            if (s.checkPhoneFormat(userPhoneNumInfo.getText()) == -1) {
                MessageBox.getBox("Thông báo", "Vui lòng chỉ nhập số", Alert.AlertType.INFORMATION).show();
            } else {
                try {
                    if (s.updateInfo(u.getuID(), userEmailInfo.getText().trim(),
                            userAdressInfo.getText().trim(), userPhoneNumInfo.getText().trim())) {
                        u.setDiaChi(userAdressInfo.getText().trim());
                        u.setEmail(userEmailInfo.getText().trim());
                        u.setSDT(userPhoneNumInfo.getText().trim());
                        MessageBox.getBox("Thông báo", "Cập nhập thông tin thành công", Alert.AlertType.INFORMATION).show();
                    } else {
                        MessageBox.getBox("Thông báo", "Cập nhập thông tin thất bại", Alert.AlertType.INFORMATION).show();
                    }
                } catch (SQLException ex) {
                    MessageBox.getBox("Thông báo", "Cập nhập thông tin thất bại", Alert.AlertType.INFORMATION).show();
                    Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
