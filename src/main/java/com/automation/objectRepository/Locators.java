package com.automation.objectRepository;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class Locators {

	@FindBy(xpath = "//span[@id='country-name']")
	public WebElement country;

	@FindBy(xpath = "//h2[normalize-space()='Choose Your Plan']")
	public WebElement ChooseYourPlan;

	@FindBy(xpath = "//strong[@id='name-lite']")
	public WebElement lite;
	@FindBy(xpath = "//strong[@id='name-classic']")
	public WebElement Classic;
	
	@FindBy(xpath = "//strong[@id='name-premium']")
	public WebElement Premium;	
	
	@FindBy(xpath = "//div[@class='info-flag']")
	public WebElement mostPopular;
	
	
	@FindBy(xpath = "//*[@id='currency-lite']//i[contains(text(),'SAR/month')] | //*[@id='currency-lite']//i" )
	public WebElement currencylite;	
	
	@FindBy(xpath = "//*[@id='currency-lite']//b")
	public WebElement litePrice;
	
	@FindBy(xpath = "//div[@id='currency-classic']//i[contains(text(),'SAR/month')] | //*[@id='currency-classic']//i")
	public WebElement currencyclassic;
	
	@FindBy(xpath = "//*[@id='currency-classic']//b")
	public WebElement classicprice;
	
	@FindBy(xpath = " //div[@id='currency-premium']//i[contains(text(),'SAR/month')] | //*[@id='currency-premium']//i")
	public WebElement currencypremium;
	
	@FindBy(xpath = "//*[@id='currency-premium']//b")
	public WebElement premiumprice;


	
}
