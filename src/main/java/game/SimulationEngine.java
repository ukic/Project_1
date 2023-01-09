package game;

import game.DNACreator.IDNACreator;
import game.MoveGenerator.INextMoveGenerator;
import game.PlantDecorator.IPlantDecorator;
import game.WorldElements.Animal;
import game.WorldMaps.AbstractWorldMap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulationEngine implements Runnable {
    private final AbstractWorldMap map;
    private final IPlantDecorator plantDecorator;
    private final IDNACreator dnaCreator;
    private final INextMoveGenerator moveHandler;
    private final List<Animal> livingAnimals = new ArrayList<>();
    private final List<Animal> totalAnimals = new ArrayList<>();
    private IObserver observer;
    private int day;
    private final int plantEnergy, numberOfNewPlants, startNumberOfAnimals, startAnimalEnergy, minEnergyHealthyAnimal,
            energyLossForChild;
    private final File file;
    private final boolean to_csv;
    private boolean isSuspended = false;

    public SimulationEngine(AbstractWorldMap map, IPlantDecorator plantDecorator, IDNACreator dnaCreator,
                            INextMoveGenerator moveHandler,
                            int startNumberOfPlants, int plantEnergy, int numberOfNewPlants, int startNumberOfAnimals,
                            int startAnimalEnergy, int minEnergyHealthyAnimal, int energyLossForChild,
                            boolean to_csv, File file){
        this.map = map;
        this.plantDecorator = plantDecorator;
        this.dnaCreator = dnaCreator;
        this.moveHandler = moveHandler;
        this.plantEnergy = plantEnergy;
        this.numberOfNewPlants = numberOfNewPlants;
        this.startNumberOfAnimals = startNumberOfAnimals;
        this.startAnimalEnergy = startAnimalEnergy;
        this.minEnergyHealthyAnimal = minEnergyHealthyAnimal;
        this.energyLossForChild = energyLossForChild;
        this.file = file;
        this.to_csv = to_csv;
        plantDecorator.addPlants(startNumberOfPlants);
        addFirstAnimals();
        day = 1;
    }

    private void simulateDay() throws IOException {
        // Remove dead animals
        for(Animal animal : livingAnimals){
            if(!animal.isActive()){
                map.removeDeadAnimal(animal);
                animal.setDeathDate(day);
            }
        }
        livingAnimals.removeIf(animal -> !animal.isActive());


        // Move all living animals
        for(Animal animal : livingAnimals){
            animal.move(moveHandler.genNextMove(animal));
        }

        // Konsupcja roślin
        map.feedAnimals(plantEnergy, plantDecorator);

        // Wzrost nowych roślin
        plantDecorator.addPlants(numberOfNewPlants);

        // Rozmnożenie zwierząt
        ArrayList<Animal> newAnimals = map.genNewAnimals(energyLossForChild, minEnergyHealthyAnimal);
        livingAnimals.addAll(newAnimals);
        totalAnimals.addAll(newAnimals);

        // Strata energii
        for(Animal animal: livingAnimals){
            animal.addEnergy(-1);
            animal.addAge(1);
        }

        // Zapis statystyk
        if(to_csv) {
            map.addDailyStatsToCsv(file);
        }
    }
    private Animal createAnimal(){
        Vector2D position = new Vector2D(new Random().nextInt(0, map.getWidth()-1), new Random().nextInt(0, map.getHeight()-1));
        return new Animal(map, dnaCreator, position, WorldDirection.W.getRandomDirection(), startAnimalEnergy, dnaCreator.createRandomDNA());
    }
    private void addFirstAnimals(){
        for(int i=0; i < startNumberOfAnimals; i++){
            Animal newAnimal = createAnimal();
            livingAnimals.add(newAnimal);
            totalAnimals.add(newAnimal);
            map.placeElement(newAnimal);
        }
    }
    public void setObserver(IObserver newObserver){
        observer = newObserver;
    }
    public void setSuspended(boolean isSuspended){
        this.isSuspended = isSuspended;
    }
    public Integer[] totalSummary(){
        Integer[] tmp = map.getBasicStats();
        return new Integer[]{day, tmp[0], tmp[1]};
    }
    public int getHealthyEnergyLevel(){
        return minEnergyHealthyAnimal;
    }
    public void run(){
        while(true){
            if(!isSuspended){
                try {
                    simulateDay();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                day += 1;
                observer.stateChanged();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }        }
    }
}
