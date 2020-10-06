package login;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MaterialIconLoader {

    MaterialDesignIconView mailIcon, visibleIcon, visibleOffIcon, passwordIcon;
    Label label1, label2, label3;
    Button button;

    public MaterialIconLoader() {
        label1 = new Label();
        label2 = new Label();
        label3 = new Label();
        label3 = new Label();
        button = new Button();
    }
    public void materialIconMaker() {
        mailIcon = new MaterialDesignIconView(MaterialDesignIcon.EMAIL);
        mailIcon.setGlyphSize(20);
        mailIcon.getStyleClass().add("glyph-icon-login");
        visibleIcon = new MaterialDesignIconView(MaterialDesignIcon.EYE);
        visibleIcon.setGlyphSize(20);
        visibleIcon.getStyleClass().add("glyph-icon-login-password");
        visibleOffIcon = new MaterialDesignIconView(MaterialDesignIcon.EYE_OFF);
        visibleOffIcon.setGlyphSize(20);
        visibleOffIcon.getStyleClass().add("glyph-icon-login-password");
        passwordIcon = new MaterialDesignIconView(MaterialDesignIcon.KEY);
        passwordIcon.setGlyphSize(20);
        passwordIcon.getStyleClass().add("glyph-icon-login");
    }

    public Label getLabel1() {
        label1.setGraphic(mailIcon);
        label1.setPadding(new Insets(0, 2, 0, 7));
        return label1;
    }

    public Label getLabel2() {
        label2.setGraphic(passwordIcon);
        label2.setPadding(new Insets(0, 2, 0, 7));
        return label2;
    }

    /*public Label getLabel3() {
        label3.setGraphic(visibleIcon);
        //label3.setPadding(new Insets(0, 2, 0, 7));
        return label3;
    }*/

    public Button getButton() {
        return button;
    }
    public Button getButtonGraphic(Boolean isVisible) {
        button.setAlignment(Pos.CENTER);
        button.setStyle("-fx-background-color: transparent");
        button.setGraphic(null);
        button.setGraphic(!isVisible ? visibleIcon : visibleOffIcon);
        button.setPadding(new Insets(0, 2, 0, 7));
        return button;
    }
}
