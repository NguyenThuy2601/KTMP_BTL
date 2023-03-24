package com.nhom2.library;

import com.nhom2.pojo.User;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class PrimaryController implements Initializable {

    @FXML
    Menu profileMN;
    @FXML
    Menu reservationCardMN;
    @FXML
    Menu retunBookMN;
    @FXML
    MenuItem directBorrowMN;
    @FXML
    MenuItem confrimReservationCardMN;
    @FXML
    Menu borrowMN;
    @FXML
    Menu borrowCardMN;
    @FXML
    Menu statisticMN;
    @FXML
    Menu loginMN;
    @FXML
    Menu logoutMN;
    @FXML
    Button orderBook;

    User u;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        u = new User();
        if (u.getuID() == 0) {
            profileMN.setVisible(false);
            reservationCardMN.setVisible(false);
            retunBookMN.setVisible(false);
            borrowMN.setVisible(false);
            borrowCardMN.setVisible(false);
            statisticMN.setVisible(false);
            logoutMN.setVisible(false);
            orderBook.setVisible(false);
        }
        loginMN.showingProperty().addListener((ObservableValue<? extends Boolean> observablevalue, Boolean oldVal, Boolean newVal) -> {
            if (newVal.booleanValue()) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
                    Parent root1 = (Parent) fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root1));
                    stage.show();
                } catch (Exception e) {
                    return;
                }
            }
            else
                return;
        });
    }

    @FXML
    public void loginMNClick(ActionEvent evt) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = (Stage)((Node)evt.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e) {
            return;
        }
    }
}
