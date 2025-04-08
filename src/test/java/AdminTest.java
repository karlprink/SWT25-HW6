import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertFalse;

public class AdminTest extends TestHelper {
    String username = "AdminTestUsername30";
    String password = "AdminTestPassword1234";


    @Test
    public void AdminRegisterAccountTest() {
        registerAccount(username, password);
        WebElement notice = driver.findElement(By.id("notice"));
        String expectedMessage = "User " + username + " was successfully created.";
        assertEquals(expectedMessage, notice.getText());

        deleteLoggedInUser();
    }

    @Test
    public void AdminLoginTest() {
        registerAccount(username, password);
        //login(username, password);

        WebElement adminHeader = driver.findElement(By.linkText("Admin"));
        assertNotNull(adminHeader);

        deleteLoggedInUser();
    }

    @Test
    public void AdminLogoutTest() {
        registerAccount(username, password);
        //login(username, password);
        logout();

        WebElement adminHeader = driver.findElement(By.id("Admin"));
        assertEquals("Welcome to Admin Site", adminHeader.getText());

        login(username, password); // peab korra sisse logima et saaks ära kustutada konto
        deleteLoggedInUser();
    }

    @Test
    public void AdminDeleteAccountTest() {


        registerAccount(username, password);
        new WebDriverWait(driver, 1).until(ExpectedConditions.elementToBeClickable(By.linkText("Admin"))).click();

        driver.findElement(By.linkText("Delete")).click();

        WebElement notice = driver.findElement(By.id("notice"));
        assertEquals("User was successfully deleted.", notice.getText());
    }

    @Test
    public void AdminAddProductTest() {
        registerAccount(username, password);
        //login(username, password);

        String title = "Test Product2";
        String description = "Test description";
        String type = "Sunglasses";
        double price = 29.99;

        driver.findElement(By.linkText("New product")).click();
        addProductToStore(title, description, type, price);

        WebElement productLink = driver.findElement(By.linkText(title));
        assertEquals(title, productLink.getText());

        deleteProduct(title);
        deleteLoggedInUser();
    }

    @Test
    public void AdminEditProductTest() {
        registerAccount(username, password);
        //login(username, password);

        String title = "Edit Me Product";
        driver.findElement(By.linkText("New product")).click();
        addProductToStore(title, "Old description", "Books", 10.0);

        driver.findElement(By.id(title)).findElement(By.linkText("Edit")).click();
        WebElement priceInput = driver.findElement(By.id("product_price"));
        priceInput.clear();
        priceInput.sendKeys("99.99");

        driver.findElement(By.xpath("//input[@value='Update Product']")).click();
        WebElement notice = driver.findElement(By.id("notice"));
        assertEquals("Product was successfully updated.", notice.getText());
        driver.findElement(By.linkText("Back")).click();

        deleteProduct(title);
        deleteLoggedInUser();
    }

    @Test
    public void AdminDeleteProductTest() {
        registerAccount(username, password);
        //login(username, password);

        String title = "Delete Me Product";
        driver.findElement(By.linkText("New product")).click();
        addProductToStore(title, "Temporary", "Other", 5.0);

        new WebDriverWait(driver, 3).until(ExpectedConditions.elementToBeClickable(By.id(title))).findElement(By.linkText("Delete")).click();


        boolean exists = isElementPresent(By.id(title));
        assertFalse(exists);
        new WebDriverWait(driver, 2).until(ExpectedConditions.elementToBeClickable(By.linkText("Admin"))).click();
        deleteLoggedInUser();
    }

    @Test
    public void AdminAddExistingProductTest() {
        registerAccount(username, password);
        String title = "Existing Product";
        String description = "Test description";
        String type = "Sunglasses";
        double price = 29.99;


        driver.findElement(By.linkText("New product")).click();
        addProductToStore(title, description, type, price);


        driver.findElement(By.linkText("New product")).click();
        addProductToStore(title, description, type, price);


        WebElement errormessage = driver.findElement(By.id("error_explanation"));
        assertNotNull(errormessage.getText());
        System.out.println(errormessage.getText());
        driver.findElement(By.linkText("Back")).click();

        deleteProduct(title);
        deleteLoggedInUser();
    }

    // BUG NR 2
    @Test
    public void AdminAddProductWithoutCategory() {
        registerAccount(username, password);

        driver.findElement(By.linkText("New product")).click();
        driver.findElement(By.id("product_title")).sendKeys("test2");
        driver.findElement(By.id("product_description")).sendKeys("test2");
        driver.findElement(By.id("product_price")).sendKeys("29.99");

        WebElement createButton = driver.findElement(By.xpath("//input[@value='Create Product']"));
        createButton.click();

        deleteProduct("test2");
        deleteLoggedInUser();

        WebElement errormessage = driver.findElement(By.id("error_explanation"));
        assertNotNull(errormessage.getText());
        System.out.println(errormessage.getText());
        driver.findElement(By.linkText("Back")).click();
    }

    // Negative test
    @Test
    public void AdminAddProductWithIncorrectPrice() {
        registerAccount(username, password);

        driver.findElement(By.linkText("New product")).click();
        driver.findElement(By.id("product_title")).sendKeys("test3");
        driver.findElement(By.id("product_description")).sendKeys("test3");
        WebElement prodType = driver.findElement(By.id("product_prod_type"));
        Select select = new Select(prodType);
        select.selectByVisibleText("Other");
        driver.findElement(By.id("product_price")).sendKeys("a");

        WebElement createButton = driver.findElement(By.xpath("//input[@value='Create Product']"));
        createButton.click();

        WebElement errormessage = driver.findElement(By.id("error_explanation"));
        assertNotNull(errormessage.getText());
        System.out.println(errormessage.getText());
        driver.findElement(By.linkText("Back")).click();

        deleteLoggedInUser();
    }


}
