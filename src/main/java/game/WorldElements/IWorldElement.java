package game.WorldElements;

import game.Vector2D;

public interface IWorldElement {
    Vector2D getPosition();
    boolean isActive();
}
