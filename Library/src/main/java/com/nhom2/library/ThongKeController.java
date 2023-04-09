/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.library;

import com.nhom2.AppUtils.MessageBox;
import com.nhom2.library.PrimaryController;
import com.nhom2.pojo.BorrowCardResponse;
import com.nhom2.pojo.User;
import com.nhom2.service.ThongKeService;
import java.net.URL;
import java.sql.SQLException;
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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 *
 * @author ADMIN
 */
public class ThongKeController implements Initializable {

    @FXML
    TextField txtQuy;
    @FXML
    TextField txtYear;
    @FXML
    Label lbTotalSach;
    @FXML
    Label lbSoSachDuocMuon;
    @FXML
    Label lbSoSachChuaTra;
    @FXML
    Label lbTongPhieuMuon;
    @FXML
    Label lbSoPhieuQuaHan;
    @FXML
    Label lbSoPhieuTraDungQD;
    @FXML
    Button btTimTheoQuy;
    @FXML
    Button btTimTheoNam;
    @FXML
    TableView<BorrowCardResponse> list;
    @FXML
    ComboBox cbbQuy;
    @FXML
    BarChart<String, Number> barchart;
    @FXML
    NumberAxis yAxis;
    @FXML
    CategoryAxis xAxis;

    User u;
    ThongKeService s;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        s = new ThongKeService();
        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();
        yAxis.setLabel("Số lượng");
        barchart = new BarChart<String, Number>(xAxis, yAxis);

        this.cbbQuy.setItems(FXCollections.observableList(setValueCBBQuy()));

        this.loadTableColumns();
    }

    public void setLoginUser(User uLogin) {
        this.u = uLogin;
    }

    public List<String> setValueCBBQuy() {
        List<String> quy = new ArrayList<>();
        quy.add("Quý 1");
        quy.add("Quý 2");
        quy.add("Quý 3");
        quy.add("Quý 4");
        return quy;
    }

    //Theo năm
    private void loadTableColumns() {
        TableColumn colCardID = new TableColumn("Mã phiếu mượn");
        colCardID.setCellValueFactory(new PropertyValueFactory("idPhieuMuon"));
        colCardID.setPrefWidth(150);

        TableColumn colBookName = new TableColumn("Tên đầu sách");
        colBookName.setCellValueFactory(new PropertyValueFactory("tenSach"));
        colBookName.setPrefWidth(300);

        TableColumn colDGName = new TableColumn("Họ tên độc giả");
        colDGName.setCellValueFactory(new PropertyValueFactory("HoLotTen"));
        colDGName.setPrefWidth(300);

        TableColumn colBorrowDate = new TableColumn("Ngày mượn");
        colBorrowDate.setCellValueFactory(new PropertyValueFactory("ngayMuon"));
        colBorrowDate.setPrefWidth(180);

        this.list.getColumns().addAll(colCardID, colBookName, colDGName, colBorrowDate);
    }

    private void loadTableDataTheoNam(int year) throws SQLException {

        List<BorrowCardResponse> card = s.getInfo(year);

        this.list.getItems().clear();
        this.list.setItems(FXCollections.observableList(card));
    }

    private void loadTableDataTheoQuy(int year) throws SQLException {

        if (cbbQuy.getSelectionModel().getSelectedItem() == "Quý 1") {
            List<BorrowCardResponse> card = s.getInfoTheoQuy1(year);
            this.list.getItems().clear();
            this.list.setItems(FXCollections.observableList(card));
        } else if (cbbQuy.getSelectionModel().getSelectedItem() == "Quý 2") {
            List<BorrowCardResponse> card = s.getInfoTheoQuy2(year);
            this.list.getItems().clear();
            this.list.setItems(FXCollections.observableList(card));
        } else if (cbbQuy.getSelectionModel().getSelectedItem() == "Quý 3") {
            List<BorrowCardResponse> card = s.getInfoTheoQuy3(year);
            this.list.getItems().clear();
            this.list.setItems(FXCollections.observableList(card));
        } else {
            List<BorrowCardResponse> card = s.getInfoTheoQuy4(year);
            this.list.getItems().clear();
            this.list.setItems(FXCollections.observableList(card));
        }

    }

    @FXML
    public void timTheoQuyClick(ActionEvent evt) {
        if (cbbQuy.getSelectionModel().getSelectedItem() == null) {
            MessageBox.getBox("Thông báo", "Vui lòng chọn quý cần xem!", Alert.AlertType.INFORMATION).show();
        } else {
            if (txtQuy.getText().isBlank()) {
                MessageBox.getBox("Thông báo", "Vui lòng nhập vào năm cần xem!", Alert.AlertType.INFORMATION).show();
            } else {
                try {
                    int year = Integer.parseInt(txtQuy.getText());
                    lbTotalSach.setText(String.valueOf(s.totalBookInLib()));
                    if (cbbQuy.getSelectionModel().getSelectedItem() == "Quý 1") {
                        lbSoSachDuocMuon.setText(String.valueOf(s.totalBorrowBookQuy1(year)));
                        lbSoSachChuaTra.setText(String.valueOf(s.totalBookNotBackYetTheoQuy1(year)));
                        lbTongPhieuMuon.setText(String.valueOf(s.totalBorrowCardQuy1(year)));
                        lbSoPhieuQuaHan.setText(String.valueOf(s.totalBorrowCardQuaHanQuy1(year)));
                        lbSoPhieuTraDungQD.setText(String.valueOf(s.totalBookBackTheoQuy1(year)));

                        loadTableDataTheoQuy(year);
                    }

                    if (cbbQuy.getSelectionModel().getSelectedItem() == "Quý 2") {
                        lbSoSachDuocMuon.setText(String.valueOf(s.totalBorrowBookQuy2(year)));
                        lbSoSachChuaTra.setText(String.valueOf(s.totalBookNotBackYetTheoQuy2(year)));
                        lbTongPhieuMuon.setText(String.valueOf(s.totalBorrowCardQuy2(year)));
                        lbSoPhieuQuaHan.setText(String.valueOf(s.totalBorrowCardQuaHanQuy2(year)));
                        lbSoPhieuTraDungQD.setText(String.valueOf(s.totalBookBackTheoQuy2(year)));

                        loadTableDataTheoQuy(year);
                    }

                    if (cbbQuy.getSelectionModel().getSelectedItem() == "Quý 3") {
                        lbSoSachDuocMuon.setText(String.valueOf(s.totalBorrowBookQuy3(year)));
                        lbSoSachChuaTra.setText(String.valueOf(s.totalBookNotBackYetTheoQuy3(year)));
                        lbTongPhieuMuon.setText(String.valueOf(s.totalBorrowCardQuy3(year)));
                        lbSoPhieuQuaHan.setText(String.valueOf(s.totalBorrowCardQuaHanQuy3(year)));
                        lbSoPhieuTraDungQD.setText(String.valueOf(s.totalBookBackTheoQuy3(year)));

                        loadTableDataTheoQuy(year);
                    }

                    if (cbbQuy.getSelectionModel().getSelectedItem() == "Quý 4") {
                        lbSoSachDuocMuon.setText(String.valueOf(s.totalBorrowBookQuy4(year)));
                        lbSoSachChuaTra.setText(String.valueOf(s.totalBookNotBackYetTheoQuy4(year)));
                        lbTongPhieuMuon.setText(String.valueOf(s.totalBorrowCardQuy4(year)));
                        lbSoPhieuQuaHan.setText(String.valueOf(s.totalBorrowCardQuaHanQuy4(year)));
                        lbSoPhieuTraDungQD.setText(String.valueOf(s.totalBookBackTheoQuy4(year)));

                        loadTableDataTheoQuy(year);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ThongKeController.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @FXML
    public void timTheoNamClick(ActionEvent evt) throws SQLException {
        if (txtYear.getText().isBlank()) {
            MessageBox.getBox("Thông báo", "Vui lòng nhập vào năm cần xem!", Alert.AlertType.INFORMATION).show();
        } else {
            try {
                //Đưa dữ liệu lên label
                int year = Integer.parseInt(txtYear.getText());
                lbTotalSach.setText(String.valueOf(s.totalBookInLib()));
                lbSoSachDuocMuon.setText(String.valueOf(s.totalBorrowBook(year)));
                lbSoSachChuaTra.setText(String.valueOf(s.totalBookNotBackYet(year)));
                lbTongPhieuMuon.setText(String.valueOf(s.totalBorrowCard(year)));
                lbSoPhieuQuaHan.setText(String.valueOf(s.totalBorrowCardQuaHan(year)));
                lbSoPhieuTraDungQD.setText(String.valueOf(s.totalBookBack(year)));

                loadTableDataTheoNam(year);
            } catch (SQLException ex) {
                Logger.getLogger(ThongKeController.class
                        .getName()).log(Level.SEVERE, null, ex);
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
}