package game.WorldElements;

import game.DNACreator.IDNACreator;
import game.WorldMaps.IWorldMap;
import game.Vector2D;
import game.WorldDirection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class Animal extends AbstractWorldElement {
    private Integer energy;
    private final IDNACreator dnaCreator;
    private final int[] genes;
    private WorldDirection direction;
    private int age, deathDate, plantsEaten, children;
    private int geneIndex, N;

    public Animal(IWorldMap map, IDNACreator dnaCreator, Vector2D position, WorldDirection direction, Integer energy,
                  int[] genes){
        super(map, position);
        if(direction != null && energy != null) {
            this.direction = direction;
            this.energy = energy;
            age = 0;
            geneIndex = new Random().nextInt(genes.length);
            this.dnaCreator = dnaCreator;
            this.genes = genes;
            plantsEaten = 0;
            N = genes.length;
        }
        else{
            throw new IllegalArgumentException("Arguments in creating world elements cannot be null");
        }
    }

    public boolean isActive(){
        return energy > 0;
    }
    public int getAge(){return age;}
    public void addAge(int older){ age += older;}
    public int getGeneIndex(){return geneIndex;}
    public void setGeneIndex(int geneIndex){
        this.geneIndex = geneIndex;
    }
    public void setDeathDate(int deathDate){
        this.deathDate = deathDate;
    }
    public Integer getEnergy(){
        return energy;
    }
    public void addEnergy(Integer newEnergy){
        if(newEnergy > 0){
            plantsEaten++;
            energy += newEnergy;
        } else if (energy + newEnergy >= 0) {
            energy += newEnergy;
        }
    }
    public int[] getDNA(){
        return genes;
    }
    public void move(Integer turn){
        direction = direction.getNewDirection(turn);
        Vector2D newPosition = position.add(direction.getMove());
        map.moveElement(this, newPosition);
        geneIndex += 1;
    }
    public void addChild(){
        children++;
    }
    public Animal haveChild(Animal animal, int childEnergy){
        animal.addEnergy(-childEnergy);
        animal.addChild();
        addEnergy(-childEnergy);
        Animal child = new Animal(map, dnaCreator, position, WorldDirection.N.getRandomDirection(), 2*childEnergy, dnaCreator.createDNA(this, animal));
        map.placeElement(child);
        this.addChild();
        return child;
    }
    public String toString(){
        return position.toString() + " Looking: " + direction.toString();
    }
    public String getStats(){
        String stats = "Stats" +
                "\nDNA: " + Arrays.toString(genes) +
                "\nCurrent gene: " + genes[geneIndex%N] +
                "\nEnergy level: " + energy +
                "\nPlants eaten: " + plantsEaten +
                "\nChildren: " + children;
        if(isActive()){
            stats += "\nAge: " + age;
        }else {
            stats += "\nDeath date: " + deathDate;
        }
        return stats;
    }
}
