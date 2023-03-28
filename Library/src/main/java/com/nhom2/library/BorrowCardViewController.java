/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.library;

import com.nhom2.pojo.BorrowCardResponse;
import com.nhom2.pojo.ReservationCardResponse;
import com.nhom2.pojo.User;
import com.nhom2.service.BorrowCardViewService;
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
public class BorrowCardViewController implements Initializable {

    @FXML
    TableView<BorrowCardResponse> borrowCardList;
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
    BorrowCardViewService s;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        s = new BorrowCardViewService();
        
        cardIDTxt.setEditable(false);
        bookIDTxt.setEditable(false);
        dateTxt.setEditable(false);
        bookNameTxt.setEditable(false);
        borrowCardList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        loadTableColumns();
        
    }
    
    public void setLoginUser(User uLogin) {
        this.u = uLogin;
        try {
            loadTableData();
        } catch (SQLException ex) {
            Logger.getLogger(ReservatioCardViewController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        }
    }
    
    private void loadTableColumns() {
        TableColumn colCardID = new TableColumn("Mã phiếu muon");
        colCardID.setCellValueFactory(new PropertyValueFactory("idPhieuMuon"));

        TableColumn colBookID = new TableColumn("Mã sách mượn");
        colBookID.setCellValueFactory(new PropertyValueFactory("idSach"));

        TableColumn colBookName = new TableColumn("Tên đầu sách");
        colBookName.setCellValueFactory(new PropertyValueFactory("tenSach"));
        colBookName.setPrefWidth(200);

        TableColumn colStatus = new TableColumn("Tình trạng phiếu");
        colStatus.setCellValueFactory(new PropertyValueFactory("TinhTrang"));

        TableColumn colReservationDate = new TableColumn("Ngày đặt");
        colReservationDate.setCellValueFactory(new PropertyValueFactory("ngayMuon"));

        this.borrowCardList.getColumns().addAll(colCardID, colBookID, colBookName,
                colStatus, colReservationDate);
    }

    private void loadTableData() throws SQLException {
        List<BorrowCardResponse> card = s.getBorrowCard(u.getuID());

        this.borrowCardList.getItems().clear();
        this.borrowCardList.setItems(FXCollections.observableList(card));

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
        cardIDTxt.setText(borrowCardList.getSelectionModel().getSelectedItem().getIdPhieuMuon());
        bookIDTxt.setText(Integer.toString(borrowCardList.getSelectionModel().getSelectedItem().getIdSach()));
        dateTxt.setText(borrowCardList.getSelectionModel().getSelectedItem().getNgayMuon());
        bookNameTxt.setText(borrowCardList.getSelectionModel().getSelectedItem().getTenSach());
        statusLbl.setText(borrowCardList.getSelectionModel().getSelectedItem().getTinhTrang());
    }
    
}
