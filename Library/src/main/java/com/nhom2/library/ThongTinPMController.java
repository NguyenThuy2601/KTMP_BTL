/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom2.library;

import com.nhom2.pojo.BorrowCardResponse;
import com.nhom2.pojo.User;
import com.nhom2.service.BorrowBookService;
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
public class ThongTinPMController implements Initializable {

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
    BorrowBookService b;
    BookBorrowController c;
    int idDG;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Thong tin sau xac nhan
        b = new BorrowBookService();
        c = new BookBorrowController();

        txtMaPhieu.setEditable(false);
        txtMaSach.setEditable(false);
        txtMaDocGia.setEditable(false);
        txtNgayMuon.setEditable(false);
        txtTinhTrang.setEditable(false);
        txtTenDG.setEditable(false);
        borrowCardList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        loadTableColumns();

    }

    public void setLoginUser(User uLogin) {
        this.u = uLogin;
    }

    public void setMaDG(int id) throws SQLException {
        this.idDG = id;
        try {
            loadTableData();
        } catch (SQLException ex) {
            Logger.getLogger(ThongTinPMController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        }
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

    private void loadTableData() throws SQLException {

        List<BorrowCardResponse> card = b.getBorrowCard(idDG);

        this.borrowCardList.getItems().clear();
        this.borrowCardList.setItems(FXCollections.observableList(card));

    }

    @FXML
    public void tbCardListClick(MouseEvent evt) {
        txtMaPhieu.setText(borrowCardList.getSelectionModel().getSelectedItem().getIdPhieuMuon());
        txtMaSach.setText(Integer.toString(borrowCardList.getSelectionModel().getSelectedItem().getIdSach()));
        txtNgayMuon.setText(borrowCardList.getSelectionModel().getSelectedItem().getNgayMuon());
        txtMaDocGia.setText(Integer.toString(borrowCardList.getSelectionModel().getSelectedItem().getIdUser()));
        txtTinhTrang.setText(borrowCardList.getSelectionModel().getSelectedItem().getTinhTrang());
        txtTenDG.setText(borrowCardList.getSelectionModel().getSelectedItem().getHoLotTen());
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
