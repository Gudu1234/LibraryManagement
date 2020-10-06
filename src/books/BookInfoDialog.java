package books;

import alert.AlertMaker;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class BookInfoDialog {
    ViewBooksController.Book book;
    static String booktype="";

    public static StackPane stackPane;
    public static AnchorPane anchorPane;

    public void showBookInfoDialog () {
        Label lblId = new Label("Book ID is:    ");
        lblId.setFont(Font.font(null, FontWeight.BOLD, 15));
        Label lblId1 = new Label(book.getBookId());
        HBox hBox1 = new HBox();
        hBox1.getChildren().addAll(lblId, lblId1);

        Label lblName = new Label("Book Name is:    ");
        lblName.setFont(Font.font(null, FontWeight.BOLD, 15));
        Label lblName1 = new Label(book.getBookName());
        HBox hBox2 = new HBox();
        hBox2.getChildren().addAll(lblName, lblName1);

        Label lblAuthor = new Label("Book Author is:    ");
        lblAuthor.setFont(Font.font(null, FontWeight.BOLD, 15));
        Label lblAuthor1 = new Label(book.getAuthor());
        HBox hBox3 = new HBox();
        hBox3.getChildren().addAll(lblAuthor, lblAuthor1);

        Label lblBookType = new Label("Category of Book is:     ");
        lblBookType.setFont(Font.font(null, FontWeight.BOLD, 15));
        Label lblBookType1 = new Label(booktype);
        HBox hBox4 = new HBox();
        hBox4.getChildren().addAll(lblBookType, lblBookType1);

        Label lblEdition = new Label("Edition of Book is:   ");
        lblEdition.setFont(Font.font(null, FontWeight.BOLD, 15));
        Label lblEdition1 = new Label(String.valueOf(book.getEdition()));
        HBox hBox5 = new HBox();
        hBox5.getChildren().addAll(lblEdition, lblEdition1);

        Label lblAvailable = new Label("Availability of Book:   ");
        lblAvailable.setFont(Font.font(null, FontWeight.BOLD, 15));
        Label lblAvailable1 = new Label(String.valueOf(book.isAvailable()));
        HBox hBox6 = new HBox();
        hBox6.getChildren().addAll(lblAvailable, lblAvailable1);

        VBox vBox = new VBox();

        vBox.getChildren().addAll(hBox1, hBox2, hBox3, hBox4, hBox5, hBox6);

        Button okButton = new Button("Ok");
        AlertMaker.showMaterialInfoDialog(stackPane, anchorPane, vBox, okButton, "Book Information");

    }

    public void init(ViewBooksController.Book book1, String type, StackPane sp, AnchorPane ap) {
        book = book1;
        booktype = type;
        stackPane = sp;
        anchorPane = ap;
    }
}
