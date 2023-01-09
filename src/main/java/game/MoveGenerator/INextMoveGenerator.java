package game.MoveGenerator;

import game.WorldElements.Animal;

public interface INextMoveGenerator {
    int genNextMove(Animal animal);
}
