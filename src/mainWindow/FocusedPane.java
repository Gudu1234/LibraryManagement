package mainWindow;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.util.Arrays;
import java.util.List;

public class FocusedPane {

    Boolean isHomePaneShowing = true;
    Boolean isStudentPaneShowing = false;
    Boolean isBooksPaneShowing = false;
    Boolean isIssuePaneShowing = false;
    Boolean isDashboardShowing = false;

    Button btnHome, btnStudent, btnBook, btnIssue, btnDashboard;

    public FocusedPane(Button btnHome, Button btnStudent, Button btnBook, Button btnIssue, Button btnDashboard) {
        this.btnHome = btnHome;
        this.btnStudent = btnStudent;
        this.btnBook = btnBook;
        this.btnIssue = btnIssue;
        this.btnDashboard = btnDashboard;
    }


    public void setListener (List<Button> controls) {
        controls.forEach(controlButton -> {
            controlButton.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (controlButton.isFocused()) {
                    System.out.println(3);
                } else if (!controlButton.isFocused() && getPaneShowing(controlButton.getText())) {
                    System.out.println(1 + " " + controlButton.getText());
                    controlButton.setStyle("-fx-background-color: #424242");
                    System.out.println(2 + " " + controlButton.getText());
                } else if (!getPaneShowing(controlButton.getText()) && !controlButton.isFocused()) {
                    System.out.println(2 + " " + controlButton.getText());
                    System.out.println(getPaneShowing(controlButton.getText()));
                    controlButton.setStyle("-fx-background-color: transparent");
                }
            });
        });
    }

    private boolean getPaneShowing(String text) {
        boolean value = false;
        if (text.equals("Home")) {
            value = isHomePaneShowing;
        } else if (text.equals("Students")) {
            value = isStudentPaneShowing;
        } else if (text.equals("Books")) {
            value = isBooksPaneShowing;
        } else if (text.equals("Issue")) {
            value = isIssuePaneShowing;
        } else if (text.equals("Dashboard")) {
            value = isDashboardShowing;
        }
        return value;
    }
    public void setHomePaneShowing(Boolean homePaneShowing, Boolean otherPaneShowing) {
        isHomePaneShowing = homePaneShowing;
        isStudentPaneShowing = isBooksPaneShowing = isIssuePaneShowing = isDashboardShowing = otherPaneShowing;
    }

    public void setStudentPaneShowing(Boolean studentPaneShowing, Boolean otherPaneShowing) {
        isStudentPaneShowing = studentPaneShowing;
        isHomePaneShowing = isBooksPaneShowing = isIssuePaneShowing = isDashboardShowing = otherPaneShowing;
    }

    public void setBooksPaneShowing(Boolean booksPaneShowing, Boolean otherPaneShowing) {
        isBooksPaneShowing = booksPaneShowing;
        isHomePaneShowing = isStudentPaneShowing = isIssuePaneShowing = isDashboardShowing = otherPaneShowing;
    }

    public void setIssuePaneShowing(Boolean issuePaneShowing, Boolean otherPaneShowing) {
        isIssuePaneShowing = issuePaneShowing;
        isHomePaneShowing = isStudentPaneShowing = isBooksPaneShowing = isDashboardShowing = otherPaneShowing;
    }

    public void setDashboardShowing(Boolean dashboardShowing, Boolean otherPaneShowing) {
        isDashboardShowing = dashboardShowing;
        isHomePaneShowing = isStudentPaneShowing = isBooksPaneShowing = isIssuePaneShowing = otherPaneShowing;
    }

    public Boolean getHomePaneShowing() {
        return isHomePaneShowing;
    }

    public Boolean getStudentPaneShowing() {
        return isStudentPaneShowing;
    }

    public Boolean getBooksPaneShowing() {
        return isBooksPaneShowing;
    }

    public Boolean getIssuePaneShowing() {
        return isIssuePaneShowing;
    }

    public Boolean getDashboardShowing() {
        return isDashboardShowing;
    }

}
