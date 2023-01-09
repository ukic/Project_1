package game;

import game.WorldElements.Animal;
import game.WorldMaps.AbstractWorldMap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MapStatistics {
    AbstractWorldMap map;
    private int livingAnimals;
    private int totalAnimals;
    private int totalPlants;
    private int sumLifeLength;
    private final HashMap<int[], Integer> genotypes = new HashMap<>();

    // Constructor
    public MapStatistics(AbstractWorldMap map){
        if(map != null){
            this.map = map;
        }
        else{
            throw new NullPointerException("Map cannot be null");
        }
    }

    // Total living animals
    public void addNewAnimal(int[] genes){
        livingAnimals += 1;
        totalAnimals += 1;
        if(genotypes.containsKey(genes)){
            int tmp = genotypes.get(genes);
            genotypes.replace(genes, tmp, tmp+1);
        }
        else{
            genotypes.put(genes, 1);
        }
    }
    public void removeDeadAnimal(int lifeLength){
        livingAnimals -= 1;
        sumLifeLength += lifeLength;
    }
    public int getTotalLivingAnimals(){
        return livingAnimals;
    }

    // Total plants
    public void addNewPlant(){
        totalPlants += 1;
    }
    public void removeDeadPlant(){
        if(totalPlants >= 1){
            totalPlants -= 1;
        }
    }
    public int getTotalPlants(){
        return map.plants.size();
    }
    public double getAverageLifeLength() {
        return (double) sumLifeLength / (totalAnimals - livingAnimals);
    }
    public int getFreePlaces(){
        int freePlaces = 0;
        for(int i=0; i < map.getWidth(); i++){
            for(int j=0; j < map.getHeight(); j++){
                if(!map.isOccupied(new Vector2D(i, j))){
                    freePlaces += 1;
                }
            }
        }
        return freePlaces;
    }
    public int[] getMostPopularGenotype(){
        int[] mostPopular = null;
        for(int[] genes: genotypes.keySet()){
            if(mostPopular == null){
                mostPopular = genes;
            } else if (genotypes.get(mostPopular) > genotypes.get(genes)) {
                mostPopular = genes;
            }
        }
        return mostPopular;
    }
    public double getAverageEnergyLevel(){
        int totalEnergy = 0;
        for(SortedSet<Animal> set : map.animals.values()){
            if(set.size() > 0){
                for(Animal animal : set){
                    totalEnergy += animal.getEnergy();
                }
            }
        }
        return (double) totalEnergy/livingAnimals;
    }
    public void addDailyStatsToCsv(File csvFile) throws IOException {
        FileWriter fileWriter = new FileWriter(csvFile, true);
        fileWriter.write("\"" + getTotalLivingAnimals() + "\",\"" + getTotalPlants() + "\",\"" + getFreePlaces()
                + "\",\"" + Arrays.toString(getMostPopularGenotype()) + "\",\"" + getAverageEnergyLevel()
                + "\",\"" + getAverageLifeLength() + "\n");
        fileWriter.close();
    }
}
