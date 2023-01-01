package com.automation.scenarios;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.openqa.selenium.JavascriptExecutor;
import org.testng.Reporter;

import com.automation.common.ReadData;
import com.automation.common.SetUpTest;
import com.automation.controllers.DriverManager;
import com.automation.extentReport.ExtentFactory;
import com.automation.utils.CommonUtilities;
import com.automation.webUtils.StctvDetails;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class Scenarios extends SetUpTest {
	private ReadData rd;

	private synchronized void setQtestParams(String testScenario, Map<Object, String> testData) {
		Reporter.getCurrentTestResult().getTestContext().setAttribute("scenario" + Thread.currentThread().hashCode(),
				testScenario);
		Reporter.getCurrentTestResult().getTestContext().setAttribute("country" + Thread.currentThread().hashCode(),
				strBrand);

	}

	public synchronized void setExtentlog(Map<Object, String> testData, String scenario) {
		try {
			ExtentFactory extentFactory = new ExtentFactory();
			ExtentReports extentReport = extentFactory.getInstance(scenario);
			Reporter.getCurrentTestResult().getTestContext()
					.setAttribute("extentReport" + Thread.currentThread().hashCode(), extentReport);
			ExtentTest extlogger = extentReport.createTest(scenario.substring(0, scenario.length() - 19));
			extlogger.assignCategory((String) Reporter.getCurrentTestResult().getTestContext()
					.getAttribute("methodName" + Thread.currentThread().hashCode()));
			Reporter.getCurrentTestResult().getTestContext()
					.setAttribute("extLogger" + Thread.currentThread().hashCode(), extlogger);
			setQtestParams(scenario, testData);
			String dynatraceId = ((JavascriptExecutor) DriverManager.getDriver())
					.executeScript("return navigator.userAgent;").toString().split("TransId=")[1].replace(";/1.0", "");
			extlogger.log(Status.INFO, "Dynatrace ID :: " + dynatraceId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void PlanDetails(Map<Object, String> testData) throws Exception {
		String scenario = "";
		String scenarioName = (String) Reporter.getCurrentTestResult().getTestContext()
				.getAttribute("methodName" + Thread.currentThread().hashCode());
		scenario = testData.get("TestCaseID") + "-" + scenarioName + "----"
				+ new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

		log.info("Scenario: " + scenario);
		setExtentlog(testData, scenario);
		StctvDetails subscriptionDetails = new StctvDetails(scenario, testData);
		CommonUtilities commonUtilities = new CommonUtilities(scenario);
		String countryCode = testData.get("countrycode");String countryName = testData.get("CountryName");
		if (testData.get("countrycode").contains("sa")) {
			subscriptionDetails.verifyCurrentUrl(countryCode, " User accessing " + countryCode);
			subscriptionDetails.verifycountryFlag(countryName);
			subscriptionDetails.verifyChooseYourPlan();
			String price = testData.get("Price");
			String currency = testData.get("currency");
			if (testData.get("countrycode").equalsIgnoreCase("sa") && testData.get("Type").contains("Lite")) {
				subscriptionDetails.verifySubscriptionPackagesLite(price, currency);
			} else if (testData.get("countrycode").equalsIgnoreCase("sa") && testData.get("Type").contains("Classic")) {
				subscriptionDetails.verifySubscriptionPackagesClassic(price, currency);
			} else {
				subscriptionDetails.verifySubscriptionPackagesPremium(price, currency);
			}
		} else if (testData.get("countrycode").contains("bh")) {
			subscriptionDetails.verifyCurrentUrl(countryCode, " User accessing " + countryCode);
			subscriptionDetails.verifycountryFlag(countryName);
			subscriptionDetails.verifyChooseYourPlan();
			String price = testData.get("Price");
			String currency = testData.get("currency");
			if (testData.get("countrycode").equalsIgnoreCase("bh") && testData.get("Type").contains("Lite")) {
				subscriptionDetails.verifySubscriptionPackagesLite(price, currency);
			} else if (testData.get("countrycode").equalsIgnoreCase("bh") && testData.get("Type").contains("Classic")) {
				subscriptionDetails.verifySubscriptionPackagesClassic(price, currency);
			} else {
				subscriptionDetails.verifySubscriptionPackagesPremium(price, currency);
			}

		} else {

			subscriptionDetails.verifyCurrentUrl(countryCode, " User accessing " + countryCode);
			subscriptionDetails.verifycountryFlag(countryName);
			subscriptionDetails.verifyChooseYourPlan();
			String price = testData.get("Price");
			String currency = testData.get("currency");
			if (testData.get("countrycode").equalsIgnoreCase("kw") && testData.get("Type").contains("Lite")) {
				subscriptionDetails.verifySubscriptionPackagesLite(price, currency);
			} else if (testData.get("countrycode").equalsIgnoreCase("kw") && testData.get("Type").contains("Classic")) {
				subscriptionDetails.verifySubscriptionPackagesClassic(price, currency);
			} else {
				subscriptionDetails.verifySubscriptionPackagesPremium(price, currency);
			}

		}

	}
}
