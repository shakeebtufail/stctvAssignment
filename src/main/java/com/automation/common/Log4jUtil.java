package com.automation.common;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Log4jUtil {

	static Logger log;
	public static final String filePath = System.getProperty("user.dir")
			+ "/src/main/resources/commonConfig/log4j.properties";

	static {
		PropertyConfigurator.configure(filePath);
	}

	public static Logger loadLogger(Class<?> className) {
		log = Logger.getLogger(className);
		return log;
	}

}
