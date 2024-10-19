package seleniumfirst;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class RunSelenium {
	public static void main(String[] args) {

		WebDriver driver = new ChromeDriver();
		
		driver.get("https://twitter.com/login");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		
		WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.cssSelector("[autocapitalize='sentences'][autocomplete='username'][autocorrect='on'][name='text'][spellcheck='true']")));
		usernameField.sendKeys("hihihahade31600");
		
		WebElement nextButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.cssSelector("[role='button'][type='button'][class=\"css-175oi2r r-sdzlij r-1phboty r-rs99b7 r-lrvibr r-ywje51 r-184id4b r-13qz1uu r-2yi16 r-1qi8awa r-3pj75a r-1loqt21 r-o7ynqc r-6416eg r-1ny4l3l\"]")));
		nextButton.click();
		
		WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.cssSelector("[name=\"password\"][spellcheck=\"true\"]")));
		passwordField.sendKeys("hoangkhochuhuhihihaha");
		
		
		WebElement loginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.cssSelector("[data-testid=\"LoginForm_Login_Button\"][type=\"button\"]")));
		loginButton.click();
		
		WebElement searchField = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.cssSelector("[placeholder='Search']")));
	    searchField.sendKeys("#blockchain");
	      
	    Actions act = new Actions(driver);
	    act.keyDown(Keys.ENTER).keyUp(Keys.ENTER).perform();
	    
	    WebElement people = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("a[href='/search?q=%23blockchain&src=typed_query&f=user']")
            ));
	    people.click();
	    
	    try {
            Thread.sleep(5000);  // Pause for 5000 milliseconds (5 seconds) Pause more for page loading and gain more data
        } catch (InterruptedException e) {
            e.printStackTrace();  // Handle the interrupted exception
        }
	    
	    List<WebElement> users = driver.findElements(By.xpath("//div[@data-testid='cellInnerDiv']"));
	    
	    int size = users.size();
	    System.out.println(size);
	    
	    for (WebElement user: users) {
	    	String profileLink = user.findElement(By.tagName("a")).getAttribute("href");
	    	System.out.println(profileLink);
	    	
	    	String displayname = user.findElement(By.xpath(".//div[contains(@dir, 'ltr')]/span")).getText();
	    	System.out.println(displayname + '\n');
	    	
	    	String username = user.findElement(By.xpath(".//a[contains(@href, '/')]//span[contains(text(), '@')]")).getText();
	    	System.out.println(username + '\n');
	    }
	    
	    
	}

}
