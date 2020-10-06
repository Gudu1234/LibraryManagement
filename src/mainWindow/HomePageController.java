package mainWindow;


import alert.AlertMaker;
import animatefx.animation.*;
import books.*;
import database.DatabaseHandler;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.textfield.CustomTextField;
import students.*;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class HomePageController implements Initializable {

    public StackPane rootStackPane;
    public AnchorPane homeAnchorPane;

    public StackPane stackPane;

    public Button btnStudent;
    public Pane paneStudents;
    public TableView<StudentHandling.Batch> tableViewStudent;
    public TableColumn<StudentHandling.Batch, Integer> batchNoCol;
    public TableColumn<StudentHandling.Batch, Integer> totalSeatsCol;
    public TableColumn<StudentHandling.Batch, Integer> totalStudentsCol;

    public Button btnBooks;
    public Pane paneBooks;
    public TableView<BookHandling.BookSection> tableViewBooks;
    public TableColumn<BookHandling.BookSection, String> bookType;
    public TableColumn<BookHandling.BookSection, Integer> bookCapacity;
    public TableColumn<BookHandling.BookSection, Integer> totalBooks;
    public TableColumn<BookHandling.BookSection, Integer> availableBooks;

    public Pane paneIssue;
    public TabPane tabPaneIssue;

    public Tab tabIssue;
    public AnchorPane tabIssueAnchorPane;
    public TextField tfStudentRollIssueTab;
    public ListView<String> listStudentInfo;
    public Button btnShowBookSections;
    public Pane paneBooksAvailableIssue;
    public AnchorPane anchorPaneBooksAvailable;
    public Button btnShowBooksAvailable;
    public Pane paneBooksToIssue;
    public ListView<String> listBooksToIssue;
    public Button btnIssueBooks;
    public Button btnIssueSection;
    public VBox vBoxBooksAvailable;
    
    public Tab tabSubmission;
    public TextField tfBookIDSubmissionTab;
    public TextField tfStudentIDSubmissionTab;
    public ListView<String> listBookInformationView;
    public Button btnRenew;
    public Button btnSubmission;

    public Button btnDashboard;
    public Pane paneDashboard;
    public Pane paneStatusDashboard;
    public Pane paneButtonDashboard;
    public VBox vBoxStatusBookStudent;
    public TextArea statusTextArea;

    public Button btnViewStudentsAndSearchInfo;
    public AnchorPane anchorPaneStudentsDashboard;
    public TableView<ViewStudentsController.Student> tableViewStudentDashboard;
    public TableColumn<ViewStudentsController.Student, String> studentRollNoCol;
    public TableColumn<ViewStudentsController.Student, String> studentNameCol;
    public TableColumn<ViewStudentsController.Student, String> studentContactCol;
    public TableColumn<ViewStudentsController.Student, Integer> noOfIssuedBooksCol;
    public CustomTextField tfStudentRollDashboard;
    public CustomTextField tfStudentNameDashboard;
    public CheckComboBox<String> comboBoxDashboard;

    public Button btnViewBooksAndSearchInfo;
    public AnchorPane anchorPaneBooksDashboard;
    public TableView<ViewBooksController.Book> tableViewBookDashboard;
    public TableColumn<ViewBooksController.Book, String> bookIDCol;
    public TableColumn<ViewBooksController.Book, String> bookNameCol;
    public TableColumn<ViewBooksController.Book, String> bookAuthorCol;
    public TableColumn<ViewBooksController.Book, Integer> bookEditionCol;
    public TableColumn<ViewBooksController.Book, Boolean> availBookCol;
    public CustomTextField tfBookIdDashboard;
    public CustomTextField tfBookAuthorDashboard;
    public CustomTextField tfBookNameDashboard;
    public CheckComboBox<String> bookComboBoxDashboard;

    public Pane paneHome;
    public Pane paneLabel;
    public Button btnHome;
    public Pane paneImageLibrary;
    public HBox hBoxHomePane;
    public Pane paneLibraryInformation;
    public Label dateTimeLabel;
    public StackPane stackPaneImageLibrary;
    public Pane paneLibraryHelp;
    public Label lblHeading;


    DatabaseHandler handler;
    StudentHandling studentHandling;

    Boolean isAbleToShowBookSections = false;
    Boolean isRenewSubmissionPossible = false;

    ObservableList<StudentHandling.Batch> batchList = FXCollections.observableArrayList();
    ObservableList<BookHandling.BookSection> bookSections = FXCollections.observableArrayList();

    ToggleGroup toggleGroup = new ToggleGroup();

    List<String> list = new ArrayList<>();
    List<String> stringList = new ArrayList<>();

    StudentSectionDashboard studentSectionDashboard;
    StudentComboBoxInitialize comboBoxInitialize;
    BookComboBoxInitialize bookComboBoxInitialize;
    BookSectionDashboard bookSectionDashboard;
    FocusedPane focusedPane;

    SingleSelectionModel<Tab> singleSelectionModel;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //STUDENT SECTION:  199
        handler = DatabaseHandler.getInstance();                               //BOOK SECTION:  449
        singleSelectionModel = tabPaneIssue.getSelectionModel();
        studentHandling = new StudentHandling();                              //ISSUE SECTION: 593
        final Label caption = new Label("");                            //SUBMISSION SECTION:   793
        caption.setVisible(true);                                           //DASHBOARD SECTION:    1015

        String query = "SELECT TYPE_NAME FROM BOOK_SECTION";
        ResultSet res = handler.execQuery(query);
        try {
            while (res.next()) {
                RadioButton rb = new RadioButton(res.getString(1));
                rb.setToggleGroup(toggleGroup);
                vBoxBooksAvailable.getChildren().add(rb);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        listBooksToIssue.setCellFactory(CheckBoxListCell.forListView(new Callback<String, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(String param) {
                BooleanProperty observable = new SimpleBooleanProperty();
                observable.addListener((observable1, oldValue, newValue) -> {
                    if (newValue) {
                        System.out.println(param + " is now selected.");
                        list.add(param);
                    } else {
                        System.out.println(param + " is now deselected.");
                        list.remove(param);
                    }
                });
                return observable;
            }
        }));
        listBooksToIssue.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        initializeStudentSectionInDashboard();
        initializeBookSectionDashboard();

        initDateTime();
        initHomePane();

        /*btnHome.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!btnHome.isFocused() && isHomePaneShowing) {
                btnHome.requestFocus();
            }
        });
        btnStudent.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            btnStudent.requestFocus();
        });
        btnBooks.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            btnBooks.requestFocus();
        });
        btnIssueSection.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            btnIssueSection.requestFocus();
        });
        btnDashboard.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            btnDashboard.requestFocus();
        });*/

        focusedPane = new FocusedPane(btnHome, btnStudent, btnBooks, btnIssueSection, btnDashboard);
    }

    //STUDENT SECTION
    @FXML
    void handleAddStudentBatch(ActionEvent event) {
        String qu = "SELECT COUNT(*) FROM BATCH";
        ResultSet res = handler.execQuery(qu);
        try {
            if (res.next()) {
                if (res.getInt(1) < 5) {
                    new AddBatchController().init(comboBoxInitialize, homeAnchorPane);
                    loadWindow("Add a new Batch", "/students/add_batch.fxml");
                } else {
                    Button button = new Button("OK");
                    AlertMaker.showMaterialWarningDialog(stackPane, paneStudents, Arrays.asList(button), "Warning", "Maximum numbers of records are already added.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Data can't be added");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleAddStudentToBatch(ActionEvent event) {
        StudentHandling.Batch selectedBatchForAddition = tableViewStudent.getSelectionModel().getSelectedItem();
        if (selectedBatchForAddition == null) {
            Button button = new Button("OK");
            AlertMaker.showMaterialWarningDialog(stackPane, paneStudents, Arrays.asList(button), "Warning", "Please select a batch for adding Students.");
            return;
        }
        if (selectedBatchForAddition.getTotalStudents() == selectedBatchForAddition.getTotalSeats()) {
            Button button = new Button("OK");
            AlertMaker.showMaterialWarningDialog(stackPane, paneStudents, Arrays.asList(button), "Warning", "You can't add further records as no seats available.");
            return;
        }
        new AddStudentsBatchController().init(selectedBatchForAddition.getBatchNo(), studentSectionDashboard, homeAnchorPane);
        loadWindow("Add Student", "/students/add_students_batch.fxml");
    }

    @FXML
    void handleDelStudentBatch(ActionEvent event) {
        StudentHandling.Batch selectedBatchForDeletion = tableViewStudent.getSelectionModel().getSelectedItem();
        if (tableViewStudent.getSelectionModel().getSelectedIndex() > 0 || selectedBatchForDeletion == null) {
            Button button = new Button("OK");
            AlertMaker.showMaterialWarningDialog(stackPane, paneStudents, Arrays.asList(button), "Warning", "Try to delete a valid batch.");
            return;
        }

        new DeleteStudent(studentSectionDashboard, comboBoxInitialize).deleteStudentBatch(selectedBatchForDeletion, stackPane, paneStudents);
    }

    @FXML
    void handleDelStudentFromBatch(ActionEvent event) {
        StudentHandling.Batch batch = tableViewStudent.getSelectionModel().getSelectedItem();
        if (batch == null) {
            Button button = new Button("OK");
            AlertMaker.showMaterialWarningDialog(stackPane, paneStudents, Arrays.asList(button), "Warning", "Please select a batch for adding Students.");
            return;
        }

        TextField textField = new TextField();
        Button okButton = new Button("Delete");
        okButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1) -> {
            if (textField.getText() == null) {
                return;
            }
            if (handler.checkAvailabilityStudent2(textField.getText(), batch.getBatchNo())) {
                new DeleteStudent(studentSectionDashboard, comboBoxInitialize).deleteStudentRecord(textField.getText(), batch.getBatchNo(), stackPane, paneStudents);
            } else {
                Button button = new Button("OK");
                AlertMaker.showMaterialErrorDialog(stackPane, paneStudents, Arrays.asList(button), "Error", "Wrong Student ID.");
            }

        });
        Button cancelButton = new Button("Cancel");
        AlertMaker.showMaterialInputDialog(stackPane, paneStudents, Arrays.asList(okButton, cancelButton), "Delete the Student Record You Want.", "Enter the Roll No. of Student", textField);
    }

    @FXML
    void handleStudentsButton(ActionEvent event) {
        if (focusedPane.getStudentPaneShowing()) {
            return;
        }

        focusedPane.setStudentPaneShowing(true, false);
        new ZoomInLeft(paneStudents).play();
        paneStudents.toFront();
        tableViewStudent.getItems().clear();
        initBatchCol();
        loadBatchData();

        tableViewStudent.setOnKeyPressed(event1 -> {
            switch (event1.getCode()) {
                case ENTER:
                    handleViewStudentsOfBatchOperation(new ActionEvent());
                default:
                    return;
            }
        });
    }

    private void loadBatchData() {
        String qu = "SELECT * FROM BATCH ORDER BY BATCH_NO ASC";
        ResultSet res = handler.execQuery(qu);
        try {
            while (res.next()) {
                int batchNum = res.getInt("batch_no");
                int totalSeats = res.getInt("total_seats");
                int students = res.getInt("students");

                batchList.add(new StudentHandling.Batch(batchNum, totalSeats, students));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableViewStudent.setItems(batchList);
        tableViewStudent.getSelectionModel().select(0);
        tableViewStudent.getSelectionModel().focus(0);
        tableViewStudent.requestFocus();
    }

    private void initBatchCol() {
        batchNoCol.setCellValueFactory(new PropertyValueFactory<>("batchNo"));
        totalSeatsCol.setCellValueFactory(new PropertyValueFactory<>("totalSeats"));
        totalStudentsCol.setCellValueFactory(new PropertyValueFactory<>("totalStudents"));
    }

    public void handleRefreshStudentBatchOperation(MouseEvent actionEvent) {
        tableViewStudent.getItems().clear();
        loadBatchData();
    }

    public void handleViewStudentsOfBatchOperation(ActionEvent actionEvent) {
        StudentHandling.Batch batch = tableViewStudent.getSelectionModel().getSelectedItem();
        if (batch == null) {
            Button button = new Button("OK");
            AlertMaker.showMaterialWarningDialog(stackPane, paneStudents, Arrays.asList(button), "Warning", "Please Select a Batch.");
            return;
        }
        if (batch.getTotalStudents() == 0) {
            Button button = new Button("OK");
            AlertMaker.showMaterialWarningDialog(stackPane, paneStudents, Arrays.asList(button), "Warning", "Please add some student records first.");
            return;
        }
        new ViewStudentsController().init(batch, studentSectionDashboard, comboBoxInitialize);
        loadWindow2("View Student Records", "/students/view_students.fxml");
    }

    public void handleEditBatchInfoOperation(ActionEvent actionEvent) {
        StudentHandling.Batch batch = tableViewStudent.getSelectionModel().getSelectedItem();
        if (batch == null) {
            Button button = new Button("OK");
            AlertMaker.showMaterialWarningDialog(stackPane, paneStudents, Arrays.asList(button), "Warning", "Please Select a Batch.");
            return;
        }

        TextField textField = new TextField();
        textField.setText(String.valueOf(batch.getTotalSeats()));
        Button okButton = new Button("Change");
        okButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            String ac = "UPDATE BATCH SET TOTAL_SEATS = " + Integer.parseInt(textField.getText()) + " WHERE BATCH_NO = " + batch.getBatchNo();
            if (handler.execAction(ac)) {
                tableViewStudent.getItems().clear();
                loadBatchData();
            } else {
                JOptionPane.showMessageDialog(null, "Database Error.");
            }
        });
        Button cancelButton = new Button("Cancel");
        AlertMaker.showMaterialInputDialog(stackPane, paneStudents, Arrays.asList(okButton, cancelButton), "You can only modify the Total no. of seats in the batch.", "Total Seats", textField);
    }


    //BOOK SECTION

    public void handleBooksButton(ActionEvent actionEvent) {
        if (focusedPane.getBooksPaneShowing()) {
            return;
        }
        focusedPane.setBooksPaneShowing(true, false);
        new ZoomInRight(paneBooks).play();
        paneBooks.toFront();
        tableViewBooks.getItems().clear();
        initBookSectionCol();
        loadBookSectionData();

        tableViewBooks.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER:
                    handleViewBooksOfSectionOperation(new ActionEvent());
                default:
                    return;
            }
        });
    }

    private void loadBookSectionData() {
        String query = "SELECT * FROM BOOK_SECTION ORDER BY TYPE";
        ResultSet res = handler.execQuery(query);
        try {
            while (res.next()) {
                String type = res.getString(2);
                int capacity = res.getInt(3);
                int totalBooks = res.getInt(4);
                int availBooks = res.getInt(5);
                bookSections.add(new BookHandling.BookSection(type, capacity, totalBooks, availBooks));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableViewBooks.setItems(bookSections);
        tableViewBooks.getSelectionModel().select(0);
        tableViewBooks.getSelectionModel().focus(0);
        tableViewBooks.requestFocus();
    }

    private void initBookSectionCol() {
        bookType.setCellValueFactory(new PropertyValueFactory<>("bookType"));
        bookCapacity.setCellValueFactory(new PropertyValueFactory<>("bookCapacity"));
        totalBooks.setCellValueFactory(new PropertyValueFactory<>("totalBooks"));
        availableBooks.setCellValueFactory(new PropertyValueFactory<>("availBooks"));
    }

    public void handleRefreshBookTypesOperation(MouseEvent actionEvent) {
        tableViewBooks.getItems().clear();
        loadBookSectionData();
    }

    public void handleAddBookSection(ActionEvent actionEvent) {
        new AddBookSectionController().init(bookComboBoxInitialize, homeAnchorPane);
        loadWindow("Add Book Section", "/books/add_book_section.fxml");
    }

    public void handleDelBookSection(ActionEvent actionEvent) {
        BookHandling.BookSection selectedBookForDeletion = tableViewBooks.getSelectionModel().getSelectedItem();
        if (selectedBookForDeletion == null) {
            Button button = new Button("OK");
            AlertMaker.showMaterialWarningDialog(stackPane, paneBooks, Arrays.asList(button), "Warning", "Please Select a Book Section to delete.");
            return;
        }
        new DeleteBook(bookSectionDashboard, bookComboBoxInitialize).deleteBookSection(selectedBookForDeletion, stackPane, paneBooks);
    }

    public void handleAddBooksToSection(ActionEvent actionEvent) {
        BookHandling.BookSection bookSection = tableViewBooks.getSelectionModel().getSelectedItem();
        if (bookSection == null) {
            Button button = new Button("OK");
            AlertMaker.showMaterialWarningDialog(stackPane, paneBooks, Arrays.asList(button), "Warning", "Please Select a Section for adding Books");
            return;
        }
        if (bookSection.getTotalBooks() == bookSection.getBookCapacity()) {
            Button button = new Button("OK");
            AlertMaker.showMaterialWarningDialog(stackPane, paneBooks, Arrays.asList(button), "Warning", "Already Maximum no. of Books Available.Please Increase Capacity.");
            return;
        }
        new AddBookController().initData(bookSection.getBookType(), bookSectionDashboard, homeAnchorPane);
        loadWindow("Add Book", "/books/add_book.fxml");
    }

    public void handleDelBooksFromSection(ActionEvent actionEvent) {
        BookHandling.BookSection bookSection = tableViewBooks.getSelectionModel().getSelectedItem();
        if (bookSection == null) {
            Button button = new Button("OK");
            AlertMaker.showMaterialWarningDialog(stackPane, paneBooks, Arrays.asList(button), "Warnig", "Please Select a Book Section.");
            return;
        }

        TextField textField = new TextField();
        Button okButton = new Button("Delete");
        okButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1) -> {
            if (textField.getText() == null) {
                return;
            }
            if (handler.checkAvailabilityBook2(textField.getText(), bookSection.getBookType())) {
                new DeleteBook(bookSectionDashboard, bookComboBoxInitialize).deleteBook(textField.getText(), bookSection.getBookType(), stackPane, paneBooks);
            } else {
                Button button = new Button("OK");
                AlertMaker.showMaterialErrorDialog(stackPane, paneBooks, Arrays.asList(button), "Error", "Wrong ID");
            }

        });
        Button cancelButton = new Button("Cancel");
        AlertMaker.showMaterialInputDialog(stackPane, paneBooks, Arrays.asList(okButton, cancelButton), "Delete the Book From this section You Want.", "Enter the Book ID of Book", textField);

    }

    public void handleViewBooksOfSectionOperation(ActionEvent actionEvent) {
        BookHandling.BookSection bookSection = tableViewBooks.getSelectionModel().getSelectedItem();
        if (bookSection == null) {
            Button button = new Button("OK");
            AlertMaker.showMaterialWarningDialog(stackPane, paneBooks, Arrays.asList(button), "Warning", "Please Select a Book Section.");
            return;
        }
        if (bookSection.getTotalBooks() == 0) {
            Button button = new Button("OK");
            AlertMaker.showMaterialWarningDialog(stackPane, paneBooks, Arrays.asList(button), "Warning", "Please Add Some Books to This Section.");
            return;
        }
        new ViewBooksController().initData(bookSection, bookSectionDashboard, bookComboBoxInitialize);
        loadWindow2("View Books", "/books/view_books.fxml");
    }

    public void handleEditBookInfoOperation(ActionEvent actionEvent) {
        BookHandling.BookSection bookSection = tableViewBooks.getSelectionModel().getSelectedItem();
        if (bookSection == null) {
            Button button = new Button("OK");
            AlertMaker.showMaterialWarningDialog(stackPane, paneBooks, Arrays.asList(button), "Warning", "Please Select a Book Section.");
            return;
        }

        TextField textField = new TextField();
        textField.setText(String.valueOf(bookSection.getBookCapacity()));
        Button okButton = new Button("Change");
        okButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            String ac = "UPDATE BOOK_SECTION SET CAPACITY = " + Integer.parseInt(textField.getText()) + " WHERE TYPE_NAME = '" + bookSection.getBookType() + "'";
            if (handler.execAction(ac)) {
                tableViewBooks.getItems().clear();
                loadBookSectionData();
            } else {
                JOptionPane.showMessageDialog(null, "Database Error.");
            }
        });
        Button cancelButton = new Button("Cancel");
        AlertMaker.showMaterialInputDialog(stackPane, paneBooks, Arrays.asList(okButton, cancelButton), "You can only modify the Book Capacity of the " + bookSection.getBookType() + "  Section", "Total Capacity", textField);
    }

    
    
    //ISSUE SECTION

    public void handleButtonIssueSection(ActionEvent actionEvent) {
        if (focusedPane.getIssuePaneShowing()) {
            return;
        }
        focusedPane.setIssuePaneShowing(true, false);
        new ZoomInUp(paneIssue).play();
        paneIssue.toFront();
    }

    public void handleOnRollEnterInIssueTab(ActionEvent actionEvent) {
        ObservableList<String> stringObservableList = FXCollections.observableArrayList();
        String query = "SELECT * FROM STUDENT_TABLE WHERE id = '" + tfStudentRollIssueTab.getText() + "'";
        ResultSet res = handler.execQuery(query);
        try {
            if (res.next()) {
                stringObservableList.add("Student Information:-");
                stringObservableList.add("\tStudent Name:   " + res.getString(2));
                stringObservableList.add("\tJoining Batch:  " + res.getInt(3));
                stringObservableList.add("\tContact No.:    " + res.getString(4));
                stringObservableList.add("\tNo. of Books Issued:    " + res.getInt(5));

                isAbleToShowBookSections = true;
                String action = "DELETE FROM TYPE_CHECK";
                handler.execAction(action);
            } else {
                stringObservableList.add("\tThe Roll no. is Invalid.");
                isAbleToShowBookSections = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        listStudentInfo.getItems().setAll(stringObservableList);
    }

    public void handleShowBookSections(ActionEvent actionEvent) {
        String id = tfStudentRollIssueTab.getText();
        if (id.isEmpty() || listStudentInfo.getItems().isEmpty() || !isAbleToShowBookSections) {
            return;
        }
        if (handler.isStudentAbleTobeIssued(id)) {
            paneBooksAvailableIssue.setVisible(true);
            paneBooksToIssue.setVisible(true);
        } else {
            Button button = new Button("OK");
            AlertMaker.showMaterialErrorDialog(stackPane, paneIssue, Arrays.asList(button), "Error", "The Student Has Already taken the Maximum no. of Books.");
        }
    }

    public void handleShowBooksAvailable(ActionEvent actionEvent) {
        ToggleGroup tg = new ToggleGroup();
        ScrollPane scrollPane = new ScrollPane();
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        scrollPane.setContent(vBox);


        String type = ((RadioButton)toggleGroup.getSelectedToggle()).getText();
        if (type == null) {
            return;
        }
        String query = "SELECT book_id FROM BOOK_TABLE WHERE book_type = '" + type + "' AND isAvail = true";
        ResultSet res = handler.execQuery(query);
        try {
            while (res.next()) {
                RadioButton rb = new RadioButton(res.getString(1));
                rb.setToggleGroup(tg);
                vBox.getChildren().add(rb);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Button okButton = new Button("Confirm");

        okButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            try {
                if (((RadioButton)tg.getSelectedToggle()).getText() != null) {
                    String id = ((RadioButton)tg.getSelectedToggle()).getText();
                    String selectQuery = "SELECT bookType from ISSUE_TABLE WHERE studentID = '" + tfStudentRollIssueTab.getText() + "'";
                    ResultSet resultSet = handler.execQuery(selectQuery);
                    while (resultSet.next()) {
                        if (type.equals(resultSet.getString(1))) {
                            Button submit = new Button("Submit");
                            Button cancel = new Button("Cancel");
                            AlertMaker.showMaterialConfirmationDialog(stackPane, paneIssue, Arrays.asList(submit, cancel), "Confirmation", "A Book From this section is already issued to the Student.To Select another book he has to submit the previous one first.");
                            submit.addEventHandler(MouseEvent.MOUSE_CLICKED, event1 -> {
                                singleSelectionModel.select(tabSubmission);
                                tfStudentIDSubmissionTab.setText(tfStudentRollIssueTab.getText());
                                tfBookIDSubmissionTab.setEditable(false);
                                listBookInformationView.getItems().clear();
                                handleOnRollEnterInIssueTab(new ActionEvent());
                            });
                            return;
                        }
                    }
                    String action = "INSERT INTO TYPE_CHECK VALUES ('" + type + "')";
                    if (!handler.execAction(action)) {
                        Button button = new Button("OK");
                        AlertMaker.showMaterialWarningDialog(stackPane, paneIssue, Arrays.asList(button), "Warning", "You can't select more than one book from a section.");
                        return;
                    }

                    listBooksToIssue.getItems().add(id);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        AlertMaker.showMaterialInfoDialog(stackPane, paneIssue, scrollPane, okButton, "Select a Book ID");
    }

    public void handleIssueBooksOperation(ActionEvent actionEvent) {

        if (list.size() == 0) {
            Button button = new Button("OK");
            AlertMaker.showMaterialWarningDialog(stackPane, paneIssue, Arrays.asList(button), "Warning", "Please Check the items you want to issue.");
            return;
        }
        if (list.size() > 5) {
            Button button = new Button("OK");
            AlertMaker.showMaterialWarningDialog(stackPane, paneIssue, Arrays.asList(button), "Warning", "The Student can't take more than 5 books.");
            return;
        }

        Button confirm = new Button("Confirm");
        Button cancel = new Button("Cancel");
        Button okButton = new Button("OK");

        AlertMaker.showMaterialConfirmationDialog(stackPane, paneIssue, Arrays.asList(confirm, cancel), "Confirmation", "Do you sure want to issue the books to student bearing roll " + tfStudentRollIssueTab.getText());
        confirm.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            for (String item:
                    list) {
                System.out.println(item);
                String query = "SELECT * FROM BOOK_TABLE WHERE BOOK_ID = '" + item + "'";
                ResultSet res = handler.execQuery(query);
                try {
                    if (res.next()) {
                        String action1 = "INSERT INTO ISSUE_TABLE(bookID, studentID, bookName, authorName, bookType, edition_book, renew_count) VALUES ("
                                + "'" + item + "', "
                                + "'" + tfStudentRollIssueTab.getText() + "', "
                                + "'" + res.getString(2) + "', "
                                + "'" + res.getString(3) + "', "
                                + "'" + res.getString(6) + "', "
                                + res.getInt(5) + ", "
                                + 0
                                +")";

                        String action2 = "UPDATE BOOK_TABLE SET isAvail = false where book_id = '" + item + "'";
                        if (handler.execAction(action1) && handler.execAction(action2)) {
                            String action5 = "UPDATE BOOK_SECTION SET avail_books = " + handler.returnNoOfAvailBooks(res.getString(6)) + " WHERE type_name ='" + res.getString(6) + "'";
                            handler.execAction(action5);
                        } else {
                            AlertMaker.showMaterialErrorDialog(stackPane, paneIssue, Arrays.asList(okButton), "Error", "Issue Operation Failed.");
                            return;
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    return;
                }
            }

            listBooksToIssue.getItems().clear();
            list.clear();
            String action3 = "DELETE FROM TYPE_CHECK";
            String action4 = "UPDATE STUDENT_TABLE SET issue_book = " + handler.returnNoOfIssuedBooks(tfStudentRollIssueTab.getText()) + " WHERE id = '" + tfStudentRollIssueTab.getText() + "'";

            if (handler.execAction(action3) && handler.execAction(action4)) {
                AlertMaker.showMaterialInformationDialog(stackPane, paneIssue, Arrays.asList(okButton), "Success", "Issue Operation Successfully Completed.");
                bookSectionDashboard.isRefresh = true;
                bookSectionDashboard.loadBookDashboardData("SELECT * FROM BOOK_TABLE");
                studentSectionDashboard.isRefresh = true;
                studentSectionDashboard.loadStudentDashboardData("SELECT * FROM STUDENT_TABLE");
                refreshIssueSection();
            }

        });
    }
    public void refreshIssueSection() {
        tfStudentRollIssueTab.setText("");
        paneBooksAvailableIssue.setVisible(false);
        paneBooksToIssue.setVisible(false);
        listBooksToIssue.getItems().clear();
        list.clear();
        listStudentInfo.getItems().clear();
        String action3 = "DELETE FROM TYPE_CHECK";
        DatabaseHandler.getInstance().execAction(action3);
    }
    public void handleRefreshIssueOperation(MouseEvent mouseEvent) {
        refreshIssueSection();
    }

    
    //SUBMISSION SECTION
    
    public void handleOnBookIDSubmissionTab(ActionEvent actionEvent) {
        if (tfBookIDSubmissionTab.getText() == null || tfBookIDSubmissionTab.getText().isEmpty()) {
            return;
        }
        ObservableList<String> bookList = FXCollections.observableArrayList();
        tfStudentIDSubmissionTab.setEditable(false);
        String bookID = tfBookIDSubmissionTab.getText();
        listBookInformationView.getItems().clear();
        if (handler.checkAvailabilityBook(bookID)) {
            String query = "SELECT * FROM ISSUE_TABLE WHERE bookID = '" + bookID + "'";
            ResultSet res = handler.execQuery(query);
            try {
                if (res.next()) {
                    bookList.add("Issue Date And Time:  " + res.getTimestamp("issueTime"));
                    bookList.add("Book Information :-");
                    String query1 = "SELECT * FROM BOOK_TABLE WHERE book_id = '" + bookID + "'";
                    ResultSet resultSet = handler.execQuery(query1);
                    if (resultSet.next()) {
                        bookList.add("\tBook Id:    " + bookID);
                        bookList.add("\tBook Name:  " + resultSet.getString(2));
                        bookList.add("\tAuthor:     " + resultSet.getString(3));
                        bookList.add("\tEdition:    " + resultSet.getInt(5));
                        bookList.add("\tBook Type:  " + resultSet.getString(6));
                    }
                    bookList.add("Member Information    :- ");
                    String query2 = "SELECT * FROM STUDENT_TABLE WHERE id = '" + res.getString(2) + "'";
                    resultSet = handler.execQuery(query2);
                    if (resultSet.next()) {
                        bookList.add("\tStudent Id:      " + resultSet.getString(1));
                        bookList.add("\tStudent Name:    " + resultSet.getString(2));
                        bookList.add("\tJoining Batch:  " + resultSet.getInt(3));
                        bookList.add("\tContact No.     " + resultSet.getString(4));
                    }

                    isRenewSubmissionPossible = true;
                } else {
                    bookList.add("The Book is not yet issued to anyone.");
                    isRenewSubmissionPossible = false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Button button = new Button("OK");
            AlertMaker.showMaterialErrorDialog(stackPane, paneIssue, Arrays.asList(button), "Error", "A Wrong Book ID was Entered.");
            return;
        }

        listBookInformationView.setItems(bookList);
    }

    public void handleOnStudentIDSubmissionTab(ActionEvent actionEvent) {
        if (tfStudentIDSubmissionTab.getText() == null || tfStudentIDSubmissionTab.getText().isEmpty()) {
            return;
        }
        tfBookIDSubmissionTab.setEditable(false);
        String studentID = tfStudentIDSubmissionTab.getText();
        listBookInformationView.getItems().clear();
        stringList.clear();

        if (!handler.checkAvailabilityStudent(studentID)) {
            Button button = new Button("OK");
            AlertMaker.showMaterialErrorDialog(stackPane, paneIssue, Arrays.asList(button), "Error", "A Wrong Student ID was Entered.");
            return;
        }
        if (!handler.checkIssueConfirmation(studentID)) {
            Button button = new Button("OK");
            AlertMaker.showMaterialErrorDialog(stackPane, paneIssue, Arrays.asList(button), "Error", "There is no book issued to this student.");
            return;
        }

        listBookInformationView.setCellFactory(CheckBoxListCell.forListView(param -> {
            BooleanProperty observable = new SimpleBooleanProperty();
            observable.addListener((observable1, oldValue, newValue) -> {
                if (newValue) {
                    System.out.println(param + " is now selected.");
                    String sub = param.substring(0, param.indexOf('\n'));
                    System.out.println(sub);
                    stringList.add(sub);
                } else {
                    System.out.println(param + " is now deselected.");
                    stringList.remove(param);
                }
            });
            return observable;
        }));
        listBookInformationView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        String query = "SELECT * FROM ISSUE_TABLE WHERE studentID = '" + studentID + "'";
        ResultSet res = handler.execQuery(query);
        try {
            while (res.next()) {
                String data = res.getString(1) + "\n" +
                        res.getString(3) + "\n" +
                        res.getString(4) + "\n" +
                        res.getString(5) + "\n" +
                        res.getInt(6) + "\n" +
                        res.getTimestamp(7) + "\n";

                listBookInformationView.getItems().add(data);
                isRenewSubmissionPossible = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleRenewOperation(ActionEvent actionEvent) {
        if (tfBookIDSubmissionTab.isEditable()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Do you sure want to submit the book ?");

            Optional<ButtonType> response = alert.showAndWait();
            if (response.get() == ButtonType.OK) {
                String id = tfBookIDSubmissionTab.getText();

            }
        }
    }

    public void handleSubmissionOperation(ActionEvent actionEvent) {
        if (tfBookIDSubmissionTab.getText().isEmpty() && tfStudentIDSubmissionTab.getText().isEmpty()) {
            return;
        }

        if (tfBookIDSubmissionTab.isEditable()) {
            Button submit = new Button("Submit");
            Button cancel = new Button("Cancel");
            Button button = new Button("OK");
            AlertMaker.showMaterialConfirmationDialog(stackPane, paneIssue, Arrays.asList(submit, cancel), "Confirmation", "Do you Sure Want to Submit the Book?");
            submit.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                String id = tfBookIDSubmissionTab.getText();
                if (new SubmissionClass().submissionSuccess(id)) {
                    AlertMaker.showMaterialInformationDialog(stackPane, paneIssue, Arrays.asList(button), "Success", "Submission Complete Successfully.");
                    bookSectionDashboard.isRefresh = true;
                    bookSectionDashboard.loadBookDashboardData("SELECT * FROM BOOK_TABLE");
                    studentSectionDashboard.isRefresh = true;
                    studentSectionDashboard.loadStudentDashboardData("SELECT * FROM STUDENT_TABLE");
                    refreshSubmissionSection();
                } else {
                    AlertMaker.showMaterialErrorDialog(stackPane, paneIssue, Arrays.asList(button), "Failure", "Submission Failed.");
                    return;
                }
            });
            return;
        }

        if (tfStudentIDSubmissionTab.isEditable()) {
            if (stringList.size() == 0) {
                Button button = new Button("OK");
                AlertMaker.showMaterialWarningDialog(stackPane, paneIssue, Arrays.asList(button), "Warning", "First Check the Books You want to Submit.");
                return;
            }
            Button submit = new Button("Submit");
            Button cancel = new Button("Cancel");
            Button button = new Button("OK");
            AlertMaker.showMaterialConfirmationDialog(stackPane, paneIssue, Arrays.asList(submit, cancel), "Confirmation", "Do you Sure Want to Submit the Book?");

            submit.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                for(String id : stringList) {
                    if (new SubmissionClass().submissionSuccess(id)) {

                    } else {
                        AlertMaker.showMaterialErrorDialog(stackPane, paneIssue, Arrays.asList(button), "Failure", "Submission Failed.");
                        return;
                    }
                }
                AlertMaker.showMaterialInformationDialog(stackPane, paneIssue, Arrays.asList(button), "Success", "Submission Complete Successfully.");
                bookSectionDashboard.isRefresh = true;
                bookSectionDashboard.loadBookDashboardData("SELECT * FROM BOOK_TABLE");
                studentSectionDashboard.isRefresh = true;
                studentSectionDashboard.loadStudentDashboardData("SELECT * FROM STUDENT_TABLE");
                refreshSubmissionSection();
            });
        }
    }

    public void refreshSubmissionSection() {
        tfBookIDSubmissionTab.setEditable(true);
        tfBookIDSubmissionTab.setText("");
        tfStudentIDSubmissionTab.setText("");
        tfStudentIDSubmissionTab.setEditable(true);
        listBookInformationView.getItems().clear();
        stringList.clear();
    }
    public void handleSubmissionRefreshIconOperation(MouseEvent mouseEvent) {
        refreshSubmissionSection();
    }

    
    //DASHBOARD SECTION

    public void handleDashboardSection(ActionEvent actionEvent) {
        if (focusedPane.getDashboardShowing()) {
            return;
        }
        focusedPane.setDashboardShowing(true, false);
        new ZoomInDown(paneDashboard).play();
        paneDashboard.toFront();
        tableViewStudentDashboard.requestFocus();
    }

    public void handleButtonViewStudentAndSearchInfoOperation(ActionEvent actionEvent) {
        anchorPaneStudentsDashboard.toFront();
        tableViewStudentDashboard.requestFocus();
    }

    public void handleButtonViewBookAndSearchInfoOperation(ActionEvent actionEvent) {
        anchorPaneBooksDashboard.toFront();
        tableViewBookDashboard.requestFocus();
    }

    public void initializeStudentSectionInDashboard() {
        studentSectionDashboard = new StudentSectionDashboard(tableViewStudentDashboard, studentRollNoCol, studentNameCol, studentContactCol, noOfIssuedBooksCol, paneStatusDashboard, statusTextArea);
        studentSectionDashboard.initializeStudentDashboardSection();
        comboBoxInitialize = new StudentComboBoxInitialize(comboBoxDashboard, studentSectionDashboard, tfStudentRollDashboard, tfStudentNameDashboard);
        comboBoxInitialize.initializeComboBox();
        comboBoxInitialize.initializeTextFields();
    }
    public void initializeBookSectionDashboard() {
        bookSectionDashboard = new BookSectionDashboard(tableViewBookDashboard, bookIDCol, bookNameCol, bookAuthorCol, bookEditionCol, availBookCol, paneStatusDashboard, statusTextArea);
        bookSectionDashboard.initializeBookDashboardSection();
        bookComboBoxInitialize = new BookComboBoxInitialize(bookComboBoxDashboard, tfBookIdDashboard, tfBookAuthorDashboard, tfBookNameDashboard, bookSectionDashboard);
        bookComboBoxInitialize.initializeBookComboBox();
        bookComboBoxInitialize.initializeTextFieldsBookSecttion();
    }


    public void handleCloseStatusOperation(MouseEvent mouseEvent) {
        paneStatusDashboard.toBack();
    }


    //HOME SECTION

    public void initHomePane() {
        Sphere sphere = new PrepareAnimation().prepareSphere();
        sphere.setRotationAxis(Rotate.Z_AXIS);
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                sphere.setRotate(sphere.getRotate() + 1);
            }
        };
        animationTimer.start();
        hBoxHomePane.getChildren().add(sphere);
        new FadeInDown(paneLabel).play();
        new FadeInUp(paneLibraryInformation).play();
        new FadeInRight(paneLibraryHelp).play();
        new ZoomIn(stackPaneImageLibrary).play();
    }
    public void initDateTime() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, event -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy  HH:mm:ss");
            dateTimeLabel.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    public void handleButtonHomeOperation(ActionEvent actionEvent) {
        if (focusedPane.getHomePaneShowing()) {
            return;
        }
        focusedPane.setHomePaneShowing(true, false);
        paneHome.toFront();

    }

    public void homeAnimation() {
        new FadeInDown(paneLabel).play();
        new FadeInUp(paneLibraryInformation).play();
        new FadeInRight(paneLibraryHelp).play();
        new ZoomIn(stackPaneImageLibrary).play();
    }
    public void loadWindow(String title, String url) {
        try {
            BoxBlur boxBlur = new BoxBlur(4, 4, 4);

            Parent root = FXMLLoader.load(getClass().getResource(url));
            Stage stage = new Stage(StageStyle.TRANSPARENT);
            stage.setTitle(title);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);
            stage.show();
            homeAnchorPane.setEffect(boxBlur);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadWindow2(String title, String url) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(url));
            Stage stage = new Stage();
            stage.setTitle(title);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
                stage.requestFocus();
            });
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleBtnHomeDashboard(ActionEvent actionEvent) {
        handleDashboardSection(new ActionEvent());
    }

    public void handleButtonHomeIssue(ActionEvent actionEvent) {
        handleButtonIssueSection(new ActionEvent());
        singleSelectionModel.select(tabIssue);
    }

    public void handleButtonHomeSubmission(ActionEvent actionEvent) {
        handleButtonIssueSection(new ActionEvent());
        singleSelectionModel.select(tabSubmission);
    }

    public void handleButtonHomeStudents(ActionEvent actionEvent) {
        handleStudentsButton(new ActionEvent());
    }

    public void handleButtonHomeBooks(ActionEvent actionEvent) {
        handleBooksButton(new ActionEvent());
    }

    public void handleExitOperation(ActionEvent actionEvent) {
        String action3 = "DELETE FROM TYPE_CHECK";
        DatabaseHandler.getInstance().execAction(action3);
        System.exit(0);
    }

    public void handleBackwardDashboardOperation(MouseEvent mouseEvent) {
        new ZoomOutDown(paneDashboard).play();
        homeAnimation();
        focusedPane.isDashboardShowing = false;
    }

    public void handleBackwardBooksOperation(MouseEvent mouseEvent) {
        new ZoomOutLeft(paneBooks).play();
        homeAnimation();
        focusedPane.isBooksPaneShowing = false;
    }

    public void handleBackwardStudentOperation(MouseEvent mouseEvent) {
        new ZoomOutRight(paneStudents).play();
        homeAnimation();
        focusedPane.isStudentPaneShowing = false;
    }

    public void handleBackwardIssueOperation(MouseEvent mouseEvent) {
        new ZoomOutUp(paneIssue).play();
        homeAnimation();
        focusedPane.isIssuePaneShowing = false;
    }
}
