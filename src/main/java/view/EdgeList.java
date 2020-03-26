package view;

import java.util.ArrayList;
import java.util.List;

public class EdgeList {
    private List<EdgeView> edgeList = new ArrayList<EdgeView>();

    public int getNumberAllEdges(){
        return edgeList.size();
    }

    public EdgeView getEdgesAtIndex(int x){
        if (x >= 0 && x < edgeList.size()){
            return edgeList.get(x);
        }
        else{
            return null;
        }
    }

    public void addEdge(EdgeView edge){
        edgeList.add(edge);
    }

    public  void removeEdge(EdgeView edge){
        edgeList.remove(edge);
    }
}