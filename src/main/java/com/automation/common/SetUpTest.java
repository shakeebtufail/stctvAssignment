package com.automation.common;

import com.automation.controllers.DriverManager;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriverException;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.*;

public class SetUpTest {
    public static String strUrlVal;
    public static String scenarioName;
    public static boolean blnPortFlag;
    public static String strBrand;
    protected static Logger log = Log4jUtil.loadLogger(SetUpTest.class);
    protected static String strClassName;
    protected static String suiteName;
    protected static String strEnv;
    protected static Properties props;
    protected static String path = System.getProperty("user.dir");
    protected static String reportPath;
    protected static String successimagespath;
    protected static String failureimagespath;
    protected static String testExecutionID;
    protected static String testExecutionKey;
    protected char fileSeparator = File.separatorChar;

    @BeforeMethod(alwaysRun = true)
    public synchronized void startOfTest(ITestContext context, Method method) {
        log.info("***************** START TEST *****************");
        try {			
			  strUrlVal= System.getProperty("url").toLowerCase(); 
			  strBrand = System.getProperty("country").toLowerCase();
			
              Reporter.getCurrentTestResult().getTestContext()
                        .setAttribute("className" + Thread.currentThread().hashCode(), method.getDeclaringClass().getSimpleName());
            
        } catch (Exception e) {
            log.error("Error Message :" + e.getMessage());
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult testResult, ITestContext context) {
        String qtestStatus = null;
        String brandName = null;
        String dtMethod = null;
        long unixTimeStamp = 0;
        String uuid = null;
        String scenario = null;
        ExtentTest extLogger = null;
        try {
            scenario = (String) Reporter.getCurrentTestResult().getTestContext()
                    .getAttribute("scenario" + Thread.currentThread().hashCode());
            dtMethod = (String) Reporter.getCurrentTestResult().getTestContext()
                    .getAttribute("dtMethodName" + Thread.currentThread().hashCode());
            unixTimeStamp = (long) Reporter.getCurrentTestResult().getTestContext()
                    .getAttribute("unixTimeStamp" + Thread.currentThread().hashCode());
            uuid = (String) Reporter.getCurrentTestResult().getTestContext()
                    .getAttribute("uuid" + Thread.currentThread().hashCode());
            if (suiteName.equalsIgnoreCase("KSA")) {
                brandName = (String) Reporter.getCurrentTestResult().getTestContext()
                        .getAttribute("countrycode" + Thread.currentThread().hashCode());
            } 
            else {
                brandName = strBrand;
            }
            extLogger = (ExtentTest) Reporter.getCurrentTestResult().getTestContext()
                    .getAttribute("extLogger" + Thread.currentThread().hashCode());
            ExtentReports extentReport = (ExtentReports) Reporter.getCurrentTestResult().getTestContext()
                    .getAttribute("extentReport" + Thread.currentThread().hashCode());
            extentReport.flush();
            if (testResult.getStatus() == ITestResult.FAILURE) {
                qtestStatus = "FAILED";
                log.info("Test -----" + brandName + "_" + scenario + "Failed");
                // Log JIRA Defect, Do not remove this code
               /* JIRAServices jiraServices = new JIRAServices();
                jiraServices.logDefect(scenario, brandName);*/
            } else if (testResult.getStatus() == ITestResult.SKIP) {
                // qtestStatus = "FAILED";
                Throwable t = testResult.getThrowable();
                log.info("Test -----" + brandName + "_" + scenario + "skipped with exception :" + t);
            } else {
                qtestStatus = "PASSED";
                log.info("Test -----" + brandName + "_" + scenario + " successfully executed");
            }
        } catch (Exception e) {
            log.error("Exception in After Method " + e.getMessage());
        } finally {
            try {
                if (!suiteName.contains("Service")) {
                    DriverManager.getDriver().quit();
                }
                if (System.getProperty("qtestFlag").equalsIgnoreCase("y") && testResult.getStatus() != ITestResult.SKIP) {
                   log.info("Create Xray Test");
                   log.info("Xray Test Upload Completed");
                } else {
                    log.info("Xray Test upload not required");
                    log.info("Qtest Test upload not required");
                }
            } catch (WebDriverException e) {
                log.error("Exception During CLosing Driver " + e.getMessage());
            } catch (Exception e) {
                log.error("Exception During Uploading Results " + e.getMessage());
            }
            log.info("***************** END TEST *****************");
        }
    } 
    
    @BeforeSuite(alwaysRun = true)
    public void launchApplication(ITestContext ctx) {
        Map<Object, String> xrayObj = new HashMap<>();
        Properties properties = new Properties();
        setPath();
        suiteName = ctx.getCurrentXmlTest().getSuite().getName();}

    public synchronized String getURL(String env, String brand) {
        Properties envProps = new Properties();
        String sitPropsPath = path + "/src/main/resources/webConfig/SIT.properties".replace('/', fileSeparator);
        String fileName = null;
        try {
            
                fileName = sitPropsPath;
            
        } catch (Exception e) {
            log.error("Error while fetching value from Props File" + e.getMessage());
        }
        try (FileReader fr = new FileReader(fileName);
             BufferedReader br = new BufferedReader(fr)) {            
                envProps.load(br);
                strUrlVal = envProps.getProperty(brand);
            
        } catch (Exception e) {
            log.error("Exception occured while getting the URL " + e.getMessage());
        }
        return strUrlVal;
    }

    private synchronized void setPath() {
        props = new Properties();
        String utilsFilePath = path + "/src/main/resources/webConfig/utils.properties".replace('/', fileSeparator);
        try (FileInputStream fr = new FileInputStream(utilsFilePath)) {
            strEnv = getEnv();
            props.load(fr);
            reportPath = props.getProperty("reportPath");
            successimagespath = path + props.getProperty("successimagespath");
            failureimagespath = path + props.getProperty("failureimagespath");
           
        } catch (Exception e) {
            log.error("Exception while setting up report path : " + e.getMessage());
        }
    }

    private String getEnv() {
        String env = System.getProperty("url").toLowerCase();
        try {
            if (env == null) {
                env = props.getProperty("defaultenv");
                log.info("Default Environment: " + props.getProperty("defaultenv"));
            } else {
                log.info("Environment: " + env);
            }
        } catch (Exception e) {
            log.error("Exception occurred while Fetching the Env Value " + e.getMessage());
        }
        return env;
    }
}
