package game.WorldMaps;

import game.Vector2D;

public class Globe extends AbstractWorldMap{
    public Globe(Integer height, Integer width) {
        super(height, width);
    }
    @Override
    protected boolean canBeMoved(Vector2D position){
        if(position.getX() < 0 || position.getX() >= width || position.getY() >= height || position.getY() < 0){
            return false;
        }
        if(position.getX() >= width){
            position.setX(position.getX() % width);
            return true;
        }
        return true;
    }
}
