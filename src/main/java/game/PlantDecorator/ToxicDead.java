package game.PlantDecorator;

import game.Vector2D;
import game.WorldMaps.IWorldMap;

import java.util.*;

public class ToxicDead extends AbstractPlantDecorator {
    private ArrayList<Vector2D> freePlaces;
    private int freePlacesSize;
    public ToxicDead(IWorldMap map) {
        super(map);
    }
    private void createFreePlacesList(){
        freePlaces = new ArrayList<>();
        freePlacesSize = 0;
        for(int x = 0; x < map.getWidth(); x++){
            for(int y = 0; y < map.getHeight(); y++){
                Vector2D vec = new Vector2D(x, y);
                if(!map.isPlantOn(vec)){
                    freePlaces.add(vec);
                    freePlacesSize++;
                }
            }
        }
        freePlaces.sort(Comparator.comparingInt(Vector2D::getDeadAnimalCount));
    }
    @Override
    protected void createPlacesLists(){
        if(freePlaces == null){ createFreePlacesList();}

        preferredPlacesSize = 0;
        otherPlacesSize = 0;

        for(int i = 0; i < freePlacesSize; i++){
            if(i < 0.8 * freePlacesSize){
                preferredPlaces.add(freePlaces.get(i));
                preferredPlacesSize++;
            }else {
                otherPlaces.add(freePlaces.get(i));
                otherPlacesSize++;
            }
        }
    }
    protected void removePlace(int x, int y){
        freePlaces.remove(new Vector2D(x, y));
        freePlacesSize--;
    }
    @Override
    public void addPlace(int x, int y){
        freePlaces.add(new Vector2D(x, y));
        freePlacesSize++;
    }
}
