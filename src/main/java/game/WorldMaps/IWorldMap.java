package game.WorldMaps;

import game.PlantDecorator.IPlantDecorator;
import game.Vector2D;
import game.WorldElements.AbstractWorldElement;
import game.WorldElements.Animal;

public interface IWorldMap {

    int getHeight();
    int getWidth();
    void placeElement(AbstractWorldElement newElement);
    void moveElement(AbstractWorldElement element, Vector2D newPosition);
    boolean isPlantOn(Vector2D position);
    void removeDeadAnimal(Animal animal);
    void feedAnimals(int plantEnergy, IPlantDecorator plantDecorator);
    boolean isOccupied(Vector2D position);
}
