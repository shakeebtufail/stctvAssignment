package com.automation.utils;

import com.automation.common.Log4jUtil;
import com.automation.common.SetUpTest;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Utils extends SetUpTest {

	static Logger log = Log4jUtil.loadLogger(Utils.class);

	public static String getAsTag(String str) {
		String res = null;
		res = ("[" + str.trim() + "]");
		return res;
	}

	public static String getAsBraceTag(String str) {
		String res = null;
		res = ("(" + str.trim() + ")");
		return res;
	}

	public synchronized static String getCurrentDateTime() {
		String res = null;
		Date date = new Date();
		String p = "yyyy-MM-dd'T'HH:mm:ssZ";
		DateFormat dateFormat = new SimpleDateFormat(p);
		res = dateFormat.format(date);
		return res;
	}

	public synchronized static String getTimeStamp() {
		String res = null;
		Date date = new Date();
		String p = "yyyyMMddHHmmss";
		DateFormat dateFormat = new SimpleDateFormat(p);
		res = dateFormat.format(date);
		return res;
	}

	public static long getRandValue(int min, int max) {
		long res = 0;
		Random random = new Random();
		res = ((random.nextInt(max - min)) + min);
		return res;
	}

	public static String getResourceContent(String resource) {
		StringBuilder res = new StringBuilder();
		InputStream inputStream;
		BufferedReader bufferedReader;
		String lineInFile;
		try {
			inputStream = Utils.class.getResourceAsStream(resource);
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			while ((lineInFile = bufferedReader.readLine()) != null)
				res.append(lineInFile);
			inputStream.close();
			bufferedReader.close();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		log.info("Request Body without actual data :" + res.toString());
		return res.toString();
	}

	public static Map<Object, String> getStringAsMap(String s, String d1, String d2) {
		Map<Object, String> res = new HashMap<Object, String>();
		String[] collectionsData = null;
		try {
			collectionsData = s.split(d1);
			for (String collectionsItem : collectionsData) {
				if (!collectionsItem.endsWith("=")) {
					String[] pairs = collectionsItem.split(d2);
					res.put(pairs[0].trim(), pairs[1].trim());
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return res;
	}
	public static String getPegaResourceContent(String resource) {
		StringBuilder res = new StringBuilder();
		InputStream inputStream;
		BufferedReader bufferedReader;
		String lineInFile;
		try {
			inputStream = Utils.class.getResourceAsStream(resource);
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			while ((lineInFile = bufferedReader.readLine()) != null)
				res.append(lineInFile);
			inputStream.close();
			bufferedReader.close();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		log.info("Request Body without actual data :" + res.toString());
		return res.toString();
	}
	public static int getRandomNumberUsingInts(int min, int max) {
		return ThreadLocalRandom
				.current()
				.nextInt(min, max + 1);
	}
	
	public static String randomEmailID() {
		new SimpleDateFormat("yyMMddhhmmssMs").format(new Date());
		return strEnv + /* strBrand + */ System.nanoTime() + "@yopmail.com";
	}

	public synchronized static String getCurrentDate( String format) {
		String res = null;
		Date date = new Date();
		String p = format;
		DateFormat dateFormat = new SimpleDateFormat(p);
		res = dateFormat.format(date);
		return res;
	}


}
