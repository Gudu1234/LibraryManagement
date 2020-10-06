package mainWindow;

import database.DatabaseHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SubmissionClass {

    DatabaseHandler handler;
    public SubmissionClass() {
        handler = DatabaseHandler.getInstance();
    }

    public boolean submissionSuccess (String id) {
        String query = "Select bookType, studentID from ISSUE_TABLE where bookID = '" + id + "'";
        ResultSet res = handler.execQuery(query);
        try {
            if (res.next()) {
                String action1 = "DELETE FROM ISSUE_TABLE WHERE bookID = '" + id + "'";
                String action2 = "UPDATE BOOK_TABLE SET isAvail = true WHERE book_id = '" + id + "'";
                if (handler.execAction(action1) && handler.execAction(action2)) {
                    String action3 = "UPDATE BOOK_SECTION SET avail_books = " + handler.returnNoOfAvailBooks(res.getString(1)) + " WHERE type_name = '" + res.getString(1) + "'";
                    String action4 = "UPDATE STUDENT_TABLE SET issue_book = " + handler.returnNoOfIssuedBooks(res.getString(2)) + " WHERE id = '" + res.getString(2) + "'";
                    if (handler.execAction(action3) && handler.execAction(action4)) {
                        return true;
                    } else {
                        System.out.println(1);
                        return false;
                    }
                } else {
                    System.out.println(2);
                    return false;
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
