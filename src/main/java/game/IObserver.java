package game;

import game.WorldElements.AbstractWorldElement;
import game.WorldElements.Animal;

public interface IObserver {
    void animalMoved(Animal animal, Vector2D newPosition);
    void newElement(AbstractWorldElement element);
    void stateChanged();

}
