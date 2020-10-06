package mainWindow;

import books.ViewBooksController;
import database.DatabaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import books.ViewBooksController.Book;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookSectionDashboard {
    public TableView<Book> tableViewBooksDashboard;
    public TableColumn<ViewBooksController.Book, String> bookIdCol;
    public TableColumn<Book, String> bookNameCol;
    public TableColumn<Book, String> authorCol;
    public TableColumn<Book, Integer> editionCol;
    public TableColumn<Book, Boolean> availableCol;

    public Pane pane;
    public TextArea textArea;
    ObservableList<ViewBooksController.Book> books = FXCollections.observableArrayList();
    DatabaseHandler databaseHandler;

    public Boolean isRefresh = false;
    Boolean isError = false;

    public BookSectionDashboard(TableView<Book> tableViewBooks, TableColumn<Book, String> bookIdCol, TableColumn<Book, String> bookNameCol, TableColumn<Book, String> authorCol, TableColumn<Book, Integer> editionCol, TableColumn<Book, Boolean> availableCol, Pane pane, TextArea textArea) {
        this.tableViewBooksDashboard = tableViewBooks;
        this.bookIdCol = bookIdCol;
        this.bookNameCol = bookNameCol;
        this.authorCol = authorCol;
        this.editionCol = editionCol;
        this.availableCol = availableCol;
        this.pane = pane;
        this.textArea = textArea;

        databaseHandler = DatabaseHandler.getInstance();
    }

    public void initializeBookDashboardSection() {
        initCol();
        tableViewBooksDashboard.requestFocus();
        String query = "SELECT * FROM BOOK_TABLE";
        loadBookDashboardData(query);
        tableViewBooksDashboard.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER:
                    showStatusPane();
                default:
                    return;
            }
        });
        tableViewBooksDashboard.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            switch (event.getCode()) {
                case F5:
                    tableViewBooksDashboard.getItems().clear();
                    loadBookDashboardData(query);
                default:
                    return;
            }
        });
    }

    private void showStatusPane() {
        Book book = tableViewBooksDashboard.getSelectionModel().getSelectedItem();
        if (book == null) {
            return;
        }
        String data = "";
        textArea.setText("");

        String query = "SELECT * FROM BOOK_TABLE WHERE book_id = '" + book.getBookId() + "'";
        ResultSet res = databaseHandler.execQuery(query);
        try {
            if (res.next()) {
                data = data + "BOOK ID:- " + "\n\t" + res.getString(1) + "\n";
                data = data + "BOOK NAME:- " + "\n\t" + res.getString(2) + "\n";
                data = data + "AUTHOR:- "  + "\n\t" +  res.getString(3) + "\n";
                data = data + "EDITION:- " + "\n\t" + res.getInt(5) + "\n";
                data = data + "CATEGORY:- " + "\n\t" + res.getString(6) + "\n";
                data = data + "AVAILABLE:- " + "\n\t" + res.getBoolean(7) + "\n";

                if (!res.getBoolean(7)) {
                    data = data + "BOOK IS ISSUED TO:-  " + "\n\n";
                    String query1 = "SELECT studentID FROM ISSUE_TABLE WHERE bookID = '" + res.getString(1) + "'";
                    ResultSet res1 = databaseHandler.execQuery(query1);
                    if (res1.next()){
                        String query2 = "SELECT * FROM STUDENT_TABLE WHERE id = '" + res1.getString(1) + "'";
                        ResultSet res2 = databaseHandler.execQuery(query2);
                        if (res2.next()) {
                            data = data + "STUDENT ROLL:- " + "\n\t" + res2.getString(1) + "\n";
                            data = data + "STUDENT NAME:- " + "\n\t" + res2.getString(2) + "\n";
                            data = data + "JOINING BATCH:- " + "\n\t" + res2.getInt(3) + "\n";
                            data = data + "CONTACT N0:- " + "\n\t" + res2.getString(4) + "\n";
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        textArea.setText(data);
        pane.toFront();
    }

    private void initCol() {
        bookIdCol.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        bookNameCol.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        editionCol.setCellValueFactory(new PropertyValueFactory<>("edition"));
        availableCol.setCellValueFactory(new PropertyValueFactory<>("available"));
    }

    public void loadBookDashboardData(String query) {
        if (isRefresh) {
            tableViewBooksDashboard.getItems().clear();
            isRefresh = false;
        }
        ResultSet res = databaseHandler.execQuery(query);
        try {
            while (res.next()) {
                String id = res.getString(1);
                String name = res.getString(2);
                String author = res.getString(3);
                int edition = res.getInt(5);
                boolean isAvail = res.getBoolean(7);
                books.add(new Book(id, name, author, edition, isAvail));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            e.printStackTrace();
        }
        if (books.size() == 0) {
            isError = true;
        } else {
            isError = false;
        }
        tableViewBooksDashboard.setItems(books);
        tableViewBooksDashboard.getSelectionModel().select(0);
    }
}
