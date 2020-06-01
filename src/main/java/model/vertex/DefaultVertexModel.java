package model.vertex;

import model.edge.IEdgeModel;

import java.util.ArrayList;
import java.util.List;

public class DefaultVertexModel implements IVertexModel {
    private List<IEdgeModel> edgeList;

    public DefaultVertexModel() {
        edgeList = new ArrayList<>();
    }

    @Override
    public void addEdge(IEdgeModel newEdge) {
        edgeList.add(newEdge);
    }

    @Override
    public void removeEdge(IEdgeModel oldEdge) {
        edgeList.remove(oldEdge);
    }

    @Override
    public IEdgeModel getEdgeAtIndex(int x) {
        if (x >= 0 && x < edgeList.size()) {
            return edgeList.get(x);
        } else {
            return null;
        }
    }

    @Override
    public int getEdgeSize() {
        return edgeList.size();
    }
}
