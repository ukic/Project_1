package game;

import game.WorldElements.Plant;
import game.WorldMaps.Globe;
import game.WorldMaps.IWorldMap;
import game.gui.ConfigApp;
import javafx.application.Application;

import java.util.HashMap;

public class World {
    public static void main(String[] args){
        try {
            Application.launch(ConfigApp.class, args);
        }
        catch(IllegalArgumentException ex){
            System.out.println("Illegal argument");
        }
        catch(NullPointerException ex) {
            System.out.println("Null cannot be an argument");
        }
    }
}
