package com.automation.controllers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.log4j.Logger;
import org.apache.poi.util.SystemOutLogger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.util.UUID;
import org.testng.Reporter;
import java.time.Instant;
 

import com.automation.common.Log4jUtil;

public class DriverClass {
	protected static Logger log = Log4jUtil.loadLogger(DriverClass.class);

	public WebDriver createInstance(String browserName) {
		WebDriver driver = null;
		if (browserName.toLowerCase().contains("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
			return driver;
		}
		if (browserName.toLowerCase().contains("internet")) {
			WebDriverManager.iedriver().setup();
			driver = new InternetExplorerDriver();
			return driver;
		}
		if (browserName.toLowerCase().contains("chrome")) {

			WebDriverManager.chromedriver().setup();		
			ChromeOptions options = new ChromeOptions();
			options.addArguments("disable-infobars");
			options.addArguments("--disable-gpu");
			options.addArguments("--disable-backgrounding-occluded-windows");
			options.setAcceptInsecureCerts(true);
			//options.addArguments("--headless");
			//DesiredCapabilities cap = DesiredCapabilities.chrome();
			options.setCapability("applicationCacheEnabled", false);
			options.addArguments("--ignore-certificate-errors");
			/*
			 * Reporter.getCurrentTestResult().getTestContext().setAttribute( "methodName" +
			 * Thread.currentThread().hashCode(), method.getTestMethod().getMethodName());
			 * 
			 * String scenario = (String) Reporter.getCurrentTestResult().getTestContext()
			 * .getAttribute("methodName" + Thread.currentThread().hashCode());
			 */

			/*
			 * ReadData read = new ReadData(); Map<Object, String> getDP = new HashMap<>();
			 * try { getDP = read.getValueFromDP(System.getProperty("brand"),
			 * method.getTestMethod().getMethodName()); } catch (Exception e) {
			 * log.info(e.getMessage()); } String testID = null;
			 * 
			 * @SuppressWarnings("unchecked") List<String> setIDs = (List<String>)
			 * Reporter.getCurrentTestResult().getTestContext() .getAttribute("setIDs" +
			 * Thread.currentThread().hashCode());
			 * 
			 * if (setIDs.contains(System.getProperty("brand") + "-" +
			 * getDP.get("TestCaseID") + "-" + method.getTestMethod().getMethodName())) {
			 * 
			 * testID = System.getProperty("brand") + "-" + getDP.get("TestCaseID") + "-" +
			 * method.getTestMethod().getMethodName(); }
			 * 
			 * 
			 * if (setIDs.contains((String) Reporter.getCurrentTestResult().getTestContext()
			 * .getAttribute("dtID" + Thread.currentThread().hashCode()))) { testID =
			 * (String) Reporter.getCurrentTestResult().getTestContext()
			 * .getAttribute("dtID" + Thread.currentThread().hashCode()); }
			 */
 
            long unixTimeStamp= Instant.now().getEpochSecond();
            UUID uuid = UUID.randomUUID();
            Reporter.getCurrentTestResult().getTestContext()
            .setAttribute("unixTimeStamp" + Thread.currentThread().hashCode(), unixTimeStamp);
            Reporter.getCurrentTestResult().getTestContext()
            .setAttribute("uuid" + Thread.currentThread().hashCode(), uuid.toString());
			String dtMethod = (String) Reporter.getCurrentTestResult().getTestContext()
					.getAttribute("dtMethodName" + Thread.currentThread().hashCode());
			log.info(dtMethod);
			options.addArguments(
					"user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36 BRAND="
							+ System.getProperty("brand") + ";" + " FLOW=" + dtMethod +";"+"TransId="+unixTimeStamp+"-"+uuid+";/1.0");
			log.info(dtMethod+"-"+unixTimeStamp+"-"+uuid);
			//************
			//DesiredCapabilities object
		      DesiredCapabilities c=DesiredCapabilities.chrome();
		    //accept insecure certificates
		      c.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
		      //accept SSL certificates
		      c.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		      
		      options.merge(c);
			

			options.setCapability(ChromeOptions.CAPABILITY, options);
			driver = new ChromeDriver(options);
		
			return driver;
			
		}
		return driver;
	}
}
