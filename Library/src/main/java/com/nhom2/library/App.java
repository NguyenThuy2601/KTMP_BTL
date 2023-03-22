package com.nhom2.library;

import com.nhom2.pojo.PhieuDat;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
//        PhieuDat p = new PhieuDat(1, LocalDateTime.now(), 1);
//        DateTimeFormatter fmt3 = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
//        String d = p.getNgayDat().format(fmt3);
//        String t [] = d.split(" ");
//        System.out.println(d);
//        System.out.println(t[0]);
//        System.out.println(t[1]);

    }

}