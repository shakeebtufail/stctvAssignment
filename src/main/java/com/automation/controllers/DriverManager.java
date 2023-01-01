package com.automation.controllers;

import org.openqa.selenium.WebDriver;

public class DriverManager {

	private static ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();

	public static WebDriver getDriver() {
		return driver.get();
	}

	public static void setDriver(WebDriver drive) {        
		driver.set(drive);
		System.out.println("In set driver method " + driver.get());
	}

	public static void removeDriver() // Quits the driver and closes the browser
	{
		// getDriver().driver.quit();
		driver.remove();
	}

	/*
	 * private static WebDriver driver;
	 * 
	 * public static WebDriver getDriver() { return driver; }
	 * 
	 * public static void setDriver(WebDriver drive) { driver = drive; }
	 */

}
