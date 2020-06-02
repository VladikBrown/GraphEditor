package view;

import algorithm.HamiltonianCycle;
import controller.Controller;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import view.edge.IEdgeView;
import view.graph.GraphTab;
import view.graph.GraphView;
import view.vertex.LabeledVertex;

import java.util.Optional;

//TODO добавить уровни абстракции в пакеты view controller model

public class View {

    //rightToolbar
    Label algorithmLabel = new Label("Algorithms:");
    private Stage primaryStage;
    Button findPlanarCycle = new Button("Make Planar");

    BorderPane view;
    TabPane tabPane;
    SplitPane splitPane;

    //Tools
    MenuBar menuBar;
    //MenuBar sections
    Menu fileMenu, helpMenu;
    //Menu Items
    MenuItem newFileItem, openFileItem, saveFileItem;
    //
    ToolBar topToolBar;
    ToolBar leftToolBar;
    ToolBar rightToolBar;
    Button findEulerianCycle = new Button("Eulerian cycle");
    //topToolbar
    Button newButton = new Button();
    Button openButton = new Button();
    Button saveButton = new Button();
    //leftToolbar
    Button vertexButton = new Button();
    Button edgeButton = new Button();
    Button deleteButton = new Button();
    Button renameButton = new Button();
    Button findHamCyclesButton = new Button("Hamiltonian cycles");
    Button findPathsButton = new Button("All Paths");
    Button infoButton = new Button("Info");
    Button findShortestPathButton = new Button("Shortest path");
    Button getDistanceButton = new Button("Distance");
    Button getIncidenceMatrixButton = new Button("Incidence Matrix");
    Button getVertexDegreeButton = new Button("Degree");
    //added
    Button getAdjacencyMatrixButton = new Button("Adjacency Matrix");
    Button makeConnectedButton = new Button("Make Connected");
    Button findRadiusButton = new Button("Radius");
    Button findDiameterButton = new Button("Diameter");
    Button findCenterButton = new Button("Show Center");
    private Controller controller;
    private ToolBar bottomToolbar;

    View(Stage stage) {
        primaryStage = stage;
        initializeButtons();
        configureToolBars();
        createAndConfigurePane();
        configureMenu();
        configureButtons();
        this.controller = new Controller(this);
    }

    private void createAndConfigurePane() {
        view = new BorderPane();
        tabPane = new TabPane();
        menuBar = new MenuBar();
        splitPane = new SplitPane(menuBar, topToolBar);
        splitPane.setOrientation(Orientation.VERTICAL);
        view.setTop(splitPane);
        view.setLeft(leftToolBar);
        view.setRight(rightToolBar);
        view.setCenter(tabPane);
        view.setBottom(bottomToolbar);
    }

    public Controller getController() {
        return controller;
    }

    public BorderPane getView() {
        return view;
    }

    public ToolBar getTopToolBar() {
        return topToolBar;
    }

    public ToolBar getLeftToolBar() {
        return leftToolBar;
    }

    public ToolBar getRightToolBar() {
        return rightToolBar;
    }

    public ToolBar getBottomToolbar() {
        return bottomToolbar;
    }


    private void configureMenu() {
        fileMenu = new Menu("File");
        helpMenu = new Menu("Help");
        newFileItem = new MenuItem("New");
        openFileItem = new MenuItem("Open");
        saveFileItem = new MenuItem("Save as...");
        newFileItem.setOnAction(actionEvent -> addTab("Unnamed"));
        openFileItem.setOnAction(actionEvent -> controller.setOnOpenButtonClicked(primaryStage, tabPane));
        saveFileItem.setOnAction(actionEvent -> controller.setOnSaveButtonClicked(primaryStage, tabPane));
        fileMenu.getItems().addAll(newFileItem, openFileItem, saveFileItem);
        menuBar.getMenus().addAll(fileMenu, helpMenu);
    }

    private void configureToolBars() {
        topToolBar = new ToolBar(newButton, openButton, saveButton);
        topToolBar.setOrientation(Orientation.HORIZONTAL);
        topToolBar.setStyle("-fx-border-color: lightgray");
        leftToolBar = new ToolBar(vertexButton, edgeButton, deleteButton, renameButton);
        leftToolBar.setStyle("-fx-border-color: lightgray");
        leftToolBar.setOrientation(Orientation.VERTICAL);

        rightToolBar = new ToolBar
                (algorithmLabel,
                        findPlanarCycle,
                        findEulerianCycle,
                        infoButton,
                        findPathsButton,
                        findShortestPathButton,
                        getDistanceButton,
                        getIncidenceMatrixButton,
                        getVertexDegreeButton,
                        getAdjacencyMatrixButton,
                        makeConnectedButton,
                        findHamCyclesButton,
                        findRadiusButton,
                        findDiameterButton,
                        findCenterButton);
        algorithmLabel.setFont(Font.font(20));
        algorithmLabel.setAlignment(Pos.CENTER);
        rightToolBar.setStyle("-fx-border-color: lightgray");
        rightToolBar.setOrientation(Orientation.VERTICAL);
        bottomToolbar = new ToolBar();
        bottomToolbar.setOrientation(Orientation.HORIZONTAL);
        rightToolBar.getItems().forEach(node -> node.setStyle("-fx-pref-width: 150px"));
    }

    private void initializeButtons() {
        //adding images
        Image newImage = new Image("image/newTabButton.png", 30, 30, true, true);
        Image openImage = new Image("image/importButton.png", 30, 30, true, true);
        Image saveImage = new Image("image/exportButton.png", 30, 30, true, true);
        Image vertexButtonImage = new Image("image/circleButton.png", 30, 30, true, true);
        Image edgeButtonImage = new Image("image/smallArrowButton.png", 30, 30, true, true);
        Image deleteButtonImage = new Image("image/deleteButtonX.png", 30, 30, true, true);
        Image changeButtonImage = new Image("image/renameButton.png", 30, 30, true, true);
        newButton.setGraphic(new ImageView(newImage));
        newButton.setStyle("-fx-border-color: black");
        openButton.setGraphic(new ImageView(openImage));
        saveButton.setGraphic(new ImageView(saveImage));
        vertexButton.setGraphic(new ImageView(vertexButtonImage));
        edgeButton.setGraphic(new ImageView(edgeButtonImage));
        deleteButton.setGraphic(new ImageView(deleteButtonImage));
        renameButton.setGraphic(new ImageView(changeButtonImage));
    }

    public void addTab(String title) {
        GraphTab tab = new GraphTab(title);
        Pane pane = new Pane();
        pane.setPrefWidth(1400);
        pane.setPrefHeight(1400);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(pane);
        tab.setPane(pane);
        tab.setContent(scrollPane);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        tab.setGraphView(new GraphView<LabeledVertex, IEdgeView>(tab));
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
        vertexButton.setDefaultButton(true);
        vertexButton.setOnAction
                (actionEvent -> controller.setOnVertexButtonClicked());
        edgeButton.setOnAction
                (actionEvent -> controller.setOnEdgeButtonClicked());
        deleteButton.setOnAction
                (actionEvent -> controller.setOnDeleteButtonClicked());
        renameButton.setOnAction
                (actionEvent -> controller.setOnRenameButtonClicked());
        findPathsButton.setOnAction
                (actionEvent -> controller.setOnFindPathsButtonClicked());
        findShortestPathButton.setOnAction
                (actionEvent -> controller.setOnFindShortestPathButton());
        getDistanceButton.setOnAction
                (actionEvent -> controller.setOnDistanceButton());
        getVertexDegreeButton.setOnAction
                (actionEvent -> controller.setOnGetDegreeButton());
        getIncidenceMatrixButton.setOnAction
                (actionEvent -> tabPane.getTabs().stream().filter(Tab::isSelected).findFirst().
                        ifPresent(tab -> ((GraphTab) tab).getGraphView().getGraphRoot().getIncidenceMatrix()));
        findEulerianCycle.setOnAction
                (actionEvent -> tabPane.getTabs().stream().filter(Tab::isSelected).findFirst()
                        .ifPresent(tab -> ((GraphTab) tab).getGraphView().getGraphRoot().findEulerianCycle()));
        findPlanarCycle.setOnAction
                (actionEvent -> tabPane.getTabs().stream().filter(Tab::isSelected).findFirst()
                        .ifPresent(tab -> ((GraphTab) tab).getGraphView().getGraphRoot().isPlanar()));
        infoButton.setOnAction(actionEvent ->
                tabPane.getTabs().stream().filter(Tab::isSelected).findFirst().
                        ifPresent(tab -> ((GraphTab) tab).getGraphView().getGraphRoot().showInfo()));
        makeConnectedButton.setOnAction
                (actionEvent -> tabPane.getTabs().stream().filter(Tab::isSelected).findFirst().
                        ifPresent(tab -> ((GraphTab) tab).getGraphView().getGraphRoot().makeConnected()));
        findRadiusButton.setOnAction
                (actionEvent -> tabPane.getTabs().stream().filter(Tab::isSelected).findFirst().
                        ifPresent(tab -> ((GraphTab) tab).getGraphView().getGraphRoot().showRadius()));
        findDiameterButton.setOnAction
                (actionEvent -> tabPane.getTabs().stream().filter(Tab::isSelected).findFirst().
                        ifPresent(tab -> ((GraphTab) tab).getGraphView().getGraphRoot().showDiameter()));
        findCenterButton.setOnAction
                (actionEvent -> tabPane.getTabs().stream().filter(Tab::isSelected).findFirst().
                        ifPresent(tab -> ((GraphTab) tab).getGraphView().getGraphRoot().showCenter()));
        getAdjacencyMatrixButton.setOnAction
                (actionEvent -> tabPane.getTabs().stream().filter(Tab::isSelected).findFirst().
                        ifPresent(tab -> ((GraphTab) tab).getGraphView().getGraphRoot().getAdjacencyMatrix()));

        findHamCyclesButton.setOnAction
                (actionEvent -> {
                    ToolBar hamiltonToolBar = new ToolBar();
                    hamiltonToolBar.setOrientation(Orientation.VERTICAL);
                    Button returnButton = new Button("Return");
                    returnButton.setOnAction(actionEvent1 -> view.setRight(rightToolBar));
                    hamiltonToolBar.getItems().removeAll(rightToolBar.getItems());
                    hamiltonToolBar.getItems().add(returnButton);
                    HamiltonianCycle hamiltonianCycle = new HamiltonianCycle(
                            (GraphTab) tabPane.getTabs().stream().filter(Tab::isSelected).findFirst().get());
                    if (!hamiltonianCycle.getCycles().isEmpty()) {
                        hamiltonToolBar.getItems().add(new Label("Existing cycles: "));
                    } else {
                        hamiltonToolBar.getItems().add(new Label("There is no existing\n cycles in graph :("));
                    }
                    for (int i = 0; i < hamiltonianCycle.getCycles().size(); i++) {
                        String buttonName = "Cycle #" + i;
                        hamiltonToolBar.getItems().
                                add(hamiltonianCycle.cycleButtonFactory(hamiltonianCycle.getCycles().get(i), buttonName));
                    }
                    view.setRight(hamiltonToolBar);
                });

        tabPane.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.R && keyEvent.isShiftDown()) {
                TextInputDialog dialog = new TextInputDialog(" ");
                dialog.setTitle("Rename graph");
                dialog.setHeaderText("Please, enter new name of graph");
                dialog.setContentText("New name: ");
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(resultString -> tabPane.selectionModelProperty().get().getSelectedItem().setText(resultString));
            }
        });
    }
}
