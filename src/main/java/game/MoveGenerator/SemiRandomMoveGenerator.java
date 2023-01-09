package game.MoveGenerator;

import game.WorldElements.Animal;

import java.util.Random;

public class SemiRandomMoveGenerator extends AbstractNextMoveGenerator{
    public SemiRandomMoveGenerator(int N) {
        super(N);
    }
    @Override
    public int genNextMove(Animal animal) {
        int isNextRandom = new Random().nextInt(5);
        if(isNextRandom == 4){
            animal.setGeneIndex(new Random().nextInt(N));
        }
        return super.genNextMove(animal);
    }
}
