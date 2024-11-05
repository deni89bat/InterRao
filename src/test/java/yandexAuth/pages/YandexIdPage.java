package yandexAuth.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class YandexIdPage {
    private final WebDriver driver;
    private final By loginInput = By.xpath("//input[@id='passp-field-login']");
    private final By passwordInput = By.xpath("//input[@id='passp-field-passwd']");
    private final By signInBtn = By.xpath(" //button[@id='passp:sign-in']");
    private final By continueBtn = By.xpath(" //button[@id='passp:sign-in' and .//span[text()='Продолжить']]");

    private final By errorMessageLogin = By.xpath("//div[@id='field:input-login:hint']");
    private final By errorMessagePassword = By.xpath("//div[@id='field:input-passwd:hint']");

    public YandexIdPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("В поле 'Логин или email' ввести {login}")
    public void fillLoginField(String login) {
        driver.findElement(loginInput).sendKeys(login);
    }

    @Step("Нажать кнопку 'Войти'")
    public void clickSignInButton() {
        driver.findElement(signInBtn).click();
    }

    @Step("В поле 'Введите пароль' ввести {password}")
    public void fillPasswordField(String password) {
        driver.findElement(passwordInput).sendKeys(password);
    }

    @Step("Кликнуть по кнопке 'Продолжить'")
    public void clickContinueBtnButton() {
        driver.findElement(continueBtn).click();
    }

    public String getErrorMessageLogin() {
        return getErrorMessage(errorMessageLogin);
    }

    public String getErrorMessagePassword() {
        return getErrorMessage(errorMessagePassword);
    }

    private String getErrorMessage(By errorElement) {
        return driver.findElement(errorElement).getText();
    }
}
