package students;

import alert.AlertMaker;
import database.DatabaseHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import mainWindow.StudentComboBoxInitialize;
import mainWindow.StudentSectionDashboard;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class DeleteStudent {
    DatabaseHandler handler;
    public static StudentSectionDashboard studentSectionDashboard;
    public static StudentComboBoxInitialize studentComboBoxInitialize;

    public DeleteStudent(StudentSectionDashboard sectionDashboard, StudentComboBoxInitialize comboBoxInitialize) {
        handler = DatabaseHandler.getInstance();
        studentSectionDashboard = sectionDashboard;
        studentComboBoxInitialize = comboBoxInitialize;
    }

    public void deleteStudentRecord (String id, int no, StackPane stackPane, Pane pane) {
        Button confirm = new Button("Confirm");
        Button cancel = new Button("Cancel");
        Button button = new Button("OK");
        confirm.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (handler.returnNoOfIssuedBooks(id) == 0) {
                String ac = "DELETE FROM STUDENT_TABLE WHERE id = '" + id + "'";
                if (handler.execAction(ac)) {
                    String ac1 = "UPDATE BATCH SET students = " + handler.returnNoOfStudents(no) + " WHERE batch_no = " + no;
                    if (handler.execAction(ac1)) {
                        studentSectionDashboard.isRefresh = true;
                        studentSectionDashboard.loadStudentDashboardData("SELECT * FROM STUDENT_TABLE");
                        AlertMaker.showMaterialInformationDialog(stackPane, pane, Arrays.asList(button), "Success", "Student Record Successfully Deleted.");
                        return;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Database Error.");
                    return;
                }
            } else {
                AlertMaker.showMaterialErrorDialog(stackPane, pane, Arrays.asList(button), "Error", "Student Record can't be deleted as The book(s) are not yet submitted by him/her.");
                return;
            }
        });
        AlertMaker.showMaterialConfirmationDialog(stackPane, pane, Arrays.asList(confirm, cancel), "Confirmation", "Are you sure you want to delete the record ?");
    }

    public void deleteStudentBatch (StudentHandling.Batch batch, StackPane stackPane, Pane pane) {
        Button confirm = new Button("Confirm");
        Button cancel = new Button("Cancel");
        int no = batch.getBatchNo();

        AlertMaker.showMaterialConfirmationDialog(stackPane, pane, Arrays.asList(confirm, cancel), "Confirmation", "Do you sure want to delete this batch?");
        confirm.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            String query = "SELECT id FROM STUDENT_TABLE WHERE YEAR_JOINING = " + no;
            ResultSet res = handler.execQuery(query);
            try {
                while (res.next()) {
                    String id = res.getString(1);
                    if (handler.returnNoOfIssuedBooks(id) == 0) {
                        String ac = "DELETE FROM STUDENT_TABLE WHERE id = '" + id + "'";
                        if (handler.execAction(ac)) {
                            String ac1 = "UPDATE BATCH SET students = " + handler.returnNoOfStudents(no) + " WHERE batch_no = " + no;
                            if (handler.execAction(ac1)) {
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Database Error.");
                            return;
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            int countStudents = handler.returnNoOfStudents(no);
            if (countStudents == 0) {
                if (handler.deleteBatch(batch)) {
                    Button button = new Button("OK");
                    AlertMaker.showMaterialInformationDialog(stackPane, pane, Arrays.asList(button), "Success", "Successfully Deleted");
                    studentComboBoxInitialize.initializeComboBox();
                } else {
                    JOptionPane.showMessageDialog(null, "Deletion Failed.");
                }
            } else {
                Button button = new Button("OK");
                AlertMaker.showMaterialWarningDialog(stackPane, pane, Arrays.asList(button), "Warning", countStudents + " student records can't be deleted as they haven't submitted their books yet.");
            }
            studentSectionDashboard.isRefresh = true;
            studentSectionDashboard.loadStudentDashboardData("SELECT * FROM STUDENT_TABLE");
        });
    }
}
