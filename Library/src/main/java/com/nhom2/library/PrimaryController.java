package com.nhom2.library;

import com.nhom2.AppUtils.MessageBox;
import com.nhom2.pojo.BookResponse;
import com.nhom2.pojo.DanhMuc;
import com.nhom2.pojo.PhieuDat;
import com.nhom2.pojo.ReservationCardResponse;
import com.nhom2.pojo.User;
import com.nhom2.service.HomepageService;
import com.nhom2.service.ReservationService;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class PrimaryController implements Initializable {

    @FXML
    Button profileBtn;
    @FXML
    Button reservationCardBtn;
    @FXML
    Button retunBookBtn;
    @FXML
    MenuItem directBorrowMN;
    @FXML
    MenuItem confirmReservationCardMN;
    @FXML
    MenuButton borrowBtn;
    @FXML
    Button borrowCardBtn;
    @FXML
    Button statisticBtn;
    @FXML
    Button loginMN;
    @FXML
    Button logoutMN;
    @FXML
    Button bookABook;
    @FXML
    TableView<BookResponse> tbBook;
    @FXML
    ComboBox<DanhMuc> cateCbb;
    @FXML
    TextField bookNameTxt;
    @FXML
    TextField PublishYearTxt;
    @FXML
    TextField authorNameTxt;


    User u;
    HomepageService s;
    ReservationService rs;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        s = new HomepageService();
        rs = new ReservationService();
        loadTableColumns();
        try {
            List<DanhMuc> cates = s.getDanhMucSach();
            this.cateCbb.setItems(FXCollections.observableList(cates));
            loadTableData();
        } catch (SQLException ex) {
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        u = new User();
        if (u.getuID() == 0) {
            profileBtn.setVisible(false);
            reservationCardBtn.setManaged(false);
            retunBookBtn.setManaged(false);
            borrowBtn.setManaged(false);
            borrowCardBtn.setManaged(false);
            statisticBtn.setManaged(false);
            logoutMN.setManaged(false);
            bookABook.setManaged(false);
        }

        tbBook.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    }

    public void authorization() {
        logoutMN.setManaged(true);
        loginMN.setManaged(false);
        if (u.getAccID().contains("DG")) {
            profileBtn.setVisible(true);
            reservationCardBtn.setManaged(true);
            borrowCardBtn.setManaged(true);
            bookABook.setManaged(true);
            bookABook.setDisable(true);
        } else {
            retunBookBtn.setManaged(true);
            borrowBtn.setManaged(true);
            statisticBtn.setManaged(true);
        }
    }

    public void setLoginUser(User uLogin) {
        this.u = uLogin;
    }

    @FXML
    public void loginMNClick(ActionEvent evt) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e) {
            return;
        }
    }

    @FXML
    public void logoutBtnClick(ActionEvent evt) {
        u = new User();
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
    
   

    private void loadTableColumns() {
        TableColumn colName = new TableColumn("Tên sách");
        colName.setCellValueFactory(new PropertyValueFactory("Ten"));
        colName.setPrefWidth(250);

        TableColumn colYear = new TableColumn("Năm xuất bản");
        colYear.setCellValueFactory(new PropertyValueFactory("NamXB"));

        TableColumn colPublisher = new TableColumn("Nhà xuất bản");
        colPublisher.setCellValueFactory(new PropertyValueFactory("NoiXB"));

        TableColumn colImportDate = new TableColumn("Ngày nhập");
        colImportDate.setCellValueFactory(new PropertyValueFactory("NgayNhap"));

        TableColumn colQty = new TableColumn("Số lượng");
        colQty.setCellValueFactory(new PropertyValueFactory("SoLuong"));

        TableColumn colAuthors = new TableColumn("Tác giả");
        colAuthors.setCellValueFactory(new PropertyValueFactory("TenTG"));

        TableColumn colCate = new TableColumn("Danh mục");
        colCate.setCellValueFactory(new PropertyValueFactory("TenDM"));

        TableColumn colLocation = new TableColumn("Vị trí sách");
        colLocation.setCellValueFactory(new PropertyValueFactory("ViTri"));

        this.tbBook.getColumns().addAll(colName, colYear, colPublisher,
                colImportDate, colQty,
                colAuthors, colCate, colLocation);
    }

    private void loadTableData() throws SQLException {

        List<BookResponse> books = s.getBooks();

        this.tbBook.getItems().clear();
        this.tbBook.setItems(FXCollections.observableList(books));

    }
    
    

    private void loadTableDataAfterFiltering(List<BookResponse> list) throws SQLException {

        this.tbBook.getItems().clear();
        this.tbBook.setItems(FXCollections.observableList(list));

    }
    
    public void resetFilteringValue()
    {
        cateCbb.getSelectionModel().clearSelection();
        PublishYearTxt.setText("");
        bookNameTxt.setText("");
        authorNameTxt.setText("");
    }

    @FXML
    public void bookABookBtnClick(ActionEvent evt) {

        if (tbBook.getSelectionModel().getSelectedItem() != null) {
            int idAvailableCopy = 0;
            try {
                idAvailableCopy = rs.getAvailableIdSach_Copies(tbBook.getSelectionModel().getSelectedItem().getIdSach());
            } catch (SQLException ex) {
                Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (idAvailableCopy != 0) {
                PhieuDat p = new PhieuDat(idAvailableCopy,
                        LocalDateTime.now(),
                        this.u.getuID());
                try {
                    if (rs.createReservationCard(p)) {
                        MessageBox.getBox("Thông báo", "Đặt sách thành công", Alert.AlertType.INFORMATION).show();
                        tbBook.getSelectionModel().clearSelection();
                    } else {
                        MessageBox.getBox("Thông báo", "Đặt sách không thành công", Alert.AlertType.INFORMATION).show();
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                MessageBox.getBox("Thông báo", "Đã có lỗi xảy ra", Alert.AlertType.INFORMATION).show();
            }

            try {
                loadTableData();
            } catch (SQLException ex) {
                Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @FXML
    public void tbBookClick(MouseEvent evt) {
        if (tbBook.getSelectionModel().getSelectedItem().getSoLuong() == 0) {
            bookABook.setDisable(true);
        } else {
            bookABook.setDisable(false);
        }
    }

    @FXML
    public void profileBtnClick(ActionEvent evt) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hoso.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            ProfileController c = fxmlLoader.getController();
            c.setLoginUser(u);
            c.loadLoginUserInfo();
            Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void findBtnClick(ActionEvent evt) {
        if (PublishYearTxt.getText().isBlank() && bookNameTxt.getText().isBlank() && authorNameTxt.getText().isBlank()
                && cateCbb.getSelectionModel().getSelectedItem() == null)
                try {
            loadTableData();
        } catch (SQLException ex) {
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
        } else {
            if (!s.checkYearFormat(PublishYearTxt.getText())) {
                MessageBox.getBox("Thông báo", "Năm không đúng định dạng", Alert.AlertType.INFORMATION).show();
            } else {
                try {
                    List<BookResponse> l = s.getBooks();
                    l = s.findBook(l, bookNameTxt.getText(), authorNameTxt.getText(),
                            s.parseYear(PublishYearTxt.getText()),
                            s.checkNullSelectedComboBoxItem(cateCbb.getSelectionModel().getSelectedItem()));
                    loadTableDataAfterFiltering(l);
                     resetFilteringValue();
                    
                } catch (SQLException ex) {
                    Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

    }
    
     @FXML
    public void reservationCardListBtnClick(ActionEvent evt) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("danhsachphieudat.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            ReservatioCardViewController c = fxmlLoader.getController();
            c.setLoginUser(u);
            Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    @FXML
    public void borrowCardListBtnClick(ActionEvent evt) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("xemphieumuon.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            BorrowCardViewController c = fxmlLoader.getController();
            c.setLoginUser(u);
            Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

    }
    
    @FXML
    public void resetBtnClick(ActionEvent evt) {
        try {
            loadTableData();
        } catch (SQLException ex) {
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
