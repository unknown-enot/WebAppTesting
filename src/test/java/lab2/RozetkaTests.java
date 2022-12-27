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

public class RozetkaTests {
    private final String baseUrl = "https://rozetka.com.ua/ua/";
    private WebDriver chromeDriver;

    @BeforeClass(alwaysRun = true)
    private void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--start-fullscreen");
        chromeOptions.setImplicitWaitTimeout(Duration.ofSeconds(5));

        this.chromeDriver = new ChromeDriver(chromeOptions);
    }

    @BeforeMethod
    private void setUpPreconditions() {
        this.chromeDriver.get(baseUrl);
    }

    @AfterClass(alwaysRun = true)
    private void shutDown() {
        this.chromeDriver.quit();
    }

    @Test
    private void testHeaderLayoutExists() {
        WebElement header = chromeDriver.findElement(By.className("header-layout"));

        Assert.assertNotNull(header);
    }

    @Test
    private void testInputFieldExists() {
        WebElement inputField = getInputField();

        Assert.assertNotNull(inputField);
    }

    @Test
    private void testSearchButtonExists() {
        WebElement searchButton = getSearchButton();

        Assert.assertNotNull(searchButton);
    }

    @Test
    private void testInputFieldSearch_ifTextIsNotEmpty() {
        WebElement inputField = getInputField();

        inputField.sendKeys("Some input text");
        inputField.sendKeys(Keys.ENTER);

        Assert.assertNotEquals(this.chromeDriver.getCurrentUrl(), baseUrl);
    }

    @Test
    private void testInputFieldSearch_ifTextIsEmpty() {
        WebElement inputField = getInputField();

        inputField.sendKeys("");
        inputField.sendKeys(Keys.ENTER);

        Assert.assertEquals(this.chromeDriver.getCurrentUrl(), baseUrl);
    }

    @Test
    private void testButtonClickSearch_ifTextIsNotEmpty() {
        WebElement inputField = getInputField();
        WebElement searchButton = getSearchButton();

        inputField.sendKeys("Some input text");
        searchButton.sendKeys(Keys.ENTER);

        Assert.assertNotEquals(this.chromeDriver.getCurrentUrl(), baseUrl);
    }

    @Test
    private void testButtonClickSearch_ifTextIsEmpty() {
        WebElement inputField = getInputField();
        WebElement searchButton = getSearchButton();

        inputField.sendKeys("");
        searchButton.sendKeys(Keys.ENTER);

        Assert.assertEquals(this.chromeDriver.getCurrentUrl(), baseUrl);
    }

    @Test
    private void testClickOnPrevButtonOfSlider() {
        WebElement prevButton = getPrevButtonOfSlider();

        for (int i = 0; i < 10; i++) {
            if (!prevButton.isEnabled()) break;

            prevButton.click();
        }

        Assert.assertTrue(prevButton.getAttribute("class").contains("visible-hidden"));
        Assert.assertFalse(prevButton.isEnabled());
    }

    @Test
    private void testClickOnNextButtonOfSlider() {
        WebElement nextButton = getNextButtonOfSlider();

        for (int i = 0; i < 10; i++) {
            if (!nextButton.isEnabled()) break;

            nextButton.click();
        }

        Assert.assertTrue(nextButton.getAttribute("class").contains("visible-hidden"));
        Assert.assertFalse(nextButton.isEnabled());
    }

    private WebElement getInputField() {
        return this.chromeDriver.findElement(By.xpath("/html/body/app-root/div/div/rz-header/rz-main-header/header/div/div/div/form/div/div/input"));
    }

    private WebElement getSearchButton() {
        return this.chromeDriver.findElement(By.xpath("/html/body/app-root/div/div/rz-header/rz-main-header/header/div/div/div/form/button"));
    }

    private WebElement getPrevButtonOfSlider() {
        return this.chromeDriver.findElement(By.xpath("/html/body/app-root/div/div/rz-main-page/div/main/rz-main-page-content/div/rz-top-slider/app-slider/div/button[1]"));
    }

    private WebElement getNextButtonOfSlider() {
        return this.chromeDriver.findElement(By.xpath("/html/body/app-root/div/div/rz-main-page/div/main/rz-main-page-content/div/rz-top-slider/app-slider/div/button[2]"));
    }
}
