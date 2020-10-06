package mainWindow;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class PrepareAnimation extends Application {

    private static final double WIDTH = 1200;
    public static final double HEIGHT = 600;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Box box = prepareBox();
        Sphere sphere = prepareSphere();

        Camera camera = new PerspectiveCamera();

        SmartGroup group = new SmartGroup();
        group.getChildren().add(sphere);

        Scene scene = new Scene(group, WIDTH, HEIGHT);
        scene.setFill(Color.SILVER);
        scene.setCamera(camera);

        group.translateXProperty().set(WIDTH / 2);
        group.translateYProperty().set(HEIGHT / 2);

        primaryStage.setTitle("Texture Effect to Box");
        primaryStage.setScene(scene);
        primaryStage.show();

        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                sphere.setRotate(sphere.getRotate() + 0.5);
            }
        };
        animationTimer.start();
    }
    public Sphere prepareSphere() {

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(new Image("/resources/library2.jpg"));

        Sphere sphere = new Sphere(70);
        sphere.setRotationAxis(Rotate.X_AXIS);
        sphere.setMaterial(material);
        return sphere;

    }

    public static void main(String[] args) {
        launch(args);
    }

    class SmartGroup extends Group {
        Rotate r;
        Transform t = new Rotate();
    }
}
