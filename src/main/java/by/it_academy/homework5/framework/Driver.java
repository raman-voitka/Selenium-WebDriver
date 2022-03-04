package by.it_academy.homework5.framework;

import java.util.Arrays;

public enum Driver {

    CHROME("chrome", new ChromeDriverCreator());

    private String driverType;
    private WebDriverCreator webDriverCreator;

    Driver(String driverType, WebDriverCreator webDriverCreator) {
        this.driverType = driverType;
        this.webDriverCreator = webDriverCreator;
    }

    public String getDriverType() {
        return driverType;
    }

    public WebDriverCreator getWebDriverCreator() {
        return webDriverCreator;
    }

    public static Driver getByDriverType(String driverType) {
        return Arrays.stream(Driver.values())
                .filter(driver -> driver.getDriverType().equals(driverType))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Driver type'" + driverType +
                        "' is not specified in Driver enum"));
    }
}