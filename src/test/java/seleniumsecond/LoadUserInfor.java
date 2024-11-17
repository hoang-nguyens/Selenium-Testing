package seleniumsecond;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.opencsv.CSVWriter;

public class LoadUserInfor {
	public static WebDriver driver;  
    public static WebDriverWait wait;

	public static void main(String[] args) {
		login();
		
//		List<String> links = readLinksFromCSV("D:\\OOP Project 2024.1\\projecttest\\Web3UsersLink.csv");

        // Process a specific range of links (example: index 0 to 9)
//        int startIndex = 0;
//        int endIndex = 2;
//
//        // Process the links range
//        //processLinksRange(links, startIndex, endIndex);
//        processKOLsData("D:\\OOP Project 2024.1\\projecttest\\result.csv", startIndex, endIndex);
		
		try {
            // Specify the link (Twitter URL or any relevant page with tweets)
            String link = "https://x.com/elonmusk"; // Replace with actual URL

            // Call the method to get tweets and retweets with links
            Map<String, List<String>> tweetData = findTweetsAndRetweetsWithLinks(link);

            // Print out the collected data (tweets and retweets with links)
            System.out.println("Tweets:");
            for (String tweetLink : tweetData.get("tweets")) {
                System.out.println(tweetLink);
            }

            System.out.println("Retweets:");
            for (String retweetLink : tweetData.get("retweets")) {
                System.out.println(retweetLink);
            }
        } finally {
            // Clean up and close the driver
            driver.quit();
        }
	}
	
	public static void login() {
        driver = new ChromeDriver(); // Ensure driver is initialized
        driver.get("https://twitter.com/login");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<String> accounts = new ArrayList<>();
        accounts.add("hihihahade31600");
        accounts.add("HoangNg31600");
        accounts.add("Hngy0403");
        accounts.add("jupite97964");
        accounts.add("phan315918");
        
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[autocapitalize='sentences'][autocomplete='username'][autocorrect='on'][name='text'][spellcheck='true']")));
        usernameField.sendKeys(accounts.get(2)); // hihihahade31600 HoangNg31600 Hngy0403 jupite97964 jokererror45300 

        WebElement nextButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[role='button'][type='button'][class=\"css-175oi2r r-sdzlij r-1phboty r-rs99b7 r-lrvibr r-ywje51 r-184id4b r-13qz1uu r-2yi16 r-1qi8awa r-3pj75a r-1loqt21 r-o7ynqc r-6416eg r-1ny4l3l\"]")));
        nextButton.click();

        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[name=\"password\"][spellcheck=\"true\"]")));
        passwordField.sendKeys("stopthis"); //	stopthis hieuabcd123

        WebElement loginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[data-testid=\"LoginForm_Login_Button\"][type=\"button\"]")));
        loginButton.click();
    }
	
	// Method to process links in the given range
	public static void processLinksRange(List<String> links, int startIndex, int endIndex) {
	    for (int i = startIndex; i <= endIndex; i++) {
	        String link = links.get(i).replace("\"", "").trim(); // Remove quotes and trim spaces
	        System.out.println("Processing link: " + link);

	        try {
	            // Concatenate the URLs correctly
	            String followersUrl = link + "/followers";
	            String verifiedFollowersUrl = link + "/verified_followers";
	            String followingUrl = link + "/following";

	            // Debugging print statements to verify the URLs
	            System.out.println("Followers URL: " + followersUrl); 
	            System.out.println("Verified Followers URL: " + verifiedFollowersUrl); 
	            System.out.println("Following URL: " + followingUrl);

	            // Collect the data
	            List<String> followers = findFollowerAndFollowing(followersUrl);
	            List<String> verifiedFollowers = findFollowerAndFollowing(verifiedFollowersUrl);
	            List<String> following = findFollowerAndFollowing(followingUrl);

	            // Write the results to CSV
	            writeResultsToCSV("result.csv", link, followers, verifiedFollowers, following);
	        } catch (Exception e) {
	            // Handle any error, log it, and proceed to the next link
	            System.err.println("Error processing link: " + link);
	            e.printStackTrace();
	        } finally {
	            // Close all tabs except the original one to save memory
	            closeAllTabsExceptOriginal();
	        }
	    }
	}
	
	public static List<String[]> readCSV(String filePath) {
        List<String[]> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                rows.add(line.split(","));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rows;
    }

	public static void writeToCSV(String filePath, List<String[]> rows) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] row : rows) {
                bw.write(String.join(",", row) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	
	    public static void processKOLsData(String filePath, int startIndex, int endIndex) {
	        List<String[]> rows = readCSV(filePath); // Assuming readCSV method exists to read the CSV data.
	        File jsonFile = new File("all.json");

	        JSONArray allData = new JSONArray();

	        // Load existing JSON data if the file exists
	        if (jsonFile.exists()) {
	            try (FileReader reader = new FileReader(jsonFile)) {
	                StringBuilder content = new StringBuilder();
	                int ch;
	                while ((ch = reader.read()) != -1) {
	                    content.append((char) ch);
	                }
	                allData = new JSONArray(content.toString());
	            } catch (IOException e) {
	                System.err.println("Error reading existing JSON file: " + e.getMessage());
	            }
	        }

	        // Process rows from startIndex to endIndex
	        for (int i = startIndex; i <= endIndex && i < rows.size(); i++) {
	            try {
	                String[] row = rows.get(i);
	                String kolUsername = row[0].replace("\"", "").trim();
	                String link = "https://x.com/" + kolUsername;

	                System.out.println("Processing KOL: " + link);

	                // Simulated method to fetch tweets and retweets with links
	                Map<String, List<String>> tweetData = findTweetsAndRetweetsWithLinks(link);

	                // Simulated maps to hold data for each KOL
	                Map<String, Set<String>> retweetPostOwnerMap = new HashMap<>();
	                Map<String, Set<String>> retweetCommentsMap = new HashMap<>();
	                Map<String, Set<String>> tweetCommentsMap = new HashMap<>();

	                // Process retweets
	                List<String> retweets = tweetData.get("retweets");
	                for (int j = 0; j < Math.min(retweets.size(), 5); j++) {
	                    String retweetLink = retweets.get(j);
	                    Map<String, Set<String>> retweetComments = findTweetsAndRetweetsComment(retweetLink);

	                    retweetPostOwnerMap.put(retweetLink, retweetComments.get("PostOwner"));
	                    retweetCommentsMap.put(retweetLink, retweetComments.get("UserComments"));
	                    closeAllTabsExceptOriginal();
	                }

	                // Process tweets
	                List<String> tweets = tweetData.get("tweets");
	                for (int j = 0; j < Math.min(tweets.size(), 5); j++) {
	                    String tweetLink = tweets.get(j);
	                    Map<String, Set<String>> tweetComments = findTweetsAndRetweetsComment(tweetLink);

	                    tweetCommentsMap.put(tweetLink, tweetComments.get("UserComments"));
	                    closeAllTabsExceptOriginal();
	                }

	                // Create a JSON object for the current KOL
	                JSONObject kolData = new JSONObject();
	                kolData.put("KOL", kolUsername);
	                kolData.put("RetweetPostOwners", retweetPostOwnerMap);
	                kolData.put("RetweetComments", retweetCommentsMap);
	                kolData.put("TweetComments", tweetCommentsMap);

	                // Append the JSON object to the JSONArray
	                allData.put(kolData);

	            } catch (Exception e) {
	                System.err.println("Error processing KOL: " + rows.get(i)[0]);
	                e.printStackTrace();
	            }
	        }

	        // Write updated JSON array back to the file
	        try (FileWriter writer = new FileWriter(jsonFile)) {
	            writer.write(allData.toString(4)); // Pretty-print JSON with an indentation of 4 spaces
	            System.out.println("Data successfully written to all.json");
	        } catch (IOException e) {
	            System.err.println("Error writing to JSON file: " + e.getMessage());
	        }
	    }
	
	
	public static void writeToJSONFile(String filePath, JSONArray jsonArray) {
	    try (FileWriter fileWriter = new FileWriter(filePath)) {
	        fileWriter.write(jsonArray.toString(4)); // Pretty-print with indentation
	        System.out.println("Data successfully saved to " + filePath);
	    } catch (IOException e) {
	        System.err.println("Error writing to JSON file: " + filePath);
	        e.printStackTrace();
	    }
	}


	

	public static void appendToCSV(String filePath, List<String[]> rows) {
	    try (FileWriter writer = new FileWriter(filePath, true);
	         CSVWriter csvWriter = new CSVWriter(writer)) {

	        for (String[] row : rows) {
	            csvWriter.writeNext(row);
	        }
	    } catch (IOException e) {
	        System.err.println("Error appending to CSV file: " + filePath);
	        e.printStackTrace();
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
            // Extract username from the link (e.g., "https://x.com/Bitcoin" -> "Bitcoin")
            String username = link.substring(link.lastIndexOf("/") + 1);

            // Prepare the CSV row with the required columns
            String row = String.format("\"%s\",\"%s\",\"%s\",\"%s\"\n",
                    username, // KOLs column now contains just the username
                    String.join(", ", followers),
                    String.join(", ", verifiedFollowers),
                    String.join(", ", following));

            // Write the row to the CSV file
            bw.write(row);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	
	public static Map<String, Set<String>> findTweetsAndRetweetsComment(String link) {
	    openNewWindow(link);

	    Set<String> userNamesSet = new HashSet<>();
	    String postOwner = null; // Variable to store the post owner's username

	    JavascriptExecutor js = (JavascriptExecutor) driver;
	    js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
	    try {
	        Thread.sleep(2000); // Wait for new tweets to load (adjust as needed)
	    } catch (InterruptedException e) {
	        Thread.currentThread().interrupt(); // Restore interrupted status
	    }

	    for (int i = 0; i < 3; i++) {
	        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-testid='User-Name']")));

	        List<WebElement> usersComment = driver.findElements(By.cssSelector("[data-testid='User-Name']"));

	        for (WebElement user : usersComment) {
	            try {
	                // Debugging: Print user element to inspect structure
	                System.out.println(user.getAttribute("innerHTML"));

	                // Wait for the <a> tag inside the user element to be visible
	                WebElement linkElement = user.findElement(By.cssSelector("a[href*='/']"));

	                // Ensure that the link element is visible
	                wait.until(ExpectedConditions.visibilityOf(linkElement));

	                String linkUser = linkElement.getAttribute("href");
	                String username = linkUser.substring(linkUser.lastIndexOf("/") + 1);

	                // If it's the first loop (i == 0) and postOwner is not set, save the post owner's username
	                if (i == 0 && postOwner == null) {
	                    postOwner = username;
	                    System.out.println("Post owner: " + postOwner);
	                }

	                // Only add the username to the set if it's not the post owner
	                if (!username.equals(postOwner)) {
	                    userNamesSet.add(username);  // Add to set to ensure uniqueness
	                }

	            } catch (Exception e) {
	                System.out.println("Error retrieving username: " + e.getMessage());
	            }
	        }

	        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
	        try {
	            Thread.sleep(2000); // Wait for new tweets to load
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt(); // Restore interrupted status
	        }
	    }

	    // Create a Map to return both postOwner and user comments
	    Map<String, Set<String>> result = new HashMap<>();
	    
	    
	    
	    // Add post owner first
	    if (postOwner != null) {
	        Set<String> postOwnerSet = new HashSet<>();
	        postOwnerSet.add(postOwner);
	        result.put("PostOwner", postOwnerSet); // Store post owner in the map
	    }

	    // Add all user comments
	    result.put("UserComments", userNamesSet);

	    // Return the map with post owner and user comments
	    return result;
	}






	public static Map<String, List<String>> findTweetsAndRetweetsWithLinks(String link) {
	    openNewWindow(link);
	    List<String> tweetLinks = new ArrayList<>();
	    List<String> retweetLinks = new ArrayList<>();
	    JavascriptExecutor js = (JavascriptExecutor) driver;

	    // Extract the username from the provided link (e.g., https://x.com/elonmusk -> elonmusk)
	    String username = link.substring(link.lastIndexOf("/") + 1);

	    // Initial scroll to load some tweets
	    js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
	    try {
	        Thread.sleep(2000); // Wait for new tweets to load (adjust as needed)
	    } catch (InterruptedException e) {
	        Thread.currentThread().interrupt(); // Restore interrupted status
	    }

	    // Loop to keep scrolling and collecting tweets
	    for (int i = 0; i < 3; i++) { // Adjust the number of iterations to load more tweets
	        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-testid='tweet']")));

	        // Collect current tweets
	        List<WebElement> tweetElements = driver.findElements(By.cssSelector("[data-testid='tweet']"));

	        for (WebElement tweetElement : tweetElements) {
	            // Extract the link from the tweet or retweet
	            try {
	                WebElement linkElement = tweetElement.findElement(By.xpath(".//a[contains(@href, '/status/')]"));
	                String postLink = linkElement.getAttribute("href");

	                // Check if the post link contains the username (indicating it's from the correct user)
	                if (postLink.contains(username)) {
	                    // If the link contains the username, it's a tweet
	                    tweetLinks.add(postLink);
	                } else {
	                    // If the link doesn't contain the username, it's a retweet
	                    retweetLinks.add(postLink);
	                }
	            } catch (NoSuchElementException e) {
	                // If no link is found, skip this element
	                continue;
	            }
	        }

	        // Scroll down to load more tweets
	        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
	        try {
	            Thread.sleep(2000); // Wait for new tweets to load
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt(); // Restore interrupted status
	        }
	    }

	    // Print collected tweet links count
	    System.out.println("Total tweet links: " + tweetLinks.size());
	    System.out.println("Total retweet links: " + retweetLinks.size());

	    // Return a map containing tweet and retweet links
	    Map<String, List<String>> tweetDataWithLinks = new HashMap<>();
	    tweetDataWithLinks.put("tweets", tweetLinks);
	    tweetDataWithLinks.put("retweets", retweetLinks);

	    return tweetDataWithLinks;
	}


	
	
	
	public static List<String> findFollowerAndFollowing(String link) {
		openNewWindow(link);
	    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-testid='cellInnerDiv']")));
	    List<WebElement> followers = driver.findElements(By.cssSelector("[data-testid='cellInnerDiv']"));
	    System.out.println("Total followers found: " + followers.size()); // Print total count

	    List<String> usernames = new ArrayList<>(); // List to store usernames

	    for (int i = 0; i < followers.size(); i++) {
	        WebElement user = followers.get(i);

	        try {
	            // Extract the href from the user link
	            String href = user.findElement(By.tagName("a")).getAttribute("href");

	            // Extract the username from the href
	            String username = href.substring(href.lastIndexOf('/') + 1); // Get everything after the last '/'
	            usernames.add(username); // Add username to the list
	        } catch (NoSuchElementException e) {
	            System.out.println("Element not found for user at index: " + i);
	        } catch (Exception e) {
	            System.out.println("An error occurred: " + e.getMessage());
	        }
	    }
	    return usernames; // Return the list of usernames
	}


	
	


	public static void openNewWindow(String url) {
	    ((JavascriptExecutor) driver).executeScript("window.open('" + url + "','_blank');");
	    String originalTab = driver.getWindowHandle();
	    
	    for (String windowHandle : driver.getWindowHandles()) {
	        if (!windowHandle.equals(originalTab)) {
	            driver.switchTo().window(windowHandle);
	            break;
	        }
	    }

	    Set<Cookie> cookies = driver.manage().getCookies();
	    for (Cookie cookie : cookies) {
	        driver.manage().addCookie(cookie);
	    }

	    driver.navigate().to(url);
	}

	
	public static void closeAllTabsExceptOriginal() {
        String originalTab = driver.getWindowHandle();
        Set<String> allTabs = driver.getWindowHandles();
        
        for (String tab : allTabs) {
            if (!tab.equals(originalTab)) {
                driver.switchTo().window(tab);
                driver.close(); 
            }
        }
        driver.switchTo().window(originalTab);
    }
	

}
