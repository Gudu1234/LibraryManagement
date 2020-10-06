package books;

import alert.AlertMaker;
import database.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import mainWindow.BookComboBoxInitialize;

import javax.swing.*;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class AddBookSectionController implements Initializable {
    public TextField tfBookType;
    public TextField tfBookCapacity;
    public Button btnAddBookSection;
    public Button btnCancel;
    public Label lblSuccess;

    public static AnchorPane anchorPane;
    DatabaseHandler databaseHandler;
    Boolean isVisible = false;
    public static BookComboBoxInitialize bookComboBoxInitialize;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        databaseHandler = DatabaseHandler.getInstance();
    }

    public void handleAddBookSection(ActionEvent actionEvent) {
        String bookType = tfBookType.getText().replaceAll(" ", "").toLowerCase();
        String typeName = tfBookType.getText();
        int bookCapacity = Integer.parseInt(tfBookCapacity.getText());
        String action = "INSERT INTO BOOK_SECTION VALUES ('" + bookType + "', " + "'" + typeName + "', " + bookCapacity + ", " + 0 + ", " + 0 + ")";
        if (databaseHandler.execAction(action)) {
            bookComboBoxInitialize.initializeBookComboBox();
            lblSuccess.setVisible(!isVisible);
            lblSuccess.setText("Book Section is Successfully Added.");
        } else {
            lblSuccess.setVisible(!isVisible);
            lblSuccess.setText("A Book Section with this name is already Present.");
            lblSuccess.setStyle("-fx-text-fill: red");
        }
    }

    public void handleCancel(ActionEvent actionEvent) {
        ((Stage)tfBookCapacity.getScene().getWindow()).close();
        anchorPane.setEffect(null);
    }

    public void init(BookComboBoxInitialize comboBoxInitialize, AnchorPane pane) {
        bookComboBoxInitialize = comboBoxInitialize;
        anchorPane = pane;
    }

    public void handleCloseOperation(MouseEvent mouseEvent) {
        ((Stage)tfBookCapacity.getScene().getWindow()).close();
        anchorPane.setEffect(null);
    }
}
