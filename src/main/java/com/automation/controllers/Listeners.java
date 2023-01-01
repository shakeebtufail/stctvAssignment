package com.automation.controllers;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.aventstack.extentreports.MediaEntityBuilder;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.testng.IAnnotationTransformer;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.IRetryAnalyzer;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.ITestAnnotation;
import org.testng.reporters.JUnitReportReporter;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.automation.common.Log4jUtil;
import com.automation.common.SetUpTest;
import com.automation.utils.Utils;

import javax.imageio.ImageIO;

public class Listeners extends JUnitReportReporter
		implements ISuiteListener, ITestListener, IInvokedMethodListener, IRetryAnalyzer, IAnnotationTransformer {
	private ExtentTest extLogger;
	public int totalpassed, totaltcs, totalskipped, totalfailed;
	Logger log = Log4jUtil.loadLogger(Listeners.class);
	HashMap<String, String> testMap = null;
	private int retryCounter = 0;
	private int retryLimit = 1;	
	
	
	public void onStart(ISuite suite) {
		totalpassed = 0;
		totaltcs = 0;
		totalskipped = 0;
		totalfailed = 0;
	}

	public void onFinish(ISuite suite) {
		log.info("Total test cases   :" + (totalpassed + totalskipped + totalfailed));
		log.info("Total passed cases :" + totalpassed);
		log.info("Total failed cases :" + totalfailed);
		log.info("Total skipped cases:" + totalskipped);
	}

	/*
	 * For Test Listener related methods
	 */
	public void onStart(ITestContext test) {
	}

	public void onFinish(ITestContext test) {
		log.info("All Tests Execution Completed");
	}

	public synchronized void onTestStart(ITestResult test) {			
		try {			
			//log.info("Thread instance: "+Thread.currentThread().getId());
			totaltcs++;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized void onTestSuccess(ITestResult test) {
		totalpassed++;
		
		extLogger.log(Status.PASS, "Test case passed: " + test.getName());
	}

	public synchronized void onTestFailure(ITestResult test) {
		//t1.remove();
		try {
			log.info("Test case failed: " + test.getName() + " --- Exception : " + test.getThrowable());
			extLogger.log(Status.FAIL, "<font color=\"#120B63\" style=\"font-size:22px\"><b>" + test.getThrowable() + "</b></font>",
					MediaEntityBuilder.createScreenCaptureFromBase64String(generatePageScreenshot()).build());
		} catch (IOException e) {
			e.printStackTrace();
		}
		test.getThrowable().printStackTrace();
		totalfailed++;
	}

	public synchronized void onTestSkipped(ITestResult test) {

		try {
			log.info("Listener If test case skipped:" + test.getName() + " --- Exception : " + test.getThrowable());
			extLogger.log(Status.SKIP, "<font color=\"#120B63\" style=\"font-size:22px\"><b>" + test.getThrowable() + "</b></font>",
					MediaEntityBuilder.createScreenCaptureFromBase64String(generatePageScreenshot()).build());
		} catch (IOException e) {
			e.printStackTrace();
		}
		test.getThrowable().printStackTrace();
		totalskipped++;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
		if (method.isTestMethod()) {
			String browserName = System.getProperty("browser").toLowerCase();
			Map<Object, String> testVal = new HashMap<>();
			SetUpTest.blnPortFlag = false;
			synchronized (this) {
				SetUpTest.scenarioName = method.getTestMethod().getMethodName();
				Reporter.getCurrentTestResult().getTestContext()
						.setAttribute("methodName" + Thread.currentThread().hashCode(), SetUpTest.scenarioName);
				Reporter.getCurrentTestResult().getTestContext()
						.setAttribute("portFlag" + Thread.currentThread().hashCode(), SetUpTest.blnPortFlag);
				testVal = (Map<Object, String>) testResult.getParameters()[0];
				
					Reporter.getCurrentTestResult().getTestContext().setAttribute(
							"dtMethodName" + Thread.currentThread().hashCode(),
							testVal.get("TestCaseID") + "-" + SetUpTest.scenarioName);
			}
			String dtMethod = (String) Reporter.getCurrentTestResult().getTestContext()
					.getAttribute("dtMethodName" + Thread.currentThread().hashCode());
			Thread.currentThread().setName(dtMethod);
			log.info("***************** START TEST *****************");
			DriverManager.setDriver((new DriverClass()).createInstance(browserName));
			WebDriver driverInstance = DriverManager.getDriver();
			driverInstance.manage().deleteAllCookies();
			driverInstance.manage().window().maximize();
			driverInstance.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			testMap = (HashMap<String, String>) testResult.getParameters()[0];
			String brandName = testMap.get("countrycode").toLowerCase();
			String URL = null;
			driverInstance.get(SetUpTest.strUrlVal);
				log.info("URL launched " + SetUpTest.strUrlVal);
				log.info("Launched Browser");
			

			// Integrate to Dynatrace
			try {
				JavascriptExecutor js = (JavascriptExecutor) driverInstance;
				for (int i = 0; i < 60; i++) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
					if ((js.executeScript("return document.readyState").toString().equals("complete"))) {
						break;
					}
				}
				js.executeScript("dtrum.identifyUser('WEB-AUTOMATION')");
				UUID uuid = UUID.randomUUID();
				String DtUsername="WA-WEB-"+SetUpTest.strBrand+"-"+uuid;
				log.info("DynatraceUrl Unique Session Id----"+DtUsername);
				js.executeScript("dtrum.identifyUser('" + DtUsername +"')");
				System.out.println("entered dynatrace session");

			} catch (Exception e) {
				System.out.println("exception");
			}

		}

	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
		if (method.isTestMethod()) {
			if (DriverManager.getDriver() != null) {
				log.info("Thread id = " + Thread.currentThread().getId());
				log.info("Hashcode of webDriver instance = " + DriverManager.getDriver().hashCode());
				this.extLogger = (ExtentTest) Reporter.getCurrentTestResult().getTestContext()
						.getAttribute("extLogger" + Thread.currentThread().hashCode());
			}
		}
	}

	@Override
	public synchronized String getTestName(ITestResult result) {
		String customMethod = null;
		Object[] dataSet = result.getParameters();
		Map<Object, String> testData = new HashMap<Object, String>();
		testData = Utils.getStringAsMap(dataSet[0].toString().replaceAll("[{}]", ""), ",", "=");
		String testCaseKey = testData.get("TestCaseID") + "-" + result.getMethod().getMethodName();
		customMethod = testCaseKey /* + " : " + testNameKey */;
		log.info("in get TestName and custom name is " + customMethod);
		return customMethod;
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	public boolean retry(ITestResult iTestResult) {
		if (!iTestResult.isSuccess()) {
			if (retryCounter < retryLimit) {
				retryCounter++;
				WebDriver driverInstance = DriverManager.getDriver();
				driverInstance.manage().deleteAllCookies();				
					driverInstance.get(SetUpTest.strUrlVal);
				
				ArrayList<String> tabs = new ArrayList<String>(driverInstance.getWindowHandles());
				if (tabs.size() > 1) {
					driverInstance.switchTo().window(tabs.get(1));
					driverInstance.close();
					driverInstance.switchTo().window(tabs.get(0));
				}
				try {
					iTestResult.setStatus(ITestResult.FAILURE);
					extendReportsFailOperations(iTestResult);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;
			} else {
				iTestResult.setStatus(ITestResult.FAILURE);
			}
		} else {
			iTestResult.setStatus(ITestResult.SUCCESS);
		}
		return false;
	}

	public void extendReportsFailOperations(ITestResult iTestResult) throws IOException {
		if (iTestResult.getStatus() == ITestResult.FAILURE) {
			Object[] dataSet = iTestResult.getParameters();
			Map<Object, String> testData = new HashMap<Object, String>();
			testData = Utils.getStringAsMap(dataSet[0].toString().replaceAll("[{}]", ""), ",", "=");
			String testCaseKey = testData.get("TestCaseID") + "-" + iTestResult.getMethod().getMethodName();
			extLogger = (ExtentTest) Reporter.getCurrentTestResult().getTestContext()
					.getAttribute("extLogger" + Thread.currentThread().hashCode());
			extLogger.log(Status.WARNING, "Retry for test name -" + testCaseKey + " is initiated" + "   ");
		}
	}

	@SuppressWarnings("rawtypes")
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		annotation.setRetryAnalyzer(Listeners.class);
	}

	/***
	 * 	 
	 * @return: String URL
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public String getURL() throws FileNotFoundException, IOException {
		String strUrl = null;
		try {
 			
 	 		
 	 		String brandName = testMap.get("country").toLowerCase();	
 				strUrl = System.getProperty("url").toLowerCase();	
		} catch (Exception e) {
			// TODO: handle exception
		}		
				
		return strUrl;
	}
	

	public String generatePageScreenshot() throws IOException {
		String strImage = null;
		try {
			int width = 800;
			File src = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.FILE);
			BufferedImage img = ImageIO.read(src);
			strImage = imgToBase64String(img, width, width * img.getHeight() / img.getWidth());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strImage;
	}

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

	public void addCookie(WebDriver driver, String cookieName, String cookieValue) throws Exception {
		driver.manage().deleteCookieNamed(cookieName);
		Cookie cookie = new Cookie(cookieName, cookieValue);
		driver.manage().addCookie(cookie);
	}
}
