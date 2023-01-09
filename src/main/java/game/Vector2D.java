package game;

import java.util.Objects;

public class Vector2D {
    private Integer x;
    private Integer y;
    private int deadAnimal;
    public Vector2D(Integer x, Integer y){
        if(x != null && y != null){
            this.x = x;
            this.y = y;
            deadAnimal = 0;
        }
        else {
            throw new IllegalArgumentException("Null cannot be vector coordinate");
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(Integer x){
        if(x != null){
            this.x = x;
        }
        else{
            throw new NullPointerException("Null cannot be new x coordinate");
        }
    }
    public void setY(Integer y){
        if(y != null){
            this.y = y;
        }
        else{
            throw new NullPointerException("Null cannot be new y coordinate");
        }
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }
    @Override
    public boolean equals(Object other){
        if (other instanceof Vector2D other_vec){
            return other_vec.x.equals(this.x) && other_vec.y.equals(this.y);
        }
        return false;
    }
    public Vector2D add(Vector2D vec){
        return new Vector2D(x + vec.getX(), y + vec.getY());
    }
    public void addDeadAnimal(){ deadAnimal++; }
    public int getDeadAnimalCount(){ return deadAnimal; }

    public String toString(){
        return x.toString() + ", " + y.toString();
    }
}
