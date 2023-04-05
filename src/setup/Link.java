package setup;

//This record should NOT be modified.
//This record models a network link by the two endpoints (i.e., routers) plus the cost of the link
public record Link(java.util.List<Integer> connectingRouterId, int weight) {

}
