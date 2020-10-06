package students;

import alert.AlertMaker;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class StudentInfoDialog {

    ViewStudentsController.Student student;
    static int batchno;
    static StackPane stackPane;
    static AnchorPane anchorPane;

    public void showInfoDialog() {
        Label lblId = new Label("Roll No.:    ");
        lblId.setFont(Font.font(null, FontWeight.BOLD, 15));
        Label lblId1 = new Label(student.getStudentId());
        HBox hBox1 = new HBox();
        hBox1.getChildren().addAll(lblId, lblId1);

        Label lblName = new Label("Name:    ");
        lblName.setFont(Font.font(null, FontWeight.BOLD, 15));
        Label lblName1 = new Label(student.getStudentName());
        HBox hBox2 = new HBox();
        hBox2.getChildren().addAll(lblName, lblName1);

        Label lblYear = new Label("Joining Batch is:    ");
        lblYear.setFont(Font.font(null, FontWeight.BOLD, 15));
        Label lblYear1 = new Label(String.valueOf(batchno));
        HBox hBox3 = new HBox();
        hBox3.getChildren().addAll(lblYear, lblYear1);

        Label lblContact = new Label("Contact No.:     ");
        lblContact.setFont(Font.font(null, FontWeight.BOLD, 15));
        Label lblContact1 = new Label(student.getStudentContact());
        HBox hBox4 = new HBox();
        hBox4.getChildren().addAll(lblContact, lblContact1);

        Label lblIssueNo = new Label("No. of Issued Books:   ");
        lblIssueNo.setFont(Font.font(null, FontWeight.BOLD, 15));
        Label lblIssueNo1 = new Label(String.valueOf(student.getNoOfIssuedBooks()));
        HBox hBox5 = new HBox();
        hBox5.getChildren().addAll(lblIssueNo, lblIssueNo1);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(hBox1, hBox2, hBox3, hBox4, hBox5);

        Button okButton = new Button("OK");
        okButton.setPrefWidth(40);
        AlertMaker.showMaterialInfoDialog(stackPane, anchorPane, vBox, okButton, "Student Information");
    }

    public void init(ViewStudentsController.Student student1, int batch, StackPane stackPane1, AnchorPane anchorPane1) {
        student = student1;
        batchno = batch;
        stackPane = stackPane1;
        anchorPane = anchorPane1;
    }
}
