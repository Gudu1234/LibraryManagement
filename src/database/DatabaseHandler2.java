package database;

import java.sql.*;

public class DatabaseHandler2 {
    private static DatabaseHandler2 databaseHandler;

    private String databaseName = "logindatabase";
    private String userName = "root";
    private String password = "Soumya*1234";
    private String url = "jdbc:mysql://localhost:3306/" + databaseName;

    private Connection conn = null;
    private Statement stmt = null;

    public DatabaseHandler2() {
        createConnection();
        try {
            setupLoginTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseHandler2 getInstance() {
        if (databaseHandler == null) {
            databaseHandler = new DatabaseHandler2();
        }
        return databaseHandler;
    }


    private void createConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(url, userName, password);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupLoginTable() throws SQLException {
        final String TABLE_NAME = "LOGIN";

        stmt = conn.createStatement();
        DatabaseMetaData dbm = conn.getMetaData();
        ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);

        if (tables.next()) {
            System.out.println("Table " + TABLE_NAME + " Already exists. Ready for go");
        } else {
            stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                    +"  email_id varchar(50) primary key,\n"
                    +"  password varchar(30)"
                    +")");
        }
    }

    public boolean execAction(String ac) {
        try {
            stmt = conn.createStatement();
            stmt.execute(ac);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ResultSet execQuery(String query) {
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return rs;
    }
}
