//package selenium1;
//
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Set;
//
//public class GraphManager {
//    private HashMap<String, UserNode> userGraph;
//
//    public GraphManager() {
//        userGraph = new HashMap<>();
//    }
//
//    // Add a user to the graph
//    public void addUser(String username) {
//        userGraph.putIfAbsent(username, new UserNode(username));
//    }
//
//    // Add an edge (relationship) between two users (follower, retweet, etc.)
//    public void addEdge(String fromUser, String toUser) {
//        UserNode fromNode = userGraph.get(fromUser);
//        UserNode toNode = userGraph.get(toUser);
//
//        if (fromNode != null && toNode != null) {
//            fromNode.addEdge(toNode);
//        }
//    }
//
//    public HashMap<String, UserNode> getUserGraph() {
//        return userGraph;
//    }
//
//    // Optional: Display the graph structure for debugging
//    public void displayGraph() {
//        for (String username : userGraph.keySet()) {
//            System.out.println(userGraph.get(username));
//        }
//    }
//}