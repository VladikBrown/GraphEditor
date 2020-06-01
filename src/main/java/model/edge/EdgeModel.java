package model.edge;

import model.vertex.IVertexModel;

public class EdgeModel implements IEdgeModel {
    private IVertexModel start;
    private IVertexModel finish;
    private int weight;

    public EdgeModel() {
        setWeight(1);
    }

    public EdgeModel(IVertexModel start, IVertexModel finish) {
        setStart(start);
        setFinish(finish);
        setWeight(1);
    }

    @Override
    public IVertexModel getStart() {
        return start;
    }

    @Override
    public void setStart(IVertexModel start) {
        this.start = start;
    }

    @Override
    public IVertexModel getFinish() {
        return finish;
    }

    @Override
    public void setFinish(IVertexModel finish) {
        this.finish = finish;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public void setWeight(int weight) {
        this.weight = weight;
    }
}
