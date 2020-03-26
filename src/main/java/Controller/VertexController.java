package Controller;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import model.*;
import view.*;

import java.util.Optional;


public class VertexController implements Observer {
    private static ModeState state;
    double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;
    boolean isEdgeBuildingInProgress = false;
    private EdgeView bufferEdge;

    public VertexController(){

    }

    public void setOnVertexClicked(MouseEvent mouseEvent, Pane pane, VertexView copy){
        switch (state) {
            case VERTEX_BUTTON_MODE : {

                break;
            }
            case EDGE_BUTTON_MODE : {
                //добавить покраску в оранжевый
                if(!isEdgeBuildingInProgress){
                    this.bufferEdge = new EdgeView(pane);
                    bufferEdge.setStart(copy);
                    copy.addEdge(bufferEdge);
                    isEdgeBuildingInProgress = true;
                    bufferEdge.getStart().setOrangeIcon();
                }
                else {
                    bufferEdge.setFinish(copy);
                    copy.addEdge(bufferEdge);
                    isEdgeBuildingInProgress = false;
                    bufferEdge.getStart().setGreyIcon();
                    pane.getChildren().add(bufferEdge);
                }
                break;
            }
            case RENAME_BUTTON_MODE:{
                TextInputDialog dialog = new TextInputDialog(" ");
                dialog.setTitle("Rename vertex");
                dialog.setHeaderText("Please, enter new name of vertex");
                dialog.setContentText("New ID: ");
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(copy::setIdentifier);
            }
        }
    }

    public void setOnPressedVertex(MouseEvent mouseEvent, VertexView copy){
        orgSceneX = mouseEvent.getSceneX();
        orgSceneY = mouseEvent.getSceneY();
        orgTranslateX = ((VertexView)(mouseEvent.getSource())).getTranslateX();
        orgTranslateY = ((VertexView)(mouseEvent.getSource())).getTranslateY();
    }

    public void setOnDragVertex(MouseEvent mouseEvent, VertexView copy){
        double offsetX = mouseEvent.getSceneX() - orgSceneX;
        double offsetY = mouseEvent.getSceneY() - orgSceneY;
        double newTranslateX = orgTranslateX + offsetX;
        double newTranslateY = orgTranslateY + offsetY;

        ((VertexView)(mouseEvent.getSource())).setTranslateX(newTranslateX);
        ((VertexView)(mouseEvent.getSource())).setTranslateY(newTranslateY);
    }

    @Override
    public void notification(ModeState message) {
        state = message;
    }
}
