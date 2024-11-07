package seleniumsecond;

import java.time.Duration;
import java.util.ArrayList;
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
	private static WebDriver driver;  
    private static WebDriverWait wait;

	public static void main(String[] args) {
		
	
		driver = new ChromeDriver();
		
		driver.get("https://twitter.com/login");
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		
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
		
		
		String newTabUrl = "https://x.com/Bitcoin_Buddah";
		String newTabUrl2 = "https://x.com/Bitcoin/verified_followers";
		String newTabUrl3 = "https://x.com/Bitcoin/followers";
		String newTabUrl4 = "https://x.com/Bitcoin/following";
		
		openNewWindow(newTabUrl);
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
	    String tweet1 = "https://x.com/Bitcoin_Buddah/status/1844308200369135683"; // copy here for testing
	    
	    
	    Map<String, Set<String>> result = findTweetsAndRetweetsComment(tweet1);

	    // Retrieve and print the post owner
	    Set<String> postOwner = result.get("PostOwner");
	    if (postOwner != null && !postOwner.isEmpty()) {
	        System.out.println("Post Owner: " + postOwner);
	    } else {
	        System.out.println("No post owner found.");
	    }

	    // Retrieve and print the user comments
	    Set<String> userComments = result.get("UserComments");
	    if (userComments != null && !userComments.isEmpty()) {
	        System.out.println("User Comments: ");
	        for (String user : userComments) {
	            System.out.println(user);
	        }
	    } else {
	        System.out.println("No user comments found.");
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






	public static Map<String, List<String>> findTweetsAndRetweetsWithLinks() {
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

	
	
	
	public static void findFollowerAndFollowing() {
	    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-testid='cellInnerDiv']")));
	    List<WebElement> followers = driver.findElements(By.cssSelector("[data-testid='cellInnerDiv']"));
	    System.out.println("Total followers found: " + followers.size()); // Print total count

	    for (int i = 0; i < followers.size(); i++) {
	        WebElement user = followers.get(i);
	        
	        try {
	            // Print user element HTML for debugging
	            //System.out.println("User element HTML: " + user.getAttribute("outerHTML"));

	            // Extract the href from the user link
	            String href = user.findElement(By.tagName("a")).getAttribute("href");

	            // Extract the username from the href
	            String username = href.substring(href.lastIndexOf('/') + 1); // Get everything after the last '/'
	            System.out.println("Follower Username: " + username);
	            
	        } catch (NoSuchElementException e) {
	            System.out.println("Element not found for user at index: " + i);
	        } catch (Exception e) {
	            System.out.println("An error occurred: " + e.getMessage());
	        }
	    }
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
