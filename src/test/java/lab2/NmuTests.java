package lab2;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class NmuTests {
    private WebDriver chromeDriver;
    private final String baseUrl = "https://www.nmu.org.ua/ua/";

    @BeforeClass(alwaysRun = true)
    private void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--start-fullscreen");
        chromeOptions.setImplicitWaitTimeout(Duration.ofSeconds(15));

        this.chromeDriver = new ChromeDriver(chromeOptions);
    }

    @BeforeMethod
    private void setUpPreconditions() {
        chromeDriver.get(baseUrl);
    }

    @AfterClass(alwaysRun = true)
    private void shutDown() {
        chromeDriver.quit();
    }

    @Test
    private void testHeaderExists() {
        WebElement header = chromeDriver.findElement(By.id("header"));

        Assert.assertNotNull(header);
    }

    @Test
    private void testClickOnStudentButton() {
        WebElement studentButton = chromeDriver.findElement(By.xpath("/html/body/center/div[4]/div/div[1]/ul/li[4]/a"));

        Assert.assertNotNull(studentButton);

        studentButton.click();

        Assert.assertNotEquals(chromeDriver.getCurrentUrl(), baseUrl);
    }

    @Test
    private void testSearchFieldReceivesInput() {
        WebElement searchField = chromeDriver.findElement(By.tagName("input"));

        Assert.assertNotNull(searchField);

        String inputText = "Some info";
        searchField.sendKeys(inputText);

        Assert.assertEquals(searchField.getText(), inputText);
    }

    @Test
    private void testClickOnSearchField() {
        WebElement searchField = chromeDriver.findElement(By.tagName("input"));

        searchField.sendKeys(Keys.ENTER);

        Assert.assertNotEquals(chromeDriver.getCurrentUrl(), baseUrl);
    }

    @Test
    private void testClickOnPrevButtonOfSlider() {
        WebElement nextButton = chromeDriver.findElement(By.className("next"));
        WebElement prevButton = chromeDriver.findElement(By.className("prev"));

        for (int i = 0; i < 10; i++) {
            prevButton.click();
        }

        Assert.assertTrue(prevButton.getAttribute("class").contains("disabled"));
        Assert.assertFalse(nextButton.getAttribute("class").contains("disabled"));
    }

    @Test
    private void testClickOnNextButtonOfSlider() {
        WebElement nextButton = chromeDriver.findElement(By.className("next"));
        WebElement prevButton = chromeDriver.findElement(By.className("prev"));

        for (int i = 0; i < 10; i++) {
            nextButton.click();
        }

        Assert.assertTrue(nextButton.getAttribute("class").contains("disabled"));
        Assert.assertFalse(prevButton.getAttribute("class").contains("disabled"));
    }
}
