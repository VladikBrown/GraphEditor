package view.vertex;

import model.vertex.IVertexModel;
import view.edge.IEdgeView;
import view.vertex.impl.VertexIconType;

import java.util.List;

public interface IVertexView {

    void addEdge(IEdgeView edgeView);

    void removeEdge(IEdgeView edgeView);

    IEdgeView getEdgesAtIndex(int x);

    IVertexModel getVertexModel();

    void setVertexModel(IVertexModel vertexModel);

    int getNumberEdges();

    void setVertexIconType(VertexIconType iconType);

    String getIdentifier();

    void setIdentifier(String newID);

    int getIdNum();

    void setIdNum(int idNum);

    List<IEdgeView> getVertexEdges();
}
