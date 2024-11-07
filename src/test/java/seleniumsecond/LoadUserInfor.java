package seleniumsecond;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
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

		Map<String, List<String>> tweetDataWithLinks = findTweetsAndRetweetsWithLinks();

	    // Get the list of tweet and retweet links
	    List<String> tweets = tweetDataWithLinks.get("tweets");
	    List<String> retweets = tweetDataWithLinks.get("retweets");

	    // Print the tweet and retweet links
	    System.out.println("Tweet Links:");
	    for (String tweetLink : tweets) {
	        System.out.println(tweetLink);
	    }

	    System.out.println("\nRetweet Links:");
	    for (String retweetLink : retweets) {
	        System.out.println(retweetLink);
	    }
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
