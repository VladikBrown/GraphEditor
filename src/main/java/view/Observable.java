package view;

import model.ModeState;

// наблюдатель
public interface Observable {
    void registerObserver(Observer o);
    void notifyObservers(ModeState message);
}