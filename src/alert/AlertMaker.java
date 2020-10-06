package alert;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.io.File;
import java.util.List;

public class AlertMaker {

    private static AlertMaker alertMaker;

    private static boolean isDialogShowing = false;
    private static boolean isWarningDialogShowing = false;
    private static boolean isErrorDialogShowing = false;
    private static boolean isInformationDialogShowing = false;
    private static boolean isConfirmationDialogShowing = false;

    public static AlertMaker getInstance() {
        if (alertMaker == null) {
            alertMaker = new AlertMaker();
        }
        return alertMaker;
    }

    public static void showMaterialInputDialog (StackPane rootPane, Node nodeToBeBlurred, List<Button> controls, String heading, String text, TextField tf) {
        BoxBlur blur = new BoxBlur(3, 3, 3);

        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);

        controls.forEach(controlButton -> {
            controlButton.getStyleClass().addAll("button-issue", "button-submit", "button-alert");
            controlButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                dialog.close();
            });
        });

        Label label = new Label(heading);
        label.setStyle("-fx-text-fill: linear-gradient(to bottom right, #428cd4, #ff9cda, #ea4492)");
        dialogLayout.setHeading(label);
        TextField textField = tf;
        textField.getStyleClass().add("text-field-issue");
        textField.setPromptText(text);
        dialogLayout.setBody(textField);
        dialogLayout.setActions(controls);
        if (!isDialogShowing) {
            isDialogShowing = true;
            dialog.show();
        }

        dialog.setOnDialogClosed((JFXDialogEvent event) -> {
            isDialogShowing = false;
            nodeToBeBlurred.setEffect(null);
        });

        nodeToBeBlurred.setEffect(blur);
    }

    public static void showMaterialInfoDialog (StackPane stackPane, Node node, Node node1, Button button, String heading) {
        BoxBlur blur = new BoxBlur(3, 3, 3);

        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        dialogLayout.setPrefWidth(600);
        JFXDialog dialog = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.TOP);

        button.getStyleClass().addAll("button-issue", "button-submit", "button-alert");
        button.setPrefWidth(100);
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            dialog.close();
        });

        Label label = new Label(heading);
        label.setFont(Font.font(null, FontWeight.EXTRA_BOLD, 20));
        label.setStyle("-fx-text-fill: linear-gradient(to bottom right, #428cd4, #ff9cda, #ea4492)");
        label.setAlignment(Pos.CENTER);
        dialogLayout.setHeading(label);
        dialogLayout.setBody(node1);
        dialogLayout.setActions(button);
        button.requestFocus();
        if (!isDialogShowing) {
            dialog.show();
            isDialogShowing = true;
        }

        dialog.setOnDialogClosed((JFXDialogEvent) -> {
            isDialogShowing = false;
            node.setEffect(null);
        });

        node.setEffect(blur);
    }

    public static void showMaterialWarningDialog (StackPane rootPane, Node nodeToBeBlurred, List<Button> controls, String heading, String body) {
        BoxBlur blur2 = new BoxBlur(3, 3, 3);

        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);

        controls.forEach(controlButton -> {
            controlButton.getStyleClass().addAll("button-issue", "button-submit", "button-alert");
            controlButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                dialog.close();
            });
        });

        Label lblHeading = new Label(heading);
        lblHeading.setFont(Font.font("Magneto", FontWeight.BOLD, 20));
        lblHeading.setStyle("-fx-text-fill: gold");
        dialogLayout.setHeading(lblHeading);
        File file = new File("C:\\Users\\Soumya\\Desktop\\FX Projects\\LibraryManagement\\src\\resources\\warning.png");
        Image image = new Image("/resources/warning.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(35);
        imageView.setFitWidth(35);
        Label label = new Label(body);
        label.setStyle("-fx-text-fill: linear-gradient(to bottom right, #428cd4, #ff9cda, #ea4492)");
        label.setFont(Font.font("Lucida Calligraphy", FontPosture.ITALIC, 15));
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(imageView, label);
        dialogLayout.setBody(hBox);
        dialogLayout.setActions(controls);
        if (!isWarningDialogShowing) {
            isWarningDialogShowing = true;
            dialog.show();
        }

        dialog.setOnDialogClosed((JFXDialogEvent event) -> {
            isWarningDialogShowing = false;
            nodeToBeBlurred.setEffect(null);
        });

        nodeToBeBlurred.setEffect(blur2);
    }

    public static void showMaterialErrorDialog (StackPane rootPane, Node nodeToBeBlurred, List<Button> controls, String heading, String body) {
        BoxBlur blur2 = new BoxBlur(3, 3, 3);

        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
        dialog.setStyle("-fx-border-radius: 20");

        controls.forEach(controlButton -> {
            controlButton.getStyleClass().addAll("button-issue", "button-submit", "button-alert");
            controlButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                dialog.close();
            });
        });

        Label lblHeading = new Label(heading);
        lblHeading.setFont(Font.font("Magneto", FontWeight.BOLD, 20));
        lblHeading.setStyle("-fx-text-fill: Red");
        dialogLayout.setHeading(lblHeading);
        Image image = new Image("/resources/error.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(35);
        imageView.setFitWidth(35);
        Label label = new Label(body);
        label.setStyle("-fx-text-fill: linear-gradient(to bottom right, #428cd4, #ff9cda, #ea4492)");
        label.setFont(Font.font("Lucida Calligraphy", FontPosture.ITALIC, 15));
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(imageView, label);
        dialogLayout.setBody(hBox);
        dialogLayout.setActions(controls);
        if (!isErrorDialogShowing) {
            isErrorDialogShowing = true;
            dialog.show();
        }
        dialog.setOnDialogClosed((JFXDialogEvent event) -> {
            isErrorDialogShowing = false;
            nodeToBeBlurred.setEffect(null);
        });
        nodeToBeBlurred.setEffect(blur2);
    }

    public static void showMaterialInformationDialog (StackPane rootPane, Node nodeToBeBlurred, List<Button> controls, String heading, String body) {
        BoxBlur blur2 = new BoxBlur(3, 3, 3);

        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
        dialog.setStyle("-fx-border-radius: 20");

        controls.forEach(controlButton -> {
            controlButton.getStyleClass().addAll("button-issue", "button-submit", "button-alert");
            controlButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                dialog.close();
            });
        });

        Label lblHeading = new Label(heading);
        lblHeading.setFont(Font.font("Magneto", FontWeight.BOLD, 20));
        lblHeading.setStyle("-fx-text-fill: skyblue");
        dialogLayout.setHeading(lblHeading);
        Image image = new Image("/resources/information.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(35);
        imageView.setFitWidth(35);
        Label label = new Label(body);
        label.setStyle("-fx-text-fill: linear-gradient(to bottom right, #428cd4, #ff9cda, #ea4492)");
        label.setFont(Font.font("Lucida Calligraphy", FontPosture.ITALIC, 15));
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(imageView, label);
        dialogLayout.setBody(hBox);
        dialogLayout.setActions(controls);
        if (!isInformationDialogShowing) {
            isInformationDialogShowing = true;
            dialog.show();
        }
        dialog.setOnDialogClosed((JFXDialogEvent event) -> {
            isInformationDialogShowing = false;
            nodeToBeBlurred.setEffect(null);
        });
        nodeToBeBlurred.setEffect(blur2);
    }

    public static void showMaterialConfirmationDialog (StackPane rootPane, Node nodeToBeBlurred, List<Button> controls, String heading, String body) {
        BoxBlur blur2 = new BoxBlur(3, 3, 3);

        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP);
        dialog.setStyle("-fx-border-radius: 20");

        controls.forEach(controlButton -> {
            controlButton.getStyleClass().addAll("button-issue", "button-submit", "button-alert");
            controlButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                dialog.close();
            });
        });

        Label lblHeading = new Label(heading);
        lblHeading.setFont(Font.font("Magneto", FontWeight.BOLD, 20));
        lblHeading.setStyle("-fx-text-fill: Green");
        dialogLayout.setHeading(lblHeading);
        Image image = new Image("/resources/confirmation.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(35);
        imageView.setFitWidth(35);
        Label label = new Label(body);
        label.setStyle("-fx-text-fill: linear-gradient(to bottom right, #428cd4, #ff9cda, #ea4492)");
        label.setFont(Font.font("Lucida Calligraphy", FontPosture.ITALIC, 15));
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(imageView, label);
        dialogLayout.setBody(hBox);
        HBox hBox1 = new HBox();
        hBox1.setAlignment(Pos.CENTER);
        hBox1.getChildren().addAll(controls);
        dialogLayout.setActions(hBox1);
        if (!isConfirmationDialogShowing) {
            isConfirmationDialogShowing = true;
            dialog.show();
        }
        dialog.setOnDialogClosed((JFXDialogEvent event) -> {
            isConfirmationDialogShowing = false;
            nodeToBeBlurred.setEffect(null);
        });
        nodeToBeBlurred.setEffect(blur2);
    }
}
