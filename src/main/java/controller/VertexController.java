package controller;

import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
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

    public void setOnVertexClicked(MouseEvent mouseEvent, GraphTab graphTab, VertexView copy){
        switch (state) {
            case VERTEX_BUTTON_MODE : {

                break;
            }
            case EDGE_BUTTON_MODE : {
                //добавить покраску в оранжевый
                if(!isEdgeBuildingInProgress){
                    this.bufferEdge = new EdgeView(graphTab);
                    bufferEdge.setStart(copy);
                    copy.addEdge(bufferEdge);
                    isEdgeBuildingInProgress = true;
                    bufferEdge.getStart().setOrangeIcon();
                }
                else {
                    bufferEdge.setFinish(copy);
                    copy.addEdge(bufferEdge);
                    graphTab.addEdge(bufferEdge);
                    isEdgeBuildingInProgress = false;
                    bufferEdge.getStart().setGreyIcon();
                    graphTab.getPane().getChildren().add(bufferEdge);
                }
                break;
            }
            case RENAME_BUTTON_MODE : {
                TextInputDialog dialog = new TextInputDialog(" ");
                dialog.setTitle("Rename vertex");
                dialog.setHeaderText("Please, enter new name of vertex");
                dialog.setContentText("New ID: ");
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(copy::setIdentifier);
                break;
            }
            case DELETE_BUTTON_MODE : {
                deleteVertex(graphTab, copy);
                break;
            }
        }
    }

    private void deleteVertex(GraphTab graphTab, VertexView vertex){
        EdgeView currentEdge;

        vertex.setVisible(false);
        graphTab.getGraphView().getGraphRoot().removeVertex(vertex.getVertexModel());
        graphTab.getGraphView().removeVertex(vertex);
        int n = vertex.getNumberEdges();
        while (n>0){
            currentEdge = vertex.getEdgesAtIndex(n - 1);
            EdgeModel oldEdgeModel = currentEdge.getEdgeModel();
            currentEdge.getStart().getVertexModel().removeEdge(oldEdgeModel);
            currentEdge.getFinish().getVertexModel().removeEdge(oldEdgeModel);

            graphTab.getEdgeList().removeEdge(currentEdge);
            currentEdge.setVisible(false);
            graphTab.getPane().getChildren().remove(currentEdge);
            currentEdge.getStart().removeEdge(currentEdge);
            currentEdge.getFinish().removeEdge(currentEdge);
            n--;
        }
        graphTab.getPane().getChildren().remove(vertex);
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
