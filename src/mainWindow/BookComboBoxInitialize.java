package mainWindow;

import database.DatabaseHandler;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.textfield.CustomTextField;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookComboBoxInitialize {

    private CheckComboBox<String> checkComboBox;
    BookSectionDashboard bookSectionDashboard;
    private CustomTextField idTextField;
    private CustomTextField authorTextField;
    private CustomTextField nameTextField;
    ObservableList<String> list = FXCollections.observableArrayList();
    DatabaseHandler databaseHandler;

    public BookComboBoxInitialize(CheckComboBox<String> checkComboBox, CustomTextField idTextField, CustomTextField authorTextField, CustomTextField nameTextField, BookSectionDashboard bookSectionDashboard) {
        this.checkComboBox = checkComboBox;
        this.idTextField = idTextField;
        this.authorTextField = authorTextField;
        this.nameTextField = nameTextField;
        this.bookSectionDashboard = bookSectionDashboard;
        databaseHandler = DatabaseHandler.getInstance();
    }

    public void initializeBookComboBox () {
        checkComboBox.getCheckModel().clearChecks();
        checkComboBox.getItems().clear();
        list.clear();
        String query = "SELECT TYPE_NAME FROM BOOK_SECTION ORDER BY type";
        ResultSet resultSet = databaseHandler.execQuery(query);
        try {
            while (resultSet.next()) {
                list.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error in showing ComboBox.");
            e.printStackTrace();
        }
        checkComboBox.getItems().addAll(list);
        checkComboBox.getCheckModel().checkAll();

        checkComboBox.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> c) {
                ObservableList<String> strings = checkComboBox.getCheckModel().getCheckedItems();
                bookSectionDashboard.isRefresh = true;
                if (strings.isEmpty()) {
                    String query = "SELECT * FROM BOOK_TABLE";
                    bookSectionDashboard.loadBookDashboardData(query);
                    return;
                }
                for (String type : strings) {
                    String query = "SELECT * FROM BOOK_TABLE WHERE book_type = '" + type + "'";
                    bookSectionDashboard.loadBookDashboardData(query);
                }
            }
        });
    }
    public void initializeTextFieldsBookSecttion () {
        FontAwesomeIconView closeIcon1 = new FontAwesomeIconView(FontAwesomeIcon.CLOSE);
        closeIcon1.setGlyphSize(16);
        FontAwesomeIconView closeIcon2 = new FontAwesomeIconView(FontAwesomeIcon.CLOSE);
        closeIcon2.setGlyphSize(16);
        FontAwesomeIconView closeIcon3 = new FontAwesomeIconView(FontAwesomeIcon.CLOSE);
        closeIcon3.setGlyphSize(16);

        FontAwesomeIconView searchIcon1 = new FontAwesomeIconView(FontAwesomeIcon.SEARCH);
        searchIcon1.setGlyphSize(16);
        FontAwesomeIconView searchIcon2 = new FontAwesomeIconView(FontAwesomeIcon.SEARCH);
        searchIcon2.setGlyphSize(16);
        FontAwesomeIconView searchIcon3 = new FontAwesomeIconView(FontAwesomeIcon.SEARCH);
        searchIcon3.setGlyphSize(16);

        Label label1 = new Label();
        label1.getStyleClass().add("searchBoxLabel");
        label1.setGraphic(searchIcon1);

        Label label2 = new Label();
        label2.getStyleClass().add("searchBoxLabel");
        label2.setGraphic(searchIcon2);

        Label label3 = new Label();
        label3.getStyleClass().add("searchBoxLabel");
        label3.setGraphic(searchIcon3);

        label1.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (!idTextField.getText().isEmpty()) {
                label1.setGraphic(searchIcon1);
                idTextField.setText("");
            }
        });
        label2.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (!authorTextField.getText().isEmpty()) {
                label2.setGraphic(searchIcon2);
                authorTextField.setText("");
            }
        });
        label3.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (!nameTextField.getText().isEmpty()) {
                label3.setGraphic(searchIcon3);
                nameTextField.setText("");
            }
        });

        idTextField.setLeft(label1);
        idTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            bookSectionDashboard.isError = false;
            String query= "";
            if (idTextField.getText().isEmpty()) {
                idTextField.setStyle("-fx-background-color: white");
                label1.setGraphic(searchIcon1);
                bookSectionDashboard.isRefresh = true;
                bookSectionDashboard.loadBookDashboardData("SELECT * FROM BOOK_TABLE");
                return;
            }
            label1.setGraphic(closeIcon1);
            query = "SELECT * FROM BOOK_TABLE WHERE book_id LIKE '" + newValue + "%'";
            bookSectionDashboard.isRefresh = true;
            bookSectionDashboard.loadBookDashboardData(query);
            if (bookSectionDashboard.isError) {
                idTextField.setStyle("-fx-background-color: red");
            } else {
                idTextField.setStyle("-fx-background-color: white");
            }
        });

        authorTextField.setLeft(label2);
        authorTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            bookSectionDashboard.isError = false;
            String query= "";
            if (authorTextField.getText().isEmpty()) {
                authorTextField.setStyle("-fx-background-color: white");
                label2.setGraphic(searchIcon2);
                bookSectionDashboard.isRefresh = true;
                bookSectionDashboard.loadBookDashboardData("SELECT * FROM BOOK_TABLE");
                return;
            }
            label2.setGraphic(closeIcon2);
            query = "SELECT * FROM BOOK_TABLE WHERE author LIKE '" + newValue + "%'";
            bookSectionDashboard.isRefresh = true;
            bookSectionDashboard.loadBookDashboardData(query);
            if (bookSectionDashboard.isError) {
                authorTextField.setStyle("-fx-background-color: red");
            } else {
                authorTextField.setStyle("-fx-background-color: white");
            }
        });

        nameTextField.setLeft(label3);
        nameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            bookSectionDashboard.isError = false;
            String query= "";
            if (nameTextField.getText().isEmpty()) {
                nameTextField.setStyle("-fx-background-color: white");
                label3.setGraphic(searchIcon3);
                bookSectionDashboard.isRefresh = true;
                bookSectionDashboard.loadBookDashboardData("SELECT * FROM BOOK_TABLE");
                return;
            }
            label3.setGraphic(closeIcon3);
            query = "SELECT * FROM BOOK_TABLE WHERE book_name LIKE '" + newValue + "%'";
            bookSectionDashboard.isRefresh = true;
            bookSectionDashboard.loadBookDashboardData(query);
            if (bookSectionDashboard.isError) {
                nameTextField.setStyle("-fx-background-color: red");
            } else {
                nameTextField.setStyle("-fx-background-color: white");
            }
        });

    }
}
