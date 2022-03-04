package by.it_academy.homework5.framework;

import com.google.common.base.Supplier;
import com.google.common.collect.Iterables;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Sleeper;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public abstract class AbstractPage {

    protected static final Logger LOG = LoggerFactory.getLogger(AbstractPage.class);
    private static final String SCRIPT_FOR_SCROLL_INTO_VIEW = "arguments[0].scrollIntoView(false)";
    private static final String SCRIPT_WAIT_FOR_PAGE_TO_LOAD = "return document.readyState";
    private static final int DEFAULT_IMPLICIT_WAIT_TIMEOUT = 10;
    private static final int DRIVER_WAIT_JQUERY_TIME = 30;
    private static final int DRIVER_WAIT_TIME = 60;

    private final WebDriver driver;
    private WebDriverDiscovery webDriverDiscovery;

    public AbstractPage() {
        webDriverDiscovery = new WebDriverDiscovery();
        driver = webDriverDiscovery.getWebDriver();
    }

    public WebElement findElement(By by) {
        return driver.findElement(by);
    }

    public List<WebElement> findElements(By by) {
        return driver.findElements(by);
    }

    public void navigate(String url) {
        driver.get(url);
    }

    public List<WebElement> waitForElementsVisible(By by) {
        Wait<WebDriver> wait = new WebDriverWait(driver, (long) DRIVER_WAIT_TIME);
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
    }

    public WebElement waitForElementVisible(By by) {
        Wait<WebDriver> wait = new WebDriverWait(driver, (long) DRIVER_WAIT_TIME);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public List<WebElement> waitForElementsPresence(By by) {
        Wait<WebDriver> wait = new WebDriverWait(driver, (long) DRIVER_WAIT_TIME);
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
    }

    public WebElement waitForElementPresence(By by) {
        Wait<WebDriver> wait = new WebDriverWait(driver, (long) DRIVER_WAIT_TIME);
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public WebElement waitForElementToBeClickable(By by) {
        Wait<WebDriver> wait = new WebDriverWait(driver, (long) DRIVER_WAIT_TIME);
        return wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    public List<String> getTextsFromWebElements(List<WebElement> webElements, String splitByRegex) {
        return webElements.stream().map(a -> StringUtils.split(a.getText(), splitByRegex)).flatMap(Arrays::stream).filter(StringUtils::isNotEmpty).collect(Collectors.toList());
    }

    /**
     * Get list of each WebElement text from the given WebElement list.
     *
     * @param webElements List of WebElements which text do you need {@link WebElement#getText()}.                    Also, each string will be trimmed.
     * @return List with trimmed strings from each WebElement of list
     */
    public List<String> getTextsFromWebElements(List<WebElement> webElements) {
        return getTextsFromWebElements(webElements, StringUtils.EMPTY);
    }


    public void refreshPage() {
        String currentURL = driver.getCurrentUrl();
        driver.navigate().to(currentURL);
        driver.navigate().refresh();
    }

    public void sleepSeconds(int value) {
        try {
            Sleeper.SYSTEM_SLEEPER.sleep(Duration.ofSeconds(value));
        } catch (InterruptedException e) {
            LOG.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    public void selectCheckBoxIfNotAlreadySelected(WebElement element) {
        if (!element.isSelected()) {
            element.click();
        }
    }

    public void switchToFrame(WebElement element) {
        driver.switchTo().frame(element);
    }

    public void switchToFrame(String frameName) {
        this.switchOutFromFrame();
        driver.switchTo().frame(frameName);
    }

    public Alert switchToAlert() {
        return driver.switchTo().alert();
    }

    public void switchToWindow(String index) {
        driver.switchTo().window(index);
    }

    public void switchOutFromFrame() {
        driver.switchTo().defaultContent();
    }

    public boolean isElementDisplayed(By by) {
        return this.isExists(by) && this.getFirstVisibleElement(by) != null;
    }

    public boolean isExists(By by) {
        return this.defineStateWithoutTimeouts(() -> {
            return !driver.findElements(by).isEmpty();
        });
    }

    private WebElement getFirstVisibleElement(By locator) {
        List<WebElement> elements = driver.findElements(locator);
        for (WebElement webElement : elements) {
            try {
                if (webElement.isDisplayed()) {
                    return webElement;
                }
            } catch (StaleElementReferenceException e) {
                continue;
            }
        }
        return null;
    }

    private boolean defineStateWithoutTimeouts(Supplier<Boolean> defineAction) {
        this.removeTimeOuts();
        try {
            return (Boolean) defineAction.get();
        } finally {
            this.setTimeOutsToDefault();
        }
    }

    private void setTimeOutsToDefault() {
        driver.manage().timeouts().implicitlyWait((long) DEFAULT_IMPLICIT_WAIT_TIMEOUT, TimeUnit.SECONDS);
    }

    private void removeTimeOuts() {
        driver.manage().timeouts().implicitlyWait(3L, TimeUnit.SECONDS);
    }

    public void acceptDialogIfAppears() {
        try {
            driver.switchTo().alert().accept();
        } catch (NoAlertPresentException e) {
            LOG.error("No Alert present", e);
        }
    }

    public void waitForPageToLoad() {
        new WebDriverWait(driver, DRIVER_WAIT_TIME).until(
                webDriver -> ((JavascriptExecutor) webDriver)
                        .executeScript(SCRIPT_WAIT_FOR_PAGE_TO_LOAD).equals("complete"));
    }

    public WebElement waitForElementToBeClickable(WebElement element) {
        Wait<WebDriver> wait = new WebDriverWait(driver, (long) DRIVER_WAIT_TIME, 100L);
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void waitForElementInvisible(By by) {
        Wait<WebDriver> wait = new WebDriverWait(driver, (long) DRIVER_WAIT_JQUERY_TIME);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    public boolean waitForElementToDisappear(By byLocator) {
        Wait<WebDriver> wait = new WebDriverWait(driver, (long) DRIVER_WAIT_TIME, 100L);
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(byLocator));
    }

    public void selectFirstItemFromList(List<WebElement> items) {
        WebElement firstItem = Optional.ofNullable(Iterables.getFirst(items, null))
                .orElseThrow(() -> new NoSuchElementException("First item is not found in the list"));
        firstItem.click();
    }

    public void fillInFieldWithValue(WebElement field, String value) {
        field.clear();
        field.sendKeys(value);
    }

    public void scrollIntoView(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript(SCRIPT_FOR_SCROLL_INTO_VIEW, element);
        } catch (WebDriverException driverDoesNotSupportJavascriptTooBad) {
            LOG.error("Error scroll into view for element. Error message: {}",
                    driverDoesNotSupportJavascriptTooBad.getMessage());
        }
    }

    public void closeBrowser() {
        try {
            driver.quit();
        } catch (Exception e) {
            LOG.error("CLOSE BROWSER ERROR: {}", e.getMessage(), e);
        }
    }
}