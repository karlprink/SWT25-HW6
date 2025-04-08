import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class BasicTest extends TestHelper {


    private String username = "admin";
    private String password = "admin";

    @Test
    public void titleExistsTest(){
        String expectedTitle = "ST Online Store";
        String actualTitle = driver.getTitle();

        assertEquals(expectedTitle, actualTitle);
    }


    /*
    In class Exercise

    Fill in loginLogoutTest() and login mehtod in TestHelper, so that the test passes correctly.

     */
    @Test
    public void loginLogoutTest(){

        login(username, password);

        // assert that correct page appeared
        WebElement adminHeader = driver.findElement(new By.ByLinkText("Admin"));
        assertNotNull(adminHeader);
        // ...

        logout();
    }

    /*
    In class Exercise

     Write a test case, where you make sure, that one can’t log in with a false password

     */
    @Test
    public void loginFalsePassword() {
        login(username, "falsePassword");

        // assert that correct page appeared
        WebElement errorMessage = driver.findElement(By.id("notice"));
        assertNotNull(errorMessage);
    }

}
