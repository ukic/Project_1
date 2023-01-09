package game.gui;

import game.IObserver;
import game.SimulationEngine;
import game.SimulationObserver;
import game.Vector2D;
import game.WorldElements.Animal;
import game.WorldElements.Plant;
import game.WorldMaps.AbstractWorldMap;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.HashMap;

public class SimulationWindow {
    Vector2D boundary;
    AbstractWorldMap map;
    SimulationEngine engine;
    IObserver observer;
    GridPane grid = new GridPane();
    Label statsInfo;
    Slider xSlider, ySlider;
    CheckBox mostPopularDNA = new CheckBox("Show animals with most popular DNA");;
    final Thread engineThread;
    XYChart.Series<Number, Number> plantCount = new XYChart.Series<>();
    XYChart.Series<Number, Number> animalCount = new XYChart.Series<>();
    HashMap<Animal, AnimalFollowingWindow> animalFollowingWindows = new HashMap<>();
    public SimulationWindow(SimulationEngine engine, Vector2D boundary, AbstractWorldMap map){
        this.boundary = boundary;
        this.engine = engine;
        observer = new SimulationObserver(this);
        this.map = map;
        engineThread = new Thread(engine);
    }
    private Scene errorMessage(String message){
        return new Scene(new Label(message), 200, 200);
    }
    private Object[] createSlider(int minValue, int maxValue, int startValue, String text){
        Slider slider = new Slider(minValue, maxValue, startValue);
        Label label = new Label(text + startValue);

        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setBlockIncrement(1);
        slider.valueProperty().addListener(
                (observable, oldValue, newValue) -> {
                    slider.setValue(newValue.intValue());
                    label.setText(text + slider.getValue());
                });

        HBox box = new HBox(label, slider);
        box.setSpacing(10);
        return new Object[]{slider, label, box};
    }
    private Scene createPrimaryScene(){
        // Create grid
        createGrid();

        //Putting animals on grid
        putWorldElementsOnGrid();

        // Stats heading
        Font heading = Font.font("Arial", FontWeight.BOLD, 14);
        Label statsHeading = new Label("Map statistics:");
        statsHeading.setFont(heading);

        // Stats info
        statsInfo = new Label();
        updateStats();

        //Chart
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Simulation day");
        NumberAxis yAxis = new NumberAxis();
        LineChart<Number,Number> lineChart = new LineChart<>(xAxis, yAxis);

        lineChart.setTitle("Number of animals and plants");
        plantCount.setName("Total plants");
        animalCount.setName("Total animals");

        lineChart.getData().add(plantCount);
        lineChart.getData().add(animalCount);

        // Follow
        Label followingLabel = new Label("Coordinates of animal to follow: ");

        Object [] tmp = createSlider(0, boundary.getX(), 0, "X = ");
        xSlider = (Slider) tmp[0];
        Label x = (Label) tmp[1];
        HBox xBox = (HBox) tmp[2];

        tmp = createSlider(0, boundary.getY(), 0, "Y = ");
        ySlider = (Slider) tmp[0];
        Label y = (Label) tmp[1];
        HBox yBox = (HBox) tmp[2];

        HBox followingBox = new HBox(followingLabel, xBox, yBox);
        followingBox.setSpacing(20);

        // Buttons
        Button startFollowingButton = new Button("Start following");
        startFollowingButton.setOnAction(event -> {
            if(map.animals.containsKey(new Vector2D((int) xSlider.getValue(), (int) ySlider.getValue())) &&
                    map.animals.get(new Vector2D((int) xSlider.getValue(), (int) ySlider.getValue())).size() > 0){
                Animal animal = map.animals.get(new Vector2D((int) xSlider.getValue(), (int) ySlider.getValue())).first();
                if(!animalFollowingWindows.containsKey(animal)){
                    animalFollowingWindows.put(animal, new AnimalFollowingWindow(animal));
                }
                animalFollowingWindows.get(animal).runAnimalFollower();
            }
            else {
                Stage errorStage = new Stage();
                errorStage.setScene(errorMessage("On given coordinates there are no animals"));
                errorStage.setTitle("Animal position error");
                errorStage.show();
            }
        });
        Button stopFollowingButton = new Button("Stop following");
        stopFollowingButton.setOnAction(event -> {
            Animal animal = map.animals.get(new Vector2D((int) xSlider.getValue(), (int) ySlider.getValue())).first();
            if(animal != null){
                if(animalFollowingWindows.containsKey(animal)){
                    animalFollowingWindows.remove(animal, new AnimalFollowingWindow(animal));
                }
            }
            else {
                Stage errorStage = new Stage();
                errorStage.setScene(errorMessage("On given coordinates there are no animals"));
                errorStage.setTitle("Animal position error");
                errorStage.show();
            }
        });

        startFollowingButton.setDisable(false);
        stopFollowingButton.setDisable(false);

        HBox followingButtonsBox = new HBox(startFollowingButton, stopFollowingButton);
        followingButtonsBox.setSpacing(20);

        // Buttons
        Button resumeButton = new Button("Resume");
        resumeButton.setOnAction(event -> {
            engine.setSuspended(false);
            startFollowingButton.setDisable(true);
            stopFollowingButton.setDisable(true);
        });
        Button pauseButton = new Button("Pause");
        pauseButton.setOnAction(event -> {
            engine.setSuspended(true);
            startFollowingButton.setDisable(false);
            stopFollowingButton.setDisable(false);
        });

        HBox pauseSimBox = new HBox(pauseButton, resumeButton);
        pauseSimBox.setSpacing(20);

        BorderPane layout = new BorderPane();
        layout.setCenter(grid);

        GridPane totalLayout = new GridPane();
        totalLayout.setPadding(new Insets(20, 20, 20, 20));
        totalLayout.setHgap(10);
        totalLayout.setVgap(10);

        totalLayout.add(statsHeading, 0, 0, 1, 1);
        totalLayout.add(statsInfo, 0, 1, 1, 3);
        totalLayout.add(lineChart, 0, 4, 1, 2);
        totalLayout.add(pauseSimBox, 0, 8, 1, 1);
        totalLayout.add(followingBox, 0, 9, 1, 1);
        totalLayout.add(followingButtonsBox, 0, 10, 1, 1);
        totalLayout.add(mostPopularDNA, 0, 11, 1, 1);
        totalLayout.add(grid, 2, 1, 5, 11);

        return new Scene(totalLayout, 1200, 800);
    }
    private void putElementOnGrid(Node gui, int x, int y){
        grid.add(gui, x, y, 1, 1);
        GridPane.setHalignment(gui, HPos.CENTER);
        GridPane.setValignment(gui, VPos.CENTER);
    }
    private void putWorldElementsOnGrid(){
        Node gui;
        // Putting plants on grid
        for(Vector2D vec: map.plants.keySet()){
            gui = new GuiElementBox(map.plants.get(vec), null, engine.getHealthyEnergyLevel()).getGUIElement();
            putElementOnGrid(gui, vec.getX(), boundary.getY() - vec.getY() - 1);
        }

        // Putting animals on map
        for(Vector2D vec: map.animals.keySet()){
            if(map.animals.get(vec).size() > 0){
                if(mostPopularDNA.isSelected()){
                    gui = new GuiElementBox(map.animals.get(vec).first(), map.getMostPopularDNA(), engine.getHealthyEnergyLevel()).getGUIElement();
                }else{
                    gui = new GuiElementBox(map.animals.get(vec).first(), null, engine.getHealthyEnergyLevel()).getGUIElement();
                }
                putElementOnGrid(gui, vec.getX(), boundary.getY() - vec.getY() - 1);
            }
        }
    }
    private void createGrid(){
        for(int x=0; x < boundary.getX(); x++){
            grid.getColumnConstraints().add(new ColumnConstraints(50));
        }
        for(int y=0; y < boundary.getY(); y++){
            grid.getRowConstraints().add(new RowConstraints(50));
        }

        grid.setGridLinesVisible(true);
    }
    private void updateGrid(){
        grid.setGridLinesVisible(false);
        grid.getColumnConstraints().clear();
        grid.getRowConstraints().clear();
        grid.getChildren().clear();

        createGrid();
        putWorldElementsOnGrid();

        grid.setGridLinesVisible(true);
        grid.setAlignment(Pos.CENTER);
    }
    private void updateStats(){
        statsInfo.setText(map.getStats());
    }
    private void updateChart(int day, int totalPlants, int totalAnimals){
        plantCount.getData().add(new XYChart.Data<>(day, totalPlants));
        animalCount.getData().add(new XYChart.Data<>(day, totalAnimals));
    }
    public void updateApp(){
        updateGrid();
        updateStats();
        Integer[] tmp = engine.totalSummary();
        updateChart(tmp[0], tmp[1], tmp[2]);
        for(AnimalFollowingWindow win : animalFollowingWindows.values()){
            win.updateStats();
        }
    }
    public void runSimulation(){
        Stage simStage = new Stage();
        simStage.setTitle("Simulation");
        simStage.setScene(createPrimaryScene());
        engine.setObserver(observer);
        engineThread.start();
        simStage.show();
    }
}
