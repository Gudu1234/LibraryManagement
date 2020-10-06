package students;

import alert.AlertMaker;
import database.DatabaseHandler;
import javafx.animation.Interpolator;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import mainWindow.StudentSectionDashboard;

import javax.swing.*;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class AddStudentsBatchController implements Initializable {
    public TextField tfStudentId;
    public TextField tfStudentName;
    public TextField tfJoiningYear;
    public TextField tfContactStudent;
    public Button btnAddStudent;
    public Button btnCancel;
    public Label lblSuccess;
    public Pane paneAddStudents;
    public StackPane stackPane;
    public AnchorPane anchorPane;

    public static StudentSectionDashboard studentSectionDashboard;
    public static AnchorPane pane;

    DatabaseHandler databaseHandler;
    boolean isLabelVisible = false;
    int issue=0;
    Boolean isEditMode = false;
    static int batchNo;
    static String ptText = "";
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        databaseHandler = DatabaseHandler.getInstance();
        tfStudentId.setPromptText(ptText);
        tfJoiningYear.setText(String.valueOf(batchNo));
        tfJoiningYear.setEditable(false);
    }

    public void handleAddStudentOperation(ActionEvent actionEvent) {

        String studentId = tfStudentId.getText();
        String studentName = tfStudentName.getText();
        int yearJoining = Integer.parseInt(tfJoiningYear.getText());
        String contact = tfContactStudent.getText();

        if (studentId.isEmpty() || studentName.isEmpty() || contact.isEmpty()) {
            lblSuccess.setVisible(!isLabelVisible);
            lblSuccess.setText("Please Fill all The fields to Add the record.");
            return;
        }

        if (isEditMode) {
            handleUpdate();
            return;
        }

        if (databaseHandler.checkValidEntry(batchNo)) {

            String ac = "INSERT INTO STUDENT_TABLE VALUES (" + "'" + studentId + "', " + "'" + studentName + "', " + yearJoining + ", '" + contact + "', " + 0 + ")";
            System.out.println(ac);
            if (databaseHandler.execAction(ac)) {
                Button button = new Button("OK");
                AlertMaker.showMaterialInformationDialog(stackPane, anchorPane, Arrays.asList(button), "Success", "Successfully added a Student Record.");
                String ac1 = "UPDATE BATCH SET STUDENTS = " + databaseHandler.returnNoOfStudents(batchNo) + " WHERE BATCH_NO = " + batchNo;
                if (databaseHandler.execAction(ac1)) {
                    //JOptionPane.showMessageDialog(null, "Successfully updated Batch table.Refresh it.");
                    lblSuccess.setVisible(!isLabelVisible);
                    lblSuccess.setText("Successfully added a record");
                    studentSectionDashboard.isRefresh = true;
                    studentSectionDashboard.loadStudentDashboardData("SELECT * FROM STUDENT_TABLE");
                } else {
                    JOptionPane.showMessageDialog(null, "Can't update Batch table.");
                }

            } else {
                JOptionPane.showMessageDialog(null, "Error occurred.");
            }
        } else {
            Button button = new Button("OK");
            AlertMaker.showMaterialWarningDialog(stackPane, anchorPane, Arrays.asList(button), "Warning", "You can't add more no. of Records. Already reached Maximum.");
        }
        paneAddStudents.requestFocus();
    }

    public void inflateUI(ViewStudentsController.Student student) {
        tfStudentId.setText(student.getStudentId());
        tfStudentId.setEditable(false);
        tfStudentName.setText(student.getStudentName());
        tfJoiningYear.setText(String.valueOf(batchNo));
        tfJoiningYear.setEditable(false);
        tfContactStudent.setText(student.getStudentContact());
        isEditMode = true;
        issue = student.getNoOfIssuedBooks();
        btnAddStudent.setText("Update");
    }

    public void handleUpdate() {
        ViewStudentsController.Student student = new ViewStudentsController.Student(tfStudentId.getText(), tfStudentName.getText(), tfContactStudent.getText(), issue);
        if (databaseHandler.updateStudent(student)) {
            lblSuccess.setVisible(!isLabelVisible);
            lblSuccess.setText("Successfully Updated the Record.");
            studentSectionDashboard.isRefresh = true;
            studentSectionDashboard.loadStudentDashboardData("SELECT * FROM STUDENT_TABLE");
            Button button = new Button("OK");
            AlertMaker.showMaterialInformationDialog(stackPane, anchorPane, Arrays.asList(button), "Success", "Successfully updated the Student Record.");
        } else {
            lblSuccess.setVisible(!isLabelVisible);
            lblSuccess.setText("Updation Failed.");
        }
    }

    public void handleCancelOperation(ActionEvent actionEvent) {
        ((Stage)tfJoiningYear.getScene().getWindow()).close();
        pane.setEffect(null);
    }

    public void init (int no, StudentSectionDashboard sectionDashboard, AnchorPane anchorPane1) {
        batchNo = no;
        ptText = batchNo%2000 + "IMCA....";
        studentSectionDashboard = sectionDashboard;
        pane = anchorPane1;
    }

    public void handleCloseOperation(MouseEvent mouseEvent) {
        ((Stage)tfJoiningYear.getScene().getWindow()).close();
        pane.setEffect(null);
    }
}
