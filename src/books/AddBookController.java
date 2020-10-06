package books;

import database.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import mainWindow.BookSectionDashboard;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class AddBookController implements Initializable {
    public TextField tfBookId;
    public TextField tfBookName;
    public TextField tfBookAuthor;
    public TextField tfBookType;
    public TextField tfBookEdition;
    public Button btnAddBook;
    public Button btnCancel;
    public Label lblMessage;
    public AnchorPane addBookAnchorPane;

    public static AnchorPane anchorPane;

    DatabaseHandler databaseHandler;
    private static String booktype = "";
    boolean isMessage = false;
    Boolean isEditMode = false;
    Boolean avail = false;
    public static BookSectionDashboard bookSectionDashboard;
    public static Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        databaseHandler = DatabaseHandler.getInstance();
        tfBookType.setText(booktype);
        tfBookType.setEditable(false);

    }

    public void handleAddBookOperation(ActionEvent actionEvent) {
        String bookId = tfBookId.getText();
        String bookName = tfBookName.getText();
        String author = tfBookAuthor.getText();
        String author_name = author.replaceAll(" ", "").toLowerCase();
        String bookType = tfBookType.getText();
        String bookEdition = tfBookEdition.getText();

        if (bookId.isEmpty() || bookName.isEmpty() || author.isEmpty() || bookEdition.isEmpty()) {
            lblMessage.setVisible(!isMessage);
            lblMessage.setText("Please Fill all the Required Fields.");
            return;
        }

        if (isEditMode) {
            handleBookUpdate();
            isEditMode = false;
            return;
        }

        if (databaseHandler.checkBookValidEntry(booktype)) {
            String action = "INSERT INTO BOOK_TABLE VALUES ("+
                    "'" + bookId + "', " +
                    "'" + bookName + "', " +
                    "'" + author + "', " +
                    "'" + author_name + "', " +
                    bookEdition + ", " +
                    "'" + bookType + "', " +
                    true +
                    ")";
            if (databaseHandler.execAction(action)) {
                lblMessage.setVisible(!isMessage);
                lblMessage.setText("Successfully Added a Book To This Section.");
                bookSectionDashboard.isRefresh = true;
                bookSectionDashboard.loadBookDashboardData("SELECT * FROM BOOK_TABLE");
                String action1 = "UPDATE BOOK_SECTION SET TOTAL_BOOKS = " + databaseHandler.returnNoOfBooks(booktype) + " WHERE TYPE_NAME = '" + booktype + "'";
                String action2 = "UPDATE BOOK_SECTION SET AVAIL_BOOKS = " + databaseHandler.returnNoOfAvailBooks(booktype) + " WHERE TYPE_NAME = '" + booktype + "'";
                if (databaseHandler.execAction(action1) && databaseHandler.execAction(action2)) {
                    //JOptionPane.showMessageDialog(null, "BOOK_SECTION Updated.");
                } else {
                    JOptionPane.showMessageDialog(null, "Database error");
                }
            } else {
                lblMessage.setVisible(!isMessage);
                lblMessage.setText("A Book With Same ID is Already Present.");
                lblMessage.setStyle("-fx-text-fill: red");
            }
        } else {
            lblMessage.setVisible(!isMessage);
            lblMessage.setText("Already Maximum no. of Books Are Added to the Current Section.");
            lblMessage.setStyle("-fx-text-fill: red");
        }
    }

    public void inflateUI (ViewBooksController.Book book) {
        tfBookId.setText(book.getBookId());
        tfBookId.setEditable(false);
        tfBookName.setText(book.getBookName());
        tfBookAuthor.setText(book.getAuthor());
        tfBookType.setText(booktype);
        tfBookEdition.setText(String.valueOf(book.getEdition()));
        btnAddBook.setText("Update");
        isEditMode = true;
        avail = book.isAvailable();
    }

    private void handleBookUpdate() {
        ViewBooksController.Book book = new ViewBooksController.Book(tfBookId.getText(), tfBookName.getText(), tfBookAuthor.getText(), Integer.parseInt(tfBookEdition.getText()), avail);
        if (databaseHandler.updateBook(book, booktype)) {
            lblMessage.setVisible(!isMessage);
            lblMessage.setText("Successfully Updated the Book Information.");
            bookSectionDashboard.isRefresh = true;
            bookSectionDashboard.loadBookDashboardData("SELECT * FROM BOOK_TABLE");
        } else {
            lblMessage.setVisible(!isMessage);
            lblMessage.setText("Book Updation Failed.");
        }
    }


    public void handleCancelOperation(ActionEvent actionEvent) {
        ((Stage)tfBookType.getScene().getWindow()).close();
        anchorPane.setEffect(null);
    }

    public void initData(String type, BookSectionDashboard sectionDashboard, AnchorPane pane) {
        booktype = type;
        bookSectionDashboard = sectionDashboard;
        anchorPane = pane;
    }

    public void handleCloseOperation(MouseEvent mouseEvent) {
        ((Stage)tfBookType.getScene().getWindow()).close();
        anchorPane.setEffect(null);
    }
}
