package login;

import alert.AlertMaker;
import animatefx.animation.ZoomIn;
import database.DatabaseHandler2;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.CustomPasswordField;
import org.controlsfx.control.textfield.CustomTextField;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    public StackPane stackPane;
    public AnchorPane anchorPane;
    public Pane leftPane;

    public Pane paneSignIn;
    public CustomTextField signInEmail;
    public CustomPasswordField signInPassword;
    public Label lblForgetPassword;

    public Pane paneSignUpStarted;
    public CustomTextField signUpEmail;

    public Pane paneSignUpPassword;
    public CustomPasswordField signUpPassword;
    public CustomPasswordField signUpConfirmPassword;

    public Pane paneForgetPassword;
    public CustomPasswordField newPassword;
    public CustomPasswordField confirmPassword;
    public Button btnChangePassword;

    public Circle btnClose;
    public Circle btnMaximize;
    public Circle btnMinimize;
    public ImageView mainImage;

    public Label labelInvalidPassword;
    public Label lblWrongPassword;
    public Label lblWrongPassword2;
    public Label lblInvalidEmailInput;
    public Label lblWrongPasswordFormat;
    public Label lblWrongPasswordFormat2;
    public BorderPane borderPane;


    DatabaseHandler2 databaseHandler;
    boolean isError = false;
    boolean isError2 = false;
    boolean isError3 = false;
    boolean isWrong = false;
    boolean isWrong2 = false;
    boolean isInput = false;

    Boolean isPasswordSignInVisible = false;
    Boolean isPasswordSignUpVisible1 = false;
    Boolean isPasswordSignUpVisible2 = false;
    Boolean isPasswordChangeVisible1 = false;
    Boolean isPasswordChangeVisible2 = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        databaseHandler = DatabaseHandler2.getInstance();
        initPaneSignIn();
        initPaneSignUpPassword();
        initPaneForgetPassword();
    }

    public void setPasswordSignInVisible(Boolean passwordSignInVisible) {
        isPasswordSignInVisible = passwordSignInVisible;
    }

    public void setPasswordSignUpVisible1(Boolean passwordSignUpVisible1) {
        isPasswordSignUpVisible1 = passwordSignUpVisible1;
    }

    public void setPasswordSignUpVisible2(Boolean passwordSignUpVisible2) {
        isPasswordSignUpVisible2 = passwordSignUpVisible2;
    }

    public void setPasswordChangeVisible1(Boolean passwordChangeVisible1) {
        isPasswordChangeVisible1 = passwordChangeVisible1;
    }

    public void setPasswordChangeVisible2(Boolean passwordChangeVisible2) {
        isPasswordChangeVisible2 = passwordChangeVisible2;
    }

    private void initPaneSignIn() {
        MaterialIconLoader materialIconLoader = new MaterialIconLoader();
        materialIconLoader.materialIconMaker();
        Button button = materialIconLoader.getButton();
        signInPassword.setRight(materialIconLoader.getButtonGraphic(isPasswordSignInVisible));
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            signInPassword.setRight(null);
            signInPassword.setRight(materialIconLoader.getButtonGraphic(!isPasswordSignInVisible));
            setPasswordSignInVisible(!isPasswordSignInVisible);
        });
    }

    private void initPaneSignUpPassword() {
        MaterialIconLoader materialIconLoader = new MaterialIconLoader();
        materialIconLoader.materialIconMaker();
        Button button = materialIconLoader.getButton();
        signUpPassword.setRight(materialIconLoader.getButtonGraphic(isPasswordSignUpVisible1));
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            signUpPassword.setRight(null);
            signUpPassword.setRight(materialIconLoader.getButtonGraphic(!isPasswordSignUpVisible1));
            setPasswordSignUpVisible1(!isPasswordSignUpVisible1);
        });
        MaterialIconLoader materialIconLoader1 = new MaterialIconLoader();
        materialIconLoader1.materialIconMaker();
        Button button1 = materialIconLoader1.getButton();
        signUpConfirmPassword.setRight(materialIconLoader1.getButtonGraphic(isPasswordSignUpVisible2));
        button1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            signUpConfirmPassword.setRight(null);
            signUpConfirmPassword.setRight(materialIconLoader1.getButtonGraphic(!isPasswordSignUpVisible2));
            setPasswordSignUpVisible2(!isPasswordSignUpVisible2);
        });
    }

    private void initPaneForgetPassword() {
        MaterialIconLoader materialIconLoader = new MaterialIconLoader();
        materialIconLoader.materialIconMaker();
        Button button = materialIconLoader.getButton();
        newPassword.setRight(materialIconLoader.getButtonGraphic(isPasswordChangeVisible1));
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            newPassword.setRight(null);
            newPassword.setRight(materialIconLoader.getButtonGraphic(!isPasswordChangeVisible1));
            setPasswordChangeVisible1(!isPasswordChangeVisible1);
        });
        MaterialIconLoader materialIconLoader1 = new MaterialIconLoader();
        materialIconLoader1.materialIconMaker();
        Button button1 = materialIconLoader1.getButton();
        confirmPassword.setRight(materialIconLoader1.getButtonGraphic(isPasswordChangeVisible2));
        button1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            confirmPassword.setRight(null);
            confirmPassword.setRight(materialIconLoader1.getButtonGraphic(!isPasswordChangeVisible2));
            setPasswordChangeVisible2(!isPasswordChangeVisible2);
        });
    }

    public void handleSignInButton(ActionEvent actionEvent) {

        String email = signInEmail.getText();
        String pass = signInPassword.getText();

        if (email.isEmpty() || pass.isEmpty()) {
            Button button = new Button("Okay!");
            AlertMaker.showMaterialWarningDialog(stackPane, anchorPane, Arrays.asList(button), "Warning", "The Fields can't be left blank.");
            return;
        }
        if (pass.length() < 8 || pass.length() > 16) {
            signInPassword.setText("");
            isError = true;
            labelInvalidPassword.setVisible(true);
            labelInvalidPassword.setText("Password must between 8 to 16 Characters.");
            labelInvalidPassword.getStyleClass().add("label-error");
            isError = false;
            return;
        }
        String query = "SELECT * FROM LOGIN WHERE email_id = '" + email + "'";
        ResultSet resultSet = databaseHandler.execQuery(query);
        try {
            if (resultSet.next()) {
                String id = resultSet.getString(1);
                String password = resultSet.getString(2);
                if (email.equals(id) && pass.equals(password)) {
                    Button button = new Button("Okay!");
                    //JOptionPane.showMessageDialog(null, "Login Success");
                    ((Stage)leftPane.getScene().getWindow()).close();
                    loadMainWindow("Library Management", "/mainWindow/home_page.fxml");
                } else {
                    isError = true;
                    signInPassword.setText("");
                    labelInvalidPassword.setVisible(isError);
                    labelInvalidPassword.setText("Invalid Password");
                    labelInvalidPassword.getStyleClass().add("label-error");
                    isError = false;
                }
            } else {
                Button button = new Button("Okay!");
                AlertMaker.showMaterialErrorDialog(stackPane, anchorPane, Arrays.asList(button), "Failure", "Login Failure.");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleSignUpButton(ActionEvent actionEvent) {
        new ZoomIn(paneSignUpStarted).play();
        lblInvalidEmailInput.setVisible(isInput);
        signUpEmail.setPromptText("Email");
        paneSignUpStarted.toFront();
    }


    public void handleGetStartedButton(ActionEvent actionEvent) {
        if (signUpEmail.getText().isEmpty()) {

            Button button = new Button("Okay!I'll Check");
            AlertMaker.showMaterialWarningDialog(stackPane, anchorPane, Arrays.asList(button), "Warning", "The Email Field can't be left blank.");
            return;
        }
        if (!signUpEmail.getText().contains(".")) {
            isInput = true;
            lblInvalidEmailInput.setVisible(isInput);
            lblInvalidEmailInput.setText("Please Enter the Correct email format.");
            if (isInput) {
                signUpEmail.setText("");
                signUpEmail.setPromptText("user@example.com");
            }
            lblInvalidEmailInput.getStyleClass().add("label-error");
            isInput = false;
            return;
        }
        new ZoomIn(paneSignUpPassword).play();
        lblWrongPassword2.setVisible(isWrong2);
        lblWrongPasswordFormat.setVisible(isError2);
        paneSignUpPassword.toFront();
    }

    public void handleBackSignUpButton(MouseEvent mouseEvent) {
        new ZoomIn(paneSignIn).play();
        labelInvalidPassword.setVisible(isError);
        paneSignIn.toFront();
    }


    public void handleCreateAccButton(ActionEvent actionEvent) {
        String email = signUpEmail.getText();
        String pass1 = signUpPassword.getText();
        String pass2 = signUpConfirmPassword.getText();

        if (pass1.isEmpty() && pass2.isEmpty()) {

            Button button = new Button("Okay!I'll Check");
            AlertMaker.showMaterialWarningDialog(stackPane, anchorPane, Arrays.asList(button), "Missing", "Enter values in Password Fields.");
            return;
        }

        if (pass1.length() < 8 || pass1.length() > 16) {
            signUpPassword.setText("");
            signUpConfirmPassword.setText("");
            isError2 = true;
            lblWrongPasswordFormat.setVisible(isError2);
            lblWrongPasswordFormat.setText("Password must between 8 to 16 Characters.");
            lblWrongPasswordFormat.getStyleClass().add("label-error");
            isError2 = false;
            return;
        }

        if (pass1.equals(pass2)) {
            Button okButton = new Button("Yes");
            okButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                String action = "INSERT INTO LOGIN VALUES (" + "'" + email  + "'" + ", " + "'" + signUpPassword.getText() + "'" + ")";
                if (databaseHandler.execAction(action)) {

                    Button button = new Button("Okay");
                    AlertMaker.showMaterialInformationDialog(stackPane, anchorPane, Arrays.asList(button), "Success", "Account has been created successfully.");
                    new ZoomIn(paneSignIn).play();
                    labelInvalidPassword.setVisible(isError);
                    paneSignIn.toFront();
                } else {
                    Button button = new Button("Okay!");
                    AlertMaker.showMaterialErrorDialog(stackPane, anchorPane, Arrays.asList(button), "Database Error", "Account Creation Failed.");
                    new ZoomIn(paneSignUpStarted).play();
                    paneSignUpStarted.toFront();
                }
            });

            Button cancelButton = new Button("No");
            cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                Button button = new Button("That's Okay!");
                AlertMaker.showMaterialInformationDialog(stackPane, anchorPane, Arrays.asList(button), "Cancelled", "Account creation has been cancelled.");
            });

            AlertMaker.showMaterialConfirmationDialog(stackPane, anchorPane, Arrays.asList(okButton, cancelButton), "Confirmation", "Do you want to create an Account?");

        } else {
            isWrong2 = true;
            lblWrongPassword2.setVisible(isWrong2);
            lblWrongPassword2.setText("Wrong Password. The Two password must same.");
            lblWrongPassword2.getStyleClass().add("label-error");
            signUpConfirmPassword.setText("");
            isWrong2 = false;
            return;
        }
    }

    public void handleBackSignUpPasswordButton(MouseEvent mouseEvent) {
        new ZoomIn(paneSignUpStarted).play();
        paneSignUpStarted.toFront();
    }



    public void handleChangePasswordButton(ActionEvent actionEvent) {
        String email = signInEmail.getText();
        String pass1 = newPassword.getText().toString();
        String pass2 = confirmPassword.getText().toString();

        if (pass1.isEmpty() && pass2.isEmpty()) {
            Button button = new Button("Okay!I'll Check");
            AlertMaker.showMaterialWarningDialog(stackPane, anchorPane, Arrays.asList(button), "Missing", "Enter values in Password Fields.");
            return;
        }
        if (pass1.length() < 8 || pass1.length() > 16) {
            newPassword.setText("");
            confirmPassword.setText("");
            isError3 = true;
            lblWrongPasswordFormat2.setVisible(isError3);
            lblWrongPasswordFormat2.setText("Password must between 8 to 16 Characters.");
            lblWrongPasswordFormat2.getStyleClass().add("label-error");
            isError3 = false;
            return;
        }

        if (pass1.equals(pass2)) {
            Button okButton = new Button("Yes");
            okButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                String action = "UPDATE LOGIN SET PASSWORD = '" + pass1 + "' WHERE EMAIL_ID = '" + email + "'";
                if (databaseHandler.execAction(action)) {

                    Button button = new Button("Okay");
                    AlertMaker.showMaterialInformationDialog(stackPane, anchorPane, Arrays.asList(button), "Success", "Password Updated Successfully.");
                    new ZoomIn(paneSignIn).play();
                    labelInvalidPassword.setVisible(isError);
                    paneSignIn.toFront();
                } else {
                    Button button = new Button("Okay!");
                    AlertMaker.showMaterialErrorDialog(stackPane, anchorPane, Arrays.asList(button), "Failure", "Password Update Failed.");
                    return;
                }
            });

            Button cancelButton = new Button("No");
            cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                Button button = new Button("That's Okay!");
                AlertMaker.showMaterialInformationDialog(stackPane, anchorPane, Arrays.asList(button), "Cancelled", "Password Updation Process Cancelled.");
            });

            AlertMaker.showMaterialConfirmationDialog(stackPane, anchorPane, Arrays.asList(okButton, cancelButton), "Confirmation", "Do you want to update your password?");

        } else {
            isWrong = true;
            lblWrongPassword.setVisible(isWrong);
            lblWrongPassword.setText("Wrong Password. The Two password must same.");
            lblWrongPassword.getStyleClass().add("label-error");
            confirmPassword.setText("");
            isWrong = false;
        }
    }

    public void handleBackForgetPasswordButton(MouseEvent mouseEvent) {
        new ZoomIn(paneSignIn).play();
        labelInvalidPassword.setVisible(isError);
        paneSignIn.toFront();
    }

    public void handleForgetPasswordButton(MouseEvent mouseEvent) {
        if (signInEmail.getText().isEmpty()) {

            Button button = new Button("Okay!");
            AlertMaker.showMaterialWarningDialog(stackPane, anchorPane, Arrays.asList(button), "Warning", "Email Address Field can't be left blank.");
            return;
        }
        String query = "SELECT COUNT(*) FROM LOGIN WHERE email_id = '" + signInEmail.getText() + "'";
        ResultSet res = databaseHandler.execQuery(query);
        try {
            if (res.next()) {
                int no = res.getInt(1);
                if (no == 1) {
                    new ZoomIn(paneForgetPassword).play();
                    lblWrongPassword.setVisible(isWrong);
                    lblWrongPasswordFormat2.setVisible(isError3);
                    paneForgetPassword.toFront();
                } else {
                    Button button = new Button("Okay!");
                    AlertMaker.showMaterialErrorDialog(stackPane, anchorPane, Arrays.asList(button), "Failure", "Invalid Email Address.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleButtonClose(MouseEvent mouseEvent) {
        Stage stage = (Stage)btnClose.getScene().getWindow();
        stage.close();
    }

    public void handleButtonMinimize(MouseEvent mouseEvent) {
        Stage stage = (Stage)btnMinimize.getScene().getWindow();
        stage.setMaximized(false);
    }

    public void handleBorderPaneMouseClicked(MouseEvent mouseEvent) {
        borderPane.requestFocus();
    }

    public void loadMainWindow (String title, String url) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(url));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.getIcons().add(new Image("/resources/icons8-library-96.png"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.toString());
            e.printStackTrace();
        } catch (Exception e1) {
            JOptionPane.showMessageDialog(null, e1.getMessage());
        }
    }
}
