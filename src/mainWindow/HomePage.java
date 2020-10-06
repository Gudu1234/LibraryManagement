package mainWindow;

import database.DatabaseHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;


public class HomePage extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        JOptionPane.showMessageDialog(null, "Loading...");
        Parent root = FXMLLoader.load(getClass().getResource("home_page.fxml"));
        JOptionPane.showMessageDialog(null, "Loaded....");
        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
        });

        //primaryStage.setMaximized(true);
        primaryStage.show();

    }

    @Override
    public void stop() throws Exception {
        String action3 = "DELETE FROM TYPE_CHECK";
        DatabaseHandler.getInstance().execAction(action3);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
