package books;

import alert.AlertMaker;
import database.DatabaseHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import mainWindow.BookComboBoxInitialize;
import mainWindow.BookSectionDashboard;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class DeleteBook {

    DatabaseHandler handler;
    public static BookSectionDashboard bookSectionDashboard;
    public static BookComboBoxInitialize bookComboBoxInitialize;
    public DeleteBook(BookSectionDashboard sectionDashboard, BookComboBoxInitialize comboBoxInitialize) {
        handler = DatabaseHandler.getInstance();
        bookSectionDashboard = sectionDashboard;
        bookComboBoxInitialize = comboBoxInitialize;
    }

    public void deleteBook(String id, String type, StackPane stackPane, Pane pane) {
        Button confirm = new Button("Confirm");
        Button cancel = new Button("Cancel");
        Button okButton = new Button("OK");
        confirm.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (handler.canBookDeleted(id)) {
                String ac = "DELETE FROM BOOK_TABLE WHERE book_id = '" + id + "'";
                if (handler.execAction(ac)) {
                    bookSectionDashboard.isRefresh = true;
                    bookSectionDashboard.loadBookDashboardData("SELECT * FROM BOOK_TABLE");
                    String ac1 = "UPDATE BOOK_SECTION SET total_books = " + handler.returnNoOfBooks(type) + " WHERE type_name = '" + type + "'";
                    String ac2 = "UPDATE BOOK_SECTION SET avail_books = " + handler.returnNoOfAvailBooks(type) + " WHERE type_name = '" + type + "'";
                    if (handler.execAction(ac1) && handler.execAction(ac2)) {
                        AlertMaker.showMaterialInformationDialog(stackPane, pane, Arrays.asList(okButton), "Success", "Book Successfully deleted.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Book Deletion Failed.");
                    AlertMaker.showMaterialErrorDialog(stackPane, pane, Arrays.asList(okButton), "Error", "Wrong ID");
                }
            } else {
                AlertMaker.showMaterialErrorDialog(stackPane, pane, Arrays.asList(okButton), "Error", "The Book Can't Be deleted. As it is issued to a Student.");
                return;
            }
        });
        AlertMaker.showMaterialConfirmationDialog(stackPane, pane, Arrays.asList(confirm, cancel), "Confirmation", "Do you sure want to delete the book?");
    }

    public void deleteBookSection(BookHandling.BookSection bookSection, StackPane stackPane, Pane pane) {
        Button confirm = new Button("Confirm");
        Button cancel = new Button("Cancel");
        Button okButton = new Button("OK");
        String type = bookSection.getBookType();

        AlertMaker.showMaterialConfirmationDialog(stackPane, pane, Arrays.asList(confirm, cancel), "Confirmation", "Do You Sure Want to Delete Book Section?");
        confirm.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            String query = "SELECT book_id, isAvail FROM BOOK_TABLE WHERE book_type = '" + type + "'";
            ResultSet res = handler.execQuery(query);
            try {
                while (res.next()) {
                    String id = res.getString(1);
                    boolean avail = res.getBoolean(2);
                    if (avail) {
                        String ac = "DELETE FROM BOOK_TABLE WHERE book_id = '" + id + "'";
                        if (handler.execAction(ac)) {
                            String ac1 = "UPDATE BOOK_SECTION SET total_books = " + handler.returnNoOfBooks(type) + " WHERE type_name = '" + type + "'";
                            String ac2 = "UPDATE BOOK_SECTION SET avail_books = " + handler.returnNoOfAvailBooks(type) + " WHERE type_name = '" + type + "'";
                            if (handler.execAction(ac1) && handler.execAction(ac2)) {
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Database Error");
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            int countBooks = handler.returnNoOfBooks(type);
            if (countBooks == 0) {
                if (handler.deleteBookSection(bookSection)) {
                    AlertMaker.showMaterialInformationDialog(stackPane, pane, Arrays.asList(okButton), "Success", "Successfully Deleted The Book Section.");
                    bookComboBoxInitialize.initializeBookComboBox();
                }
            } else {
                AlertMaker.showMaterialWarningDialog(stackPane, pane, Arrays.asList(okButton), "Warning", countBooks + " Book(s) can't be deleted as they are not submitted yet.");
            }
            bookSectionDashboard.isRefresh = true;
            bookSectionDashboard.loadBookDashboardData("SELECT * FROM BOOK_TABLE");
        });
    }
}
