package setup;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Router {
    private final int _Id;
    private final int _port;
    private final JSONTool _jsonTool;

    // maximum size of a UDP packet allowed
    private final int COMM_BYTE_SIZE = 1048;

    // the router's own distance vector
    private final Table _table = new Table();

    // a list of all neighbors
    private final List<Integer> _neighborIds = new ArrayList<>();

    private final DatagramSocket _datagramSocket;


    /*
       The constructor starts the router:
          - it initializes the distance vector;
	  - it populates the list of neighbors, and
          - it sends out the initial distance vector to all neighbors;
       The constructor also starts a thread to periodically send out its distance vector to all neighbors (keepalive)
     */
    public Router(int routerNumber) throws Exception {
        _Id = routerNumber;
        try {
            _jsonTool = new JSONTool(routerNumber);
        } catch (IOException e) {
            throw new Error(e.getMessage());
        }
        // Get and initialize neighbor links
        List<Link> links = _jsonTool.getLinks();
        initializeTable(links);
        initializeNeighbors(links);

        // Get self port
        _port = _jsonTool.getPort();

        // Create a DataGramSocket to listen for communication
        _datagramSocket = new DatagramSocket(_port);

        sendTablePeriodically(5, 10);
    }

    
    // This method keeps the router running by executing an infinite loop
    @SuppressWarnings("InfiniteLoopStatement")
    public void runRouter() throws Exception{
        while (true) {
	    //TODO: implement the distance vector routing protocol

        }
    }

    /* Private methods */

    private Table splitHorizon(int destinationRouterId) {
	//TODO: implement the split horizon rule technique, as follows:
	//      before sending the distance vector to a neighbor,
	//      remove all the entries for which the neighbor is used as the next hop
	//      (Note that you should first replicate the distance vector, then perform
	//       the removals on the copy, and then return the pruned copy.)
	

    }

    // This method is called whenever a distance vector is received from a neighbor.
    private boolean optimizeTable(Table incomingTable){
	//TODO: complete this method by implementing the Bellman-Ford algorithm
	// Note that this method should return true if the optimization is successful ( i.e.,
	// at least one entry of the router's own distance vector has been optimized.)
	// Otherwise, if the router's own distance vector remains unchanged after the optimization attempt,
	// this method should return false.
	

    }

    private void initializeTable() {
	//TODO: complete this method to initialize the distance vector: _table
        // use JSONTool to get links
        List<Link> links = _jsonTool.getLinks();
        //loop though each link
        for(Link link: links){
            //Find out ID of neighbor using the link
            int otherRouterId =  link.connectingRouterId().get(0) == _Id ? link.connectingRouterId().get(1) : link.connectingRouterId().get(0);
            //add it to the routing table, represented here by member variable _table
            _table.addEntry(otherRouterId, new RouteRecord(link.weight(), otherRouterId));
        }
        // add entry for self, equal to 0
        _table.addEntry(_Id, new RouteRecord(0, _Id));
        //print it to console to show intitial table with direct links
        System.out.println("Initial direct link table");
        System.out.println(_table);
    }

    private void initializeNeighbors(List<Link> links) {
	//TODO: complete this method by populating the neighbor list: _neighborIds

    }

    /* BELOW METHOD SHOULD NOT NEED CHANGED */

    /**
     * Receives table from incoming DatagramPacket
     * @param dgp DatagramPacket
     * @return Table
     * @throws Exception
     */
    private Table receiveTable(DatagramPacket dgp) throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(dgp.getData(), 0, dgp.getLength());
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        return (Table) objectInputStream.readObject();
    }

    /**
     * Sends table to specified router
     * @param IP
     * @param port
     * @param table
     * @throws Exception
     */
    private void sendTable(InetAddress IP, int port, Table table) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(COMM_BYTE_SIZE);
        ObjectOutputStream oos = new ObjectOutputStream(outputStream);
        oos.writeObject(table);
        if (outputStream.size() > COMM_BYTE_SIZE) throw new Exception("Message too large");
        _datagramSocket.send(new DatagramPacket(outputStream.toByteArray(), outputStream.size(), IP, port));
    }

    /**
     * Sends the _table member variable every {interval} seconds
     * @param delay
     * @param interval
     */
    private void sendTablePeriodically(int delay, int interval) {
        Runnable helloRunnable = () -> {
            for (int neighborId : _neighborIds) {
                try {
                    InetAddress otherAddress = _jsonTool.getIPById(neighborId);
                    int otherPort = _jsonTool.getPortById(neighborId);
                    sendTable(otherAddress, otherPort, _table);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(helloRunnable, delay, interval, TimeUnit.SECONDS);
    }
}
