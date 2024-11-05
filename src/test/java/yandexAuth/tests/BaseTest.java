package yandexAuth.tests;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import lombok.SneakyThrows;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import yandexAuth.pages.YandexMainPage;
import yandexAuth.utils.UIProps;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


public class BaseTest {
    WebDriver driver;
    UIProps props = ConfigFactory.create(UIProps.class);
    YandexMainPage yandexMainPage;

    @SneakyThrows
    @BeforeEach
    void setUp() {
        ChromeOptions options = createChromeOptions();
        URL url = new URL(props.selenoidUrl());
        driver = new RemoteWebDriver(url, options);
        //driver = new ChromeDriver(options);      //Локальный запуск
        driver.manage().window().setSize(new Dimension(1920, 1080));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(props.pageLoadTimeout()));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(props.implicitWait()));
        openBaseUrl();
    }

    @Step("Перейти на страницу https://yandex.ru/")
    public YandexMainPage openBaseUrl() {
        yandexMainPage = new YandexMainPage(driver);
        driver.get(props.baseURL());
        return yandexMainPage;
    }

    private ChromeOptions createChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--accept-lang=ru");
        return options;
    }

    @AfterEach
    @Step("Сделать скриншот и закрыть драйвер")
    void tearDown() throws IOException {
        takeScreenshot(driver); // на большом кол-ве тестов убрать, чтобы не забивать память. А тут для демонстрации в отчёте
        driver.quit();
    }


    @Step("Сделать скриншот")
    @Attachment(value = "Скриншот", type = "image/png")
    public static byte[] takeScreenshot(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}

