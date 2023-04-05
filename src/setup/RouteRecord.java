package setup;

import java.io.Serializable;


public class RouteRecord implements Serializable {


    //TODO: This class may be implemented as a "helper class" for helping implement the Table class
    // This class is optional: if you do not need this class, you can keep it empty
    private int cost;

    private int nextHop;

    public RouteRecord(int cst, int nxthop){
        this.cost = cst;
        this.nextHop = nxthop;
    }

    public int getCost() {
        return cost;
    }

    public int getNextHop() {
        return nextHop;
    }
    public void setCost(int cst) {
        this.cost = cst;
    }

    public void setNextHop(int nxthop) {
        this.nextHop = nxthop;
    }

}
