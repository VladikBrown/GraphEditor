package controller;

import fileManager.GraphXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.ModeState;
import model.constants.VertexConst;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import view.Observable;
import view.Observer;
import view.View;
import view.edge.IEdgeView;
import view.edge.impl.EdgeView;
import view.graph.GraphTab;
import view.vertex.IVertexView;
import view.vertex.LabeledVertex;
import view.vertex.impl.VertexView;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Controller implements Observable {

    private final View view;
    private final List<Observer> observers;
    private ModeState state;

    public Controller(View view) {
        this.view = view;
        state = ModeState.VERTEX_BUTTON_MODE;
        observers = new LinkedList<>();
        observers.add(VertexController.getVertexController(this));
        observers.add(EdgeView.edgeController = new EdgeController());
        notifyObservers(state);
    }

    public void setOnPaneClicked(MouseEvent e, GraphTab<IVertexView, IEdgeView> graphTab) {
        if (this.state.equals(ModeState.VERTEX_BUTTON_MODE)) {
            if (e.getClickCount() == 2) {
                LabeledVertex vertex = new VertexView(new ImageView(VertexConst.GREY_VERTEX_IMAGE), "", graphTab);
                if (graphTab.getGraphView().getStart() == null) {
                    graphTab.getGraphView().setStart(vertex);
                }
                vertex.asLabel().setTranslateX(e.getX() - VertexConst.VERTEX_SIZE_X / 2.0);
                vertex.asLabel().setTranslateY(e.getY() - VertexConst.VERTEX_SIZE_X / 2.0);
                graphTab.getGraphView().addVertex(vertex);
                graphTab.getPane().getChildren().addAll(vertex.asLabel());
            }
        }
    }

    public void setOnSaveButtonClicked(Stage stage, TabPane tabPane) {
        @NotNull
        Optional<Tab> checkTab = tabPane.getTabs().stream().filter(Tab::isSelected).findFirst();
        checkTab.ifPresentOrElse(
                tab -> {
                    FileChooser fileChooser = new FileChooser();
                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
                    fileChooser.getExtensionFilters().add(extFilter);
                    File file = fileChooser.showSaveDialog(stage);
                    if (file != null) {
                        if (!file.getPath().endsWith(".xml")) {
                            file = new File(file.getPath() + ".xml");
                        }
                        GraphXML graphXML = new GraphXML(file, (GraphTab) tab);
                        try {
                            graphXML.writeFile();
                        } catch (ParserConfigurationException | TransformerException e) {
                            e.printStackTrace();
                        }
                    }
                },
                () -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("You haven't selected any tab to save");
                    alert.setContentText("Please, press \"New\" to create new tab\n or \"Open\" to open existing file.");
                    alert.show();
                });
    }


    public void setOnOpenButtonClicked(Stage stage, TabPane tabPane) {
        @Nullable
        Optional<Tab> checkTab = tabPane.getTabs().stream().filter(Tab::isSelected).findFirst();
        checkTab.ifPresentOrElse(
                tab -> {
                    FileChooser fileChooser = new FileChooser();
                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
                    fileChooser.getExtensionFilters().add(extFilter);
                    File file = fileChooser.showOpenDialog(stage);
                    if (file != null) {
                        // Make sure it has the correct extension
                        if (!file.getPath().endsWith(".xml")) {
                            file = new File(file.getPath() + ".xml");
                        }
                        GraphXML graphXML = new GraphXML(file, (GraphTab) tab);
                        graphXML.readFile();
                    }
                },
                () -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("You haven't selected any tab");
                    alert.setContentText("You must select tab and only then open existing file.");
                    alert.show();
                });
    }

    public void setOnVertexButtonClicked() {
        this.state = ModeState.VERTEX_BUTTON_MODE;
        notifyObservers(ModeState.VERTEX_BUTTON_MODE);
    }

    public void setOnEdgeButtonClicked() {
        this.state = ModeState.EDGE_BUTTON_MODE;
        notifyObservers(ModeState.EDGE_BUTTON_MODE);
    }

    public void setOnDeleteButtonClicked() {
        this.state = ModeState.DELETE_BUTTON_MODE;
        notifyObservers(ModeState.DELETE_BUTTON_MODE);
    }

    public void setOnRenameButtonClicked() {
        this.state = ModeState.RENAME_BUTTON_MODE;
        notifyObservers(ModeState.RENAME_BUTTON_MODE);
    }

    public void setOnFindPathsButtonClicked() {
        this.state = ModeState.FIND_PATH_MODE;
        notifyObservers(ModeState.FIND_PATH_MODE);
    }

    public void setOnFindShortestPathButton() {
        this.state = ModeState.FIND_SHORTEST_PATH_MODE;
        notifyObservers(ModeState.FIND_SHORTEST_PATH_MODE);
    }

    public void setOnDistanceButton() {
        this.state = ModeState.GET_DISTANCE_MODE;
        notifyObservers(ModeState.GET_DISTANCE_MODE);
    }

    public void setOnGetDegreeButton() {
        this.state = ModeState.VERTEX_DEGREE_BUTTON;
        notifyObservers(ModeState.VERTEX_DEGREE_BUTTON);
    }

    public View getView() {
        return view;
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void notifyObservers(ModeState message) {
        for (Observer observer : observers) {
            observer.notification(message);
        }
    }
}