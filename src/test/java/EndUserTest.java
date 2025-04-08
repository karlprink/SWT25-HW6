import junit.framework.TestCase;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class EndUserTest extends TestHelper{
    public void setupProduct() {
        registerAccount("admin4", "admin4");

        String title = "Test Product";
        String description = "Test description";
        String type = "Other";
        double price = 29.99;

        driver.findElement(By.linkText("New product")).click();
        addProductToStore(title, description, type, price);

        deleteLoggedInUser();
    }

    public void cleanUp() {
        registerAccount("admin3", "admin3");
        deleteProduct("Test Product");
        deleteLoggedInUser();
    }

    @Test
    public void addProductsToCart() {
        setupProduct();
        driver.get(baseUrl);

        WebElement productDiv = driver.findElement(By.id("Test Product_entry"));
        WebElement addToCartButton = productDiv.findElement(By.xpath(".//input[@value='Add to Cart']"));
        addToCartButton.click();

        WebElement cartRow = driver.findElement(By.id("current_item"));
        WebElement name = cartRow.findElement(By.xpath("./td[2]"));
        assertEquals("Test Product", name.getText());

        cleanUp();
    }

    @Test
    public void increaseProductQuantity() {
        setupProduct();
        driver.get(baseUrl);

        WebElement productDiv = driver.findElement(By.id("Test Product_entry"));
        WebElement addToCartButton = productDiv.findElement(By.xpath(".//input[@value='Add to Cart']"));
        addToCartButton.click();

        WebElement cartRow = driver.findElement(By.id("current_item"));
        WebElement increase = cartRow.findElement(By.xpath("./td[5]"));
        increase.click();

        cartRow = driver.findElement(By.id("current_item"));
        WebElement quantity = cartRow.findElement(By.xpath("./td[1]"));
        assertEquals("2×", quantity.getText());

        cleanUp();
    }

    @Test
    public void decreaseProductQuantity() {
        setupProduct();
        driver.get(baseUrl);

        WebElement productDiv = driver.findElement(By.id("Test Product_entry"));
        WebElement addToCartButton = productDiv.findElement(By.xpath(".//input[@value='Add to Cart']"));
        addToCartButton.click();

        WebElement cartRow = driver.findElement(By.id("current_item"));
        WebElement increase = cartRow.findElement(By.xpath("./td[5]"));
        increase.click();

        cartRow = driver.findElement(By.id("current_item"));
        WebElement decrease = cartRow.findElement(By.xpath("./td[4]"));
        decrease.click();

        cartRow = driver.findElement(By.id("current_item"));
        WebElement quantity = cartRow.findElement(By.xpath("./td[1]"));
        assertEquals("1×", quantity.getText());

        cleanUp();
    }

    @Test
    public void deleteOneProduct() {
        setupProduct();
        driver.get(baseUrl);

        WebElement productDiv = driver.findElement(By.id("Test Product_entry"));
        WebElement addToCartButton = productDiv.findElement(By.xpath(".//input[@value='Add to Cart']"));
        addToCartButton.click();

        WebElement cartRow = driver.findElement(By.id("current_item"));
        WebElement deleteButton = cartRow.findElement(By.xpath("./td[6]"));
        deleteButton.click();

        WebElement notice = driver.findElement(By.id("notice"));
        TestCase.assertEquals("Item successfully deleted from cart.", notice.getText());

        cleanUp();
    }

    @Test
    public void deleteEntireCart() {
        setupProduct();
        driver.get(baseUrl);

        WebElement productDiv = driver.findElement(By.id("Test Product_entry"));
        WebElement addToCartButton = productDiv.findElement(By.xpath(".//input[@value='Add to Cart']"));
        addToCartButton.click();

        WebElement deleteButton = driver.findElement(By.xpath("//input[@value='Empty cart']"));
        deleteButton.click();

        WebElement notice = driver.findElement(By.id("notice"));
        TestCase.assertEquals("Cart successfully deleted.", notice.getText());

        cleanUp();
    }

    @Test
    public void searchProduct() {
        setupProduct();
        driver.get(baseUrl);

        driver.findElement(By.id("search_input")).sendKeys("Test Product");
        waitForElementById("Test Product_entry");
        WebElement productDiv = driver.findElement(By.linkText("Test Product"));
        assertEquals("Test Product", productDiv.getText());

        cleanUp();
    }

    @Test
    public void filterProducts() {
        setupProduct();
        driver.get(baseUrl);

        driver.findElement(By.linkText("Other")).click();
        waitForElementById("Test Product_entry");
        WebElement productDiv = driver.findElement(By.linkText("Test Product"));
        assertEquals("Test Product", productDiv.getText());

        cleanUp();
    }

    @Test
    public void purchaseItems() {
        setupProduct();
        driver.get(baseUrl);

        WebElement productDiv = driver.findElement(By.id("Test Product_entry"));
        WebElement addToCartButton = productDiv.findElement(By.xpath(".//input[@value='Add to Cart']"));
        addToCartButton.click();

        WebElement checkoutButton = driver.findElement(By.xpath("//input[@value='Checkout']"));
        checkoutButton.click();

        driver.findElement(By.id("order_name")).sendKeys("user");
        driver.findElement(By.id("order_address")).sendKeys("userAddress");
        driver.findElement(By.id("order_email")).sendKeys("userEmail");
        WebElement paymentDropdown = driver.findElement(By.id("order_pay_type"));
        Select select = new Select(paymentDropdown);
        select.selectByVisibleText("Credit card");

        WebElement placeOrderButton = driver.findElement(By.xpath("//input[@value='Place Order']"));
        placeOrderButton.click();

        WebElement receipt = driver.findElement(By.id("order_receipt"));
        assertEquals("Thank you for your order\nName: user\nAddress: userAddress\nEmail: userEmail\nPay type: Credit card\n1× Test Product €29.99\nTotal: €29.99", receipt.getText());

        cleanUp();
    }

    // BUG NR 1
    @Test
    public void onlyOtherTypesOfProducts() {
        setupProduct();
        driver.get(baseUrl);

        driver.findElement(By.linkText("Other")).click();
        List<WebElement> cateories = driver.findElements(By.id("category"));
        for (WebElement cateory : cateories) {
            assertEquals("Other", cateory.getText());
        }

        cleanUp();
    }

    // Negative Test
    @Test
    public void testDecreaseProductQuantityToZero() {
        setupProduct();
        driver.get(baseUrl);

        WebElement productDiv = driver.findElement(By.id("Test Product_entry"));
        WebElement addToCartButton = productDiv.findElement(By.xpath(".//input[@value='Add to Cart']"));
        addToCartButton.click();

        WebElement cartRow = driver.findElement(By.id("current_item"));
        WebElement decrease = cartRow.findElement(By.xpath("./td[4]"));
        decrease.click();

        cleanUp();
    }

    // Negative Test
    @Test
    public void testNonsensicalSearch() {
        driver.get(baseUrl);

        driver.findElement(By.id("search_input")).sendKeys("blabla");
        WebElement productDiv = driver.findElement(By.id("Test Product_entry"));
        assertEquals("", productDiv.getText());

    }


}
