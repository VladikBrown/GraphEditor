package view;

import model.ModeState;

// слушатель
public interface Observer {
    void notification(ModeState message);
}

