package setup;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


//This class represents the distance vector that a router initiates, optimizes, and exchanges with neighbors
public class Table implements Serializable {
 //TODO: add any member variables and member methods.
    private List<RouteRecord> records;

    public Table() {
        records = new ArrayList<RouteRecord>();
    }


    @Override
    public String toString() {
        //TODO: you must complete the toString() method to print out the content of the distance vector
        String contents= "";
        for( RouteRecord rec: records){
            contents = String.format("Destination: %d -> cost: %d nextHop: %d ",rec.getDest(), rec.getCost(), rec.getNextHop());
        }
        return contents;
    }

    public void addEntry(RouteRecord record){
        records.add(record);
    }

    public RouteRecord getEntryByDest(int destId){
        for(RouteRecord record : records){
            if(destId == record.getDest()){
                return record;
            }
        }
        return null;
    }
    public RouteRecord getSelfEntry(){
        RouteRecord SelfEntry = null;
        for(RouteRecord record : records){
            if(record.getCost()==0){
                SelfEntry=record;
            }
        }
        return SelfEntry;
    }

    public List<RouteRecord> getRecords() {
        return records;
    }
}
