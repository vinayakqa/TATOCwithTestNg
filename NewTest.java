import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class NewTest 
{
	WebDriver driver;

	@BeforeTest
	public void beforeTest() throws InterruptedException{
		driver=new ChromeDriver();
		Thread.sleep(1000);
	}

	@AfterTest
	public void afterTest() {
		driver.close();
	}
	@Test
	public void step1_openUrl() throws InterruptedException{
		driver.manage().window().maximize();
		driver.get("http://10.0.1.86/tatoc");
		Assert.assertEquals("Welcome - T.A.T.O.C", driver.getTitle());
		Thread.sleep(1000);

	}
	@Test(dependsOnMethods= {"step1_openUrl"})
	public void step2_selectBasicCourse() throws InterruptedException  {
		List<WebElement> links=driver.findElements(By.className("page"));
		Assert.assertEquals(driver.findElement(By.className("page")).isDisplayed(), true);
		links.get(0).findElement(By.cssSelector("a")).click();
		Thread.sleep(1000);
	}
	@Test(dependsOnMethods= {"step2_selectBasicCourse"})
	public void step3_selectGreenBox() throws InterruptedException {
		Assert.assertEquals(driver.findElement(By.className("greenbox")).isDisplayed(), true);
		driver.findElement(By.className("greenbox")).click();
		Thread.sleep(1000);
	}
	@Test(dependsOnMethods= {"step3_selectGreenBox"})
	public void step4_matchColorOfBothBox() throws InterruptedException {
		driver.switchTo().frame("main");
		Assert.assertEquals(driver.findElement(By.id("answer")).isDisplayed(), true);
		String box1=driver.findElement(By.id("answer")).getAttribute("class");
		//System.out.println(box1);
		Assert.assertNotNull(box1);
		boolean check=true;
		while(check) {
			driver.switchTo().frame("child");
			Assert.assertEquals(driver.findElement(By.id("answer")).isDisplayed(), true);
			String box2=driver.findElement(By.id("answer")).getAttribute("class");
			Assert.assertNotNull(box2);
			if(box1.equals(box2)) {
				check=false;
				driver.switchTo().parentFrame();
				WebElement link=driver.findElement(By.partialLinkText("Proceed"));
				Assert.assertEquals(driver.findElement(By.partialLinkText("Proceed")).isDisplayed(), true);
				link.click();

			}else
			{
				driver.switchTo().parentFrame();
				WebElement link=driver.findElement(By.partialLinkText("Repaint"));
				Assert.assertEquals(driver.findElement(By.partialLinkText("Repain")).isDisplayed(), true);
				link.click();

			}
			Thread.sleep(100);
		}
	}
	@Test(dependsOnMethods= {"step4_matchColorOfBothBox"})
	public void step5_DragElementToCorrectPosition() {
		WebElement dragElement=driver.findElement(By.className("ui-draggable"));
		Assert.assertEquals(driver.findElement(By.className("ui-draggable")).isDisplayed(), true);
		Actions move=new Actions(driver);
		move.dragAndDropBy(dragElement, 20,-76).build().perform();
		driver.findElement(By.linkText("Proceed")).click();
		Assert.assertEquals(driver.findElement(By.linkText("Proceed")).isDisplayed(),true); 
	}
	@Test(dependsOnMethods= {"step5_DragElementToCorrectPosition"})
	public void step6_launchPopupWindow() {
		driver.findElement(By.linkText("Launch Popup Window")).click();
		Assert.assertEquals(driver.findElement(By.linkText("Launch Popup Window")).isDisplayed(),true);
	}
	@Test(dependsOnMethods= {"step6_launchPopupWindow"})
	public void step7_handlingNewPopupWindow() {
     String mainWindow=driver.getWindowHandle();
     Assert.assertNotNull(mainWindow);
     Set<String> listOfNewWindows=driver.getWindowHandles();
     Iterator<String> itr=listOfNewWindows.iterator();
     while(itr.hasNext()) {
    	 String childWindow=itr.next();
    	 if(!mainWindow.equalsIgnoreCase(childWindow)) {
    		 driver.switchTo().window(childWindow);
    		 driver.findElement(By.id("name")).sendKeys("Test");
    		 Assert.assertEquals(driver.findElement(By.id("name")).isDisplayed(), true);
    		 driver.findElement(By.id("submit")).click();
       	 }
    	 
      }
    driver.switchTo().window(mainWindow);
	}
    @Test(dependsOnMethods= {"step7_handlingNewPopupWindow"})
    public void step8_afterhandlingPopupWindow() {
     driver.findElement(By.linkText("Proceed")).click();
     String expectedUrl = ("http://10.0.1.86/tatoc/basic/cookie");
     Assert.assertEquals(expectedUrl, driver.getCurrentUrl());
    }
    @Test(dependsOnMethods = {"step8_afterhandlingPopupWindow"})
    public void step9_generatingToken() {
    	driver.findElement(By.partialLinkText("Generate Token")).click();
    	Assert.assertEquals(driver.findElement(By.partialLinkText("Generate Token")).isDisplayed(), true);
    }
    @Test(dependsOnMethods= {"step9_generatingToken"})
    public void step10_generateCookie() throws InterruptedException {
    	String token=driver.findElement(By.id("token")).getText();
    	Assert.assertNotNull(token);
    	String[] tokenValue=token.split(" ");
    	Cookie myCookie=new Cookie("Token",tokenValue[1]);
    	driver.manage().addCookie(myCookie);
    	driver.findElement(By.linkText("Proceed")).click();
    	Thread.sleep(1000);;
   }
}
