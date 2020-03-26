package Controller;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import model.ModeState;
import model.constants.VertexConst;
import view.EdgeView;
import view.Observable;
import view.Observer;
import view.VertexView;

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

    public void setOnPaneClicked(MouseEvent e, Pane pane) {
        if(this.state.equals(ModeState.VERTEX_BUTTON_MODE)){
            if (e.getClickCount() == 2) {
            VertexView vertex = new VertexView(new ImageView(VertexConst.GREY_VERTEX_IMAGE), "", pane);
            vertex.setTranslateX(e.getX() - VertexConst.VERTEX_SIZE_X/2.0);
            vertex.setTranslateY(e.getY() - VertexConst.VERTEX_SIZE_X/2.0);
            pane.getChildren().addAll(vertex);
            }
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