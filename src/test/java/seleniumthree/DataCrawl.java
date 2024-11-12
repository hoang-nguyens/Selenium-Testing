package seleniumthree;

import java.io.*;
import java.util.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.*;

import seleniumsecond.LoadUserInfor;

public class DataCrawl {

    public static void main(String[] args) throws InterruptedException {
        // LoadUserInfor handles login and initialization of WebDriver
        LoadUserInfor loadUserInfor = new LoadUserInfor();
        LoadUserInfor.login();  // Use the login method from LoadUserInfor
        
        // Read links from input CSV (Modify path as needed)
        List<String> links = readLinksFromCSV("D:\\OOP Project 2024.1\\projecttest\\bitcoinUsersLink.csv");

        // Process a specific range of links (example: index 0 to 9)
        int startIndex = 0;
        int endIndex = 9;

        // Process the links range
        processLinksRange(links, startIndex, endIndex, loadUserInfor);
    }

    // Method to process links in the given range
    public static void processLinksRange(List<String> links, int startIndex, int endIndex, LoadUserInfor loadUserInfor) {
        for (int i = startIndex; i <= endIndex; i++) {
            String link = links.get(i);
            System.out.println("Processing link: " + link);
            
            // Call the method to get follower and following info
            List<String> followers = LoadUserInfor.findFollowerAndFollowing(link);
            List<String> verifiedFollowers = LoadUserInfor.findFollowerAndFollowing(link);  // Adjust as needed for verified followers
            List<String> following = LoadUserInfor.findFollowerAndFollowing(link);  // Adjust as needed for following
            
            // Write the results to CSV
            writeResultsToCSV("result.csv", link, followers, verifiedFollowers, following);
        }
    }

    // Read links from CSV file
    public static List<String> readLinksFromCSV(String filePath) {
        List<String> links = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                links.add(line);  // Add each link to the list
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return links;
    }

    // Write the results (followers, verified followers, following) to a CSV file
    public static void writeResultsToCSV(String filePath, String link, List<String> followers, List<String> verifiedFollowers, List<String> following) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            // Prepare the CSV row with the required columns
            String row = String.format("\"%s\",\"%s\",\"%s\",\"%s\"\n",
                    link, 
                    String.join(", ", followers),
                    String.join(", ", verifiedFollowers),
                    String.join(", ", following));
            bw.write(row);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
