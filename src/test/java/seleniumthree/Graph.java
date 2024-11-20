package seleniumthree;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import javax.swing.*;
import org.apache.commons.csv.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Graph {

    // Map to store the graph (node to a set of connected nodes)
    private Map<String, Set<String>> graph;

    public Graph() {
        graph = new HashMap<>();
    }

    // Method to add nodes to the graph
    private void addNode(String node) {
        graph.putIfAbsent(node, new HashSet<>());
    }

    // Method to add edges from a list of connected nodes (followers, following, etc.)
    private void addEdgesFromList(String node, String connectedUsers) {
        if (connectedUsers != null && !connectedUsers.isEmpty()) {
            String[] users = connectedUsers.split(", ");
            for (String user : users) {
                addNode(node);  // Ensure the node exists in the graph
                addNode(user);  // Ensure the connected user exists in the graph
                graph.get(node).add(user);  // Add directed edge from node to user
            }
        }
    }

    // Method to process the CSV and build the graph using column indices
    public void readCSVAndBuildGraph(String csvFile) throws IOException {
        Reader reader = Files.newBufferedReader(Paths.get(csvFile));
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());

        // Print headers for debugging (column names might not be correct)
        System.out.println("CSV Headers: " + csvParser.getHeaderNames());

        // Read each record (row) from the CSV file
        for (CSVRecord record : csvParser) {
            // Access columns by index (adjust indices based on CSV structure)
            String kol = record.get(0);  // KOL name (assumes first column is KOL)
            String followers = record.get(1);  // Followers (assumes second column)
            String verifiedFollowers = record.get(2);  // Verified Followers (assumes third column)
            String following = record.get(3);  // Following (assumes fourth column)

            // Add KOL as a node
            addNode(kol);

            // Add edges from KOL to their followers, verified followers, and following
            addEdgesFromList(kol, followers);
            addEdgesFromList(kol, verifiedFollowers);
            addEdgesFromList(kol, following);
        }

        csvParser.close();
    }

    // Method to process the JSON data and build the graph
    public void processJSONAndBuildGraph(String jsonFile) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(jsonFile)));
        JSONObject jsonObject = new JSONObject(content);

        // Iterate over each KOL's data
        for (String kol : jsonObject.keySet()) {
            JSONObject kolData = jsonObject.getJSONObject(kol);

            // Process retweets
            JSONObject retweetComments = kolData.getJSONObject("retweetComments");
            for (String tweetId : retweetComments.keySet()) {
                JSONArray commentorsArray = retweetComments.getJSONArray(tweetId);
                for (int i = 0; i < commentorsArray.length(); i++) {
                    String commentor = commentorsArray.getString(i);  // Ensure you get the commentor as a String
                    addNode(commentor);  // Ensure the commentor exists in the graph
                    addNode(kol);  // Ensure KOL exists in the graph
                    graph.get(commentor).add(tweetId);  // Add edge from commentor to tweet
                    graph.get(kol).add(tweetId);  // Add edge from KOL to tweet
                }
            }

            // Process tweet comments
            JSONObject tweetComments = kolData.getJSONObject("tweetComments");
            for (String tweetId : tweetComments.keySet()) {
                JSONArray commentorsArray = tweetComments.getJSONArray(tweetId);
                for (int i = 0; i < commentorsArray.length(); i++) {
                    String commentor = commentorsArray.getString(i);  // Ensure you get the commentor as a String
                    addNode(commentor);  // Ensure the commentor exists in the graph
                    addNode(kol);  // Ensure KOL exists in the graph
                    graph.get(commentor).add(tweetId);  // Add edge from commentor to tweet
                    graph.get(kol).add(tweetId);  // Add edge from KOL to tweet
                }
            }

            // Process reposts
            JSONObject repostOwner = kolData.getJSONObject("repostOwner");
            for (String tweetId : repostOwner.keySet()) {
                String repostedBy = repostOwner.getString(tweetId);
                addNode(repostedBy);  // Ensure the repost owner exists in the graph
                addNode(kol);  // Ensure KOL exists in the graph
                graph.get(repostedBy).add(tweetId);  // Add edge from repost owner to tweet
                graph.get(kol).add(tweetId);  // Add edge from KOL to tweet
            }
        }
    }

    // Method to display the graph for debugging
    public void printGraph() {
        for (String node : graph.keySet()) {
            System.out.println(node + " -> " + graph.get(node));
        }
    }

    // Method to implement PageRank (simplified version)
    public Map<String, Double> computePageRank(int maxIterations, double d) {
        Map<String, Double> pageRank = new HashMap<>();

        // Initialize the page rank for each node
        for (String node : graph.keySet()) {
            pageRank.put(node, 1.0);
        }

        // Iteratively update the page rank
        for (int i = 0; i < maxIterations; i++) {
            Map<String, Double> newPageRank = new HashMap<>();
            for (String node : graph.keySet()) {
                double rankSum = 0.0;
                for (String neighbor : graph.keySet()) {
                    if (graph.get(neighbor).contains(node)) {
                        rankSum += pageRank.get(neighbor) / graph.get(neighbor).size();
                    }
                }
                newPageRank.put(node, (1 - d) + d * rankSum);
            }
            pageRank = newPageRank;
        }
        return pageRank;
    }
    
    
    
    public void visualizeGraphWithZoom() {
        mxGraph mxGraph = new mxGraph();
        Object parent = mxGraph.getDefaultParent();

        // Begin graph transaction
        mxGraph.getModel().beginUpdate();
        try {
            // Map to store JGraphX nodes
            Map<String, Object> vertexMap = new HashMap<>();

            // Add nodes to the graph
            for (String node : graph.keySet()) {
                vertexMap.put(node, mxGraph.insertVertex(parent, null, node, 0, 0, 80, 30)); // Node size
            }

            // Add edges between nodes
            for (String node : graph.keySet()) {
                for (String connectedNode : graph.get(node)) {
                    mxGraph.insertEdge(parent, null, "", vertexMap.get(node), vertexMap.get(connectedNode));
                }
            }
        } finally {
            mxGraph.getModel().endUpdate();
        }

        // Apply a layout (e.g., circle layout for better visualization)
        mxCircleLayout layout = new mxCircleLayout(mxGraph);
        layout.setRadius(200); // Adjust radius to control node distribution
        layout.execute(mxGraph.getDefaultParent());

        // Create a graph component
        mxGraphComponent graphComponent = new mxGraphComponent(mxGraph);

        // Apply zoom settings
        graphComponent.zoomAndCenter(); // Centers the graph initially
        graphComponent.zoomTo(0.009, true); // Adjust zoom to 75% of the original size

        // Display the graph in a JFrame
        JFrame frame = new JFrame("Graph Visualization with Zoom");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(graphComponent);
        frame.setSize(800, 600); // Set the size of the JFrame
        frame.setVisible(true);
    }

    // Main method to read the CSV and JSON, build the graph and compute PageRank
    public static void main(String[] args) {
        try {
            // File paths - Update the file paths based on your local files
            String csvFile = "D:\\file link\\result.csv";  // Path to the CSV file
            String jsonFile = "D:\\file link\\all.json";  // Path to the JSON file

            // Create the graph
            Graph graph = new Graph();

            // Build the graph from CSV
            graph.readCSVAndBuildGraph(csvFile);

            // Build the graph from JSON
            graph.processJSONAndBuildGraph(jsonFile);

            // Print the graph for debugging
            graph.printGraph();
            
//            graph.visualizeGraphWithZoom();

            // Compute and print PageRank
//            Map<String, Double> pageRank = graph.computePageRank(20, 0.85);
//            System.out.println("PageRank:");
//            for (Map.Entry<String, Double> entry : pageRank.entrySet()) {
//                System.out.println(entry.getKey() + " -> " + entry.getValue());
//            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
