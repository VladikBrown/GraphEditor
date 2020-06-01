package model.edge;

import model.vertex.IVertexModel;

public interface IEdgeModel {

    IVertexModel getStart();

    void setStart(IVertexModel start);

    IVertexModel getFinish();

    void setFinish(IVertexModel finish);

    int getWeight();

    void setWeight(int weight);
}
