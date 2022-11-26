import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import io.github.bonigarcia.wdm.WebDriverManager;

public class SmartBuyTestDiscountClass {
	public WebDriver driver;
	String url = "https://smartbuy-me.com/smartbuystore/en";
	SoftAssert softAssertProcess = new SoftAssert();

//--1. Test Login to SmartBuy WebSite--------------------------------------------------------
	@BeforeTest
	public void test_login_website() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get(url);

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(50000));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,550)");
	}

//--2. Test original Price and discount prices and the differences bewteen them--------------
	@Test()
	public void test_items_discount() {
		List<WebElement> item_name_list = driver.findElements(By.className("carousel__item--name"));
		List<WebElement> price_org_list = driver.findElements(By.className("discountPrice"));
		List<WebElement> price_discount_list = driver.findElements(By.className("orignalPrice"));
		List<WebElement> price_data_list = driver.findElements(By.className("price-data"));

		String item_name_str;
		String price_org_str;
		String price_discount_str;
		String price_data_str;
		String item_discount_str;
		String item_discount_msg_str;
		String item_discount_msg_str_new;

		//	System.out.println("The size of price data list is: "+ price_data_list.size());

		List<Double> price_org_double = new ArrayList<>();
		List<Double> price_discount_double = new ArrayList<>();
		List<Double> discount_amount_double = new ArrayList<>();
		List<Double> price_diff_double = new ArrayList<>();

		for (int i = 0; i < price_data_list.size(); i++) {
//-----to get the texts from the above defined webelement lists-----------------------------------------------
			item_name_str = item_name_list.get(i).getText();
			price_org_str = price_org_list.get(i).getText().replace(" JOD", "").replace(",", "").trim();
			price_discount_str = price_discount_list.get(i).getText().replace(" JOD", "").replace(",", "").trim();
			price_data_str = price_data_list.get(i).getText();

			if (price_data_str.contains("%")) {
//-----to get the texts from the list price_data_list because they didn't retrieve from the class name----------------
				item_discount_str = price_data_str.substring(0, 5).replace("%", "").trim();
				item_discount_msg_str = price_data_str.substring((price_data_str.indexOf("-"))).trim();
				item_discount_msg_str_new = item_discount_msg_str.substring(11).replace(" JOD", "").trim();

				
				System.out.println("--------THE BEGIN OF TRY NO. " + (i+1) + " ----------------------");
				System.out.println("The item name No. "+(i+1) + " is: "+ item_name_str);

//-----to get the original price as a value to run calculation on it-------------------------------------------------
				Double price_org_double_val = Double.parseDouble(price_org_str);
				price_org_double.add(price_org_double_val);
				System.out.println("The Original Prices as Double Values is: " + price_org_double_val);

//-----to get the discount price as a value to run calculation on it-------------------------------------------------
				Double price_discount_double_val = Double.parseDouble(price_discount_str);
				price_discount_double.add(price_discount_double_val);
				System.out.println("The discount Prices as Double Values is: " + price_discount_double_val);

//-----to get the discount percentage as a value to run calculation on it-------------------------------------------------
				Double discount_amount_double_val = Double.parseDouble(item_discount_str) / 100;
				discount_amount_double.add(discount_amount_double_val);
				System.out.println("The discount percentage as Double Values is: " + discount_amount_double_val);

//-----to get the differences amount between prices as a value to run calculation on it-------------------------------------------------
				Double price_diff_double_val = Double.parseDouble(item_discount_msg_str_new);
				price_diff_double.add(price_diff_double_val);
				System.out.println("The differences amount between prices as Double Values is: " + price_diff_double_val);

//-----to calculate the discount prices manually -------------------------------------------------
				Double price_discount_calculated_val = (double) Math
						.round((price_org_double_val * (1 - discount_amount_double_val)));
				System.out.println("The Calculated discount price is: " + price_discount_calculated_val);

//-----to calculate the differences between prices manually -------------------------------------------------
				Double price_diff_calculated_val = (price_org_double_val - price_discount_double_val);
				System.out.println("The calculated differences between orginal & discount price is: " + price_diff_calculated_val);

				System.out.println("--------THE END OF TRY NO. " + (i+1) + " ----------------------");
				System.out.println("------------------------------------------------------------------------------");
				System.out.println("");
				
//-----Printing Statements to check -------------------------------------------------
				// System.out.println("The item name No. "+(i+1) + " is: "+ item_name_str);
				// System.out.println("The price orginal No. " +(i+1) + " is: "+ price_org_str);
				// System.out.println("The price discount No "+(i+1) + " is: "+
				// price_discount_str);
				// System.out.println("The discount msg No "+(i+1) + " is: "+
				// item_discount_msg_str);
				// System.out.println("The price data No. "+(i+1) + " is: "+ price_data_str);
				// System.out.println("The item discount No. "+(i+1) + " is: "+
				// item_discount_str);
				// System.out.println("The item discount msg. "+(i+1) + " is: "+
				// item_discount_msg_str);
				// System.out.println("The item discount msg. new "+(i+1) + " is: "+
				// item_discount_msg_str_new);
				// System.out.println(item_discount_msg_str_new);

//-----to Assert the prices after discount is correct -------------------------------------------------
			softAssertProcess.assertEquals(price_discount_double_val, price_discount_calculated_val,
						"compare price_dscount");

//-----to Assert the discount amount between prices is correct -------------------------------------------------
				softAssertProcess.assertEquals(price_diff_double_val, price_diff_calculated_val,
						"compare price differences");
				softAssertProcess.assertAll();
			}

		}

//------to Print the value of arrays after adding items in them
		
		//System.out.println("The Value of orginal price array is: " + price_org_double);
		//System.out.println("The Value of discount price array is: " + price_discount_double);
		//System.out.println("The Value of discount amount array is: " + discount_amount_double);
		//System.out.println("The differences between prices array is: " + price_diff_double);

	}

//--3. Test Quit Smart Buy WebSite-----------------------------------------------------------
	@AfterTest()
	public void test_close_website() throws InterruptedException {
		Thread.sleep(5000);
		driver.quit();
	}

}
