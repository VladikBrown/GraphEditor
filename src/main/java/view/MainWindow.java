package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

public class MainWindow extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("GraphEditor beta");
        primaryStage.getIcons().add(new Image("image/appIcon.png"));

        View view = new View(primaryStage);
        Scene root = new Scene(view.asParent(), 1000, 600);
        JMetro jMetro = new JMetro(root, Style.LIGHT);
        //root.getStylesheets().add("Theme.css");
        primaryStage.setScene(root);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
