package game.MoveGenerator;

import game.WorldElements.Animal;

abstract class AbstractNextMoveGenerator implements INextMoveGenerator{
    protected final int N;
    public AbstractNextMoveGenerator(int N){
        if(N > 0){
            this.N = N;
        }
        else{
            throw new IllegalArgumentException("N cannot be null and has to be greater > 0.");
        }
    }
    @Override
    public int genNextMove(Animal animal) {
        return animal.getDNA()[animal.getGeneIndex()%N];
    }
}
