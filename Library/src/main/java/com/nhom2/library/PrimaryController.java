package com.nhom2.library;

import com.nhom2.pojo.PhieuDat;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        
        App.setRoot("secondary");
    }
}
