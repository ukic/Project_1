package game.WorldMaps;

import game.Vector2D;

import java.util.Random;

public class HellsPortal extends AbstractWorldMap{
    public HellsPortal(Integer height, Integer width) {
        super(height, width);
    }
    private Vector2D genRandomPlace(){
        Random random = new Random();
        return new Vector2D(random.nextInt(0, width-1), random.nextInt(0, height-1));
    }
    @Override
    protected boolean canBeMoved(Vector2D position){
        if(position.getY() >= height || position.getY() < 0 || position.getX() < 0 || position.getX() >= width){
            Vector2D newPosition = genRandomPlace();
            position.setX(newPosition.getX());
            position.setY(newPosition.getY());
        }
        return true;
    }
}
