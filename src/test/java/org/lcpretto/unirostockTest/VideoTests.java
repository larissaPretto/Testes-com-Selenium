package org.lcpretto.unirostockTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//Componentes testados: Video
@DisplayName("Testes do Video")
public class VideoTests {
    WebElement videoElement;
    WebElement video;
    private static WebDriver driver;
    private static WebDriverWait wait;

    @BeforeAll
    public static void setUpAll() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://www.uni-rostock.de/");
    }

    @AfterEach
    public void destroy() {
        driver.manage().deleteAllCookies();
        driver.get("https://www.uni-rostock.de/");
    }

    @AfterAll
    public static void close(){
        driver.quit();
    }

    @BeforeEach
    void setUp() {
        videoElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("video[controls][poster][preload='metadata']")));
    }

    @Test
    @DisplayName("Verificar presença e visibilidade do vídeo")
    public void testVideoPresence() {
        assertTrue(videoElement.isDisplayed() && videoElement.isEnabled());
    }

    @Test
    @DisplayName("Verificar presença dos controles de vídeo")
    public void testVideoControls() {
        assertNotNull(videoElement.getAttribute("controls"));
    }

    @Test
    @DisplayName("Verificar pôster do vídeo")
    public void testVideoPoster() {
        String posterUrl = videoElement.getAttribute("poster");
        assertEquals("https://web.uni-rostock.de/vs1/mz/pcast/067_oben%20ankommen_fremdprod/067_prerview.jpg", posterUrl);
    }

    @Test
    @DisplayName("Verificar fontes de vídeo")
    public void testVideoSources() {
        List<WebElement> sources = videoElement.findElements(By.tagName("source"));
        assertEquals(1, sources.size());

        WebElement sourceElement = sources.getFirst();
        assertEquals("https://web.uni-rostock.de/vs1/mz/pcast/067_oben%20ankommen_fremdprod/OBEN%20ANKOMMEN_SD_Derivat_YT.mp4", sourceElement.getAttribute("src"));
        assertEquals("video/mp4", sourceElement.getAttribute("type"));
    }

    @Test
    @DisplayName("Verificar o video em diferentes tamanhos de tela")
    public void testSearchButtonResponsiveness() {
        Dimension smallScreen = new Dimension(800, 600);
        Dimension largeScreen = new Dimension(1920, 1080);

        driver.manage().window().setSize(smallScreen);
        assertTrue(videoElement.isDisplayed());

        driver.manage().window().setSize(largeScreen);
        assertTrue(videoElement.isDisplayed());
    }

    @Test
    @DisplayName("Verificar reprodução do vídeo")
    public void testVideoPlayback() {
        JavascriptExecutor js;
        js = (JavascriptExecutor) driver;

        video = driver.findElement(By.cssSelector("div.embed-responsive video"));
        js.executeScript("arguments[0].scrollIntoView(true);", video);

        wait.until(ExpectedConditions.visibilityOf(video));

        assertTrue((Boolean) js.executeScript("return arguments[0].paused;", video));

        js.executeScript("arguments[0].play();", video);

        js.executeScript("arguments[0].pause();", video);

        wait.until(ExpectedConditions.attributeToBe(video, "paused", "true"));
        assertTrue((Boolean) js.executeScript("return arguments[0].paused;", video));
    }

}
