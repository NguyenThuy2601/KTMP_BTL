/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.library;

import com.nhom2.pojo.ReservationCardResponse;
import com.nhom2.pojo.User;
import com.nhom2.service.ReservationService;
import java.net.URL;
import java.sql.SQLException;
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
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author CamHa
 */
public class ReservatioCardViewController implements Initializable {

    @FXML
    TableView<ReservationCardResponse> reservationCardList;
    @FXML
    TextField cardIDTxt;
    @FXML
    TextField bookIDTxt;
    @FXML
    TextField dateTxt;
    @FXML
    TextField bookNameTxt;
    @FXML
    Label statusLbl;

    User u;
    ReservationService s;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        s = new ReservationService();
        cardIDTxt.setEditable(false);
        bookIDTxt.setEditable(false);
        dateTxt.setEditable(false);
        bookNameTxt.setEditable(false);
        reservationCardList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        loadTableColumns();
        
    }

    public void setLoginUser(User uLogin) {
        this.u = uLogin;
        loadTableData();
    }

    private void loadTableColumns() {
        TableColumn colCardID = new TableColumn("Mã phiếu đặt");
        colCardID.setCellValueFactory(new PropertyValueFactory("idPhieuDat"));

        TableColumn colBookID = new TableColumn("Mã sách đặt");
        colBookID.setCellValueFactory(new PropertyValueFactory("idSach"));

        TableColumn colBookName = new TableColumn("Tên đầu sách");
        colBookName.setCellValueFactory(new PropertyValueFactory("tenSach"));
        colBookName.setPrefWidth(200);

        TableColumn colStatus = new TableColumn("Tình trạng phiếu");
        colStatus.setCellValueFactory(new PropertyValueFactory("TinhTrang"));

        TableColumn colReservationDate = new TableColumn("Ngày đặt");
        colReservationDate.setCellValueFactory(new PropertyValueFactory("ngayDat"));

        this.reservationCardList.getColumns().addAll(colCardID, colBookID, colBookName,
                colStatus, colReservationDate);
    }

    private void loadTableData()  {
        
        
        List<ReservationCardResponse> card;
        try {
            card = s.getReservationCard(u.getuID());
            this.reservationCardList.getItems().clear();
            this.reservationCardList.setItems(FXCollections.observableList(card));
        } catch (SQLException ex) {
            Logger.getLogger(ReservatioCardViewController.class.getName()).log(Level.SEVERE, null, ex);
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

    @FXML
    public void tbCardListClick(MouseEvent evt) {
        cardIDTxt.setText(reservationCardList.getSelectionModel().getSelectedItem().getIdPhieuDat());
        bookIDTxt.setText(Integer.toString(reservationCardList.getSelectionModel().getSelectedItem().getIdSach()));
        dateTxt.setText(reservationCardList.getSelectionModel().getSelectedItem().getNgayDat());
        bookNameTxt.setText(reservationCardList.getSelectionModel().getSelectedItem().getTenSach());
        statusLbl.setText(reservationCardList.getSelectionModel().getSelectedItem().getTinhTrang());
    }
}
