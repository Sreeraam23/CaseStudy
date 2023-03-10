package casestudy;

import java.io.FileInputStream;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.util.Assert;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import commonUtils.Utility;
import io.github.bonigarcia.wdm.WebDriverManager;

public class CaseStudy {
	WebDriver driver;
	ExtentReports report;
	ExtentSparkReporter spark;
	ExtentTest test;
	Properties prop;
	String[] col;
	WebDriverWait wait;
	Alert alert;
	
	@BeforeClass(groups={"feature1","feature2"})
	public void property() throws FileNotFoundException, IOException {
		String path = System.getProperty("user.dir")+"\\src\\test\\resources\\config\\store.properties";
		prop = new Properties();
		prop.load(new FileInputStream(path));
	}
	@BeforeClass(groups={"feature1","feature2"})
	public void setup() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://www.demoblaze.com/");
	}
	@BeforeTest(groups={"feature1","feature2"})
	public void extenreport() {
		report = new ExtentReports();
		spark = new ExtentSparkReporter("target\\casestudy.html");
		report.attachReporter(spark);
	}
  @Test(groups={"feature1","feature2"},priority=1)
  public void login() throws InterruptedException {
	  test = report.createTest("login");
	  Thread.sleep(5000);
	  driver.findElement(By.id("login2")).click();
	  Thread.sleep(3000);
	  driver.findElement(By.id("loginusername")).sendKeys(prop.getProperty("username"));
	  driver.findElement(By.id("loginpassword")).sendKeys(prop.getProperty("password"));
	  Thread.sleep(5000);
	  driver.findElement(By.xpath("(//button[@class='btn btn-primary'])[3]")).click();
	  SoftAssert softassert = new SoftAssert();
	  softassert.assertEquals(driver.findElement(By.id("nameofuser")).getText(), "Welcome Tester12345");
	  }
  @Test(groups={"feature1","feature2"},priority=2,dataProvider ="products",dependsOnMethods = "login")
  public void select(String btn,String p1) throws InterruptedException {
	  test = report.createTest("select");
	  wait = new WebDriverWait(driver,Duration.ofSeconds(10));
	  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	  driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
	  driver.findElement(By.partialLinkText(btn)).click();
	  driver.findElement(By.partialLinkText(p1)).click();
	  driver.findElement(By.xpath("//div/a[@class='btn btn-success btn-lg']")).click();
	  wait.until(ExpectedConditions.alertIsPresent());
	  alert = driver.switchTo().alert();
	  alert.accept();
	  driver.findElement(By.xpath("(//ul//li/a)[1]")).click();//home
	  }
  @Test(groups={"feature1","feature2"},priority=3)
  public void ele_cart() throws InterruptedException {
	  test = report.createTest("ele_cart");
	  driver.findElement(By.xpath("(//li/a)[4]")).click();
	  SoftAssert softassert = new SoftAssert();
	  Thread.sleep(2000);
	  List<WebElement> lists = driver.findElements(By.xpath("(//td)"));
	  System.out.println(lists.size()/3);
	  if(lists.size()==5) {
		  softassert.assertTrue(true);
	  }
	  else {
		  softassert.assertTrue(false);
	  }
	  Thread.sleep(5000);
	  	 	
  }
  @Test(groups="feature2",priority=4)
  public void del_cart() throws InterruptedException {
	  test = report.createTest("del_cart");
	  driver.findElement(By.xpath("(//li/a)[4]")).click();
	  Thread.sleep(3000);
//	  wait = new WebDriverWait(driver,Duration.ofSeconds(10));
//	  wait.until(ExpectedConditions.elementToBeSelected(By.xpath("(//tr/td/a)[2]")));
	  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	  driver.findElement(By.xpath("(//tr[4]/td/a)[1]")).click();
	  SoftAssert softassert = new SoftAssert();
	  List<WebElement> lists = driver.findElements(By.xpath("(//td)[2]"));
	  if(lists.size()<5) {
		  softassert.assertTrue(true);
	  }
	  else {
		  softassert.assertTrue(false);
	  }
	  Thread.sleep(2000);
  }
  @Test(groups={"feature1","feature2"},priority=5)
  public void checkout() throws InterruptedException {
	  test = report.createTest("checkout");
	  SoftAssert softassert = new SoftAssert();
	  Thread.sleep(5000);
	  driver.findElement(By.xpath("//button[text()='Place Order']")).click();
	  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	  Thread.sleep(3000);
	  driver.findElement(By.id("name")).sendKeys(prop.getProperty("username"));
	  driver.findElement(By.id("country")).sendKeys(prop.getProperty("password"));
	  driver.findElement(By.id("city")).sendKeys(prop.getProperty("city"));
	  driver.findElement(By.id("card")).sendKeys(prop.getProperty("credit"));
	  driver.findElement(By.id("month")).sendKeys(prop.getProperty("month"));
	  driver.findElement(By.id("year")).sendKeys(prop.getProperty("year"));
//	  Thread.sleep(3000);
	  driver.findElement(By.xpath("(//button)[text()='Purchase']")).click(); 
	  boolean isdisp = driver.findElement(By.xpath("//h2[text()='Thank you for your purchase!']")).isDisplayed();
	  if(isdisp) {
		softassert.assertTrue(true); 
	  }
	  else {
		  softassert.assertTrue(false);
	  }
	  softassert.assertAll();
	  driver.findElement(By.xpath("//button[text()='OK']")).click();
  }
  @DataProvider(name="products")
  public Object[][] getdata() throws CsvValidationException, IOException{
	  String path = System.getProperty("user.dir")+"\\src\\test\\resources\\Data_Folder\\products.csv";
	  CSVReader reader = new CSVReader(new FileReader(path));
	  ArrayList<Object> datalist = new ArrayList<Object>();
	  while((col = reader.readNext())!=null) {
		  Object[] record = {col[0],col[1]};
		  datalist.add(record);
	  }
	  
	  	return datalist.toArray(new Object[datalist.size()][]);
	  
  }
  @AfterMethod(groups={"feature1","feature2"})
  public void finish(ITestResult result) throws IOException {
	  if(ITestResult.FAILURE == result.getStatus()) {
		  test.log(Status.FAIL, result.getThrowable().getMessage());
		  String strpath = Utility.getScreenshotpath(driver);
		  test.addScreenCaptureFromPath(strpath);
	  }
	  report.flush();
  }
  @AfterTest(groups={"feature1","feature2"})
  public void tearout() {
	  driver.close();
  }
}
