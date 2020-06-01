package model.vertex;

import model.edge.IEdgeModel;

public interface IVertexModel {
    void addEdge(IEdgeModel newEdge);

    void removeEdge(IEdgeModel oldEdge);

    IEdgeModel getEdgeAtIndex(int x);

    int getEdgeSize();
}
