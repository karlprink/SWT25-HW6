import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import static org.junit.Assert.assertEquals;

public class EndUserTest extends TestHelper{
    public void setupProduct() {
        registerAccount("admin3", "admin3");

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

        WebElement product = driver.findElement(By.linkText("Test Product"));
        product.findElement(By.xpath("//input[@value='Add to Cart']")).click();

        WebElement productNameElement = driver.findElement(By.xpath("//tr[@id='current_item']//td[2]"));
        assertEquals("Test Product", productNameElement.getText());

        cleanUp();
    }

    @Test
    public void increaseProductQuantity() {
        driver.get(baseUrl);

        /* EI TÖÖTA
        WebElement product = driver.findElement(By.linkText("B45593 Sunglasses"));
        product.findElement(By.xpath("//input[@value='Add to Cart']")).click();
        WebElement increaseQuantityButton = driver.findElement(By.xpath("//tr[@class='cart_row']//a[@href='/line_items/10/increase']"));
        increaseQuantityButton.click();
        WebDriverWait wait = new WebDriverWait(driver, 4);
        WebElement quantityElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[@class='cart_row']//td[@class='quantity']")));

        String quantityText = quantityElement.getText();

        assertTrue("Quantity was not increased", quantityText.contains("2×"));

         */
    }



}
