package yandexAuth.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import static com.codeborne.selenide.Condition.appear;

public class YandexMainPage {
    private final WebDriver driver;
    private final By loginBtn = By.xpath("//div[@data-testid='login-button-wrapper']//button[@aria-label='Войти']"); //button[@data-testid='login-button']/span[text()='Войти']
    private final By loginByYandexIdBtn = By.xpath("//a[@aria-label='Войти через Яндекс ID']");
    private final By profileMenuBtn = By.xpath("//div[@aria-label='Меню профиля']//button");

    public YandexMainPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step(" Нажать кнопку 'Войти'")
    public void clickLoginButton() {
        driver.findElement(loginBtn).click();
    }

    @Step("Нажать кнопку 'Войти через Яндекс ID'")
    public void clickLoginByYandexIdButton() {
        driver.findElement(loginByYandexIdBtn).click();
    }

    @Step("Нажать кнопку 'Меню профиля'")
    public void clickProfileMenuButton() {
        driver.findElement(profileMenuBtn).click();
    }

    @Step("Проверить, что кнопка 'Меню профиля' отображается ")
    public boolean isProfileMenuBtnVisible() {
        boolean isProfileMenuVisible = driver.findElement(profileMenuBtn).isDisplayed();
        boolean isLoginBtnNotVisible = !isElementPresent(loginBtn);

        return isProfileMenuVisible && isLoginBtnNotVisible;
    }

    private boolean isElementPresent(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}
