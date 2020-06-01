package view.edge;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import model.edge.IEdgeModel;
import view.vertex.LabeledVertex;

public interface IEdgeView {

    int getIdentifier();

    void setIdentifier(String id);

    IEdgeModel getEdgeModel();

    void setEdgeModel(IEdgeModel edgeModel);

    LabeledVertex getStart();

    void setStart(LabeledVertex start);

    LabeledVertex getFinish();

    void setFinish(LabeledVertex finish);

    void setEdgeColor(Color color);

    Node asNode();

    void erase();
}
