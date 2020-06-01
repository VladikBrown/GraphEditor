package controller;

import javafx.scene.control.TextInputDialog;
import model.ModeState;
import model.edge.IEdgeModel;
import view.Observer;
import view.edge.IEdgeView;
import view.graph.GraphTab;

import java.util.Optional;

public class EdgeController implements Observer {
    private static ModeState state;

    public void setOnEdgeClicked(GraphTab graphTab, IEdgeView copy) {
        switch (state) {
            case RENAME_BUTTON_MODE: {
                TextInputDialog dialog = new TextInputDialog(" ");
                dialog.setTitle("Set weight of edge");
                dialog.setHeaderText("Please, enter new weight of edge");
                dialog.setContentText("New weight: ");
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(copy::setIdentifier);
                break;
            }
            case DELETE_BUTTON_MODE: {
                deleteEdge(graphTab, copy);
                break;
            }
        }
    }

    public void deleteEdge(GraphTab graphTab, IEdgeView edgeView) {
        IEdgeModel oldEdgeModel = edgeView.getEdgeModel();
        edgeView.getStart().getVertexModel().removeEdge(oldEdgeModel);
        edgeView.getFinish().getVertexModel().removeEdge(oldEdgeModel);

        graphTab.getGraphView().removeEdge(edgeView);
        edgeView.getStart().removeEdge(edgeView);
        edgeView.getFinish().removeEdge(edgeView);
        graphTab.getEdgeList().removeEdge(edgeView);
        edgeView.asNode().setVisible(false);
    }

    @Override
    public void notification(ModeState message) {
        state = message;
    }
}
