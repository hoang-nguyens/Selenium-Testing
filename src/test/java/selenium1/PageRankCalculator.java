//package selenium1;
//import java.util.HashMap;
//import java.util.Map;
//
//public class PageRankCalculator {
//
//    private static final double DAMPING_FACTOR = 0.85;
//    private static final int MAX_ITERATIONS = 100;
//    private static final double THRESHOLD = 0.001;
//
//    public Map<String, Double> calculatePageRank(HashMap<String, UserNode> graph) {
//        Map<String, Double> pageRanks = new HashMap<>();
//        int totalUsers = graph.size();
//
//        // Initialize the PageRank for each user
//        for (String username : graph.keySet()) {
//            pageRanks.put(username, 1.0 / totalUsers);
//        }
//
//        // PageRank algorithm: iterative computation
//        for (int i = 0; i < MAX_ITERATIONS; i++) {
//            Map<String, Double> newPageRanks = new HashMap<>();
//
//            for (String username : graph.keySet()) {
//                UserNode user = graph.get(username);
//                double rankSum = 0.0;
//
//                // Calculate rank contributions from incoming edges
//                for (UserNode follower : graph.values()) {
//                    if (follower.getEdges().contains(user)) {
//                        rankSum += pageRanks.get(follower.getUsername()) / follower.getEdges().size();
//                    }
//                }
//
//                // Update the new PageRank with damping
//                newPageRanks.put(username, (1 - DAMPING_FACTOR) / totalUsers + DAMPING_FACTOR * rankSum);
//            }
//
//            // Check convergence
//            if (hasConverged(pageRanks, newPageRanks)) {
//                break;
//            }
//            pageRanks = newPageRanks;
//        }
//
//        return pageRanks;
//    }
//
//    // Check if the PageRank has converged (i.e., difference is smaller than the threshold)
//    private boolean hasConverged(Map<String, Double> oldRanks, Map<String, Double> newRanks) {
//        for (String username : oldRanks.keySet()) {
//            if (Math.abs(oldRanks.get(username) - newRanks.get(username)) > THRESHOLD) {
//                return false;
//            }
//        }
//        return true;
//    }
//}