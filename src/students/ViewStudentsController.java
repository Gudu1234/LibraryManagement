package students;

import alert.AlertMaker;
import database.DatabaseHandler;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mainWindow.StudentComboBoxInitialize;
import mainWindow.StudentSectionDashboard;
import org.controlsfx.control.textfield.CustomTextField;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ViewStudentsController implements Initializable {
    public StackPane stackPane;
    public AnchorPane anchorPane;
    
    public TableView<Student> tableViewStudentData;
    public CustomTextField tfSearchID;
    public CustomTextField tfSearchName;
    public Button btnAddStudentRecord;
    public Button btnDelStudentRecord;
    public Button btnEditStudentInfo;
    public TableColumn<Student, String> idCol;
    public TableColumn<Student, String> nameCol;
    public TableColumn<Student, String> contactCol;
    public TableColumn<Student, Integer> issueCol;

    static int batchno = 0;
    static int totalseats = 0;
    static int totalstudents = 0;
    static String query = "";

    Boolean errorFlag = false;


    public static StudentSectionDashboard studentSectionDashboard;
    public static StudentComboBoxInitialize studentComboBoxInitialize;
    DatabaseHandler databaseHandler;
    ObservableList<Student> list1 = FXCollections.observableArrayList();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        databaseHandler = DatabaseHandler.getInstance();
        initCol();
        query = "SELECT * FROM STUDENT_TABLE WHERE YEAR_JOINING = " + batchno + " ORDER BY ID";
        loadData(query);

        initializeStudentTextFields();
        tableViewStudentData.requestFocus();
        tableViewStudentData.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER:
                    showDialog();
                default:
                    return;
            }
        });
    }

    private void initializeStudentTextFields() {
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
            if (!tfSearchID.getText().isEmpty()) {
                label1.setGraphic(searchIcon1);
                tfSearchID.setText("");
            }
        });
        label2.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (!tfSearchName.getText().isEmpty()) {
                label2.setGraphic(searchIcon2);
                tfSearchName.setText("");
            }
        });

        tfSearchID.setLeft(label1);
        tfSearchID.textProperty().addListener((observable, oldValue, newValue) -> {
            errorFlag = false;
            String query= "";
            if (tfSearchID.getText().isEmpty()) {
                tfSearchID.setStyle("-fx-background-color: white");
                label1.setGraphic(searchIcon1);
                tableViewStudentData.getItems().clear();
                loadData("SELECT * FROM STUDENT_TABLE WHERE YEAR_JOINING = " + batchno);
                return;
            }
            label1.setGraphic(closeIcon1);
            tfSearchName.setText("");
            query = "SELECT * FROM STUDENT_TABLE WHERE YEAR_JOINING = " + batchno + " AND id LIKE '" + newValue + "%'";
            tableViewStudentData.getItems().clear();
            loadData(query);
            if (errorFlag) {
                tfSearchID.setStyle("-fx-background-color: red");
            } else {
                tfSearchID.setStyle("-fx-background-color: white");
            }
        });
        tfSearchName.setLeft(label2);
        tfSearchName.textProperty().addListener((observable, oldValue, newValue) -> {
            errorFlag = false;
            String query= "";
            if (tfSearchName.getText().isEmpty()) {
                tfSearchName.setStyle("-fx-background-color: white");
                label2.setGraphic(searchIcon2);
                tableViewStudentData.getItems().clear();
                loadData("SELECT * FROM STUDENT_TABLE WHERE year_joining = " + batchno);
                return;
            }
            label2.setGraphic(closeIcon2);
            tfSearchID.setText("");
            query = "SELECT * FROM STUDENT_TABLE WHERE name LIKE '" + newValue + "%' AND year_joining = " + batchno;
            tableViewStudentData.getItems().clear();
            loadData(query);
            if (errorFlag) {
                tfSearchName.setStyle("-fx-background-color: red");
            } else {
                tfSearchName.setStyle("-fx-background-color: white");
            }
        });
    }

    private void showDialog() {
        Student student = tableViewStudentData.getSelectionModel().getSelectedItem();
        if (student == null) {
            return;
        }

        StudentInfoDialog studentInfoDialog = new StudentInfoDialog();
        studentInfoDialog.init(student, batchno, stackPane, anchorPane);
        studentInfoDialog.showInfoDialog();
    }

    private void initCol() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("studentContact"));
        issueCol.setCellValueFactory(new PropertyValueFactory<>("noOfIssuedBooks"));
    }

    private void loadData(String query) {
        ResultSet res = databaseHandler.execQuery(query);
        try {
            while (res.next()) {
                String id = res.getString("id");
                String name = res.getString("name");
                String contact = res.getString("contact");
                int issue = res.getInt(5);

                list1.add(new Student(id, name, contact, issue));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (list1.size() == 0) {
            errorFlag = true;
        } else {
            errorFlag = false;
        }
        tableViewStudentData.setItems(list1);
        tableViewStudentData.getSelectionModel().select(0);
    }

    public void init(StudentHandling.Batch batch, StudentSectionDashboard studentSectionDashboard, StudentComboBoxInitialize comboBoxInitialize) {
        batchno = batch.getBatchNo();
        totalseats = batch.getTotalSeats();
        totalstudents = batch.getTotalStudents();
        this.studentSectionDashboard = studentSectionDashboard;
        studentComboBoxInitialize = comboBoxInitialize;
    }

    public void handleRefreshOperation(MouseEvent actionEvent) {
        tableViewStudentData.getItems().clear();
        loadData(query);
        tfSearchID.setText("");
    }

    public void handleSearchIDOperation(ActionEvent actionEvent) {
        if (tfSearchID.getText().isEmpty()) {
            tableViewStudentData.getItems().clear();
            loadData(query);
            return;
        }
        String query1 = "SELECT * FROM STUDENT_TABLE WHERE ID = '" + tfSearchID.getText() + "'";
        tableViewStudentData.getItems().clear();
        loadData(query1);
    }

    public void handleAddStudentRecordOperation(ActionEvent actionEvent) {
        new AddStudentsBatchController().init(batchno, studentSectionDashboard, anchorPane);
        loadWindow("Add Student", "/students/add_students_batch.fxml");
        totalstudents = databaseHandler.returnNoOfStudents(batchno);
    }

    public void handleDelStudentRecordOperation(ActionEvent actionEvent) {
        Student student = tableViewStudentData.getSelectionModel().getSelectedItem();
        if (student == null) {
            Button button = new Button("OK");
            AlertMaker.showMaterialWarningDialog(stackPane, anchorPane, Arrays.asList(button), "Warning", "Please Select a Student First.");
            return;
        }
        new DeleteStudent(studentSectionDashboard, studentComboBoxInitialize).deleteStudentRecord(student.getStudentId(), batchno, stackPane, anchorPane);
    }

    public void loadWindow(String title, String url) {
        try {
            BoxBlur boxBlur = new BoxBlur(4, 4, 4);

            Parent root = FXMLLoader.load(getClass().getResource(url));
            Stage stage = new Stage(StageStyle.TRANSPARENT);
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.setAlwaysOnTop(true);
            anchorPane.setEffect(boxBlur);
            stage.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
                stage.requestFocus();
            });
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleSearchNameOperation(ActionEvent actionEvent) {

    }

    public void handleEditStudentInformation(ActionEvent actionEvent) {
        Student student = tableViewStudentData.getSelectionModel().getSelectedItem();
        if (student == null) {
            Button button = new Button("OK");
            AlertMaker.showMaterialWarningDialog(stackPane, anchorPane, Arrays.asList(button), "Warning", "Please select a Student for edit.");
            return;
        }

        new AddStudentsBatchController().init(batchno, studentSectionDashboard, anchorPane);
        try {
            BoxBlur boxBlur = new BoxBlur(4,4,4);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/students/add_students_batch.fxml"));
            Parent parent = loader.load();

            AddStudentsBatchController controller = (AddStudentsBatchController) loader.getController();
            controller.inflateUI(student);

            Stage stage = new Stage(StageStyle.TRANSPARENT);
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);
            stage.setTitle("Edit Student Info");
            stage.show();
            anchorPane.setEffect(boxBlur);
        } catch (IOException e) {
            e.printStackTrace();
        }
        studentSectionDashboard.isRefresh = true;
        studentSectionDashboard.loadStudentDashboardData("SELECT * FROM STUDENT_TABLE");
    }

    public static class Student {
        private final SimpleStringProperty studentId;
        private final SimpleStringProperty studentName;
        private final SimpleStringProperty studentContact;
        private final SimpleIntegerProperty noOfIssuedBooks;

        public Student(String studentId, String studentName, String studentContact, int noOfIssuedBooks) {
            this.studentId = new SimpleStringProperty(studentId);
            this.studentName = new SimpleStringProperty(studentName);
            this.studentContact = new SimpleStringProperty(studentContact);
            this.noOfIssuedBooks = new SimpleIntegerProperty(noOfIssuedBooks);
        }

        public String getStudentId() {
            return studentId.get();
        }

        public String getStudentName() {
            return studentName.get();
        }

        public String getStudentContact() {
            return studentContact.get();
        }

        public int getNoOfIssuedBooks() { return noOfIssuedBooks.get(); }
    }
}
