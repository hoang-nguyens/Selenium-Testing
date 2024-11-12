package seleniumsecond;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
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

public class LoadUserInfor {
	public static WebDriver driver;  
    public static WebDriverWait wait;

	public static void main(String[] args) {
		login();
		
		//List<String> links = readLinksFromCSV("D:\\OOP Project 2024.1\\projecttest\\bitcoinUsersLink.csv");

        // Process a specific range of links (example: index 0 to 9)
        int startIndex = 2;
        int endIndex = 10;

        // Process the links range
        //processLinksRange(links, startIndex, endIndex);
        processKOLsData("D:\\OOP Project 2024.1\\projecttest\\result.csv", 2, 2);
        
     
		
//		
//		String newTabUrl = "https://x.com/Bitcoin_Buddah";
//		String newTabUrl2 = "https://x.com/Bitcoin/verified_followers";
//		String newTabUrl3 = "https://x.com/Bitcoin/followers";
//		String newTabUrl4 = "https://x.com/Bitcoin/following";
//		
//		openNewWindow(newTabUrl);
//		openNewWindow(newTabUrl2);
//		
//		try {
//            Thread.sleep(5000);  
//        } catch (InterruptedException e) {
//            e.printStackTrace();  
//        }
//		     
//		closeAllTabsExceptOriginal();
		
		
//		findFollowerAndFollowing();

//		Map<String, List<String>> tweetDataWithLinks = findTweetsAndRetweetsWithLinks();
//
//	    // Get the list of tweet and retweet links
//	    List<String> tweets = tweetDataWithLinks.get("tweets");
//	    List<String> retweets = tweetDataWithLinks.get("retweets");

	    // Print the tweet and retweet links
//	    System.out.println("Tweet Links:");
//	    for (String tweetLink : tweets) {
//	        System.out.println(tweetLink);
//	    }
//
//	    System.out.println("\nRetweet Links:");
//	    for (String retweetLink : retweets) {
//	        System.out.println(retweetLink);
//	    }
	    
//	    String tweet1 = tweets.get(0);
//	    String tweet1 = "https://x.com/Bitcoin_Buddah/status/1844308200369135683"; // copy here for testing
//	    
//	    
//	    Map<String, Set<String>> result = findTweetsAndRetweetsComment(tweet1);
//
//	    // Retrieve and print the post owner
//	    Set<String> postOwner = result.get("PostOwner");
//	    if (postOwner != null && !postOwner.isEmpty()) {
//	        System.out.println("Post Owner: " + postOwner);
//	    } else {
//	        System.out.println("No post owner found.");
//	    }
//
//	    // Retrieve and print the user comments
//	    Set<String> userComments = result.get("UserComments");
//	    if (userComments != null && !userComments.isEmpty()) {
//	        System.out.println("User Comments: ");
//	        for (String user : userComments) {
//	            System.out.println(user);
//	        }
//	    } else {
//	        System.out.println("No user comments found.");
//	    }
	    
	    
	}
	
	public static void login() {
        driver = new ChromeDriver(); // Ensure driver is initialized
        driver.get("https://twitter.com/login");
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[autocapitalize='sentences'][autocomplete='username'][autocorrect='on'][name='text'][spellcheck='true']")));
        usernameField.sendKeys("hihihahade31600");

        WebElement nextButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[role='button'][type='button'][class=\"css-175oi2r r-sdzlij r-1phboty r-rs99b7 r-lrvibr r-ywje51 r-184id4b r-13qz1uu r-2yi16 r-1qi8awa r-3pj75a r-1loqt21 r-o7ynqc r-6416eg r-1ny4l3l\"]")));
        nextButton.click();

        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[name=\"password\"][spellcheck=\"true\"]")));
        passwordField.sendKeys("stopthis");

        WebElement loginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[data-testid=\"LoginForm_Login_Button\"][type=\"button\"]")));
        loginButton.click();
    }
	
	// Method to process links in the given range
	public static void processLinksRange(List<String> links, int startIndex, int endIndex) {
	    for (int i = startIndex; i <= endIndex; i++) {
	        String link = links.get(i).replace("\"", "").trim(); // Remove quotes and trim spaces
	        System.out.println("Processing link: " + link);

	        // Concatenate the URLs correctly
	        String followersUrl = link + "/followers";
	        String verifiedFollowersUrl = link + "/verified_followers";
	        String followingUrl = link + "/following";

	        // Debugging print statement to verify the URLs
	        System.out.println("Followers URL: " + followersUrl); // Ensure this is correct
	        System.out.println("Verified Followers URL: " + verifiedFollowersUrl); // Ensure this is correct
	        System.out.println("Following URL: " + followingUrl); // Ensure this is correct

	        // Collect the data
	        List<String> followers = findFollowerAndFollowing(followersUrl);
	        List<String> verifiedFollowers = findFollowerAndFollowing(verifiedFollowersUrl);
	        List<String> following = findFollowerAndFollowing(followingUrl);

	        // Write the results to CSV
	        writeResultsToCSV("result.csv", link, followers, verifiedFollowers, following);

	        // Close all tabs except the original one to save memory
	        closeAllTabsExceptOriginal();
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
	    List<String[]> rows = readCSV(filePath);
	    List<String[]> updatedRows = new ArrayList<>();

	    // Update header with new columns for post-owner and comments
	    String[] header = rows.get(0);
	    header = Arrays.copyOf(header, header.length + 3);
	    header[header.length - 3] = "Retweet Post Owners";
	    header[header.length - 2] = "Retweet Comments";
	    header[header.length - 1] = "Tweet Comments";
	    updatedRows.add(header);

	    // Iterate through the rows in the specified range
	    for (int i = startIndex; i <= endIndex && i < rows.size(); i++) {
	        String[] row = rows.get(i);
	        String kolUsername = row[0].replace("\"", "").trim(); // Get the username of the KOL
	        String link = "https://x.com/" + kolUsername;  // Construct the KOL URL
	        System.out.println("Processing KOL: " + link);

	        // Get tweets and retweets with links
	        Map<String, List<String>> tweetData = findTweetsAndRetweetsWithLinks(link);

	        // Maps to store post-owner info and comments
	        Map<String, Set<String>> retweetPostOwnerMap = new HashMap<>();
	        Map<String, Set<String>> tweetCommentsMap = new HashMap<>();
	        Map<String, Set<String>> retweetCommentsMap = new HashMap<>();

	        // Iterate through retweets and find comments
	        for (String retweetLink : tweetData.get("retweets")) {
	            // Get the post owner and comments for each retweet
	            Map<String, Set<String>> retweetComments = findTweetsAndRetweetsComment(retweetLink);
	            retweetPostOwnerMap.put(retweetLink, retweetComments.get("PostOwner"));
	            retweetCommentsMap.put(retweetLink, retweetComments.get("UserComments"));
	            closeAllTabsExceptOriginal();
	        }

	        // Iterate through tweets and find comments
	        for (String tweetLink : tweetData.get("tweets")) {
	            // Get the post owner and comments for each tweet
	            Map<String, Set<String>> tweetComments = findTweetsAndRetweetsComment(tweetLink);
	            tweetCommentsMap.put(tweetLink, tweetComments.get("UserComments"));
	            closeAllTabsExceptOriginal();
	        }

	        // Create a new list to hold the row with additional columns
	        List<String> newRow = new ArrayList<>(Arrays.asList(row));

	        // Add new columns to the row
	        newRow.add(retweetPostOwnerMap.toString()); // 5th column: Retweet post owners map
	        newRow.add(retweetCommentsMap.toString()); // 6th column: Retweet comments map
	        newRow.add(tweetCommentsMap.toString()); // 7th column: Tweet comments map

	        // Add the updated row to the list
	        updatedRows.add(newRow.toArray(new String[0])); // Convert list back to array before adding
	    }

	    // Write the updated rows (with new columns) to the CSV file
	    writeToCSV(filePath, updatedRows);
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

	    for (int i = 0; i < 5; i++) {
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
	    
	    Set<String> finalUserNamesSet = new LinkedHashSet<>();
	    
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

	    // Initial scroll to load some tweets
	    js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
	    try {
	        Thread.sleep(2000); // Wait for new tweets to load (adjust as needed)
	    } catch (InterruptedException e) {
	        Thread.currentThread().interrupt(); // Restore interrupted status
	    }

	    // Loop to keep scrolling and collecting tweets
	    for (int i = 0; i < 5; i++) { // Adjust the number of iterations to load more tweets
	        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-testid='tweet']")));

	        // Collect current tweets
	        List<WebElement> tweetElements = driver.findElements(By.cssSelector("[data-testid='tweet']"));

	        for (WebElement tweetElement : tweetElements) {
	            boolean isRetweet = false;

	            // Check for retweet indicator
	            try {
	                WebElement retweetIndicator = tweetElement.findElement(By.xpath(".//*[contains(text(), 'reposted') or contains(@aria-label, 'reposted')]"));
	                if (retweetIndicator.isDisplayed()) {
	                    isRetweet = true;
	                }
	            } catch (NoSuchElementException e) {
	                isRetweet = false; 
	            } catch (Exception e) {
	                // Log error if needed
	            }

	            // Extract the link from the tweet or retweet
	            try {
	                WebElement linkElement = tweetElement.findElement(By.xpath(".//a[contains(@href, '/status/')]"));
	                String postLink = linkElement.getAttribute("href");

	                // Add the post link to the appropriate list
	                if (isRetweet) {
	                    retweetLinks.add(postLink);
	                } else {
	                    tweetLinks.add(postLink);
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
