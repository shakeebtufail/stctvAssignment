package com.automation.webUtils;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

import com.automation.common.ReadData;
import com.automation.common.SetUpTest;
import com.automation.controllers.DriverManager;
import com.automation.utils.CommonUtilities;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import com.automation.objectRepository.Locators;

public class StctvDetails extends SetUpTest {
	private WebDriver driverInstance;
	private Locators LocatorsPage;
	private ReadData rd;
	private CommonUtilities commonUtilities;
	private ExtentTest extLogger;
	private String scenario;
	Map<Object, String> testData;
	WebDriverWait explicitWait;
	private boolean booleanVal = false;
	private ArrayList<String> tabs;


	public StctvDetails(String scenario, Map<Object, String> testData) throws Exception {
		this.driverInstance = DriverManager.getDriver();
		this.LocatorsPage = PageFactory.initElements(this.driverInstance, Locators.class);
		this.rd = new ReadData();
		this.scenario = scenario;
		this.testData = testData;
		this.extLogger = (ExtentTest) Reporter.getCurrentTestResult().getTestContext()
				.getAttribute("extLogger" + Thread.currentThread().hashCode());
		this.commonUtilities = new CommonUtilities(scenario);
		explicitWait = new WebDriverWait(driverInstance, 120);
	}

	public boolean click(WebElement locator) throws Exception {
		try {
			explicitWait.until(ExpectedConditions.elementToBeClickable(locator));
			// closePopUp();
			if (locator.isDisplayed()) {
				Thread.sleep(1500);
				locator.click();
				log.info("Clicked on element: " + locator);
				booleanVal = true;
			}
		} catch (Exception e) {
			log.error("Unable to click on element: " + locator);
			commonUtilities.addScreenshotToStep("Unable to click on element: " + locator);
			e.printStackTrace();
			assertTrue(false);
			throw (e);
		}
		return booleanVal;
	}

	public boolean type(WebElement locator, String message, String strData) throws Exception {
		try {
			explicitWait.until(ExpectedConditions.visibilityOf(locator));
			if (locator.isDisplayed()) {
				if (locator.getText() != null || !locator.getText().equalsIgnoreCase("")) {
					locator.clear();
				}
				Thread.sleep(500);
				locator.sendKeys(strData);
				log.info("Text entered in the textbox: " + strData);
				extLogger.log(Status.INFO, message + strData);
				booleanVal = true;
			}
		} catch (RuntimeException localRuntimeException) {
			log.error("Unable to Enter the value in the Textbox :" + locator);
			commonUtilities.addScreenshotToStep("Unable to Enter the value in the Textbox at :" + locator);
			throw (localRuntimeException);
		}
		return booleanVal;
	}

	public void hoverAndClick(WebElement srcLocator, WebElement destLocator) throws Exception {
		explicitWait.until(ExpectedConditions.elementToBeClickable(srcLocator));
		Actions action = new Actions(driverInstance);
		action.moveToElement(srcLocator).click(destLocator).build().perform();
	}
	
	public void verifyCurrentUrl(String urlAssert, String message) throws Exception {
		commonUtilities.waitForPageLoad();
		String url = driverInstance.getCurrentUrl();
		Assert.assertTrue(url.contains(urlAssert), message);
		try {
			log.info(message+ "---"+ url);
			extLogger.log(Status.INFO, "User able to access the  "+ urlAssert +" regoin"  );
			commonUtilities.addScreenshotToStep("Sucessfully landed on "+ urlAssert +" regoin");
		} catch (Exception e) {
			
			log.info("unable to access the"+ urlAssert +"regoin");
		}
	}

	public void verifycountryFlag(String country) throws Exception {
				
		String flag = commonUtilities
				.getText(LocatorsPage.country, "country is displayed In Homepage").trim();
		log.info(flag);
		Assert.assertTrue(commonUtilities.isElementPresentWithWait(LocatorsPage.country, 5),
				flag + " is displayed In Homepage");
		Assert.assertEquals(flag, country);
		extLogger.log(Status.INFO, "country Heading : " + flag + "</b></p>");
		commonUtilities.addScreenshotToStep("User assessing the -- " +flag + " --Region ");
			}
	
	public void verifyChooseYourPlan() throws Exception {
				
		String choosePlan = commonUtilities
				.getText(LocatorsPage.ChooseYourPlan, "ChooseYourPlan is not displayed In Homepage");
		log.info(choosePlan);
		Assert.assertTrue(commonUtilities.isElementPresentWithWait(LocatorsPage.ChooseYourPlan, 5),
				"ChooseYourPlan is displayed In Homepage");
		extLogger.log(Status.INFO, "choosePlan Heading : " + choosePlan + "</b></p>");
		commonUtilities.addScreenshotToStep("plans are available");
			}
	
	public void verifySubscriptionPackagesLite(String price, String currency) throws Exception {
		
		commonUtilities.waitForPageLoad();
		String litePlan = commonUtilities
				.getText(LocatorsPage.lite, "Lite plan is displayed In Homepage");
		log.info(litePlan);
		Assert.assertTrue(commonUtilities.isElementPresentWithWait(LocatorsPage.lite, 5),
				"Lite Plan is displayed In Homepage");
		extLogger.log(Status.INFO, "choosePlan Heading : " + litePlan + "</b></p>");
		commonUtilities.addScreenshotToStep(litePlan + "----plan are available");		
		commonUtilities.waitForPageLoad();	
		String litePrice = commonUtilities
					.getText(LocatorsPage.litePrice, "price is displayed");
			Assert.assertEquals(price, litePrice);
			extLogger.log(Status.INFO, "LitePlan Price : " + litePrice);
			log.info("LitePlan Price : " + litePrice+ " is verified");	
			commonUtilities.refreshPage();
			Thread.sleep(2000);
			String litecurrency = commonUtilities
					.getText(LocatorsPage.currencylite, "currency is displayed").split("/")[0];			
			Assert.assertEquals(currency, litecurrency);
			extLogger.log(Status.INFO, "LitePlan currency : " + litecurrency);
			log.info("LitePlan currency : " + litecurrency+ " is verified");
					
	}

	public void verifySubscriptionPackagesClassic(String price, String currency) throws Exception {
		
		String ClassicPlan = commonUtilities
				.getText(LocatorsPage.Classic, "classic plan is not displayed In Homepage");
		log.info(ClassicPlan);
		Assert.assertTrue(commonUtilities.isElementPresentWithWait(LocatorsPage.Classic, 5),
				"classic Plan is displayed In Homepage");
		extLogger.log(Status.INFO, "choosePlan Heading : " + ClassicPlan + "</b></p>");
		commonUtilities.addScreenshotToStep(ClassicPlan + "----plan are available");
		commonUtilities.waitForPageLoad();	
		String classicPrice = commonUtilities
					.getText(LocatorsPage.classicprice, "price is displayed");
			Assert.assertEquals(price, classicPrice);
			extLogger.log(Status.INFO, "classicPlan Price : " + classicPrice);
			log.info("classicPlan Price : " + classicPrice+ " is verified");	
			commonUtilities.refreshPage();
			Thread.sleep(2000);
			String classicCurrency = commonUtilities
					.getText(LocatorsPage.currencyclassic, "currency is displayed").split("/")[0];			
			Assert.assertEquals(currency, classicCurrency);
			extLogger.log(Status.INFO, "classicPlan currency : " + classicCurrency);
			log.info("classicPlan currency : " + classicCurrency+ " is verified");
				
	}

	public void verifySubscriptionPackagesPremium(String price, String currency) throws Exception {
		
		String PremiumPlan = commonUtilities
				.getText(LocatorsPage.Premium, "Premium plan is displayed");
		log.info(PremiumPlan);
		Assert.assertTrue(commonUtilities.isElementPresentWithWait(LocatorsPage.Premium, 5),
				"Premium Plan is displayed In Homepage");
		extLogger.log(Status.INFO, "choosePlan Heading : " + PremiumPlan + "</b></p>");
		commonUtilities.addScreenshotToStep(PremiumPlan + "----plan are available");
		commonUtilities.waitForPageLoad();	
		String PremiumPrice = commonUtilities
					.getText(LocatorsPage.premiumprice, "price is displayed");
			Assert.assertEquals(price, PremiumPrice);
			extLogger.log(Status.INFO, "PremiumPlan Price : " + PremiumPrice);
			log.info("PremiumPlan Price : " + PremiumPrice+ " is verified");	
			commonUtilities.refreshPage();
			Thread.sleep(2000);
			String PremiumCurrency = commonUtilities
					.getText(LocatorsPage.currencypremium, "currency is displayed").split("/")[0];			
			Assert.assertEquals(currency, PremiumCurrency);
			extLogger.log(Status.INFO, "LitePlan currency : " + PremiumCurrency);
			log.info("LitePlan currency : " + PremiumCurrency+ " is verified");
				
	}
public void verifyMostPopularBadge() throws Exception {
		
		String mostPopular = commonUtilities
				.getText(LocatorsPage.mostPopular, "Lite plan is not displayed In Homepage");
		log.info(mostPopular);
		Assert.assertTrue(commonUtilities.isElementPresentWithWait(LocatorsPage.mostPopular, 5),
				"mostPopular badge is displayed");
		extLogger.log(Status.INFO, "choosePlan Heading : " + mostPopular + "</b></p>");
		commonUtilities.addScreenshotToStep("plans are available");		
		
	}
	
	
		
}
