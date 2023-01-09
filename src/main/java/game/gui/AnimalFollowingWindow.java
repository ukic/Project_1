package game.gui;

import game.WorldElements.Animal;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class AnimalFollowingWindow {
    private final Animal animal;
    private Label stats = new Label();
    public AnimalFollowingWindow(Animal animal){
        this.animal = animal;
    }
    public void updateStats(){
        stats.setText(animal.getStats());
    }
    private Scene createPrimaryScene(){
        // Stats heading
        Font heading = Font.font("Arial", FontWeight.BOLD, 14);
        Label statsHeading = new Label("Animal statistics:");
        statsHeading.setFont(heading);

        updateStats();

        return new Scene(new VBox(statsHeading, stats), 100, 100);
    }
    public void runAnimalFollower(){
        Stage stage = new Stage();
        stage.setTitle("Simulation");
        stage.setScene(createPrimaryScene());
        stage.show();
    }
}
