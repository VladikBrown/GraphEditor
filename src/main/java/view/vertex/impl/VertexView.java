package view.vertex.impl;

import controller.VertexController;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import model.constants.VertexConst;
import model.vertex.DefaultVertexModel;
import model.vertex.IVertexModel;
import view.edge.IEdgeView;
import view.graph.GraphTab;
import view.vertex.IVertexView;
import view.vertex.LabeledVertex;

import java.util.ArrayList;
import java.util.List;


public class VertexView extends Label implements LabeledVertex {
    private final GraphTab<IVertexView, IEdgeView> graphTab;
    private final ImageView greenVertexImageView;
    private final ImageView greyVertexImageView;
    private final ImageView orangeVertexImageView;
    private final List<IEdgeView> vertexEdges;
    private final VertexController vertexController = VertexController.getVertexController();
    private int idNum;
    private String identifier;
    private IVertexModel vertexModel;

    public VertexView(ImageView icon, String identifier, GraphTab graphTab) {
        super(identifier, icon);
        this.vertexEdges = new ArrayList<>();
        this.graphTab = graphTab;
        this.identifier = identifier;
        this.vertexModel = new DefaultVertexModel();
        this.setCursor(Cursor.HAND);
        this.greenVertexImageView = new ImageView(VertexConst.GREEN_VERTEX_IMAGE);
        this.greyVertexImageView = new ImageView(VertexConst.GREY_VERTEX_IMAGE);
        this.orangeVertexImageView = new ImageView(VertexConst.ORANGE_VERTEX_IMAGE);
        initVertexControls();
    }

    private void initVertexControls(){
        this.setOnMouseEntered
                (mouseEvent -> this.setGraphic(greenVertexImageView));

        this.setOnMouseExited
                (mouseEvent -> this.setGraphic(greyVertexImageView));

        this.setOnMouseClicked
                (mouseEvent -> vertexController.setOnVertexClicked(graphTab, this));

        this.setOnMousePressed
                (mouseEvent -> vertexController.setOnPressedVertex(mouseEvent));

        this.setOnMouseDragged
                (mouseEvent -> vertexController.setOnDragVertex(mouseEvent));
    }

    @Override
    public void addEdge(IEdgeView edge) {
        vertexEdges.add(edge);
        vertexModel.addEdge(edge.getEdgeModel());
    }

    @Override
    public void removeEdge(IEdgeView edge) {
        vertexEdges.remove(edge);
    }

    @Override
    public IEdgeView getEdgesAtIndex(int x) {
        if (x >= 0 && x < vertexEdges.size()) {
            return vertexEdges.get(x);
        } else {
            return null;
        }
    }

    @Override
    public int getNumberEdges() {
        return vertexEdges.size();
    }

    @Override
    public void setVertexIconType(VertexIconType iconType) {
        switch (iconType) {
            case Active:
                setActiveIcon();
                break;
            case NonActive:
                setNonActiveIcon();
                break;
            case Hovered:
                setHoveredIcon();
                break;
        }
    }

    private void setActiveIcon() {
        this.setGraphic(orangeVertexImageView);
        this.setOnMouseExited
                (mouseEvent -> this.setGraphic(orangeVertexImageView));
    }

    private void setNonActiveIcon() {
        this.setGraphic(greyVertexImageView);
        this.setOnMouseExited
                (mouseEvent -> this.setGraphic(greyVertexImageView));
    }

    private void setHoveredIcon() {
        this.setGraphic(greenVertexImageView);
        this.setOnMouseExited
                (mouseEvent -> this.setGraphic(greyVertexImageView));
    }

    @Override
    public IVertexModel getVertexModel() {
        return this.vertexModel;
    }

    @Override
    public void setVertexModel(IVertexModel vertexModel) {
        this.vertexModel = vertexModel;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public void setIdentifier(String newID) {
        this.setText(newID);
        this.identifier = newID;
    }

    @Override
    public int getIdNum() {
        return idNum;
    }

    @Override
    public void setIdNum(int idNum) {
        this.idNum = idNum;
    }

    @Override
    public List<IEdgeView> getVertexEdges() {
        return vertexEdges;
    }


    @Override
    public Label asLabel() {
        return this;
    }
}
