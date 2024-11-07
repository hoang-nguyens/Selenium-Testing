package selenium1;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.Keys;
import java.util.*;

public class RunSelenium {
	public static void main(String[] args) {

		WebDriver driver = new ChromeDriver();

		driver.get("https://twitter.com/login");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(
				"[autocapitalize='sentences'][autocomplete='username'][autocorrect='on'][name='text'][spellcheck='true']")));
		usernameField.sendKeys("huythetime1@gmail.com");

		WebElement nextButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(
				"[role='button'][type='button'][class=\"css-175oi2r r-sdzlij r-1phboty r-rs99b7 r-lrvibr r-ywje51 r-184id4b r-13qz1uu r-2yi16 r-1qi8awa r-3pj75a r-1loqt21 r-o7ynqc r-6416eg r-1ny4l3l\"]")));
		nextButton.click();

		WebElement nameField = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[inputmode='text']")));

		nameField.sendKeys("@HuyQuan82209440");
		WebElement nextField = wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.cssSelector("[data-testid='ocfEnterTextNextButton']")));

		nextField.click();

		WebElement passwordField = wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.cssSelector("[name=\"password\"][spellcheck=\"true\"]")));

		passwordField.sendKeys("nostand2");

		WebElement loginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.cssSelector("[data-testid=\"LoginForm_Login_Button\"][type=\"button\"]")));
		loginButton.click();

		WebElement searchEngine = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[placeholder=Search]")));
		searchEngine.sendKeys("Blockchain");
		searchEngine.submit();
		WebElement people = wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("People")));
		people.click();
		WebElement test = wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.cssSelector("[data-testid='UserCell'][role='button']")));
		List<WebElement> anchors = driver.findElements(By.cssSelector("[data-testid='UserCell'][role='button']"));
		List<String> str = new ArrayList<String>();

		
		
		Actions act=new Actions(driver);
		
		
			
			for (int i = 0; i < 30; i++) {

					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					}
				
				anchors = driver.findElements(By.cssSelector("[data-testid='UserCell'][role='button']"));
				if(i>anchors.size()-1) test=anchors.get(anchors.size()-1);
				else
					test = anchors.get(i);
					test.sendKeys(Keys.ARROW_DOWN);	
					String s = test.findElement(By.tagName("a")).getAttribute("href");
				
					
					str.add(s);
					System.out.println(s);
					
				
				anchors = driver.findElements(By.cssSelector("[data-testid='UserCell'][role='button']"));
			
		}

	
}
}