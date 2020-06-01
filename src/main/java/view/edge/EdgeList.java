package view.edge;

import java.util.ArrayList;
import java.util.List;

public class EdgeList {

    private List<IEdgeView> edgeList;

    public EdgeList(){
        edgeList = new ArrayList<>();
    }

    public int getNumberAllEdges(){
        return edgeList.size();
    }

    public IEdgeView getEdgesAtIndex(int x) {
        if (x >= 0 && x < edgeList.size()) {
            return edgeList.get(x);
        } else {
            return null;
        }
    }

    public void addEdge(IEdgeView edge) {
        edgeList.add(edge);
    }

    public void removeEdge(IEdgeView edge) {
        edgeList.remove(edge);
    }
}