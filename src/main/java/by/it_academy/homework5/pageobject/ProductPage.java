package by.it_academy.homework5.pageobject;

import by.it_academy.homework5.framework.AbstractPage;

public class ProductPage extends AbstractPage {
    public static final String PRODUCTS_INSIDE_OF_ACCESSORIES = "//div[@class='catalog-navigation-list__aside-title' and contains(text()," +
            " 'Комплектующие') and not (contains(text(), 'Комплектующие для'))]" +
            "//following-sibling::div[@class='catalog-navigation-list__dropdown']" +
            "//a//span[@class='catalog-navigation-list__dropdown-title'" +
            " or @class='catalog-navigation-list__dropdown-description']";
}