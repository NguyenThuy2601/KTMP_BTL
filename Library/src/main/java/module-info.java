module com.mycompany.library {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    opens com.nhom2.library to javafx.fxml;
    exports com.nhom2.library;
    exports com.nhom2.pojo;
    exports com.nhom2.service;
}
