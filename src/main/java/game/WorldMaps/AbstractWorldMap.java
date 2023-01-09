package game.WorldMaps;

import game.MapStatistics;
import game.MoveGenerator.INextMoveGenerator;
import game.PlantDecorator.IPlantDecorator;
import game.Vector2D;
import game.WorldElements.AbstractWorldElement;
import game.WorldElements.Animal;
import game.WorldElements.Plant;

import java.io.File;
import java.io.IOException;
import java.util.*;

public abstract class AbstractWorldMap implements IWorldMap {
    private final MapStatistics statistics;
    protected final Integer height;
    protected final Integer width;
    public HashMap<Vector2D, SortedSet<Animal>> animals = new HashMap<>();
    public HashMap<Vector2D, Plant> plants = new HashMap<>();
    public AbstractWorldMap(Integer height, Integer width){
        if(height != null && width != null && height > 0 && width > 0){
            this.height = height;
            this.width = width;
            statistics = new MapStatistics(this);
        }
        else{
            throw new IllegalArgumentException("Map size cannot be null");
        }
    }
    public int getHeight(){
        return height;
    }
    public int getWidth(){
        return width;
    }

    // PLACES ON MAP
    private boolean isPositionOnMap(Integer x, Integer y) {
        return 0 <= x && x < width && 0 <= y && y < height;
    }
    protected boolean canBeMoved(Vector2D position){
        return isPositionOnMap(position.getX(), position.getY());
    }
    public boolean isPlantOn(Vector2D position){
        return plants.containsKey(position);
    }
    @Override
    public boolean isOccupied(Vector2D position) {
        if(plants.containsKey(position)) return true;
        return animals.containsKey(position) && animals.get(position).size() > 0;
    }

    // PLACE OR MOVE ELEMENT
    private void placeAnimal(Animal animal, Vector2D position){
        if(!animals.containsKey(position)) {
            animals.put(position, new TreeSet<>((a1, a2) -> {
                if (a1.getEnergy().equals(a2.getEnergy())) {
                    return a1.getAge() - a2.getAge();
                }
                return a1.getEnergy().compareTo(a2.getEnergy());
            }));
        }
        animals.get(position).add(animal);
    }
    public void placeElement(AbstractWorldElement newElement) {
        Vector2D position = newElement.getPosition();
        if(newElement instanceof Plant && canBeMoved(position) && !isPlantOn(position)){
            plants.put(newElement.getPosition(), (Plant) newElement);
            statistics.addNewPlant();
        } else if (newElement instanceof Animal animal && canBeMoved(position)) {
            placeAnimal(animal, position);
            statistics.addNewAnimal(animal.getDNA());
        }
    }
    public void moveElement(AbstractWorldElement element, Vector2D newPosition) {
        if(element instanceof Animal animal && canBeMoved(newPosition)){
            SortedSet<Animal> oldSet = animals.get(animal.getPosition());
            oldSet.remove(animal);
            animal.setPosition(newPosition);
            placeAnimal(animal, newPosition);
        }
    }

    // DAILY ROUTINE
    public void feedAnimals(int plantEnergy, IPlantDecorator plantDecorator){
        ArrayList<Vector2D> vecs = new ArrayList<>();
        for(Vector2D plantVec: plants.keySet()){
            if(animals.containsKey(plantVec) && animals.get(plantVec).size() > 0){
                Animal animal = animals.get(plantVec).first();
                animal.addEnergy(plantEnergy);
                vecs.add(plantVec);
                statistics.removeDeadPlant();
                plantDecorator.addPlace(plantVec.getX(), plantVec.getY());
            }
        }
        for(Vector2D vec:vecs){
            plants.remove(vec);
        }
    }
    public void removeDeadAnimal(Animal animal){
        animals.get(animal.getPosition()).remove(animal);
        statistics.removeDeadAnimal(animal.getAge());
    }
    public ArrayList<Animal> genNewAnimals(int childEnergy, int MinParentEnergy){
        ArrayList<Animal> children = new ArrayList<>();
        for(SortedSet<Animal> set: animals.values()){
            Animal[] parents = new Animal[2];
            int i = 0;
            for(Animal animal: set){
                parents[i] = animal;
                i ++;
                if (i == 2) break;
            }
            if(parents[0] != null && parents[1] != null && parents[0].getEnergy() >= MinParentEnergy && parents[1].getEnergy() >= MinParentEnergy){
                children.add(parents[0].haveChild(parents[1], childEnergy));
            }
        }
        return children;
    }

    // STATISTICS
    public void addDailyStatsToCsv(File file) throws IOException {
        statistics.addDailyStatsToCsv(file);
    }
    public String getStats(){
        return "Total plants: " + statistics.getTotalPlants() + "\n" +
                "Total living animals: " + statistics.getTotalLivingAnimals() + "\n" +
                "Free places: " + statistics.getFreePlaces() + "\n" +
                "Most popular DNA: " + Arrays.toString(statistics.getMostPopularGenotype()) + "\n" +
                "Average energy level: " + statistics.getAverageEnergyLevel() + "\n" +
                "Average life length living animals: " + statistics.getAverageLifeLength() + "\n";
    }
    public Integer[] getBasicStats(){
        return new Integer[]{statistics.getTotalLivingAnimals(), statistics.getTotalPlants()};
    }
    public int[] getMostPopularDNA(){
        return statistics.getMostPopularGenotype();
    }
}
