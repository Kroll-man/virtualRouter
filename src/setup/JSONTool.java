package setup;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//Do NOT modify this class
public class JSONTool {
    private final List<Link> _links;
    private final JSONObject _routerInfo;
    private final int _routerId;

    /**
     * Tool used for parsing /src/setup/config.json
     * @param routerId
     * @throws IOException
     */
    public JSONTool(Integer routerId) throws IOException {
        // Read config.json and split into link weights data and router info data
        String path = (System.getProperty("user.dir") + "/src/setup/config.json");
        FileReader reader = new FileReader(path);
        JSONObject a = (JSONObject) JSONValue.parse(reader);
        List<JSONObject> jsonLinks = (List<JSONObject>) a.get("links");
        _routerInfo = (JSONObject) a.get("router-info");
        _routerId = routerId;
        _links = parseLinksToLinkObj(jsonLinks);
    }

    /**
     * Gets all links in config file
     * @return List<Link>
     */
    public List<Link> getLinks() {
        return _links;
    }

    /**
     * Gets own IP
     * @return InetAddress
     * @throws UnknownHostException
     */
    public InetAddress getIP() throws UnknownHostException {
        String sip = ((JSONObject)_routerInfo.get(String.valueOf(_routerId))).get("ip").toString();
        if (Objects.equals(sip, "localhost")) {
            return InetAddress.getLocalHost();
        } else {
            return InetAddress.getByName(sip);
        }
    }

    /**
     * Gets IP of passed router ID
     * @param id RouterId
     * @return InetAddress
     * @throws UnknownHostException
     */
    public InetAddress getIPById(int id) throws UnknownHostException {
        String sip = ((JSONObject)_routerInfo.get(String.valueOf(id))).get("ip").toString();
        if (Objects.equals(sip, "localhost")) {
            return InetAddress.getLocalHost();
        } else {
            return InetAddress.getByName(sip);
        }
    }

    /**
     * Gets own port number
     * @return Integer
     */
    public Integer getPort() {
        return Integer.parseInt(((JSONObject)_routerInfo.get(String.valueOf(_routerId))).get("port").toString());
    }

    /**
     * Gets Port of passed router ID
     * @param id RouterId
     * @return Integer
     */
    public Integer getPortById(int id) {
        return Integer.parseInt(((JSONObject)_routerInfo.get(String.valueOf(id))).get("port").toString());
    }

    /**
     * Casts JSONObject from config file to Link
     * @param list list of JSONObject that are links
     * @return List<Link>
     */
    private List<Link> parseLinksToLinkObj(List<JSONObject> list) {
        List<Link> links = new ArrayList<>();

        for (JSONObject o : list) {
            List<Integer> routers = new ArrayList<>();
            List<JSONObject> jsonRouters = (JSONArray) o.get("routers");
            int weight = Integer.parseInt(o.get("weight").toString());
            routers.add(Integer.parseInt(String.valueOf(jsonRouters.get(0))));
            routers.add(Integer.parseInt(String.valueOf(jsonRouters.get(1))));

            if (routers.contains(_routerId)) {
                int otherId = routers.get(0) == _routerId ? routers.get(1) : routers.get(0);
                links.add(new Link(routers, weight));
            }
        }
        return links;
    }

}
