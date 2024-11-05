package yandexAuth.tests;

import io.qameta.allure.Step;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WindowType;
import yandexAuth.pages.YandexIdPage;

import java.util.stream.Stream;


public class YandexAuthTests extends BaseTest {
    YandexIdPage yandexIdPage;

    @BeforeEach
    void setup() {
        yandexIdPage = new YandexIdPage(driver);
    }

    @ParameterizedTest
    @MethodSource("loginData")
    @DisplayName("Авторизация через Яндекс ID с корректными данными")
    void loginWithYandexID(String testName, String loginType) {
        String login = loginType.equals("fullLogin") ? props.fullLogin() : props.shortLogin();
        String password = props.password();

        performLogin(login, password);
        Assertions.assertTrue(yandexMainPage.isProfileMenuBtnVisible(), "Кнопка 'меню профиля' не отображается");
    }

    static Stream<Arguments> loginData() {
        return Stream.of(
                Arguments.of("ТК1: Успешная авторизация через Яндекс ID с корректными данными", "fullLogin"),
                        Arguments.of("ТК2: Успешная авторизация через Яндекс ID с корректными данными (короткий логин)", "shortLogin"));
    }

    @Test
    @DisplayName("ТК3: Пользователь остался авторизованным после закрытия вкладки")
    void loginWithSessionPersistence() {
        performLogin(props.fullLogin(), props.password());
        Assertions.assertTrue(yandexMainPage.isProfileMenuBtnVisible(), "Кнопка 'меню профиля' не отображается");
        // Сохраняем текущую вкладку и открываем новую
        String currentWindow = driver.getWindowHandle();
        driver.switchTo().newWindow(WindowType.TAB);
        // Закрываем текущую вкладку
        driver.switchTo().window(currentWindow).close();
        // Переключаемся на новую вкладку и открываем основной URL
        driver.switchTo().window(driver.getWindowHandles().iterator().next());
        openBaseUrl();
        Assertions.assertTrue(yandexMainPage.isProfileMenuBtnVisible(), "Кнопка 'меню профиля' не отображается");
    }

    @Test
    @DisplayName("ТК7: Авторизация с некорректным паролем")
    @Tag("Negative")
    void loginWithIncorrectPassword() {
        String wrongPass = "wrongPassword";
        String expectedErrorMessage = "Неверный пароль";

        loginWithYandexId();
        yandexIdPage.fillLoginField(props.fullLogin());
        yandexIdPage.clickSignInButton();
        yandexIdPage.fillPasswordField(wrongPass);
        yandexIdPage.clickContinueBtnButton();

        String actualErrorMessage = yandexIdPage.getErrorMessagePassword();
        verifyErrorMessage(expectedErrorMessage, actualErrorMessage);
    }

    @Test
    @DisplayName("ТК8: Авторизация с некорректным логином")
    @Tag("Negative")
    void loginWithIncorrectLogin() {
        String login = "rao-test-wronguser";
        String expectedErrorMessage = "Нет такого аккаунта. Проверьте логин или войдите по телефону";

        loginWithYandexId();
        yandexIdPage.fillLoginField(login);
        yandexIdPage.clickSignInButton();

        String actualErrorMessage = yandexIdPage.getErrorMessageLogin();
        verifyErrorMessage(expectedErrorMessage, actualErrorMessage);
    }

    @Test
    @DisplayName("ТК9: Авторизация с пустым логином")
    @Tag("Negative")
    void loginWithEmptyLogin() {
        String expectedErrorMessage = "Логин не указан";

        loginWithYandexId();
        yandexIdPage.fillLoginField("");
        yandexIdPage.clickSignInButton();

        String actualErrorMessage = yandexIdPage.getErrorMessageLogin();
        verifyErrorMessage(expectedErrorMessage, actualErrorMessage);
    }

    @Test
    @DisplayName("ТК10: Авторизация с пустым паролем")
    @Tag("Negative")
    void loginWithEmptyPassword() {
        String expectedErrorMessage = "Пароль не указан";

        loginWithYandexId();
        yandexIdPage.fillLoginField(props.fullLogin());
        yandexIdPage.clickSignInButton();
        yandexIdPage.clickContinueBtnButton();

        String actualErrorMessage = yandexIdPage.getErrorMessagePassword();
        verifyErrorMessage(expectedErrorMessage, actualErrorMessage);
    }

    @Step("Проверка сообщения об ошибке")
    protected void verifyErrorMessage(String expectedErrorMessage, String actualErrorMessage) {
        Assertions.assertEquals(expectedErrorMessage, actualErrorMessage, "Сообщение об ошибке не соответствует ожидаемому");
    }

    private void performLogin(String login, String password) {
        loginWithYandexId();
        yandexIdPage.fillLoginField(login);
        yandexIdPage.clickSignInButton();
        yandexIdPage.fillPasswordField(password);
        yandexIdPage.clickContinueBtnButton();
    }

    private void loginWithYandexId() {
        yandexMainPage.clickLoginButton();
        yandexMainPage.clickLoginByYandexIdButton();
    }
}
