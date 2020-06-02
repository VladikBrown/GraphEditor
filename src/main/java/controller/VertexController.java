package controller;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import model.ModeState;
import model.edge.IEdgeModel;
import view.Observer;
import view.edge.IEdgeView;
import view.edge.impl.EdgeView;
import view.graph.GraphTab;
import view.vertex.IVertexView;
import view.vertex.LabeledVertex;
import view.vertex.impl.VertexIconType;
import view.vertex.impl.VertexView;

import java.util.Optional;

//TODO Абстрагировать
public class VertexController implements Observer {
    private static ModeState state;
    private static VertexController vertexController;
    private double orgSceneX, orgSceneY;
    private double orgTranslateX, orgTranslateY;
    private boolean isActionInProgress = false;
    private IVertexView bufferVertex;
    private IEdgeView bufferEdge;
    //TODO final and constructors
    private Controller controller;

    private VertexController() {

    }

    private VertexController(Controller controller) {
        this.controller = controller;
    }

    public static VertexController getVertexController() {
        if (vertexController == null) {
            vertexController = new VertexController();
        }
        return vertexController;
    }

    public static VertexController getVertexController(Controller controller) {
        if (vertexController == null) {
            vertexController = new VertexController(controller);
        }
        return vertexController;
    }

    //TODO вынести кейсы в отдельные методы

    public void setOnVertexClicked(GraphTab<IVertexView, IEdgeView> graphTab, VertexView copy) {
        switch (state) {
            case VERTEX_BUTTON_MODE: {
                break;
            }
            case EDGE_BUTTON_MODE: {
                if (!isActionInProgress) {
                    this.bufferEdge = new EdgeView(graphTab);
                    bufferEdge.setStart(copy);
                    //copy.addEdge(bufferEdge);
                    isActionInProgress = true;
                    bufferEdge.getStart().setVertexIconType(VertexIconType.Active);
                } else {
                    bufferEdge.setFinish(copy);
                    if (!graphTab.getGraphView().getGraphRoot().isEdgeRedudant(bufferEdge)) {
                        System.out.println("not redudant!");
                        bufferEdge.getStart().addEdge(bufferEdge);
                        bufferEdge.getFinish().addEdge(bufferEdge);
                        graphTab.addEdge(bufferEdge.getStart(), bufferEdge.getFinish(), bufferEdge);
                    }
                    isActionInProgress = false;
                    bufferEdge.getStart().setVertexIconType(VertexIconType.NonActive);
                    bufferEdge = null;
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
            case DELETE_BUTTON_MODE: {
                deleteVertex(graphTab, copy);
                break;
            }
            case FIND_PATH_MODE: {
                if (!isActionInProgress) {
                    this.bufferVertex = copy;
                    isActionInProgress = true;
                    bufferVertex.setVertexIconType(VertexIconType.Active);
                } else {
                    controller.getView().getBottomToolbar().getItems().clear();
                    controller.getView().getBottomToolbar().getItems().addAll(
                            graphTab.getGraphView().getGraphRoot().pathHighlightButtonFactory(
                                    graphTab.getGraphView().getGraphRoot().findAllPaths(bufferVertex, copy)));
                    isActionInProgress = false;
                    bufferVertex.setVertexIconType(VertexIconType.NonActive);
                    bufferVertex = null;
                }
                break;
            }
            case FIND_SHORTEST_PATH_MODE: {
                if (!isActionInProgress) {
                    this.bufferVertex = copy;
                    isActionInProgress = true;
                    bufferVertex.setVertexIconType(VertexIconType.Active);
                } else {
                    graphTab.getGraphView().getGraphRoot().findShortestPath(bufferVertex, copy);
                    isActionInProgress = false;
                    bufferVertex.setVertexIconType(VertexIconType.NonActive);
                    bufferVertex = null;
                }
                break;
            }
            case GET_DISTANCE_MODE: {
                if (!isActionInProgress) {
                    this.bufferVertex = copy;
                    isActionInProgress = true;
                    bufferVertex.setVertexIconType(VertexIconType.Active);
                } else {
                    int distance = graphTab.getGraphView().getGraphRoot().getDistance(bufferVertex, copy);
                    Alert distanceAlert = new Alert(Alert.AlertType.INFORMATION);
                    String startIdentifier, finishIdentifier;
                    if (bufferVertex.getIdentifier().isEmpty()) {
                        startIdentifier = "noname1";
                    } else startIdentifier = bufferVertex.getIdentifier();
                    if (copy.getIdentifier().isEmpty()) {
                        finishIdentifier = "noname2";
                    } else finishIdentifier = copy.getIdentifier();
                    distanceAlert.setHeaderText("Distance between " + startIdentifier + " and "
                            + finishIdentifier);
                    distanceAlert.setContentText(String.valueOf(distance));
                    isActionInProgress = false;
                    bufferVertex.setVertexIconType(VertexIconType.NonActive);
                    bufferVertex = null;
                    distanceAlert.showAndWait();
                }
                break;
            }
            case VERTEX_DEGREE_BUTTON: {
                Alert degreeAlert = new Alert(Alert.AlertType.INFORMATION);
                String vertexID;
                if (copy.getIdentifier().isEmpty()) {
                    vertexID = "noname";
                } else vertexID = copy.getIdentifier();
                degreeAlert.setHeaderText("Degree of " + vertexID + " is");
                System.out.println(graphTab.getGraphView().getGraphRoot().getVertexDegree(copy));
                degreeAlert.setContentText(String.valueOf(graphTab.getGraphView().getGraphRoot().getVertexDegree(copy)));
                degreeAlert.showAndWait();
                break;
            }
        }
    }

    private void deleteVertex(GraphTab graphTab, LabeledVertex vertex) {
        IEdgeView currentEdge;

        vertex.asLabel().setVisible(false);
        graphTab.getGraphView().removeVertex(vertex);
        int n = vertex.getNumberEdges();
        while (n > 0) {
            currentEdge = vertex.getEdgesAtIndex(n - 1);
            IEdgeModel oldEdgeModel = currentEdge.getEdgeModel();
            currentEdge.getStart().getVertexModel().removeEdge(oldEdgeModel);
            currentEdge.getFinish().getVertexModel().removeEdge(oldEdgeModel);

            graphTab.getEdgeList().removeEdge(currentEdge);
            currentEdge.asNode().setVisible(false);
            graphTab.getPane().getChildren().remove(currentEdge);
            currentEdge.getStart().removeEdge(currentEdge);
            currentEdge.getFinish().removeEdge(currentEdge);
            n--;
        }
        graphTab.getPane().getChildren().remove(vertex);
    }

    public void setOnPressedVertex(MouseEvent mouseEvent) {
        orgSceneX = mouseEvent.getSceneX();
        orgSceneY = mouseEvent.getSceneY();
        orgTranslateX = ((VertexView) (mouseEvent.getSource())).getTranslateX();
        orgTranslateY = ((VertexView) (mouseEvent.getSource())).getTranslateY();
    }

    public void setOnDragVertex(MouseEvent mouseEvent) {
        double offsetX = mouseEvent.getSceneX() - orgSceneX;
        double offsetY = mouseEvent.getSceneY() - orgSceneY;
        double newTranslateX = orgTranslateX + offsetX;
        double newTranslateY = orgTranslateY + offsetY;

        ((VertexView) (mouseEvent.getSource())).setTranslateX(newTranslateX);
        ((VertexView) (mouseEvent.getSource())).setTranslateY(newTranslateY);
    }

    public Controller getController() {
        return controller;
    }

    @Override
    public void notification(ModeState message) {
        state = message;
    }
}
