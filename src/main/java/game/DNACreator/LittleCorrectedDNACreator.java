package game.DNACreator;

import game.WorldElements.Animal;

import java.util.Random;

public class LittleCorrectedDNACreator extends AbstractDNACreator{
    public LittleCorrectedDNACreator(int N, int minMutationsCount, int maxMutationCount) {
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
                int newGeneValue = new Random().nextInt(2);
                if(newGeneValue == 0){
                    newDNA[currMutation] -= 1;
                    if(newDNA[currMutation] == -1){
                        newDNA[currMutation] = 7;
                    }
                }
                else{
                    newDNA[currMutation] += 1;
                    if(newDNA[currMutation] == 8){
                        newDNA[currMutation] = 0;
                    }
                }
                mutated[currMutation] = true;
                mutationsCount -= 1;
            }
        }
        return newDNA;
    }
}
