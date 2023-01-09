package game.DNACreator;

import game.WorldElements.Animal;

import java.util.Random;

abstract public class AbstractDNACreator implements IDNACreator{
    protected int N;
    protected int minMutationsCount;
    protected int maxMutationCount;
    public AbstractDNACreator(int N, int minMutationsCount, int maxMutationCount){
        this.N = N;
        this.minMutationsCount = minMutationsCount;
        this.maxMutationCount = maxMutationCount;
    }
    public int[] createDNA(Animal mum, Animal dad){
        int[] newDNA = new int[N];
        int[] mumDNA = mum.getDNA();
        int[] dadDNA = dad.getDNA();

        int mumGenes = mum.getEnergy()/(mum.getEnergy() + dad.getEnergy());
        int side = new Random().nextInt(2);
        if(side == 0){
            for(int i=0; i < N; i++){
                if(i < mumGenes){
                    newDNA[i] = mumDNA[i];
                }
                else{
                    newDNA[i] = dadDNA[i];
                }
            }
        }
        else{
            mumGenes = N - mumGenes;
            for(int i=0; i < N; i++){
                if(i > mumGenes){
                    newDNA[i] = mumDNA[i];
                }
                else{
                    newDNA[i] = dadDNA[i];
                }
            }
        }
        return newDNA;
    }
    public int[] createRandomDNA(){
        int[] newDNA = new int[N];
        for(int i=0; i < N; i++){
            newDNA[i] = new Random().nextInt(8);
        }
        return newDNA;
    }
}
