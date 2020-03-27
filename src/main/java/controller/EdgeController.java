package controller;

import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import model.EdgeModel;
import model.ModeState;
import view.EdgeView;
import view.GraphTab;
import view.Observer;

import java.util.Optional;

public class EdgeController implements Observer {
    private static ModeState state;


    public void setOnEdgeClicked(MouseEvent mouseEvent, GraphTab graphTab, EdgeView copy){
        switch (state) {
            case RENAME_BUTTON_MODE:{
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

    public void deleteEdge(GraphTab graphTab, EdgeView edgeView){
        EdgeModel oldEdgeModel = edgeView.getEdgeModel();
        edgeView.getStart().getVertexModel().removeEdge(oldEdgeModel);
        edgeView.getFinish().getVertexModel().removeEdge(oldEdgeModel);

        edgeView.setVisible(false);
        edgeView.getStart().removeEdge(edgeView);
        edgeView.getFinish().removeEdge(edgeView);
        graphTab.getEdgeList().removeEdge(edgeView);
    }

    @Override
    public void notification(ModeState message) {
        state = message;
    }
}
