package controller;

import fileManager.GraphXML;
import javafx.scene.control.Dialog;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.ModeState;
import model.constants.VertexConst;
import org.jetbrains.annotations.Nullable;
import view.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


public class Controller implements Observable {

    private ModeState state;
    private List<Observer> observers;

    public Controller() {
        //default
        state = ModeState.VERTEX_BUTTON_MODE;
        observers = new LinkedList<>();
        observers.add(VertexView.vertexController = new VertexController());
        observers.add(EdgeView.edgeController = new EdgeController());
    }

    public void setOnPaneClicked(MouseEvent e, GraphTab graphTab) {
        if(this.state.equals(ModeState.VERTEX_BUTTON_MODE)){
            if (e.getClickCount() == 2) {
            VertexView vertex = new VertexView(new ImageView(VertexConst.GREY_VERTEX_IMAGE), "", graphTab);
            if (graphTab.getGraphView().getStart() == null){
                graphTab.getGraphView().setStart(vertex);
            }
            vertex.setTranslateX(e.getX() - VertexConst.VERTEX_SIZE_X/2.0);
            vertex.setTranslateY(e.getY() - VertexConst.VERTEX_SIZE_X/2.0);
            //TODO вынести действия в отдельные функции(расположение вершины и добавление в модель)
            graphTab.getGraphView().addVertex(vertex);
            graphTab.getGraphView().getGraphRoot().addVertex(vertex.getVertexModel());
            graphTab.getPane().getChildren().addAll(vertex);
            }
        }
    }

    public void setOnSaveButtonClicked(Stage stage, TabPane tabPane) {
        @Nullable
        Tab currentTab = tabPane.getTabs().stream().filter(Tab::isSelected).findFirst().get();

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            GraphXML graphXML = new GraphXML(file, (GraphTab) currentTab);
            try {
            graphXML.writeFile();
            } catch (ParserConfigurationException | TransformerException | IOException e) {
            e.printStackTrace();
            }
        }
    }

    public void setOnOpenButtonClicked(Stage stage, TabPane tabPane){
        @Nullable
        Tab currentTab = tabPane.getTabs().stream().filter(Tab::isSelected).findFirst().get();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            GraphXML graphXML = new GraphXML(file, (GraphTab) currentTab);
            graphXML.readFile();
        }
    }

    public void setOnVertexButtonClicked(){
        this.state = ModeState.VERTEX_BUTTON_MODE;
        notifyObservers(ModeState.VERTEX_BUTTON_MODE);
    }

    public void setOnEdgeButtonClicked(){
        this.state = ModeState.EDGE_BUTTON_MODE;
        notifyObservers(ModeState.EDGE_BUTTON_MODE);
    }

    public void setOnDeleteButtonClicked(){
        this.state = ModeState.DELETE_BUTTON_MODE;
        notifyObservers(ModeState.DELETE_BUTTON_MODE);
    }

    public void setOnRenameButtonClicked(){
        this.state = ModeState.RENAME_BUTTON_MODE;
        notifyObservers(ModeState.RENAME_BUTTON_MODE);
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