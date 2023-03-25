package com.nhom2.library;

import com.nhom2.pojo.User;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class SecondaryController  implements  Initializable  {
    @FXML private TextField txtMaDG;
    @FXML private TextField txtMaSach1;
    @FXML private TextField txtMaSach2;
    @FXML private TextField txtMaSach3;
    @FXML private TextField txtMaSach4;
    @FXML private TextField txtMaSach5;
    @FXML private Button btXacNhan;
    
    public void confirmBorrowBooks (ActionEvent event) {
        int idDG = Integer.parseInt(this.txtMaDG.getText());
        int idSach1 = Integer.parseInt(this.txtMaSach1.getText());
        int idSach2 = Integer.parseInt(this.txtMaSach2.getText());
        int idSach3 = Integer.parseInt(this.txtMaSach3.getText());
        int idSach4 = Integer.parseInt(this.txtMaSach4.getText());
        int idSach5 = Integer.parseInt(this.txtMaSach5.getText());
        
        
    }
    
    
     @Override
    public void initialize(URL url, ResourceBundle rb) {
      
    }
}