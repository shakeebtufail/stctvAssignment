package com.automation.utils;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

import com.automation.common.Log4jUtil;
import com.automation.common.SetUpTest;
import com.automation.controllers.DriverManager;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class CommonUtilities extends SetUpTest {

	protected WebDriver driverInstance = DriverManager.getDriver();
	Logger log = Log4jUtil.loadLogger(CommonUtilities.class);
	private String scenario;
	private ExtentTest extLogger;
	private String directory;
private String strImage = "";


public CommonUtilities(String scenario) {
	try {
		
		this.scenario = scenario;
		this.extLogger = (ExtentTest) Reporter.getCurrentTestResult().getTestContext()
				.getAttribute("extLogger" + Thread.currentThread().hashCode());
		
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	public WebDriverWait driverWait() {
		WebDriverWait explicitWait = new WebDriverWait(driverInstance, 10);
		return explicitWait;
	}

	public WebDriverWait driverWaitWithTimeOut(int timeOut) {
		WebDriverWait explicitWait = new WebDriverWait(driverInstance, timeOut);
		return explicitWait;
	}

	/**
	 * METHOD DESCRIPTION HERE
	 *
	 * @param - INPUT PARAM INFO / REMOVE IF NO INPUT PARAMS
	 * @return - RETURN VALUES INFO / REMOVE IF NO RETURN VALUES
	 */
	public boolean isElementPresent(WebElement ele) {
		try {
			driverWait().until(ExpectedConditions.visibilityOf(ele));
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public boolean click(WebElement locator) throws Exception {
		boolean blnVal = false;
		try {
			driverWait().until(ExpectedConditions.elementToBeClickable(locator));
			if (locator.isDisplayed()) {
				JavascriptExecutor executor = (JavascriptExecutor) driverInstance;	
				executor.executeScript("arguments[0].setAttribute('style','background:pink;border:3px double blue;');", locator);
				locator.click();
				log.info("Clicked on element: " + locator);
				blnVal = true;
			}

		} catch (Exception e) {
			log.error("Unable to click on element: " + locator);
			extLogger.log(Status.FAIL, "Unable to click on element: " + locator);
			e.printStackTrace();
			assertTrue(blnVal);
			throw (e);
		}
		return blnVal;
	}

	/**
	 * This Method is to double-click on given webelement.
	 * @param element
	 * @return
	 */
	public boolean doubleClick(WebElement element) {
		boolean blnVal = false;
		try {
			driverWait().until(ExpectedConditions.elementToBeClickable(element));
			if (element.isDisplayed()) {
				Actions actions = new Actions(driverInstance);
				actions.doubleClick(element).perform();
				blnVal = true;
			}

		} catch (Exception e) {
			log.info("Unable to click on element: " + element);
		}
		return blnVal;
	}

	public boolean type(WebElement locator, String message, String strData) throws Exception {
		boolean blnVal = false;
		try {
			driverWait().until(ExpectedConditions.elementToBeClickable(locator));
			if (locator.isDisplayed()) {
				if (locator.getText() != null || !locator.getText().equalsIgnoreCase("")) {
					locator.clear();
				}
				locator.sendKeys(strData);
				log.info("Text entered in the textbox is: " + strData);
				extLogger.log(Status.INFO, message + strData);
				blnVal = true;

			}
		} catch (RuntimeException localRuntimeException) {
			log.error("Unable to Enter the value in the Textbox :" + locator);
			extLogger.log(Status.FAIL, "Unable to Enter the value in the Textbox :" + locator);
			throw (localRuntimeException);
		}
		return blnVal;

	}


	public boolean clearAndTypeAttribute(WebElement locator, String message, String strData) throws Exception {
		boolean blnVal = false;
		try {
			driverWait().until(ExpectedConditions.visibilityOf(locator));
			if (locator.isDisplayed() || !locator.getAttribute("value").equals("")) {
				locator.sendKeys(Keys.CONTROL, Keys.chord("a"));
				locator.sendKeys(Keys.BACK_SPACE);
				locator.sendKeys(strData);
				log.info("Text entered in the textbox is: " + strData);
				extLogger.log(Status.INFO, message + strData);
				blnVal = true;
			}
		} catch (RuntimeException localRuntimeException) {
			log.error("Unable to Enter the value in the Textbox :" + locator);
			extLogger.log(Status.FAIL, "Unable to Enter the value in the Textbox :" + locator);
			throw (localRuntimeException);
		}
		return blnVal;
	}
	
	

	

	/**
	 * This method is used to click on the element using Javascript
	 * @param: By
	 * 
	 * @return: NA
	 */
	public boolean JSClick(WebElement locator) throws Exception {
		boolean flag = false;
		try {
			driverWait().until(ExpectedConditions.elementToBeClickable(locator));
			if (locator.isDisplayed()) {
				JavascriptExecutor executor = (JavascriptExecutor) driverInstance;
				executor.executeScript("arguments[0].setAttribute('style','background:pink;border:3px double blue;');", locator);
				executor.executeScript("arguments[0].click();", locator);
				log.info("Clicked on the element using Javascript: " + locator);
				flag = true;
			}
		} catch (Exception e) {
			log.error("Error in clicking on the element using Javascript: " + locator);
			addScreenshotToStep("Element not found " + locator);
			extLogger.log(Status.FAIL, "Error in clicking on the element using Javascript: " + locator);
			e.printStackTrace();
			assertTrue(flag);
			throw (e);

		}
		return flag;

	}

	public boolean JSWebElementClick(WebElement locator) throws Exception {
		boolean flag = false;
		try {
			JavascriptExecutor executor = (JavascriptExecutor) driverInstance;
			executor.executeScript("arguments[0].setAttribute('style','background:pink;border:3px double blue;');", locator);
			executor.executeScript("arguments[0].click();", locator);
			log.info("Clicked on the element using Javascript: " + locator);
			flag = true;
		} catch (Exception e) {
			log.error("Error in clicking on the element using Javascript: " + locator);
			extLogger.log(Status.FAIL, "Error in clicking on the element using Javascript: " + locator);
			e.printStackTrace();
			assertTrue(flag);
			throw (e);
		}
		return flag;
	}

	public void pageRefresh() throws Exception {
		checkPageLoad();
		driverInstance.navigate().refresh();
		if (!SetUpTest.strUrlVal.contains("straighttalk")) {
			JavascriptExecutor js = (JavascriptExecutor) driverInstance;
			js.executeScript("document.cookie = 'ae_live_scanner=true;'");
		}
		Thread.sleep(2000);
	}

	public void checkPageLoad() {
		JavascriptExecutor js = (JavascriptExecutor) driverInstance;
		for (int i = 0; i < 60; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			if ((js.executeScript("return document.readyState").toString().equals("complete"))
			/* && ((Long) js.executeScript("return jQuery.active") == 0) */) {
				break;
			}
		}
	}


	//method to select element from drop down by text
	public boolean selectFromDDByText(WebElement locator, String textToSelect) throws Exception {
		boolean blnVal = false;

		try {
			driverWait().until(ExpectedConditions.elementToBeClickable(locator));
			if (locator.isDisplayed()) {
				Select selectDD = new Select(locator);
				selectDD.selectByVisibleText(textToSelect);
				log.info("Selected : " + textToSelect);
				extLogger.log(Status.INFO, "Selected : " + textToSelect);
				blnVal = true;
			}

		} catch (Exception e) {
			log.error("Unable to select the element: " + locator);
			extLogger.log(Status.FAIL, "Unable to select the element: " + locator);
			assertTrue(blnVal);
			throw (e);

		}
		return blnVal;
	}

	//modified method to add explicit wait and called new method to
	// capture screenshot
	public boolean waitForElement(WebElement locator) throws Exception {
		boolean blnVal = false;
		try {
			driverWait().until(ExpectedConditions.elementToBeClickable(locator));
			JavascriptExecutor js = (JavascriptExecutor) driverInstance;
			if (locator.isDisplayed()) {
				if (js.executeScript("return document.readyState").toString().equals("complete")) {
					log.info("Element found: " + locator);
					blnVal = true;
				}
			}
		} catch (RuntimeException localRuntimeException) {
			log.error("Element not found : " + locator);
			addScreenshotToStep("Element not found " + locator);
			extLogger.log(Status.INFO, "Element not found : " + locator);
		}
		return blnVal;
	}

	public boolean waitForElementInvisibility(By locator) throws Exception {
		return waitForElementInvisibility(locator, 120);
	}

	public boolean waitForElementInvisibility(By locator, int timeOutInSeconds) throws Exception {
		try {
			if (driverWaitWithTimeOut(timeOutInSeconds).until(ExpectedConditions.invisibilityOfElementLocated(locator))) {
				log.info("Element " + locator + "is no more visible");
				return true;
			}
		} catch (RuntimeException localRuntimeException) {
			log.info("Element " + locator + "is still visible");
			extLogger.log(Status.INFO, "Element " + locator + "is still visible");
		}
		return false;
	}

	public boolean waitForElementVisibility(WebElement locator) throws Exception {
		boolean blnVal = false;
		try {
			if (driverWait().until(ExpectedConditions.visibilityOf(locator)) != null) {
				log.info("Element " + locator + "is visible");
			}
		} catch (RuntimeException localRuntimeException) {
			log.info("Element " + locator + "is still not visible");
			extLogger.log(Status.FAIL, "Element " + locator + "is not visible");
		}
		return blnVal;
	}

	//method to refresh the page
	public void refreshPage() throws Exception {
		driverInstance.navigate().refresh();
		JavascriptExecutor js = (JavascriptExecutor) driverInstance;
		if (js.executeScript("return document.readyState").toString().equals("complete")) {
		}
		;
	}

	public void clearTxt(WebElement locator) throws Exception {
		if (!locator.getText().equals("")) {
			locator.clear();
		}
		log.info("Cleared default text in the text box");
	}


	public synchronized void generateScreenShot(String testName, String brandName) {

		this.directory = reportPath + "\\" + new SimpleDateFormat("MM-dd-yyyy").format(new Date()) + "\\"
					+ suiteName.toUpperCase() + "\\" + strBrand + "\\" + scenario + "\\";
		
		String pathforscreenshot;
		try {
			String timestamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
			pathforscreenshot = directory + testName + timestamp + ".png";
			File targetfile = new File(pathforscreenshot);
			File scrFile = ((TakesScreenshot) driverInstance).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, targetfile);
		} catch (Exception e) {
			extLogger.log(Status.FAIL, "Screenshot not generated : ");
			e.printStackTrace();
		}
	}

	public void addFullScreenshotToStep(String strMssg, WebElement coverageMap) throws Exception {
		
		extLogger.log(Status.INFO, "<font color=\"#120B63\" style=\"font-size:22px\"><b>" + strMssg + "</b></font>",
				MediaEntityBuilder.createScreenCaptureFromBase64String(generateFullPageScreenshot(coverageMap))
						.build());
	}

	public String generateFullPageScreenshot(WebElement CoverageMap) throws IOException {
		try {
			int width = 800;
			BufferedImage img = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(700))
					.takeScreenshot(driverInstance).getImage();
			if (waitForElement(CoverageMap)) {
				BufferedImage cropimage = img.getSubimage(0, 0, img.getWidth(), CoverageMap.getLocation().getY());
				strImage = imgToBase64String(cropimage, width, CoverageMap.getLocation().getY());
			} else {
				strImage = imgToBase64String(img, width, width * img.getHeight() / img.getWidth());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strImage;
	}

	//Added method which returns path which is used to save images of
	// captured screenshots in each step which is used in Extent report
	public synchronized String captureScreen() throws IOException {
		String dest = null;
		try {
			path = successimagespath + "\\" + new SimpleDateFormat("MM-dd-yyyy").format(new Date()) + "\\" + scenario
					+ "\\";
			TakesScreenshot screen = (TakesScreenshot) driverInstance;
			File src = screen.getScreenshotAs(OutputType.FILE);
			String timestamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
			dest = directory + timestamp + ".png";
			File target = new File(dest);
			FileUtils.copyFile(src, target);

		} catch (Exception e) {
			log.info("Have not captured screenshot");
		}
		return dest;
	}

	//method to switch browser tab / new window
	public void switchToNewWindow(int windowNumber) throws Exception {
		ArrayList<String> tabs = new ArrayList<String>(driverInstance.getWindowHandles());
		driverInstance.switchTo().window(tabs.get(windowNumber));
	}

	//method to switch browser parent window
	public void switchToParentWindow() throws Exception {
		driverInstance.switchTo().defaultContent();
	}

	public void getWindow() throws Exception {
		driverInstance.getWindowHandle();
	}

	//method to select element from drop down by index
	public boolean selectFromDDByIndex(WebElement locator, int index) throws Exception {
		boolean blnVal = false;

		try {
			driverWait().until(ExpectedConditions.elementToBeClickable(locator));
			if (locator.isDisplayed()) {
				Select selectDD = new Select(locator);
				selectDD.selectByIndex(index);
				extLogger.log(Status.INFO, "Selected : card at index " + index);
				blnVal = true;
			}

		} catch (Exception e) {
			log.error("Unable to select the element: " + locator);
			extLogger.log(Status.FAIL, "Unable to select the element: " + locator);
			assertTrue(blnVal);
			throw (e);

		}
		return blnVal;
	}

	// method to select element from drop down by value
	public boolean selectFromDDByValue(WebElement locator, String value) throws Exception {
		boolean blnVal = false;

		try {
			driverWait().until(ExpectedConditions.elementToBeClickable(locator));
			if (locator.isDisplayed()) {
				Select selectDD = new Select(locator);
				selectDD.selectByValue(value);
				extLogger.log(Status.INFO, "Selected : card at value " + value);
				blnVal = true;
			}

		} catch (Exception e) {
			log.error("Unable to select the element: " + locator);
			extLogger.log(Status.FAIL, "Unable to select the element: " + locator);
			assertTrue(blnVal);
			throw (e);

		}
		return blnVal;
	}

	//Method is modified from window to tab
	public void switchToTab(int tabNumber) throws Exception {
		ArrayList<String> tabs = new ArrayList<String>(driverInstance.getWindowHandles());
		driverInstance.switchTo().window(tabs.get(tabNumber));
	}

	//method to handle drop down value by moving to that element
	public boolean selectFromDDAngularWeb1(WebElement locator, String DDValue) throws Exception {
		boolean blnVal = false;
		try {
			Actions builder = new Actions(driverInstance);
			builder.moveToElement(locator).click(locator).build().perform();
			Thread.sleep(2000);
			WebElement elementToBeClicked = driverInstance.findElement(By.xpath("//p[text()='" + DDValue + "']"));
			builder.moveToElement(elementToBeClicked).click(elementToBeClicked).build().perform();
			blnVal = true;
		} catch (Exception e) {
			extLogger.log(Status.FAIL, "Unable to select the element: " + locator);
			e.printStackTrace();
		}
		return blnVal;

	}

	//method to handle mouseOver action
	public boolean moveHover(WebElement locator1, WebElement locator2) throws Exception {
		boolean blnval = false;
		try {
			Actions action = new Actions(driverInstance);
			action.moveToElement(locator1).moveToElement(locator2).click(locator2).build().perform();
			blnval = true;
		} catch (Exception e) {
			extLogger.log(Status.FAIL, "Unable to hover the element ");
			assertTrue(blnval);
		}
		return blnval;
	}

	/**
	 * @param locator
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public String getElementText(By locator, String message) throws Exception {
		String strRetVal = null;
		WebElement element;
		try {
			element = driverWait().until(ExpectedConditions.elementToBeClickable(locator));
			if (element.isDisplayed()) {
				strRetVal = element.getText();
				log.info("Successfully captured text from " + message + ": " + strRetVal);
			}

		} catch (Exception e) {

			log.error("Unable to captured the text :" + locator);
			extLogger.log(Status.FAIL, "Unable to captured the text :" + locator);
			e.printStackTrace();
			fail();
			throw (e);
		}
		return strRetVal;
	}
	public String getText(WebElement locator, String message) throws Exception {
		String strRetVal = null;
		WebElement element;
		try {
			element = driverWait().until(ExpectedConditions.visibilityOf(locator));
			if (element.isDisplayed()) {
				strRetVal = element.getText();
				log.info("Successfully captured text from " + message + ": " + strRetVal);
			}

		} catch (Exception e) {

			log.error("Unable to captured the text :" + locator);
			extLogger.log(Status.FAIL, "Unable to captured the text :" + locator);
			e.printStackTrace();
			fail();
			throw (e);
		}
		return strRetVal;
	}


	//method to getText of a locator and log
	public String getElementText(WebElement locator, String message) throws Exception {
		String strRetVal = null;
		try {
			driverWait().until(ExpectedConditions.elementToBeClickable(locator));
			if (locator.isDisplayed()) {
				strRetVal = locator.getText();
				log.info("Successfully captured text from " + message + ": " + strRetVal);
			}

		} catch (Exception e) {

			log.error("Unable to captured the text :" + locator);
			extLogger.log(Status.FAIL, "Unable to captured the text :" + locator);
			e.printStackTrace();
			assertTrue(false);
			throw (e);
		}
		return strRetVal;
	}

	//method to handle drag and drop action
	public void dragAndDrop(By source, By destination) {
		WebElement e1 = driverInstance.findElement(source);
		WebElement e2 = driverInstance.findElement(destination);
		Actions a = new Actions(driverInstance);
		a.dragAndDrop(e1, e2).build().perform();
	}


	//method to scroll to view and locate element
	public boolean scrollToView(WebElement locator) throws Exception {
		boolean blnval = false;
		try {
			JavascriptExecutor js = (JavascriptExecutor) driverInstance;
			js.executeScript("arguments[0].scrollIntoView();", locator);
			blnval = true;
		} catch (Exception e) {
			assertTrue(blnval);
		}
		return blnval;
	}

	//method to open new tab
	public void newTabAndSwitch() {
		((JavascriptExecutor) driverInstance).executeScript("window.open();");
		ArrayList<String> tabs = new ArrayList<String>(driverInstance.getWindowHandles());
		driverInstance.switchTo().window(tabs.get(1)); // switches to new tab
	}


	public boolean click(WebElement locator, String locatorName) throws Exception {
		boolean blnVal = false;
		try {
			driverWait().until(ExpectedConditions.elementToBeClickable(locator));
			if (locator.isDisplayed()) {
				Thread.sleep(500);	
				JavascriptExecutor executor = (JavascriptExecutor) driverInstance;	
				executor.executeScript("arguments[0].setAttribute('style','background:pink;border:3px double blue;');", locator);
				locator.click();
				log.info("Clicked on element: " + locator);
				extLogger.log(Status.INFO, "Successfully clicked " + locatorName);
				blnVal = true;
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			log.error("Unable to click on element: " + locator);
			extLogger.log(Status.FAIL, "Unable to click on element: " + locator);
			e.printStackTrace();
			assertTrue(blnVal);
			throw (e);
		}
		return blnVal;
	}

	/**
	 * @param locator
	 * @return int
	 * @throws Exception
	 */
	public int getElementsSize(WebElement locator) throws Exception {
		int elementSize = 0;
		try {
			elementSize = driverWait().until(ExpectedConditions.visibilityOfAllElements(locator)).size();
		} catch (Exception e) {
			log.error("Unable to find on element: " + locator);
			extLogger.log(Status.FAIL, "Unable to click on element: " + locator);
			e.printStackTrace();
		}
		return elementSize;
	}

	//mouseover to a specific element
	public void moveHover(WebElement locator) throws Exception {

		Actions action = new Actions(driverInstance);
		action.moveToElement(locator).click(locator);
		action.perform();
	}

	//get attriute value
	public String getAttributeValue(WebElement locator, String message) throws Exception {
		String strRetVal = null;
		try {
			driverWait().until(ExpectedConditions.elementToBeClickable(locator));
			if (locator.isDisplayed()) {
				strRetVal = locator.getAttribute("value");
				log.info("Successfully captured text from " + message + ": " + strRetVal);
			}

		} catch (Exception e) {

			log.error("Unable to captured the text :" + locator);
			extLogger.log(Status.FAIL, "Unable to captured the text :" + locator);
			e.printStackTrace();
			throw (e);
		}
		return strRetVal;
	}

	/**
	 * * This method is used to get attribute value
	 * 
	 * @param - WebElement, String
	 * @return - String
	 */

	public String getAttribute(WebElement locator, String message, String Value) throws Exception {
		String strRetVal = null;
		try {
			driverWait().until(ExpectedConditions.elementToBeClickable(locator));
			if (locator.isDisplayed()) {
				strRetVal = locator.getAttribute(Value);
				log.info("Successfully captured text from " + message + ": " + strRetVal);
			}

		} catch (Exception e) {

			log.error("Unable to captured the text :" + locator);
			extLogger.log(Status.FAIL, "Unable to captured the text :" + locator);
			e.printStackTrace();
			throw (e);
		}
		return strRetVal;
	}

	//method for type using get attribute
	public boolean typeAttribute(WebElement locator, String message, String strData) throws Exception {
		boolean blnVal = false;
		try {
			driverWait().until(ExpectedConditions.visibilityOf(locator));
			if (locator.isDisplayed()) {
				if (!locator.getAttribute("value").equals("")) {
					locator.clear();
				}
				Thread.sleep(5000);
				locator.clear();
				locator.sendKeys(strData);
				log.info("Text entered in the textbox is: " + strData);
				extLogger.log(Status.INFO, message + strData);
				blnVal = true;
			}
		} catch (RuntimeException localRuntimeException) {
			log.error("Unable to Enter the value in the Textbox :" + locator);
			extLogger.log(Status.FAIL, "Unable to Enter the value in the Textbox :" + locator);
			throw (localRuntimeException);
		}
		return blnVal;
	}

	//method to verify that the page is completely loaded
	public void waitForPageLoad() {
		ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driverInstance).executeScript("return document.readyState")
						.equals("complete");
			}
		};
		try {
			WebDriverWait wait = new WebDriverWait(driverInstance, 30);
			wait.until(expectation);
		} catch (Throwable error) {
			Assert.fail("Timeout waiting for Page Load Request to complete.");
		}
	}

	//method to verify that user is able to CONTINUE to next page
	public boolean cntToNxtPage(WebElement cntEleBtn, String expectedPagePhrase) throws Exception {
		String currentPageURL = driverInstance.getCurrentUrl();
		click(cntEleBtn);
		waitForPageLoad();
		if (currentPageURL.contains(expectedPagePhrase)) {
			return true;
		}
		return false;
	}

	//method to select element from drop down by text
	public int getNoOfOptionsFromDropDown(WebElement locator) throws Exception {
		int numberOfOptions = 0;
		try {
			driverWait().until(ExpectedConditions.elementToBeClickable(locator));
			if (locator.isDisplayed()) {
				Select selectDD = new Select(locator);
				List<WebElement> options = selectDD.getOptions();
				numberOfOptions = options.size();
				log.info("Number of options are : " + options.size());
				extLogger.log(Status.INFO, "Number of options are : " + options.size());
			}

		} catch (Exception e) {
			extLogger.log(Status.FAIL, "Unable to get the options for : " + locator);
			log.error("Unable to get the options for : " + locator);
			throw (e);
		}
		return numberOfOptions;
	}

	public void pressTAB(WebElement locator) {
		driverWait().until(ExpectedConditions.visibilityOf(locator)).sendKeys(Keys.TAB);
	}


	public void scrollToElement(int x_coordinate, int y_coordinate) {
		JavascriptExecutor javScriptExecutor = (JavascriptExecutor) driverInstance;
		javScriptExecutor.executeScript("window.scrollBy(" + x_coordinate + ", " + y_coordinate + ");");
	}

	// method to switch to a frame id
	public void switchToIFrame(String frameId) {
		driverInstance.switchTo().frame(frameId);
	}

	/*
	 * This method is used to capture screenshot in page wise (As and When required)
	 * 
	 * @return: String
	 */
	public String generatePageScreenshot() throws IOException {
		try {
			int width = 800;
			File src = ((TakesScreenshot) driverInstance).getScreenshotAs(OutputType.FILE);
			BufferedImage img = ImageIO.read(src);
			strImage = imgToBase64String(img, width, width * img.getHeight() / img.getWidth());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strImage;
	}

	/*
	 * This method is used to encode and resize the captured image
	 * 
	 * @param: RenderedImage, Integer, Integer
	 * 
	 * @return: String
	 */
	public String imgToBase64String(final RenderedImage img, int width, int height) {
		final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		try {
			Image tmp = ((Image) img).getScaledInstance(width, height, Image.SCALE_SMOOTH);
			BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = resizedImage.createGraphics();
			g2d.drawImage(tmp, 0, 0, null);
			g2d.dispose();
			ImageIO.write(resizedImage, "png", java.util.Base64.getEncoder().wrap(byteStream));
			return byteStream.toString(StandardCharsets.ISO_8859_1.name());
		} catch (final IOException ioe) {
			throw new UncheckedIOException(ioe);
		}
	}

	/*
	 * This method is used to take screenshot at page level
	 * @param: String
	 */
	public void addScreenshotToStep(String strMssg) throws Exception {
		if (!suiteName.equalsIgnoreCase("tas")) {
			checkPageLoad();
		}
		extLogger.log(Status.INFO, "<font color=\"#120B63\" style=\"font-size:22px\"><b>" + strMssg + "</b></font>",
				MediaEntityBuilder.createScreenCaptureFromBase64String(generatePageScreenshot()).build());
	}

	/*
	 * This method is used to click on the element using Javascript
	 * @param: By
	 * 
	 * @return: NA
	 */
	public WebDriverWait elementWait() {
		WebDriverWait elementWait = new WebDriverWait(driverInstance, 120);
		return elementWait;
	}

	/*** Get the href attribute of a WebElement ***/
	public String getAttributehref(WebElement locator, String message) throws Exception {
		String strRetVal = null;
		try {
			driverWait().until(ExpectedConditions.elementToBeClickable(locator));
			if (locator.isDisplayed()) {
				strRetVal = locator.getAttribute("href");
			}
		} catch (Exception e) {
			log.error("UNABLE TO CAPTURE HREF ATTRIBUTE :" + locator);
			e.printStackTrace();
			throw (e);
		}
		return strRetVal;
	}

	/***  Get the list of WebElement href attributes ***/
	public List<String> getListOfAttributeshref(List<WebElement> locators, String message) throws Exception {
		List<String> hrefAttributes = new ArrayList<String>();
		try {
			for (int i = 0; i < locators.size(); i++) {
				if (scrollToView(locators.get(i))) {
					hrefAttributes.add(getAttributehref(locators.get(i), message));
				}
			}
		} catch (Exception e) {
			log.error("Unable to capture the href:" + locators);
			e.printStackTrace();
			throw (e);
		}
		return hrefAttributes;
	}

	/*** Surya Vadaga - Get the List Webelements Texts ***/
	public List<String> getListOfElementsText(List<WebElement> locators, String message) {
		List<String> textValues = new ArrayList<String>();
		try {
			for (int i = 0; i < locators.size(); i++) {
				if (scrollToView(locators.get(i))) {
					textValues.add(getElementText(locators.get(i), message));
				}
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			assertTrue(false);
		}
		return textValues;
	}

	// mouseover to a specific element
	public void mouseOver(WebElement locator) throws Exception {
		Actions action = new Actions(driverInstance);
		action.moveToElement(locator);
		action.perform();
	}
	
	public boolean typeWithJS(WebElement locator, String message, String strData) throws Exception {
		boolean blnVal = false;
		try {
			driverWait().until(ExpectedConditions.elementToBeClickable(locator));
			if (locator.isDisplayed()) {
				if (locator.getText() != null || !locator.getText().equalsIgnoreCase("")) {
					locator.clear();
				}				
				JavascriptExecutor executor = (JavascriptExecutor) driverInstance;
				executor.executeScript("arguments[0].value='" + strData + "';", locator);
				locator.click();
				Thread.sleep(1000);
				log.info("Text entered in the textbox is: " + strData);
				extLogger.log(Status.INFO, message + strData);
				blnVal = true;

			}
		} catch (RuntimeException localRuntimeException) {
			log.error("Unable to Enter the value in the Textbox :" + locator);
			extLogger.log(Status.FAIL, "Unable to Enter the value in the Textbox :" + locator);
			throw (localRuntimeException);
		}
		return blnVal;

	}
	public boolean isElementPresentWithWait(WebElement ele,int timeOutInSeconds) {
		try {
			driverWaitWithTimeOut(timeOutInSeconds).until(ExpectedConditions.visibilityOf(ele));
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	//Method to close current tab and switch to index tab
	public void closeSecondTabAndSwitchToDefaultTab() throws Exception {
		ArrayList<String> tabs = new ArrayList<String>(driverInstance.getWindowHandles());
		driverInstance.close();
		driverInstance.switchTo().window(tabs.get(0));
	}

	//Method to get calendar days for a month
	public int getDaysforCurrentMonth() {
		Calendar c = Calendar.getInstance();
		int monthMaxDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		return monthMaxDays;
	}


}
