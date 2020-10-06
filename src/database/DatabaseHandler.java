package database;

import books.BookHandling;
import books.ViewBooksController;
import students.StudentHandling;
import students.ViewStudentsController;

import java.sql.*;

public class DatabaseHandler {
    private static DatabaseHandler databaseHandler;

    private String databaseName = "librarydatabase";
    private String userName = "root";
    private String password = "Soumya*1234";
    private String url = "jdbc:mysql://localhost:3306/" + databaseName;

    private Connection conn = null;
    private Statement stmt = null;

    public DatabaseHandler() {
        createConnection();
        try {
            setupBatchTable();
            setupStudentTable();
            setupBookTypeTable();
            setupBookTable();
            setupIssueTable();
            setupTypeCheckTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseHandler getInstance() {
        if (databaseHandler == null) {
            databaseHandler = new DatabaseHandler();
        }
        return databaseHandler;
    }

    private void createConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(url, userName, password);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupBatchTable() throws SQLException {
        final String TABLE_NAME = "BATCH";

        stmt = conn.createStatement();
        DatabaseMetaData dbm = conn.getMetaData();
        ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);

        if (tables.next()) {
            System.out.println("Table " + TABLE_NAME + " Already exists.Ready for go.");
        } else {
            stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                    +"  batch_no int not null primary key,\n"
                    +"  total_seats int,\n"
                    +"  students int"
                    +")");
        }
    }

    void setupStudentTable() throws SQLException {
        final String TABLE_NAME = "STUDENT_TABLE";

        stmt = conn.createStatement();
        DatabaseMetaData dbm = conn.getMetaData();
        ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);

        if (tables.next()) {
            System.out.println("Table " + TABLE_NAME + " Already exists. Ready for go.");
        } else {
            stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                    +"  id varchar(20) NOT NULL primary key,\n"
                    +"  name varchar(30),\n"
                    +"  year_joining int,\n"
                    +"  contact varchar(12),\n"
                    +"  issue_book int"
                    +")");
        }
    }

    void setupBookTypeTable() throws SQLException{
        final String TABLE_NAME = "BOOK_SECTION";

        stmt = conn.createStatement();
        DatabaseMetaData dbm = conn.getMetaData();
        ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);

        if (tables.next()) {
            System.out.println("Table " + TABLE_NAME + " Already exists. Ready for go.");
        } else {
            stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                    +"  type varchar(40) NOT NULL PRIMARY KEY,\n"
                    +"  type_name varchar(40),\n"
                    +"  capacity int,\n"
                    +"  total_books int,\n"
                    +"  avail_books int"
                    +")");
        }
    }

    void setupBookTable() throws SQLException {
        final String TABLE_NAME = "BOOK_TABLE";

        stmt = conn.createStatement();
        DatabaseMetaData dbm = conn.getMetaData();
        ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);

        if (tables.next()) {
            System.out.println("Table " + TABLE_NAME + " Already exists. Ready for go.");
        } else {
            stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                    +"  book_id varchar(20) NOT NULL PRIMARY KEY,\n"
                    +"  book_name varchar(100),\n"
                    +"  author varchar(100),\n"
                    +"  author_name varchar(100),\n"
                    +"  edition int,\n"
                    +"  book_type varchar(40),\n"
                    +"  isAvail boolean default true"
                    +"  )");
        }
    }

    void setupIssueTable() throws SQLException {
        final String TABLE_NAME = "ISSUE_TABLE";

        stmt = conn.createStatement();
        DatabaseMetaData dbm = conn.getMetaData();
        ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);

        if (tables.next()) {
            System.out.println("Table " + TABLE_NAME + " Already exists. Ready for go.");
        } else {
            stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                    +"  bookID varchar(20) NOT NULL PRIMARY KEY,\n"
                    +"  studentID varchar(20),\n"
                    +"  bookName varchar(100),\n"
                    +"  authorName varchar(100),\n"
                    +"  bookType varchar(20),\n"
                    +"  edition_book int,\n"
                    +"  issueTime timestamp default current_timestamp,\n"
                    +"  renew_count int,\n"
                    +"  FOREIGN KEY (bookID) REFERENCES BOOK_TABLE(book_id),\n"
                    +"  FOREIGN KEY (studentID) REFERENCES STUDENT_TABLE(id)"
                    +")");
        }
    }

    void setupTypeCheckTable() throws SQLException {
        final String TABLE_NAME = "TYPE_CHECK";

        stmt = conn.createStatement();
        DatabaseMetaData dbm = conn.getMetaData();
        ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);

        if (tables.next()) {
            System.out.println("Table " + TABLE_NAME + " Already exists. Ready for go.");
        } else {
            stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                    +"  bookType varchar(40) NOT NULL PRIMARY KEY"
                    +")");
        }
    }

    public boolean execAction(String ac) {
        try {
            stmt = conn.createStatement();
            stmt.execute(ac);
            return true;
        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, "Database Error");
            e.printStackTrace();
        }
        return false;
    }

    public ResultSet execQuery(String qu) {
        ResultSet resultSet = null;
        try {
            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(qu);
        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, "Database Error");
            e.printStackTrace();
        }
        return resultSet;
    }

    public boolean deleteBatch(StudentHandling.Batch batch) {
        String ac = "DELETE FROM BATCH WHERE BATCH_NO = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(ac);
            stmt.setInt(1, batch.getBatchNo());
            int res = stmt.executeUpdate();
            if (res == 1) {
                return true;
            }
        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, "Database Error");
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkForBatchYear(int no) {
        String query = "SELECT BATCH_NO FROM BATCH ORDER BY BATCH_NO";
        try {
            stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery(query);
            if (res.next()) {
                if (res.getInt(1) < no) {
                    return true;
                }
            } else {
                return true;
            }
        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, "Database error");
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkValidEntry(int no) {
        String query1 = "SELECT TOTAL_SEATS FROM BATCH WHERE BATCH_NO = " + no;
        ResultSet resultSet = execQuery(query1);
        String query = "SELECT COUNT(*) FROM STUDENT_TABLE WHERE YEAR_JOINING = " + no;
        try {
            if (resultSet.next()) {
                stmt = conn.createStatement();
                ResultSet res = stmt.executeQuery(query);
                if (res.next()) {
                    if (res.getInt(1) < resultSet.getInt(1)) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, "Database Error");
            e.printStackTrace();
        }
        return false;
    }

    public int returnNoOfStudents(int no) {
        String query1 = "SELECT TOTAL_SEATS FROM BATCH WHERE BATCH_NO = " + no;
        ResultSet resultSet = execQuery(query1);
        String query = "SELECT COUNT(*) FROM STUDENT_TABLE WHERE YEAR_JOINING = " + no;
        try {
            if (resultSet.next()) {
                stmt = conn.createStatement();
                ResultSet res = stmt.executeQuery(query);
                if (res.next()) {
                    if (res.getInt(1) <= resultSet.getInt(1)) {
                        return res.getInt(1);
                    }
                }
            }

        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, "Database Error");
            e.printStackTrace();
        }
        return 0;
    }

    public boolean deleteBookSection(BookHandling.BookSection bookSection) {
        String ac = "DELETE FROM BOOK_SECTION WHERE TYPE = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(ac);
            stmt.setString(1, bookSection.getBookType().replaceAll(" ", "").toLowerCase());
            int res = stmt.executeUpdate();
            if (res == 1) {
                return true;
            }
        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, "Database Error");
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkBookValidEntry(String bookType) {
        String query = "SELECT CAPACITY FROM BOOK_SECTION WHERE TYPE_NAME = '" + bookType + "'";
        String query1 = "SELECT COUNT(*) FROM BOOK_TABLE WHERE BOOK_TYPE = '" + bookType + "'";
        try {
            stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);
            if (resultSet.next()) {
                stmt = conn.createStatement();
                ResultSet res = stmt.executeQuery(query1);
                if (res.next()) {
                    if (res.getInt(1) < resultSet.getInt(1)) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, "Database Error");
            e.printStackTrace();
        }
        return false;
    }

    public int returnNoOfBooks(String bookType) {
        String query = "SELECT CAPACITY FROM BOOK_SECTION WHERE TYPE_NAME = '" + bookType + "'";
        String query1 = "SELECT COUNT(*) FROM BOOK_TABLE WHERE BOOK_TYPE = '" + bookType + "'";
        ResultSet res=null, resultSet=null;
        int x = 0;
        try {
            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(query);
            stmt = conn.createStatement();
            res = stmt.executeQuery(query1);
            if (res.next()) {
                return res.getInt(1);
            }
            if (resultSet.next()) {
                x = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, "Database Error");
            e.printStackTrace();
        }
        return x;
    }

    public int returnNoOfAvailBooks(String bookType) {
        String query1 = "SELECT COUNT(*) FROM BOOK_TABLE WHERE BOOK_TYPE = '" + bookType + "' AND isAvail = TRUE";
        String query = "SELECT AVAIL_BOOKS FROM BOOK_SECTION WHERE TYPE_NAME = '" + bookType + "'";
        ResultSet resultSet = null;
        ResultSet res = null;
        int x = 0;
        try {
            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(query);
            stmt = conn.createStatement();
            res = stmt.executeQuery(query1);
            if (res.next()) {
                return res.getInt(1);
            }
            if (resultSet.next()) {
                x = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, "Database Error");
            e.printStackTrace();
        }
        return x;
    }

    public boolean updateBook(ViewBooksController.Book book, String type) {
        String action = "UPDATE BOOK_TABLE SET book_name = ?, author = ?, author_name = ?, edition = ?, book_type = ?, isAvail = ? WHERE book_id = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(action);
            stmt.setString(1, book.getBookName());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getAuthor().replaceAll(" ", "").toLowerCase());
            stmt.setInt(4, book.getEdition());
            stmt.setString(5, type);
            stmt.setBoolean(6, book.isAvailable());
            stmt.setString(7, book.getBookId());

            int count = stmt.executeUpdate();
            return (count > 0);
        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, "Database Error");
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateStudent(ViewStudentsController.Student student) {
        String action = "UPDATE STUDENT_TABLE SET name = ?, contact = ?, issue_book = ? WHERE id = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(action);
            stmt.setString(1, student.getStudentName());
            stmt.setString(2, student.getStudentContact());
            stmt.setInt(3, student.getNoOfIssuedBooks());
            stmt.setString(4, student.getStudentId());

            int count = stmt.executeUpdate();
            return (count > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isStudentAbleTobeIssued (String id) {
        String query = "SELECT issue_book FROM STUDENT_TABLE WHERE id = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, id);
            ResultSet res = stmt.executeQuery();
            if (res.next()) {
                return (res.getInt(1) < 5);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkAvailabilityBook (String id) {
        String query = "SELECT COUNT(*) FROM BOOK_TABLE WHERE book_id = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, id);
            ResultSet res = stmt.executeQuery();
            if (res.next()) {
                return (res.getInt(1) == 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkAvailabilityStudent (String id) {
        String query = "SELECT COUNT(*) FROM STUDENT_TABLE WHERE id = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, id);
            ResultSet res = stmt.executeQuery();
            if (res.next()) {
                return (res.getInt(1) == 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkIssueConfirmation (String id) {
        String query = "SELECT issue_book FROM STUDENT_TABLE WHERE id = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, id);
            ResultSet res = stmt.executeQuery();
            if (res.next()) {
                return (res.getInt(1) > 0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int returnNoOfIssuedBooks (String id) {
        String query = "SELECT COUNT(*) FROM ISSUE_TABLE WHERE studentID = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, id);
            ResultSet res = stmt.executeQuery();
            if (res.next()) {
                return res.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean checkAvailabilityStudent2 (String id, int no) {
        String query = "SELECT COUNT(*) FROM STUDENT_TABLE WHERE id = ? AND year_joining = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, id);
            stmt.setInt(2, no);
            ResultSet res = stmt.executeQuery();
            if (res.next()) {
                return (res.getInt(1) == 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkAvailabilityBook2 (String id, String type) {
        String query = "SELECT COUNT(*) FROM BOOK_TABLE WHERE book_id = ? AND book_type = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, id);
            stmt.setString(2, type);
            ResultSet res = stmt.executeQuery();
            if (res.next()) {
                return (res.getInt(1) == 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean canBookDeleted (String id) {
        String query = "SELECT COUNT(*) FROM ISSUE_TABLE WHERE bookID = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, id);
            ResultSet res = stmt.executeQuery();
            if (res.next()) {
                return (res.getInt(1) == 0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
