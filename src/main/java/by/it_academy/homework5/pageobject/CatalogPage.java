package by.it_academy.homework5.pageobject;

import by.it_academy.homework5.framework.AbstractPage;
import org.openqa.selenium.By;

import java.util.List;

public class CatalogPage extends AbstractPage {

    private static final String CATALOG_CLASSIFIER_LINK_XPATH_PATTERN =
            "//*[contains(@class, 'catalog-navigation-classifier__item')]//*[contains(text(), '%s')]";
    private static final String CATALOG_CLASSIFIER_CATEGORY_XPATH_PATTERN =
            "//*[@class='catalog-navigation-list__aside-title' and contains(text(), '%s')]";

    public static final String CATEGORIES_INSIDE_OF_COMPUTERS_AND_NETS = "//*[@class='catalog-navigation-list__aside-title']";
    public static final String CATALOG_CLASSIFIER_LINKS =
            "//*[@class = 'catalog-navigation-classifier ']//*[@class = 'catalog-navigation-classifier__item ']";

    public List<String> getElementsInsideCategory(String str) {
        return getTextsFromWebElements(findElements(By.xpath(str)));
    }

    public CatalogPage clickOnCatalogClassifierLink(String link) {
        waitForElementVisible(By.xpath(String.format(CATALOG_CLASSIFIER_LINK_XPATH_PATTERN, link)))
                .click();
        return this;
    }

    public CatalogPage selectCategory(String category) {
        waitForElementVisible(By.xpath(String.format(CATALOG_CLASSIFIER_CATEGORY_XPATH_PATTERN, category)))
                .click();
        return this;
    }
}