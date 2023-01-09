package game.gui;

import game.WorldElements.Animal;
import game.WorldElements.IWorldElement;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;

public class GuiElementBox extends Node {
    private final ImageView iv = new ImageView();
    private final int[] dna;
    private final int healthyEnergyLevel;
    private final IWorldElement element;
    private String getImagePath(){
        if(element instanceof Animal animal){
            if(dna != null && Arrays.equals(animal.getDNA(), dna)){
                if(animal.getEnergy() < 6){
                    return "exhaustedGiraffeDNA.png";
                } else if (animal.getEnergy() < healthyEnergyLevel) {
                    return "tiredGiraffeDNA.png";
                } else {
                    return "healthyGiraffeDNA.png";
                }
            }else{
                if(animal.getEnergy() < 6){
                    return "exhaustedGiraffe.png";
                } else if (animal.getEnergy() < 11) {
                    return "tiredGiraffe.png";
                } else {
                    return "healthyGiraffe.png";
                }
            }
        }
        else{
            return "plant.png";
        }
    }

    public GuiElementBox(IWorldElement element, int[] dna, int healthyEnergyLevel){
        this.element = element;
        this.dna = dna;
        this.healthyEnergyLevel = healthyEnergyLevel;
    }
    private void setImage(){
        Image image;
        try{
            image = new Image(new FileInputStream("src/main/resources/" + getImagePath()));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        iv.setImage(image);
        iv.setFitWidth(20);
        iv.setFitHeight(20);
    }

    public VBox getGUIElement(){
        setImage();

        VBox vb = new VBox();
        vb.getChildren().add(iv);
        vb.setAlignment(Pos.CENTER);
        return vb;
    }
}
