package game;

import game.WorldElements.AbstractWorldElement;
import game.WorldElements.Animal;
import game.gui.SimulationWindow;
import javafx.application.Platform;

public class SimulationObserver implements IObserver{
    private final SimulationWindow simulationWindow;
    public SimulationObserver(SimulationWindow simulationWindow) {
        this.simulationWindow = simulationWindow;
    }

    @Override
    public void animalMoved(Animal animal, Vector2D newPosition) {
        stateChanged();
    }

    @Override
    public void newElement(AbstractWorldElement element) {
        stateChanged();
    }
    @Override
    public void stateChanged() {
        Platform.runLater(simulationWindow::updateApp);
    }
}
