import setup.JSONTool;
import setup.Router;

import java.net.SocketException;

//This class should NOT be modified
public class Main {

    public static void main(String[] args) throws Exception {
	//A single command-line argument is required, which is the ID of this router
        int routerNum = Integer.parseInt(args[0]);

        // Create the router
        Router router = new Router(routerNum);

	// Keep the router running forever
        router.runRouter();
    }
}
