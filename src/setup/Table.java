package setup;

import java.io.Serializable;
import java.util.Dictionary;


//This class represents the distance vector that a router initiates, optimizes, and exchanges with neighbors
public class Table implements Serializable {
 //TODO: add any member variables and member methods.
    private Dictionary dict;

    @Override
    public String toString() {
        //TODO: you must complete the toString() method to print out the content of the distance vector
        
        return "temp";
    }

    public void addEntry(int destId, RouteRecord record){
        dict.put(destId,record);
    }

}
