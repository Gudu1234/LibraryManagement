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

public class StudentComboBoxInitialize {

    private CheckComboBox<String> checkComboBox;
    private StudentSectionDashboard studentSectionDashboard;
    private CustomTextField idTextField;
    private CustomTextField nameTextField;
    ObservableList<String> list = FXCollections.observableArrayList();
    DatabaseHandler databaseHandler;

    public StudentComboBoxInitialize(CheckComboBox<String> stringCheckComboBox, StudentSectionDashboard sectionDashboard, CustomTextField idTextField, CustomTextField nameTextField) {
        databaseHandler = DatabaseHandler.getInstance();
        checkComboBox = stringCheckComboBox;
        studentSectionDashboard = sectionDashboard;
        this.idTextField = idTextField;
        this.nameTextField = nameTextField;
    }

    public void initializeComboBox () {
        checkComboBox.getCheckModel().clearChecks();
        checkComboBox.getItems().clear();
        list.clear();
        String query = "SELECT BATCH_NO FROM BATCH ORDER BY BATCH_NO";
        ResultSet resultSet = databaseHandler.execQuery(query);
        try {
            while (resultSet.next()) {
                list.add(String.valueOf(resultSet.getInt(1)));
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
                studentSectionDashboard.isRefresh = true;
                if (strings.isEmpty()) {
                    String query = "SELECT * FROM STUDENT_TABLE";
                    studentSectionDashboard.loadStudentDashboardData(query);
                    return;
                }
                for (String no : strings) {
                    int batchNo = Integer.parseInt(no);
                    String query = "SELECT * FROM STUDENT_TABLE WHERE YEAR_JOINING = " + batchNo;
                    studentSectionDashboard.loadStudentDashboardData(query);
                }
            }
        });
    }

    public void initializeTextFields () {
        FontAwesomeIconView closeIcon1 = new FontAwesomeIconView(FontAwesomeIcon.CLOSE);
        closeIcon1.setGlyphSize(16);
        FontAwesomeIconView closeIcon2 = new FontAwesomeIconView(FontAwesomeIcon.CLOSE);
        closeIcon2.setGlyphSize(16);

        FontAwesomeIconView searchIcon1 = new FontAwesomeIconView(FontAwesomeIcon.SEARCH);
        searchIcon1.setGlyphSize(16);
        FontAwesomeIconView searchIcon2 = new FontAwesomeIconView(FontAwesomeIcon.SEARCH);
        searchIcon2.setGlyphSize(16);

        Label label1 = new Label();
        label1.getStyleClass().add("searchBoxLabel");
        label1.setGraphic(searchIcon1);

        Label label2 = new Label();
        label2.getStyleClass().add("searchBoxLabel");
        label2.setGraphic(searchIcon2);

        label1.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (!idTextField.getText().isEmpty()) {
                label1.setGraphic(searchIcon1);
                idTextField.setText("");
            }
        });
        label2.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (!nameTextField.getText().isEmpty()) {
                label2.setGraphic(searchIcon2);
                nameTextField.setText("");
            }
        });

        idTextField.setLeft(label1);
        idTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            studentSectionDashboard.isError = false;
            String query= "";
            if (idTextField.getText().isEmpty()) {
                idTextField.setStyle("-fx-background-color: white");
                label1.setGraphic(searchIcon1);
                studentSectionDashboard.isRefresh = true;
                studentSectionDashboard.loadStudentDashboardData("SELECT * FROM STUDENT_TABLE");
                return;
            }
            label1.setGraphic(closeIcon1);
            nameTextField.setText("");
            query = "SELECT * FROM STUDENT_TABLE WHERE id LIKE '" + newValue + "%'";
            studentSectionDashboard.isRefresh = true;
            studentSectionDashboard.loadStudentDashboardData(query);
            if (studentSectionDashboard.isError) {
                idTextField.setStyle("-fx-background-color: red");
            } else {
                idTextField.setStyle("-fx-background-color: white");
            }
        });

        nameTextField.setLeft(label2);
        nameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            studentSectionDashboard.isError = false;
            String query= "";
            if (nameTextField.getText().isEmpty()) {
                nameTextField.setStyle("-fx-background-color: white");
                label2.setGraphic(searchIcon2);
                studentSectionDashboard.isRefresh = true;
                studentSectionDashboard.loadStudentDashboardData("SELECT * FROM STUDENT_TABLE");
                return;
            }
            label2.setGraphic(closeIcon2);
            idTextField.setText("");
            query = "SELECT * FROM STUDENT_TABLE WHERE name LIKE '" + newValue + "%'";
            studentSectionDashboard.isRefresh = true;
            studentSectionDashboard.loadStudentDashboardData(query);
            if (studentSectionDashboard.isError) {
                nameTextField.setStyle("-fx-background-color: red");
            } else {
                nameTextField.setStyle("-fx-background-color: white");
            }
        });

    }

}
