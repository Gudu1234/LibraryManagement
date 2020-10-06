package students;

import alert.AlertMaker;
import database.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import mainWindow.StudentComboBoxInitialize;
import org.controlsfx.control.CheckComboBox;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;

public class AddBatchController implements Initializable {
    public TextField tfBatchNo;
    public TextField tfTotalSeats;
    public Button btnaddBatch;
    public Button btnCancel;
    public Label lblSuccess;
    public StackPane stackPane;
    public AnchorPane anchorPane;

    DatabaseHandler databaseHandler;
    boolean isLabelVisible = false;
    public static AnchorPane anchorPane1;

    public static StudentComboBoxInitialize studentComboBoxInitialize;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        databaseHandler = DatabaseHandler.getInstance();
        lblSuccess.setVisible(isLabelVisible);
    }

    public void handleAddBatchOperation(ActionEvent actionEvent) {
        String qu = "SELECT COUNT(*) FROM BATCH";
        ResultSet res = databaseHandler.execQuery(qu);
        lblSuccess.setVisible(isLabelVisible);
        try {
            if (res.next()) {
                if (res.getInt(1) < 5) {
                    int batchno = 0, totalseats = 0;
                    if (tfBatchNo.getText().isEmpty() || tfTotalSeats.getText().isEmpty()) {
                        lblSuccess.setVisible(!isLabelVisible);
                        lblSuccess.setText("Please Input Data in All Fields.");
                        return;
                    }
                    try {
                        batchno = Integer.parseInt(tfBatchNo.getText());
                        totalseats = Integer.parseInt(tfTotalSeats.getText());
                        if (!databaseHandler.checkForBatchYear(batchno)) {
                            isLabelVisible = true;
                            lblSuccess.setVisible(isLabelVisible);
                            lblSuccess.setText("Please Input a Valid Batch Year");
                            isLabelVisible = false;
                            return;
                        }
                        String ac = "INSERT INTO BATCH VALUES (" + batchno + ", " + totalseats + ", " + 0 + ")";
                        if (databaseHandler.execAction(ac)) {
                            isLabelVisible = true;
                            lblSuccess.setVisible(isLabelVisible);
                            lblSuccess.setText("Successfully Added");
                            isLabelVisible = false;
                            lblSuccess.setVisible(isLabelVisible);
                            tfBatchNo.setText("");
                            tfTotalSeats.setText("");
                            tfTotalSeats.requestFocus();
                            studentComboBoxInitialize.initializeComboBox();
                        } else {
                            JOptionPane.showMessageDialog(null, "Database Error");
                        }
                    } catch (Exception e) {
                        isLabelVisible = true;
                        lblSuccess.setVisible(isLabelVisible);
                        lblSuccess.setText("Please Enter according to the example");
                        isLabelVisible = false;
                    }
                } else {
                    Button button = new Button("OK");
                    AlertMaker.showMaterialWarningDialog(stackPane, anchorPane, Arrays.asList(button), "Warning", "Maximum numbers of records are already added.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void handleCancelOperation(ActionEvent actionEvent) {
        ((Stage)tfTotalSeats.getScene().getWindow()).close();
        anchorPane1.setEffect(null);

    }

    public void init(StudentComboBoxInitialize comboBoxInitialize, AnchorPane pane) {
        studentComboBoxInitialize = comboBoxInitialize;
        anchorPane1 = pane;
    }

    public void handleCloseOperation(MouseEvent mouseEvent) {
        ((Stage)tfTotalSeats.getScene().getWindow()).close();
        anchorPane1.setEffect(null);
    }
}
