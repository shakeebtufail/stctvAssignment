package com.automation.extentReport;

import com.automation.common.SetUpTest;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import org.testng.Reporter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExtentFactory extends SetUpTest {

    public ExtentReports getInstance(String scenarioMethod) throws IOException {
        ExtentSparkReporter htmlReport = null;

        String extentPath;
        // String dateName = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new
        // Date());
        String brandName = (String) Reporter.getCurrentTestResult().getTestContext()
                .getAttribute("brandName" + Thread.currentThread().hashCode());
        if (suiteName.equalsIgnoreCase("English")) {
            extentPath = reportPath + "/" + new SimpleDateFormat("MM-dd-yyyy").format(new Date()) + "/"
                    + suiteName.toUpperCase() + "/" + System.getProperty("testType").toUpperCase() + "/" + brandName
                    + "/" + scenarioMethod;
        }  else {
            extentPath = reportPath + "/" + new SimpleDateFormat("MM-dd-yyyy").format(new Date()) + "/"
                    + suiteName.toUpperCase() + "/" + strBrand + "/" + System.getProperty("testType").toUpperCase()
                    + "/" + scenarioMethod;

        }

        File targetfile = new File(extentPath.replace('/', fileSeparator));
        if (!targetfile.exists()) {
            targetfile.mkdirs();
            htmlReport = new ExtentSparkReporter(extentPath + "/" + scenarioMethod + ".html");
            System.out.println("HTML Report Path: " + extentPath);
        }
        ExtentReports extent = new ExtentReports();
        Locale.setDefault(Locale.ENGLISH);
        extent.attachReporter(htmlReport);
        htmlReport.loadXMLConfig(
                new File(System.getProperty("user.dir") + "/src/test/resources/extent/extent-config.xml".replace('/', fileSeparator)));
        htmlReport.config().setCss("img {width: 400%;}");
        return extent;
    }

    public ExtentReports getInstance() throws IOException {
        ExtentSparkReporter htmlReport;
        String extentPath;
        String dateName = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        extentPath = System.getProperty("user.dir") + "/Reports/" + suiteName + "_Test_Report_" + dateName + ".html";
        htmlReport = new ExtentSparkReporter(extentPath.replace('/', fileSeparator));
        // System.out.println("path :"+extentPath);
        ExtentReports extent = new ExtentReports();
        Locale.setDefault(Locale.ENGLISH);
        extent.attachReporter(htmlReport);
        // extent.setSystemInfo("Environment", (String) props.get("env"));
        htmlReport.loadXMLConfig(
                new File(System.getProperty("user.dir") + "/src/test/resources/extent/extent-config.xml".replace('/', fileSeparator)));
        return extent;
    }
}
