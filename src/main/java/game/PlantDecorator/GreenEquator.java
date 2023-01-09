package game.PlantDecorator;

import game.Vector2D;
import game.WorldElements.Plant;
import game.WorldMaps.IWorldMap;

import java.util.Random;

public class GreenEquator extends AbstractPlantDecorator{
    private final int minGreenRowIndex;
    private final int maxGreenRowIndex;

    public GreenEquator(IWorldMap map) {
        super(map);
        int equatorWidth = (int) (map.getHeight()*0.8);
        minGreenRowIndex = (map.getHeight() - equatorWidth)/2;
        maxGreenRowIndex = minGreenRowIndex + equatorWidth;
        createPlacesLists();
    }
    @Override
    protected boolean classifyPlace(int x, int y){
        return y >= minGreenRowIndex && y < maxGreenRowIndex;
    }
}
