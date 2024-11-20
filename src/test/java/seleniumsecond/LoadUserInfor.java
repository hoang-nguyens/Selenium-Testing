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
		
		
		int startIndex = 638;
		int batchSize = 3; // Number of KOLs to process per iteration
		int totalIterations = 50;
		int accountsCount = 5; // Total accounts available for login

		for (int i = 0; i < totalIterations; i++) {
		    int accountIndex = i % accountsCount; // Cycle through accounts
		    try {
		        login(accountIndex); // Login with the current account
		        processKOLsData("D:\\OOP Project 2024.1\\projecttest\\result.csv", startIndex, startIndex + batchSize - 1);
		        startIndex += batchSize; // Move to the next batch of KOLs
		        System.out.println("Processed up to startIndex: " + startIndex);
		    } catch (Exception e) {
		        System.err.println("Error during iteration " + i + ": " + e.getMessage());
		        e.printStackTrace();
		    } finally {
		        // Ensure the driver is closed after each iteration to free resources
		        if (driver != null) {
		            try {
		                driver.quit();
		            } catch (Exception e) {
		                System.err.println("Error closing driver: " + e.getMessage());
		            }
		        }
		    }
		}

	}
	
	public static void login(int i) {
        driver = new ChromeDriver(); // Ensure driver is initialized
        driver.get("https://twitter.com/login");
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        List<String> accounts = new ArrayList<>();
        
		accounts.add("xxxxxxxxx");
        accounts.add("xxxxxxxxx");
		accounts.add("xxxxxxxxx");
		accounts.add("xxxxxxxxx");
		accounts.add("xxxxxxxxx");
        
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[autocapitalize='sentences'][autocomplete='username'][autocorrect='on'][name='text'][spellcheck='true']")));
        usernameField.sendKeys(accounts.get(i)); // hihihahade31600 HoangNg31600 Hngy0403 jupite97964 jokererror45300 

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

	    JSONObject allData = new JSONObject();

	    // Load or initialize JSON data
	    if (jsonFile.exists()) {
	        try (FileReader reader = new FileReader(jsonFile)) {
	            StringBuilder content = new StringBuilder();
	            int ch;
	            while ((ch = reader.read()) != -1) {
	                content.append((char) ch);
	            }

	            // Parse existing JSON data if not empty
	            if (content.length() > 0) {
	                allData = new JSONObject(content.toString());
	            }
	        } catch (Exception e) {
	            System.err.println("Error reading or parsing all.json: " + e.getMessage());
	            allData = new JSONObject(); // Initialize as empty JSONObject if parsing fails
	        }
	    } else {
	        // If file doesn't exist, start with an empty JSON object
	        allData = new JSONObject();
	    }

	    // Process rows from startIndex to endIndex
	    for (int i = startIndex; i <= endIndex && i < rows.size(); i++) {
	        try {
	            String[] row = rows.get(i);
	            String kolUsername = row[0].replace("\"", "").trim();
	            System.out.println("Processing KOL: " + kolUsername);

	            // Fetch tweets, retweets, and followers
	            Map<String, Object> tweetData = findTweetsAndRetweetsWithLinks("https://x.com/" + kolUsername);

	            // Extract follower count as a string
	            String followers = (String) tweetData.get("followers");

	            // Maps to store processed data for this KOL
	            Map<String, String> repostOwnerMap = new HashMap<>();
	            Map<String, List<String>> tweetCommentsMap = new HashMap<>();
	            Map<String, List<String>> retweetCommentsMap = new HashMap<>();

	            // Process retweets
	            List<String> retweets = (List<String>) tweetData.get("retweets");
	            for (int j = 0; j < Math.min(retweets.size(), 5); j++) {
	                String retweetLink = retweets.get(j);
	                String retweetId = extractPostId(retweetLink); // Extract post ID
	                Map<String, Set<String>> retweetComments = findTweetsAndRetweetsComment(retweetLink);

	                // Only add post owners who are not the KOL
	                Set<String> postOwners = retweetComments.get("PostOwner");
	                for (String owner : postOwners) {
	                    if (!owner.equals(kolUsername)) {
	                        repostOwnerMap.put(retweetId, owner);
	                    }
	                }

	                // Add comments for this retweet
	                Set<String> comments = retweetComments.get("UserComments");
	                retweetCommentsMap.put(retweetId, new ArrayList<>(comments));
	            }

	            // Process tweets
	            List<String> tweets = (List<String>) tweetData.get("tweets");
	            for (int j = 0; j < Math.min(tweets.size(), 5); j++) {
	                String tweetLink = tweets.get(j);
	                String tweetId = extractPostId(tweetLink); // Extract post ID
	                Map<String, Set<String>> tweetComments = findTweetsAndRetweetsComment(tweetLink);

	                // Add comments for this tweet
	                Set<String> comments = tweetComments.get("UserComments");
	                tweetCommentsMap.put(tweetId, new ArrayList<>(comments));
	            }

	            // Create JSON object for this KOL
	            JSONObject kolData = new JSONObject();
	            kolData.put("followers", followers); // Add follower count
	            kolData.put("repostOwner", repostOwnerMap);
	            kolData.put("tweetComments", tweetCommentsMap);
	            kolData.put("retweetComments", retweetCommentsMap);

	            // Add to main JSON object
	            allData.put(kolUsername, kolData);

	        } catch (Exception e) {
	            System.err.println("Error processing KOL at row " + i + ": " + e.getMessage());
	            e.printStackTrace();
	        }
	    }

	    // Write updated JSON data to the file
	    try (FileWriter writer = new FileWriter(jsonFile)) {
	        writer.write(allData.toString(4)); // Pretty-print JSON with 4-space indentation
	        System.out.println("Data successfully written to all.json");
	    } catch (IOException e) {
	        System.err.println("Error writing to all.json: " + e.getMessage());
	    }
	}



	// Helper method to extract the post ID from a link
	public static String extractPostId(String link) {
	    // Assuming the ID is the last part of the URL after the last slash (/)
	    return link.substring(link.lastIndexOf("/") + 1);
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
	    closeAllTabsExceptOriginal();

	    // Return the map with post owner and user comments
	    return result;
	}



    public static int parseNumber(String numStr) {
        if (numStr == null || numStr.isEmpty()) {
            return 0;
        }

        // Remove commas
        numStr = numStr.replace(",", "");

        // Check for K and M suffix
        if (numStr.endsWith("K")) {
            return (int) (Double.parseDouble(numStr.replace("K", "")) * 1_000);
        } else if (numStr.endsWith("M")) {
            return (int) (Double.parseDouble(numStr.replace("M", "")) * 1_000_000);
        } else {
            // No suffix, parse directly
            return Integer.parseInt(numStr);
        }
    }



    public static Map<String, Object> findTweetsAndRetweetsWithLinks(String link) {
        openNewWindow(link);

        // Extract follower count as a string
        String followerCountStr = "";
        WebElement followerContainer = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(., 'Followers')]")));
        WebElement followerCountElement = followerContainer.findElement(By.xpath(".//span[1]"));
        followerCountStr = followerCountElement.getText();

        // Convert follower count string to a numeric string
        String numericFollowerCountStr = String.valueOf(parseNumber(followerCountStr));

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

        // Print collected tweet links count and follower count
        System.out.println("Total tweet links: " + tweetLinks.size());
        System.out.println("Total retweet links: " + retweetLinks.size());
        System.out.println("Follower count: " + numericFollowerCountStr);

        // Return a map containing tweet links, retweet links, and follower count
        Map<String, Object> tweetDataWithLinks = new HashMap<>();
        tweetDataWithLinks.put("tweets", tweetLinks);
        tweetDataWithLinks.put("retweets", retweetLinks);
        tweetDataWithLinks.put("followers", numericFollowerCountStr); // Store as a plain string

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
