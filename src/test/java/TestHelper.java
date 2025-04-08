
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class TestHelper {

    WebDriver driver;
    final int waitForResposeTime = 4;

    // here write a link to your admin website (e.g. http://my-app.herokuapp.com/admin)
    String baseUrlAdmin = "http://localhost:3000/admin";

    // here write a link to your website (e.g. http://my-app.herokuapp.com/)
    String baseUrl = "http://localhost:3000/";


    @Before
    public void setUp() {

        // if you use Chrome:
        // System.setProperty("webdriver.chrome.driver", "C:\\Users\\prink\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        // driver = new ChromeDriver();

        // if you use Firefox:
        System.setProperty("webdriver.gecko.driver", "D:\\geckodriver.exe");
        driver = new FirefoxDriver();

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(baseUrl);

    }

    void goToPage(String page) {
        WebElement elem = driver.findElement(By.linkText(page));
        elem.click();
        waitForElementById(page);
    }

    public void waitForElementById(String id) {
        new WebDriverWait(driver, waitForResposeTime).until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
    }

    public boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void login(String username, String password) {

        driver.get(baseUrlAdmin);

        driver.findElement(By.linkText("Login")).click();

        driver.findElement(By.id("name")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);

        // ...

        By loginButtonXpath = By.xpath("//input[@value='Login']");
        // click on the button
        WebElement login = driver.findElement(loginButtonXpath);
        login.click();
    }

    public void logout() {
        WebElement logout = driver.findElement(By.linkText("Logout"));
        logout.click();

        waitForElementById("Admin");
    }

    public void registerAccount(String username, String password) {
        driver.get(baseUrlAdmin);

        driver.findElement(By.linkText("Register")).click();
        driver.findElement(By.id("user_name")).sendKeys(username);
        driver.findElement(By.id("user_password")).sendKeys(password);
        driver.findElement(By.id("user_password_confirmation")).sendKeys(password);

        By registerButtonXpath = By.xpath("//input[@value='Create User']");

        WebElement register = driver.findElement(registerButtonXpath);
        register.click();
    }

    public void addProductToStore(String title, String description, String type, double price) {

        driver.findElement(By.id("product_title")).sendKeys(title);
        driver.findElement(By.id("product_description")).sendKeys(description);

        WebElement typeDropdown = driver.findElement(By.id("product_prod_type"));
        typeDropdown.findElement(By.xpath("//option[. = '" + type + "']")).click();

        driver.findElement(By.id("product_price")).sendKeys(String.valueOf(price));

        driver.findElement(By.xpath("//input[@value='Create Product']")).click();
    }

    public void deleteLoggedInUser() {
        driver.findElement(By.linkText("Admin")).click();
        driver.findElement(By.linkText("Delete")).click();
    }

    public void deleteProduct(String title) {
        if (isElementPresent(By.id(title))) {
            driver.findElement(By.id(title)).findElement(By.linkText("Delete")).click();
        }
    }

    @After
    public void tearDown() {
        driver.close();
    }

}