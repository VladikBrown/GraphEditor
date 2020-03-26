package Controller;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import model.ModeState;
import view.*;

import java.util.Optional;

public class EdgeController implements Observer {
    private static ModeState state;


    public void setOnVertexClicked(MouseEvent mouseEvent, Pane pane, EdgeView copy){
        switch (state) {
            case RENAME_BUTTON_MODE:{
                TextInputDialog dialog = new TextInputDialog(" ");
                dialog.setTitle("Set weight of edge");
                dialog.setHeaderText("Please, enter new weight of edge");
                dialog.setContentText("New weight: ");
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(copy::setIdentifier);
            }
        }
    }


    @Override
    public void notification(ModeState message) {
        state = message;
    }
}
