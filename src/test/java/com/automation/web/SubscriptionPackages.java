package com.automation.web;

import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.automation.common.Log4jUtil;
import com.automation.common.ReadData;
import com.automation.scenarios.Scenarios;



public class SubscriptionPackages extends ReadData {

	Logger log = Log4jUtil.loadLogger(SubscriptionPackages.class);
	Scenarios stctvScenario = new Scenarios();


	@Test(dataProvider = "getCSVDataReader", groups = { "KsaSubscriptionPackages", "Regression", "Smoke" })
	public void KsaSubscriptionPackages(Map<Object, String> testDataVal) throws Exception {
		stctvScenario.PlanDetails(testDataVal);		
	}

	@Test(dataProvider = "getCSVDataReader", groups = { "KwSubscriptionPackages", "Regression"})
	public void KwSubscriptionPackages(Map<Object, String> testDataVal) throws Exception {
		stctvScenario.PlanDetails(testDataVal);		
	}
	@Test(dataProvider = "getCSVDataReader", groups = { "BhSubscriptionPackages", "Regression"})
	public void BhSubscriptionPackages(Map<Object, String> testDataVal) throws Exception {
		stctvScenario.PlanDetails(testDataVal);		
	}
	

}
