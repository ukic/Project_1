package game.WorldElements;

import game.WorldMaps.IWorldMap;
import game.Vector2D;

abstract public class AbstractWorldElement implements IWorldElement {
    protected Vector2D position;
    protected final IWorldMap map;
    public AbstractWorldElement(IWorldMap map, Vector2D position){
        if(map != null && position != null){
            this.map = map;
            this.position = position;
        }
        else{
            throw new IllegalArgumentException("Arguments in creating world elements cannot be null");
        }
    }

    @Override
    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D newPosition){
        position = newPosition;
    }

}
