package game.PlantDecorator;

import game.Vector2D;
import game.WorldElements.Plant;
import game.WorldMaps.IWorldMap;

import java.util.ArrayList;
import java.util.Random;

abstract class AbstractPlantDecorator implements IPlantDecorator{
    protected IWorldMap map;
    protected ArrayList<Vector2D> preferredPlaces = new ArrayList<>();
    protected int preferredPlacesSize = 0;
    protected ArrayList<Vector2D> otherPlaces = new ArrayList<>();
    protected int otherPlacesSize = 0;
    public AbstractPlantDecorator(IWorldMap map){
        this.map = map;
        createPlacesLists();
    }
    protected void createPlacesLists(){
        for(int x=0; x < map.getWidth(); x++){
            for(int y=0; y < map.getHeight(); y++){
                addPlace(x, y);
            }
        }
    }
    protected boolean classifyPlace(int x, int y){
        return true;
    }
    protected void removePlace(int x, int y){};
    private Vector2D addPlant(){
        int list = new Random().nextInt(5);
        Vector2D place;
        if(list == 4 && otherPlacesSize > 0){
            place = otherPlaces.get(new Random().nextInt(0, otherPlacesSize));
            otherPlaces.remove(place);
            otherPlacesSize -= 1;
            removePlace(place.getX(), place.getY());
            return place;
        }
        else {
            if(preferredPlacesSize > 0){
                place = preferredPlaces.get(new Random().nextInt(0, preferredPlacesSize));
                preferredPlaces.remove(place);
                preferredPlacesSize -= 1;
                removePlace(place.getX(), place.getY());
                return place;
            }
        }
        return null;
    }
    public void addPlants(int n) {
        createPlacesLists();
        for(int i=0; i < n; i++){
            Vector2D newPosition = addPlant();
            if(newPosition == null){ return; }
            map.placeElement(new Plant(map, newPosition));
        }
    }
    public void addPlace(int x, int y){
        if(otherPlacesSize == 0 || classifyPlace(x, y)){
            preferredPlaces.add(new Vector2D(x, y));
            preferredPlacesSize++;
        }
        else {
            otherPlaces.add(new Vector2D(x, y));
            otherPlacesSize++;
        }
    }
}
