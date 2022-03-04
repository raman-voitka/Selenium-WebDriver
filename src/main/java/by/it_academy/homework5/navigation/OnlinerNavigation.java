package by.it_academy.homework5.navigation;

import by.it_academy.homework5.pageobject.OnlinerHomePage;

public class OnlinerNavigation {
    public static void navigateToOnlinerHomePage() {
        new OnlinerHomePage().navigate("https://www.onliner.by/");
    }
}