package game.DNACreator;

import game.WorldElements.Animal;

import java.util.Random;

public class FullyRandomDNACreator extends AbstractDNACreator{
    public FullyRandomDNACreator(int N, int minMutationsCount, int maxMutationCount) {
        super(N, minMutationsCount, maxMutationCount);
    }

    @Override
    public int[] createDNA(Animal mum, Animal dad) {
        int[] newDNA = super.createDNA(mum, dad);
        int mutationsCount = new Random().nextInt(minMutationsCount, maxMutationCount+1);
        boolean[] mutated = new boolean[N];
        while(mutationsCount > 0){
            int currMutation = new Random().nextInt(N);
            if(!mutated[currMutation]){
                int newGeneValue = new Random().nextInt(8);
                if(newGeneValue != newDNA[currMutation]){
                    newDNA[currMutation] = newGeneValue;
                    mutated[currMutation] = true;
                    mutationsCount -= 1;
                }
            }
        }
        return newDNA;
    }
}
