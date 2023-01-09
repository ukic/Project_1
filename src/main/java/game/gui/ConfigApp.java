package game.gui;

import game.DNACreator.FullyRandomDNACreator;
import game.DNACreator.IDNACreator;
import game.DNACreator.LittleCorrectedDNACreator;
import game.MoveGenerator.FullPredestinedGenerator;
import game.MoveGenerator.INextMoveGenerator;
import game.MoveGenerator.SemiRandomMoveGenerator;
import game.PlantDecorator.GreenEquator;
import game.PlantDecorator.IPlantDecorator;
import game.PlantDecorator.ToxicDead;
import game.SimulationEngine;
import game.Vector2D;
import game.WorldMaps.AbstractWorldMap;
import game.WorldMaps.Globe;
import game.WorldMaps.HellsPortal;
import game.WorldMaps.IWorldMap;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Objects;

public class ConfigApp extends Application {
    Scene scene;
    ChoiceBox<String> mapType, plantGrowthType, mutationOption, behaviourOption;
    Slider heightSlider, widthSlider, startPlantSlider, newPlantSlider, plantEnergySlider,
            startAnimalSlider, startAnimalEnergySlider, minHealthyAnimalEnergySlider,
            dnaLengthSlider, minMutationNumberSlider, maxMutationNumberSlider, energyChildLossSlider;
    File file;
    CheckBox to_csv;
    private AbstractWorldMap configureMap(String mapType){
        if(mapType.equals("Globe")){
            return new Globe((int) heightSlider.getValue(), (int) widthSlider.getValue());
        } else if (mapType.equals("Hell's Portal")) {
            return new HellsPortal((int) heightSlider.getValue(), (int) widthSlider.getValue());
        }
        return null;
    }
    private IPlantDecorator configurePlantDecorator(IWorldMap map, String plantGrowthType){
        if(plantGrowthType.equals("Green Equator")){
            return new GreenEquator(map);
        }
        if(plantGrowthType.equals("Toxic dead")){
            return new ToxicDead(map);
        }
        return null;
    }
    private IDNACreator configureDNACreator(int N, int minMutationNumber, int maxMutationNumber, String mutationOption){
        if(minMutationNumber > maxMutationNumber){
            errorMessage("Minimum number of mutations is greater than maximum number of mutations.");
        }
        if(mutationOption.equals("Fully random")){
            return new FullyRandomDNACreator(N, minMutationNumber, maxMutationNumber);
        } else if (mutationOption.equals("Little corrected")) {
            return new LittleCorrectedDNACreator(N, minMutationNumber, maxMutationNumber);
        }
        return null;
    }
    private INextMoveGenerator configureNextMoveGenerator(int N, String behaviourOption){
        if(behaviourOption.equals("Fully predestined")){
            return new FullPredestinedGenerator(N);
        } else if (behaviourOption.equals("Semi random")) {
            return new SemiRandomMoveGenerator(N);
        }
        return null;
    }
    private Scene errorMessage(String message){
        return new Scene(new Label(message), 200, 200);
    }
    private void runSimulation(AbstractWorldMap map, Vector2D vec, IPlantDecorator plantDecorator,
                               IDNACreator dnaCreator, INextMoveGenerator moveHandler, int startPlants, int plantEnergy,
                               int newPlants, int startAnimals, int startAnimalEnergy, int minHealthyAnimalEnergy,
                               int energyChildLoss, boolean to_csv, File file){
        if(map != null){
            if(plantDecorator != null){
                if(dnaCreator != null){
                    if(moveHandler != null){
                        SimulationEngine engine = new SimulationEngine(map, plantDecorator, dnaCreator, moveHandler,
                                startPlants, plantEnergy, newPlants, startAnimals, startAnimalEnergy,
                                minHealthyAnimalEnergy, energyChildLoss, to_csv, file);
                        SimulationWindow newSimulation = new SimulationWindow(engine, vec, map);
                        newSimulation.runSimulation();
                    }
                    else{
                        Stage errorStage = new Stage();
                        errorStage.setScene(errorMessage("Animal behaviour settings have to be properly declared!"));
                        errorStage.setTitle("Configuration error");
                        errorStage.show();
                    }
                }
                else{
                    Stage errorStage = new Stage();
                    errorStage.setScene(errorMessage("Animal settings have to be properly declared!"));
                    errorStage.setTitle("Configuration error");
                    errorStage.show();
                }
            }
            else{
                Stage errorStage = new Stage();
                errorStage.setScene(errorMessage("Plant settings have to be properly declared!"));
                errorStage.setTitle("Configuration error");
                errorStage.show();
            }
        }
        else{
            Stage errorStage = new Stage();
            errorStage.setScene(errorMessage("Map and its sizes has to be declared!"));
            errorStage.setTitle("Map configuration error");
            errorStage.show();
        }
    }
    private void configureSimulation(){
        AbstractWorldMap map = configureMap(mapType.getValue());
        if(map != null) {
            Vector2D vec = new Vector2D(map.getWidth(), map.getHeight());
            IPlantDecorator plantDecorator = configurePlantDecorator(map, plantGrowthType.getValue());

            int N = (int) dnaLengthSlider.getValue();
            int minMutation = (int) minMutationNumberSlider.getValue();
            int maxMutation = (int) maxMutationNumberSlider.getValue();
            IDNACreator dnaCreator = configureDNACreator(N, minMutation, maxMutation, mutationOption.getValue());
            INextMoveGenerator moveHandler = configureNextMoveGenerator(N, behaviourOption.getValue());
            runSimulation(map, vec, plantDecorator, dnaCreator, moveHandler,(int) startPlantSlider.getValue(),
                    (int) plantEnergySlider.getValue(), (int) newPlantSlider.getValue(),
                    (int) startAnimalSlider.getValue(), (int) startAnimalEnergySlider.getValue(),
                    (int) minHealthyAnimalEnergySlider.getValue(), (int) energyChildLossSlider.getValue(),
                    to_csv.isSelected(), file);
        } else{
            Stage errorStage = new Stage();
            errorStage.setScene(errorMessage("Map and its sizes has to be declared!"));
            errorStage.setTitle("Map configuration error");
            errorStage.show();
        }
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
    private void createConfigFile(File file) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write("map:" + mapType.getValue() +
                "\nheight:" + (int) heightSlider.getValue() +
                "\nwidth:" + (int) widthSlider.getValue() +
                "\nplantGrowth:" + plantGrowthType.getValue() +
                "\ndnaLength:" + (int) dnaLengthSlider.getValue() +
                "\nminMutations:" + (int) minMutationNumberSlider.getValue() +
                "\nmaxMutations:" + (int) maxMutationNumberSlider.getValue() +
                "\nmutationType:" + mutationOption.getValue() +
                "\nbehaviourType:" + behaviourOption.getValue() +
                "\nstartPlants:" + (int) startPlantSlider.getValue() +
                "\nplantEnergy:" + (int) plantEnergySlider.getValue() +
                "\nnewPlants:" + (int) newPlantSlider.getValue() +
                "\nstartAnimal:" + (int) startAnimalSlider.getValue() +
                "\nstartAnimalEnergy:" + (int) startAnimalEnergySlider.getValue() +
                "\nminHealthyEnergy:" + (int) minHealthyAnimalEnergySlider.getValue() +
                "\nenergyChildLoss:" + (int) energyChildLossSlider.getValue() +
                "\nto_csv:" + to_csv.isSelected() +
                "\nfile:" + file.getAbsolutePath());
        fileWriter.close();
    }
    private void uploadConfigFile(File file) throws IOException {
        int height = -1, width = -1, N = -1, minMutationNumber = -1, maxMutationNumber = -1, startPlants = -1,
                plantEnergy = -1, newPlants = -1, startAnimals = -1, startAnimalEnergy = -1, minHealthyAnimalEnergy = -1,
                energyChildLoss = -1;
        String mapType = null, plantGrowthType = null, mutationOption = null, behaviourOption = null;
        boolean to_csv = true;
        File csv_file = null;

        BufferedReader fileReader = new BufferedReader(new FileReader(file));

        String st;
        String[] splitted;
        while((st = fileReader.readLine()) != null){
            splitted = st.split(":");
            switch(splitted[0]){
                case "map" -> mapType = splitted[1];
                case "height" -> height = Integer.parseInt(splitted[1]);
                case "width" -> width = Integer.parseInt(splitted[1]);
                case "plantGrowth" -> plantGrowthType = splitted[1];
                case "dnaLength" -> N = Integer.parseInt(splitted[1]);
                case "minMutations" -> minMutationNumber = Integer.parseInt(splitted[1]);
                case "maxMutations" -> maxMutationNumber = Integer.parseInt(splitted[1]);
                case "mutationType" -> mutationOption = splitted[1];
                case "behaviourType" -> behaviourOption = splitted[1];
                case "startPlants" -> startPlants = Integer.parseInt(splitted[1]);
                case "plantEnergy" -> plantEnergy = Integer.parseInt(splitted[1]);
                case "newPlants" -> newPlants = Integer.parseInt(splitted[1]);
                case "startAnimal" -> startAnimals = Integer.parseInt(splitted[1]);
                case "startAnimalEnergy" -> startAnimalEnergy = Integer.parseInt(splitted[1]);
                case "minHealthyEnergy" -> minHealthyAnimalEnergy = Integer.parseInt(splitted[1]);
                case "energyChildLoss" -> energyChildLoss = Integer.parseInt(splitted[1]);
                case "to_csv" -> to_csv = Boolean.parseBoolean(splitted[1]);
                case "file" -> csv_file = new File(splitted[1]);
            }
        }

        AbstractWorldMap map = null;
        Vector2D vec = null;
        if((height > 0 || width > 0) && mapType != null){
            map = configureMap(mapType);
            vec = new Vector2D(width, height);
        }
        if(map != null && plantGrowthType != null && mutationOption != null && behaviourOption != null){
            IPlantDecorator plantDecorator = configurePlantDecorator(map, plantGrowthType);
            IDNACreator dnaCreator = configureDNACreator(N, minMutationNumber, maxMutationNumber, mutationOption);
            INextMoveGenerator moveHandler = configureNextMoveGenerator(N, behaviourOption);
            if(N > 0 && minMutationNumber > -1 && maxMutationNumber > minMutationNumber && startPlants > -1 &&
                    plantEnergy > -1 && newPlants > -1 && startAnimals > -1 && startAnimalEnergy > -1 &&
                    minHealthyAnimalEnergy > -1 && energyChildLoss > -1){
                runSimulation(map, vec, plantDecorator, dnaCreator, moveHandler, startPlants, plantEnergy, newPlants,
                        startAnimals, startAnimalEnergy, minHealthyAnimalEnergy, energyChildLoss, to_csv, csv_file);
            }
            else{
                errorMessage("Configuration file is incorrect");
            }
        }
        else{
            errorMessage("Configuration file is incorrect");
        }
    }
    private void createPrimaryScene(){
        Object[] tmp;
        // Rodzaj mapy
        mapType = new ChoiceBox<>(FXCollections.observableArrayList(
                "Globe", "Hell's Portal")
        );
        mapType.setValue("Globe");
        HBox mapBox = new HBox(new Label("Choose map type: "), mapType);

        //Wysokość mapy
        tmp = createSlider(1, 100, 10, "Map height: ");

        heightSlider = (Slider) tmp[0];
        Label mapHeight = (Label) tmp[1];
        HBox heightBox = (HBox) tmp[2];

        // Szerokość mapy
        tmp = createSlider(1, 100, 10, "Map width: ");

        widthSlider = (Slider) tmp[0];
        Label mapWidth = (Label) tmp[1];
        HBox widthBox = (HBox) tmp[2];

        // Sposób wzrostu roślin
        plantGrowthType = new ChoiceBox<>(FXCollections.observableArrayList(
                "Green Equator", "Toxic dead")
        );
        plantGrowthType.setValue("Green Equator");
        HBox plantGrowthBox = new HBox(new Label("Choose plant growth type: "), plantGrowthType);
        
        // Początkowa liczba roślin
        tmp = createSlider(0, 100, 10, "Start number of plants: ");

        startPlantSlider = (Slider) tmp[0];
        Label startPlant = (Label) tmp[1];
        HBox startPlantBox = (HBox) tmp[2];

        // Liczba nowych roślin każdego dnia
        tmp = createSlider(0, 100, 10, "Number of plants growing every day: ");

        newPlantSlider = (Slider) tmp[0];
        Label newPlant = (Label) tmp[1];
        HBox newPlantBox = (HBox) tmp[2];

        // Plant energy
        tmp = createSlider(1, 100, 10, "Plant energy: ");

        plantEnergySlider = (Slider) tmp[0];
        Label plantEnergy = (Label) tmp[1];
        HBox plantEnergyBox = (HBox) tmp[2];
        
        // Mutacje
        mutationOption = new ChoiceBox<>(FXCollections.observableArrayList(
                "Fully random", "Little corrected")
        );
        mutationOption.setValue("Fully random");
        HBox mutationBox = new HBox(new Label("Choose mutation type: "), mutationOption);
        
        // Zachowanie się zwierząt
        behaviourOption = new ChoiceBox<>(FXCollections.observableArrayList(
                "Fully predestined", "Semi random")
        );
        behaviourOption.setValue("Fully predestined");
        HBox behaviourBox = new HBox(new Label("Choose animal behaviour type: "), behaviourOption);

        // Start number of animals
        tmp = createSlider(0, 100, 10, "Start number of animals: ");

        startAnimalSlider = (Slider) tmp[0];
        Label startAnimal = (Label) tmp[1];
        HBox startAnimalBox = (HBox) tmp[2];

        // Start energy level of animals
        tmp = createSlider(1, 100, 10, "Start animal energy: ");

        startAnimalEnergySlider = (Slider) tmp[0];
        Label startAnimalEnergy = (Label) tmp[1];
        HBox startAnimalEnergyBox = (HBox) tmp[2];

        // Energy loss for child
        tmp = createSlider(1, 100, 10, "Energy loss for a child: ");

        energyChildLossSlider = (Slider) tmp[0];
        Label energyChildLoss = (Label) tmp[1];
        HBox energyChildLossBox = (HBox) tmp[2];

        // Minimal healthy animal energy level
        tmp = createSlider(1, 100, 10, "Minimal healthy animal energy level: ");

        minHealthyAnimalEnergySlider = (Slider) tmp[0];
        Label minHealthyAnimalEnergy = (Label) tmp[1];
        HBox minHealthyAnimalEnergyBox = (HBox) tmp[2];

        // DNA length
        tmp = createSlider(1, 100, 10, "DNA length: ");

        dnaLengthSlider = (Slider) tmp[0];
        Label dnaLength = (Label) tmp[1];
        HBox dnaLengthBox = (HBox) tmp[2];

        // Minimum number of mutations in child
        tmp = createSlider(0, 100, 10, "Minimum number of mutations in child DNA: ");

        minMutationNumberSlider = (Slider) tmp[0];
        Label minMutationNumber = (Label) tmp[1];
        HBox minMutationNumberBox = (HBox) tmp[2];

        // Maximum number of mutations in child
        tmp = createSlider(0, 100, 10, "Maximum number of mutations in child DNA: ");

        maxMutationNumberSlider = (Slider) tmp[0];
        Label maxMutationNumber = (Label) tmp[1];
        HBox maxMutationNumberBox = (HBox) tmp[2];

        // Start button
        Button startButton = new Button("Start");
        startButton.setOnAction(event -> {
            configureSimulation();
        });

        // File path button
        Button fileButton = new Button("Choose file...");
        fileButton.setOnAction(event -> {
            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("CSV", "*.csv")
            );
            fileChooser.setTitle("Choose csv file");
            File newFile = fileChooser.showOpenDialog(stage);
            file = Objects.requireNonNullElseGet(newFile, () -> new File("simulation_statistics.csv"));
        });

        to_csv = new CheckBox("Save day data to csv");

        // Configuration files
        Button saveToConfigFile = new Button("Save configurations...");
        saveToConfigFile.setOnAction(event -> {
            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("txt", "*.txt")
            );
            fileChooser.setTitle("Choose txt file");
            File newFile = fileChooser.showOpenDialog(stage);
            file = Objects.requireNonNullElseGet(newFile, () -> new File("config.txt"));
            try {
                createConfigFile(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Button uploadConfigFile = new Button("Start saved configurations...");
        uploadConfigFile.setOnAction(event -> {
            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("txt", "*.txt")
            );
            fileChooser.setTitle("Choose txt file");
            File newFile = fileChooser.showOpenDialog(stage);
            try {
                uploadConfigFile(newFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        HBox configFileBox = new HBox(uploadConfigFile, saveToConfigFile);
        configFileBox.setSpacing(20);

        HBox fileBox = new HBox(to_csv, fileButton);
        fileBox.setSpacing(20);

        Font headings = Font.font("Arial", FontWeight.BOLD, 14);
        Label mapHeading = new Label("Map configuration");
        mapHeading.setFont(headings);

        Label plantHeading = new Label("Plant growth configuration");
        plantHeading.setFont(headings);

        Label animalHeading = new Label("Animal configuration");
        animalHeading.setFont(headings);

        Label startHeading = new Label("Start simulation");
        startHeading.setFont(headings);

        VBox settings = new VBox(
                mapHeading,
                mapBox, heightBox, widthBox,
                plantHeading,
                plantGrowthBox,startPlantBox, newPlantBox,
                animalHeading,
                startAnimalBox, startAnimalEnergyBox,
                minHealthyAnimalEnergyBox, dnaLengthBox,
                mutationBox, minMutationNumberBox, maxMutationNumberBox,
                energyChildLossBox, behaviourBox, startHeading,
                fileBox,
                startButton, configFileBox);
        settings.setSpacing(20);

        BorderPane border = new BorderPane();
        ScrollPane scrollPane = new ScrollPane(settings);
        scrollPane.setPrefSize(500,500);

        border.setCenter(scrollPane);
        BorderPane.setMargin(scrollPane, new Insets(20,20,20,20));

        scene = new Scene(border, 1000, 800);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        createPrimaryScene();
        primaryStage.setTitle("Simulation configuration");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
