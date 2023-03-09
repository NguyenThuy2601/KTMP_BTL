module com.mycompany.library {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.library to javafx.fxml;
    exports com.mycompany.library;
}
