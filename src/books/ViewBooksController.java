package books;

import alert.AlertMaker;
import database.DatabaseHandler;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mainWindow.BookComboBoxInitialize;
import mainWindow.BookSectionDashboard;
import org.controlsfx.control.textfield.CustomTextField;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ViewBooksController implements Initializable {
    public StackPane stackPane;
    public AnchorPane anchorPane;

    public TableView<Book> tableViewBooks;
    public TableColumn<Book, String> bookIdCol;
    public TableColumn<Book, String> bookNameCol;
    public TableColumn<Book, String> authorCol;
    public TableColumn<Book, Integer> editionCol;
    public TableColumn<Book, Boolean> availableCol;
    public CustomTextField tfBookName;
    public CustomTextField tfAuthorName;
    public CustomTextField tfEdition;
    public Button btnAddBooks;
    public Button btnDeleteBooks;
    public Button btnEditBookInfo;


    public static BookSectionDashboard bookSectionDashboard;
    public static BookComboBoxInitialize bookComboBoxInitialize;
    DatabaseHandler databaseHandler;
    ObservableList<Book> list = FXCollections.observableArrayList();
    static String booktype="";
    BookHandling.BookSection bookSection;
    Boolean errorFlag = false;
    String query = "";
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        databaseHandler = DatabaseHandler.getInstance();
        query = "SELECT * FROM BOOK_TABLE WHERE BOOK_TYPE = '" + booktype + "'" + " ORDER BY BOOK_ID";
        initCol();
        loadData(query);

        initializeBookTextFields();
        tableViewBooks.requestFocus();
        tableViewBooks.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER:
                    showDialog();
                default:
                    return;
            }
        });
    }

    private void initializeBookTextFields() {
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
            if (!tfEdition.getText().isEmpty()) {
                label1.setGraphic(searchIcon1);
                tfEdition.setText("");
            }
        });
        label2.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (!tfAuthorName.getText().isEmpty()) {
                label2.setGraphic(searchIcon2);
                tfAuthorName.setText("");
            }
        });
        label3.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (!tfBookName.getText().isEmpty()) {
                label3.setGraphic(searchIcon3);
                tfBookName.setText("");
            }
        });

        tfEdition.setLeft(label1);
        tfEdition.textProperty().addListener((observable, oldValue, newValue) -> {
            errorFlag = false;
            String query= "";
            if (tfEdition.getText().isEmpty()) {
                tfEdition.setStyle("-fx-background-color: white");
                label1.setGraphic(searchIcon1);
                tableViewBooks.getItems().clear();
                loadData("SELECT * FROM BOOK_TABLE WHERE book_type = '" + booktype + "'");
                return;
            }
            label1.setGraphic(closeIcon1);
            query = "SELECT * FROM BOOK_TABLE WHERE edition LIKE '" + newValue + "%' AND book_type = '" + booktype + "'";
            tableViewBooks.getItems().clear();
            loadData(query);
            if (errorFlag) {
                tfEdition.setStyle("-fx-background-color: red");
            } else {
                tfEdition.setStyle("-fx-background-color: white");
            }
        });

        tfAuthorName.setLeft(label2);
        tfAuthorName.textProperty().addListener((observable, oldValue, newValue) -> {
            errorFlag = false;
            String query= "";
            if (tfAuthorName.getText().isEmpty()) {
                tfAuthorName.setStyle("-fx-background-color: white");
                label2.setGraphic(searchIcon2);
                tableViewBooks.getItems().clear();
                loadData("SELECT * FROM BOOK_TABLE WHERE book_type = '" + booktype + "'");
                return;
            }
            label2.setGraphic(closeIcon2);
            query = "SELECT * FROM BOOK_TABLE WHERE author LIKE '" + newValue + "%' AND book_type = '" + booktype + "'";
            tableViewBooks.getItems().clear();
            loadData(query);
            if (errorFlag) {
                tfAuthorName.setStyle("-fx-background-color: red");
            } else {
                tfAuthorName.setStyle("-fx-background-color: white");
            }
        });

        tfBookName.setLeft(label3);
        tfBookName.textProperty().addListener((observable, oldValue, newValue) -> {
            errorFlag = false;
            String query= "";
            if (tfBookName.getText().isEmpty()) {
                tfBookName.setStyle("-fx-background-color: white");
                label3.setGraphic(searchIcon3);
                tableViewBooks.getItems().clear();
                loadData("SELECT * FROM BOOK_TABLE WHERE book_type = '" + booktype + "'");
                return;
            }
            label3.setGraphic(closeIcon3);
            query = "SELECT * FROM BOOK_TABLE WHERE book_name LIKE '" + newValue + "%' AND book_type = '" + booktype + "'";
            tableViewBooks.getItems().clear();
            loadData(query);
            if (errorFlag) {
                tfBookName.setStyle("-fx-background-color: red");
            } else {
                tfBookName.setStyle("-fx-background-color: white");
            }
        });
    }

    public void showDialog() {
        Book book = tableViewBooks.getSelectionModel().getSelectedItem();
        if (book == null) {
            return;
        }

        BookInfoDialog bookInfoDialog = new BookInfoDialog();
        bookInfoDialog.init(book, booktype, stackPane, anchorPane);
        bookInfoDialog.showBookInfoDialog();
    }

    private void loadData(String query) {
        tableViewBooks.getItems().clear();
        ResultSet resultSet = databaseHandler.execQuery(query);
        try {
            while (resultSet.next()) {
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);
                String author = resultSet.getString(3);
                int edition = resultSet.getInt(5);
                Boolean avail = resultSet.getBoolean(7);

                list.add(new Book(id, name, author, edition, avail));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (list.size() == 0) {
            errorFlag = true;
        } else {
            errorFlag = false;
        }
        tableViewBooks.setItems(list);
        tableViewBooks.getSelectionModel().select(0);
    }

    private void initCol() {
        bookIdCol.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        bookNameCol.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        editionCol.setCellValueFactory(new PropertyValueFactory<>("edition"));
        availableCol.setCellValueFactory(new PropertyValueFactory<>("available"));
    }

    public void handleTableRefreshOperation(MouseEvent actionEvent) {
        tableViewBooks.getItems().clear();
        loadData(query);
        tfAuthorName.setText("");
        tfEdition.setText("");
    }

    public void handleSearchByAuthor(ActionEvent actionEvent) {
        String author = tfAuthorName.getText().replaceAll(" ", "").toLowerCase();
        if (author.isEmpty()) {
            return;
        }

        String query1 = "SELECT * FROM BOOK_TABLE WHERE BOOK_TYPE = '" + booktype + "' AND AUTHOR_NAME = '" + author + "%'";
        loadData(query1);
    }

    public void handleSearchByEdition(ActionEvent actionEvent) {
        if (tfEdition.getText().isEmpty()) {
            return;
        }
        int edition = Integer.parseInt(tfEdition.getText());
        String query1 = "SELECT * FROM BOOK_TABLE WHERE BOOK_TYPE = '" + booktype + "' AND EDITION = " + edition;
        loadData(query1);
    }

    public void handleAddBooksOperation(ActionEvent actionEvent) {
        new AddBookController().initData(booktype, bookSectionDashboard, anchorPane);
        loadWindow("Add Books to this Section", "/books/add_book.fxml");
    }

    public void loadWindow(String title, String url) {
        try {
            BoxBlur boxBlur = new BoxBlur(4,4,4);
            Parent root = FXMLLoader.load(getClass().getResource(url));

            Stage stage = new Stage(StageStyle.TRANSPARENT);
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.setAlwaysOnTop(true);
            stage.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
                stage.requestFocus();
            });
            stage.show();
            anchorPane.setEffect(boxBlur);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleDeleteBooksOperation(ActionEvent actionEvent) {
        Book book = tableViewBooks.getSelectionModel().getSelectedItem();
        if (book == null) {
            Button button = new Button("OK");
            AlertMaker.showMaterialWarningDialog(stackPane, anchorPane, Arrays.asList(button), "Warning", "Please Select a Book For Delete.");
            return;
        }
        new DeleteBook(bookSectionDashboard, bookComboBoxInitialize).deleteBook(book.getBookId(), booktype, stackPane, anchorPane);
    }


    public void handleEditBookInfoOperation(ActionEvent actionEvent) {
        Book book = tableViewBooks.getSelectionModel().getSelectedItem();
        if (book == null) {
            Button button = new Button("OK");
            AlertMaker.showMaterialWarningDialog(stackPane, anchorPane, Arrays.asList(button), "Warning", "Please Select a Book For Edit.");
            return;
        }

        new AddBookController().initData(booktype, bookSectionDashboard, anchorPane);
        try {
            BoxBlur boxBlur = new BoxBlur(4,4,4);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/books/add_book.fxml"));
            Parent parent = loader.load();

            AddBookController controller = (AddBookController)loader.getController();
            controller.inflateUI(book);

            Stage stage = new Stage(StageStyle.TRANSPARENT);
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle("Edit Book Info");
            stage.setAlwaysOnTop(true);
            stage.show();
            anchorPane.setEffect(boxBlur);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initData(BookHandling.BookSection bookSection, BookSectionDashboard sectionDashboard, BookComboBoxInitialize comboBoxInitialize) {
        this.bookSection = bookSection;
        booktype = this.bookSection.getBookType();
        bookSectionDashboard = sectionDashboard;
        bookComboBoxInitialize = comboBoxInitialize;
    }

    public static class Book {
        private final SimpleStringProperty bookId;
        private final SimpleStringProperty bookName;
        private final SimpleStringProperty author;
        private final SimpleIntegerProperty edition;
        private final SimpleBooleanProperty available;

        public Book(String bookId, String bookName, String author, int edition, Boolean available) {
            this.bookId = new SimpleStringProperty(bookId);
            this.bookName = new SimpleStringProperty(bookName);
            this.author = new SimpleStringProperty(author);
            this.edition = new SimpleIntegerProperty(edition);
            this.available = new SimpleBooleanProperty(available);
        }

        public String getBookId() {
            return bookId.get();
        }

        public String getBookName() {
            return bookName.get();
        }

        public String getAuthor() {
            return author.get();
        }

        public int getEdition() {
            return edition.get();
        }

        public boolean isAvailable() {
            return available.get();
        }
  }
}
