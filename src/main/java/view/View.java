package view;

import controller.Controller;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


//TODO list tabs and tab wrapper
public class View {
    private Controller controller = new Controller();
    private Stage primaryStage;

    View(Stage stage) {
        primaryStage = stage;
        initializeButtons();
        configureToolBars();
        createAndConfigurePane();
        configureButtons();
    }

    BorderPane view;
    TabPane tabPane;
    ToolBar topToolBar;
    ToolBar leftToolBar;
    ToolBar rightToolBar;
    //topToolbar
    Button newButton = new Button();
    Button openButton = new Button();
    Button saveButton = new Button();
    //leftToolbar
    Button vertexButton = new Button();
    Button edgeButton = new Button();
    Button deleteButton = new Button();
    Button renameButton = new Button();
    //rightToolbar
    Button findCyclesButton = new Button("find cycles");
    private void createAndConfigurePane() {
        view = new BorderPane();
        tabPane = new TabPane();
        view.setTop(topToolBar);
        view.setLeft(leftToolBar);
        view.setRight(rightToolBar);
        view.setCenter(tabPane);
    }

    private void configureToolBars() {
        topToolBar = new ToolBar(newButton, openButton, saveButton);
        topToolBar.setOrientation(Orientation.HORIZONTAL);
        leftToolBar = new ToolBar(vertexButton, edgeButton, deleteButton, renameButton);
        leftToolBar.setOrientation(Orientation.VERTICAL);
        rightToolBar = new ToolBar(findCyclesButton);
        rightToolBar.setOrientation(Orientation.VERTICAL);
    }

    private void initializeButtons() {
        //adding images
        Image newImage = new Image("image/NewButton.png", 20, 20, false, false);
        Image openImage = new Image("image/OpenButton.png", 20, 20, false, false);
        Image saveImage = new Image("image/SaveButton.png", 20, 20, false, false);
        Image vertexButtonImage = new Image("image/VertexButton.png", 20, 20, false, false);
        Image edgeButtonImage = new Image("image/EdgeButton.png", 20, 20, false, false);
        Image deleteButtonImage = new Image("image/DeleteButton.png", 20, 20, false, true);
        Image changeButtonImage = new Image("image/ChangesButton.png", 20, 20, false, false);
        newButton.setGraphic(new ImageView(newImage));
        openButton.setGraphic(new ImageView(openImage));
        saveButton.setGraphic(new ImageView(saveImage));
        vertexButton.setGraphic(new ImageView(vertexButtonImage));
        edgeButton.setGraphic(new ImageView(edgeButtonImage));
        deleteButton.setGraphic(new ImageView(deleteButtonImage));
        renameButton.setGraphic(new ImageView(changeButtonImage));
        findCyclesButton.setText("Find cycles");
    }

    public void addTab(String title) {
        GraphTab tab = new GraphTab(title);
        Pane pane = new Pane();
        tab.setPane(pane);
        tab.setGraphView(new GraphView(tab));
        tab.getPane().setOnMouseClicked(mouseEvent -> controller.setOnPaneClicked(mouseEvent, tab));
        tabPane.getTabs().add(tab);
    }

    public Parent asParent() {
        return view;
    }

    private void configureButtons(){
        newButton.setOnAction
                (actionEvent -> addTab("Unnamed"));
        saveButton.setOnAction
                (actionEvent -> controller.setOnSaveButtonClicked(primaryStage, tabPane));
        openButton.setOnAction
                (actionEvent -> controller.setOnOpenButtonClicked(primaryStage, tabPane));
        vertexButton.setOnAction
                (actionEvent -> controller.setOnVertexButtonClicked());
        edgeButton.setOnAction
                (actionEvent -> controller.setOnEdgeButtonClicked());
        deleteButton.setOnAction
                (actionEvent -> controller.setOnDeleteButtonClicked());
        renameButton.setOnAction
                (actionEvent -> controller.setOnRenameButtonClicked());
    }
}
