package by.it_academy.homework5;

import by.it_academy.homework5.navigation.OnlinerNavigation;
import by.it_academy.homework5.pageobject.CatalogPage;
import by.it_academy.homework5.pageobject.OnlinerHeader;
import by.it_academy.homework5.pageobject.ProductPage;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class OnlinerTest {
    private static OnlinerHeader onlinerHeader = new OnlinerHeader();
    private CatalogPage catalogPage;

    @BeforeAll
    public static void navigateToOnliner() {
        OnlinerNavigation.navigateToOnlinerHomePage();
    }

    @BeforeEach
    public void navigateToCatalogOfOnliner() {
        catalogPage = onlinerHeader.clickOnCatalogNavigationLink();
    }

    @DisplayName("Test That Every Category Of Catalog Is Not Empty")
    @Test
    public void testEveryCategoryOfCatalogIsNotEmpty() {
        List<String> categoriesOfCatalog = catalogPage
                .getElementsInsideCategory(CatalogPage.CATALOG_CLASSIFIER_LINKS);
        assertThat(categoriesOfCatalog)
                .noneMatch(Objects::isNull);
    }

    @DisplayName("Test That Category 'Computers And Nets' Contains Four Preset Elements")
    @Test
    public void testCategory_ComputersAndNets_HasFourPresetElements() {
        List<String> categoriesOfComputersAndNets = navigateToCategoryComputersAndNets()
                .getElementsInsideCategory(CatalogPage.CATEGORIES_INSIDE_OF_COMPUTERS_AND_NETS);
        assertThat(categoriesOfComputersAndNets)
                .anyMatch(n -> n.equals("Ноутбуки, компьютеры, мониторы"))
                .anyMatch(n -> n.equals("Комплектующие"))
                .anyMatch(n -> n.equals("Хранение данных"))
                .anyMatch(n -> n.equals("Сетевое оборудование"));
    }

    @DisplayName("Test That Products And Their Description Of Category 'Accessories' Are Not Empty")
    @Test
    public void testProductsAndDescriptionOfAccessoriesAreNotEmpty() {
        List<String> productsAndDescriptionsOfAccessories = navigateToCategoryComputersAndNets()
                .selectCategory("Комплектующие")
                .getElementsInsideCategory(ProductPage.PRODUCTS_INSIDE_OF_ACCESSORIES);
        assertThat(productsAndDescriptionsOfAccessories)
                .noneMatch(Objects::isNull);
    }

    private CatalogPage navigateToCategoryComputersAndNets() {
        return catalogPage.clickOnCatalogClassifierLink("Компьютеры и\u00A0сети");
    }

    @AfterAll
    public static void closeBrowser() {
        onlinerHeader.closeBrowser();
    }
}