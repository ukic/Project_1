package game;

import java.util.Random;

public enum WorldDirection {
    N,
    NE,
    E,
    SE,
    S,
    SW,
    W,
    NW;
    public Vector2D getMove(){
        return switch (this) {
            case N -> new Vector2D(0, 1);
            case NE -> new Vector2D(1, 1);
            case E -> new Vector2D(1, 0);
            case SE -> new Vector2D(1, -1);
            case S -> new Vector2D(0, -1);
            case SW -> new Vector2D(-1, -1);
            case W -> new Vector2D(-1, 0);
            case NW -> new Vector2D(-1, 1);
        };
    }
    private WorldDirection getNext(){
        return switch (this){
            case N -> WorldDirection.NE;
            case NE -> WorldDirection.E;
            case E -> WorldDirection.SE;
            case SE -> WorldDirection.S;
            case S -> WorldDirection.SW;
            case SW -> WorldDirection.W;
            case W -> WorldDirection.NW;
            case NW -> WorldDirection.N;
        };
    }
    public WorldDirection getNewDirection(Integer turn){
        WorldDirection newDirection = this;
        turn = turn % 8;
        for(int i=0; i<turn; i++){
            newDirection = newDirection.getNext();
        }
        return newDirection;
    }
    public WorldDirection getRandomDirection(){
        return switch (new Random().nextInt(8)){
            case 0 -> N;
            case 1 -> NE;
            case 2 -> E;
            case 3 -> SE;
            case 4 -> S;
            case 5 -> SW;
            case 6 -> W;
            case 7 -> NW;
            default -> throw new IllegalStateException("Unexpected value");
        };
    }

    @Override
    public String toString() {
        return switch (this){
            case N -> "North";
            case NE -> "North-East";
            case E -> "East";
            case SE -> "South-East";
            case S -> "South";
            case SW -> "South-West";
            case W -> "West";
            case NW -> "North-West";
        };
    }
}
