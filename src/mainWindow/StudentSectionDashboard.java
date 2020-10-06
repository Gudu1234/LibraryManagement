package mainWindow;

import database.DatabaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import students.ViewStudentsController;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentSectionDashboard {
    public TableView<ViewStudentsController.Student> tableViewStudentDashboard;
    public TableColumn<ViewStudentsController.Student, String> studentRollNoCol;
    public TableColumn<ViewStudentsController.Student, String> studentNameCol;
    public TableColumn<ViewStudentsController.Student, String> studentContactCol;
    public TableColumn<ViewStudentsController.Student, Integer> noOfIssuedBooksCol;

    public Pane pane;
    public TextArea textArea;
    ObservableList<ViewStudentsController.Student> students = FXCollections.observableArrayList();
    DatabaseHandler databaseHandler;

    public Boolean isRefresh = false;
    Boolean isError = false;

    public StudentSectionDashboard(TableView<ViewStudentsController.Student> tableViewStudentDashboard, TableColumn<ViewStudentsController.Student, String> studentRollNoCol, TableColumn<ViewStudentsController.Student, String> studentNameCol, TableColumn<ViewStudentsController.Student, String> studentContactCol, TableColumn<ViewStudentsController.Student, Integer> noOfIssuedBooksCol, Pane pane1, TextArea area) {
        this.tableViewStudentDashboard = tableViewStudentDashboard;
        this.studentRollNoCol = studentRollNoCol;
        this.studentNameCol = studentNameCol;
        this.studentContactCol = studentContactCol;
        this.noOfIssuedBooksCol = noOfIssuedBooksCol;
        pane = pane1;
        textArea = area;

        databaseHandler = DatabaseHandler.getInstance();
    }

    public void initializeStudentDashboardSection() {
        initCol();
        tableViewStudentDashboard.requestFocus();
        String query = "SELECT * FROM STUDENT_TABLE";
        loadStudentDashboardData(query);
        tableViewStudentDashboard.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER:
                    showStatusPane();
                default:
                    return;
            }
        });
        tableViewStudentDashboard.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            switch (event.getCode()) {
                case F5:
                    tableViewStudentDashboard.getItems().clear();
                    loadStudentDashboardData(query);
                default:
                    return;
            }
        });
    }

    private void showStatusPane() {
        ViewStudentsController.Student student = tableViewStudentDashboard.getSelectionModel().getSelectedItem();
        if (student == null) {
            return;
        }
        String data = "";

        textArea.setText("");
        String query = "SELECT * FROM STUDENT_TABLE WHERE id = '" + student.getStudentId() + "'";
        ResultSet res = databaseHandler.execQuery(query);
        try {
            if (res.next()) {
                data = data + "STUDENT ROLL:- " + "\n\t" + res.getString(1) + "\n";
                data = data + "STUDENT NAME:- " + "\n\t" + res.getString(2) + "\n";
                data = data + "JOINING BATCH:- " + "\n\t" + res.getInt(3) + "\n";
                data = data + "CONTACT NO:- " + "\n\t" + res.getString(4) + "\n";
                data = data + "ISSUED BOOKS:- " + "\n\t" + res.getInt(5) + "\n";

                if (res.getInt(5) != 0) {
                    String query2 = "SELECT bookID, bookName from ISSUE_TABLE where studentID = '" + student.getStudentId() + "'";
                    ResultSet resultSet = databaseHandler.execQuery(query2);
                    data = data + "BOOKS ARE:- " + "\n\n";
                    while (resultSet.next()) {
                        data = data + "BOOK ID:- " + "\n\t" + resultSet.getString(1) + "\n";
                        data = data + "BOOK NAME:- " + "\n\t" + resultSet.getString(2) + "\n\n";
                    }
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        textArea.setBorder(Border.EMPTY);
        textArea.setText(data);
        pane.toFront();
    }

    private void initCol() {
        studentRollNoCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        studentNameCol.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        studentContactCol.setCellValueFactory(new PropertyValueFactory<>("studentContact"));
        noOfIssuedBooksCol.setCellValueFactory(new PropertyValueFactory<>("noOfIssuedBooks"));
    }

    public void loadStudentDashboardData(String query) {
        if (isRefresh) {
            tableViewStudentDashboard.getItems().clear();
            isRefresh = false;
        }
        ResultSet res = databaseHandler.execQuery(query);
        //System.out.println(1);
        try {
            while (res.next()) {
                String id = res.getString(1);
                String name = res.getString(2);
                String contact = res.getString(4);
                int issueCount = res.getInt(5);
                students.add(new ViewStudentsController.Student(id, name, contact, issueCount));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            e.printStackTrace();
        }
        if (students.size() == 0) {
            isError = true;
        } else {
            isError = false;
        }
        tableViewStudentDashboard.setItems(students);
        tableViewStudentDashboard.getSelectionModel().select(0);

    }
}
