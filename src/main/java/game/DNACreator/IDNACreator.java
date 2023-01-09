package game.DNACreator;

import game.WorldElements.Animal;

public interface IDNACreator {
    int[] createDNA(Animal mum, Animal dad);
    int[] createRandomDNA();
}
