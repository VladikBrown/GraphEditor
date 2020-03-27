package view;

import controller.Controller;
import javafx.scene.control.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.constants.VertexConst;

public class MainWindow extends Application {
    Controller controller = new Controller();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("GraphEditor beta");
        BorderPane borderPane = new BorderPane();
        ImageView vertexImageView = new ImageView(VertexConst.GREY_VERTEX_IMAGE);
        if(vertexImageView.isHover()){
            vertexImageView.setImage(VertexConst.GREEN_VERTEX_IMAGE);
        }
        vertexImageView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.isControlDown()){
                    vertexImageView.setImage(VertexConst.GREEN_VERTEX_IMAGE);
                }
                else vertexImageView.setImage(VertexConst.GREY_VERTEX_IMAGE);
            }
        });

        Pane pane = new Pane();
        TabPane tabPane = new TabPane();
        Tab tab = new Tab("Unnamed");
        tabPane.getTabs().addAll(tab);
        tab.setContent(pane);

        Button newButton = new Button();
        Button openButton = new Button();
        Button saveButton = new Button();

        Image NewImage =  new Image("image/NewButton.png",20, 20, false, false);
        Image OpenImage =  new Image("image/OpenButton.png",20, 20, false, false);
        Image SaveImage =  new Image("image/SaveButton.png",20, 20, false, false);
        newButton.setGraphic(new ImageView(NewImage));
        openButton.setGraphic(new ImageView(OpenImage));
        saveButton.setGraphic(new ImageView(SaveImage));

        ToolBar toolBar = new ToolBar(newButton, openButton, saveButton);
        toolBar.setOrientation(Orientation.HORIZONTAL);

        Button vertexButton = new Button();
        Button edgeButton = new Button();
        Button findCyclesButton = new Button("find cycles");

        Image vertexButtonImage =  new Image("image/VertexButton.png",20, 20, false, false);
        Image edgeButtonImage =  new Image("image/EdgeButton.png",20, 20, false, false);
        vertexButton.setGraphic(new ImageView(vertexButtonImage));
        edgeButton.setGraphic(new ImageView(edgeButtonImage));

        ToolBar toolBar1 = new ToolBar(vertexButton, edgeButton);
        toolBar1.setOrientation(Orientation.VERTICAL);



        ToolBar specialTasks = new ToolBar(findCyclesButton);
        specialTasks.setOrientation(Orientation.VERTICAL);


        borderPane.setTop(toolBar);
        borderPane.setLeft(toolBar1);
        borderPane.setCenter(tabPane);
        borderPane.setRight(specialTasks);

        View view = new View(primaryStage);
        Scene root = new Scene(view.asParent(), 1000, 600);
        primaryStage.setScene(root);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
