package game.WorldElements;

import game.WorldMaps.IWorldMap;
import game.Vector2D;

public class Plant extends AbstractWorldElement {
    private boolean isActive = true;

    public Plant(IWorldMap map, Vector2D position) {
        super(map, position);
    }
    @Override
    public boolean isActive() {
        return isActive;
    }
    public void setInactive(){ isActive = false; }
}
