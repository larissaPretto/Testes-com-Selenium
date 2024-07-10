package org.lcpretto.unirostockTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

//Componentes testados: carousel, h3, p, img
@DisplayName("Testes dos componentes do Carrossel")
public class CarouselTests {
    private static WebDriver driver;
    private static WebDriverWait wait;

    @BeforeAll
    public static void setUp() {
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

    @Nested
    @DisplayName("Testes do Carrossel")
    class CarouselTTests {

        @Test
        @DisplayName("Verificar presença do carrossel")
        public void testCarouselPresence() {
            WebElement carousel = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#slider-216403")));
            assertTrue(carousel.isDisplayed());
        }

        @Test
        @DisplayName("Verificar presença de slide do carrossel")
        public void testSlidePresence() {
            WebElement firstSlide = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".slick-slide[data-slick-index='0']")));
            assertTrue(firstSlide.getAttribute("class").contains("slick-current"));
        }

        @Test
        @DisplayName("Verificar presença e habilitação dos controles de navegação do carrossel")
        public void testCarouselNavigationControlsPresence() {
            WebElement prevButton = driver.findElement(By.cssSelector(".slick-prev"));
            WebElement nextButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".slick-next")));
            nextButton.click();

            assertTrue(prevButton.isDisplayed());
            assertTrue(prevButton.isEnabled());
            assertTrue(nextButton.isDisplayed());
            assertTrue(nextButton.isEnabled());

            if (driver.findElement(By.cssSelector(".slick-slide.slick-current")).getAttribute("data-slick-index").equals("0")) {
                assertTrue(prevButton.getAttribute("class").contains("slick-disabled"), "btn anterior deve estar desabilitado no 1 slide.");
            }
        }

        @Test
        @DisplayName("Verificar presença e habilitação dos pontos de navegação do carrossel")
        public void testCarouselDotsPresence() {
            WebElement dot1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".slick-dots li:nth-child(1) button")));
            WebElement dot2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".slick-dots li:nth-child(2) button")));

            assertTrue(dot1.isDisplayed());
            assertTrue(dot2.isDisplayed());
            assertTrue(dot1.isEnabled());
            assertTrue(dot2.isEnabled());
        }

        @Test
        @DisplayName("Verificar funcionamento dos botões de navegação do carrossel")
        public void testCarouselNavigationButtons() {
            WebElement firstSlide = driver.findElement(By.cssSelector(".slick-slide[data-slick-index='0']"));
            WebElement secondSlide = driver.findElement(By.cssSelector(".slick-slide[data-slick-index='1']"));

            assertTrue(firstSlide.getAttribute("class").contains("slick-current"));

            driver.findElement(By.cssSelector(".slick-next")).click();
            wait.until(ExpectedConditions.attributeContains(secondSlide, "class", "slick-current"));

            assertTrue(secondSlide.getAttribute("class").contains("slick-current"));
        }

        @Test
        @DisplayName("Verificar funcionamento dos pontos de navegação do carrossel")
        public void testCarouselDotsFunctionality() {
            WebElement dot2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".slick-dots li:nth-child(2) button")));
            WebElement secondSlide = driver.findElement(By.cssSelector(".slick-slide[data-slick-index='1']"));

            dot2.click();
            wait.until(ExpectedConditions.attributeContains(secondSlide, "class", "slick-current"));

            assertTrue(secondSlide.getAttribute("class").contains("slick-current"));
        }

        @Test
        @DisplayName("Verificar o carrossel diferentes tamanhos de tela")
        public void testSearchButtonResponsiveness() {
            Dimension smallScreen = new Dimension(800, 600);
            Dimension largeScreen = new Dimension(1920, 1080);

            WebElement carousel = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#slider-216403")));

            driver.manage().window().setSize(smallScreen);
            assertTrue(carousel.isDisplayed());

            driver.manage().window().setSize(largeScreen);
            assertTrue(carousel.isDisplayed());
        }
    }

    @Nested
    @DisplayName("Testes do Título do Carrossel")
    class CarouselTitleTests {
        WebElement title;

        @BeforeEach
        void setUp() {
            title = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".slider__caption .h1-style")));
        }

        @Test
        @DisplayName("Verificar presença do título")
        public void testTitlePresence() {
            assertTrue(title.isDisplayed());
        }

        @Test
        @DisplayName("Verificar se o título é visível")
        public void testTextVisibility() {
            assertTrue(title.isDisplayed());
        }

        @Test
        @DisplayName("Verificar o texto do título")
        public void testTitleText() {
            assertEquals("Traditio et Innovatio", title.getText());
        }

        @Test
        @DisplayName("Verificar a classe do título")
        public void testTitleClass() {
            assertEquals("h1-style", title.getAttribute("class"));
        }

        @Test
        @DisplayName("Verificar se o título é um elemento de cabeçalho")
        public void testTitleTagName() {
            assertEquals("h3", title.getTagName());
        }
    }

    @Nested
    @DisplayName("Testes do Texto do Carrossel")
    class CarouselTextTests {
        WebElement text;

        @BeforeEach
        void setUp() {
            text = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".slider__caption p")));
        }

        @Test
        @DisplayName("Verificar presença do texto")
        public void testTextPresence() {
            assertTrue(text.isDisplayed());
        }

        @Test
        @DisplayName("Verificar o conteúdo do texto")
        public void testTextContent() {
            String expectedText = "Die im Jahr 1419 gegründete Universität Rostock ist die älteste im Ostseeraum. Unsere 600 Jahre an Wissen und Erfahrung sind ein sicheres Fundament für die Themen und Herausforderungen der Zukunft.";
            assertEquals(expectedText, text.getText());
        }

        @Test
        @DisplayName("Verificar a classe do texto")
        public void testTextClass() {
            assertEquals("", text.getAttribute("class"));
        }

        @Test
        @DisplayName("Verificar se o texto é um elemento de parágrafo")
        public void testTextTagName() {
            assertEquals("p", text.getTagName());
        }

        @Test
        @DisplayName("Verificar o alinhamento do texto")
        public void testTextAlignment() {
            String textAlign = text.getCssValue("text-align");
            assertEquals("start", textAlign);
        }

        @Test
        @DisplayName("Verificar o tamanho da fonte do texto")
        public void testTextFontSize() {
            String fontSize = text.getCssValue("font-size");
            assertEquals("15px", fontSize);
        }

        @Test
        @DisplayName("Verificar a altura da linha do texto")
        public void testTextLineHeight() {
            String lineHeight = text.getCssValue("line-height");
            assertEquals("21px", lineHeight);
        }

        @Test
        @DisplayName("Verificar se o texto é visível")
        public void testTextVisibility() {
            assertTrue(text.isDisplayed());
        }
    }

    @Nested
    @DisplayName("Testes da Imagem do Carrossel")
    class CarouselImageTests {
        WebElement image;

        @BeforeEach
        void setUp() {
            image = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".slider__image img")));
        }

        @Test
        @DisplayName("Verificar a presença da imagem")
        public void testImagePresence() {
            assertTrue(image.isDisplayed());
        }

        @Test
        @DisplayName("Verificar o atributo 'alt' da imagem")
        public void testImageAltAttribute() {
            assertEquals("Hauptgebäude", image.getAttribute("alt"));
        }

        @Test
        @DisplayName("Verificar o atributo 'src' da imagem")
        public void testImageSrcAttribute() {
            assertEquals("https://www.uni-rostock.de/storages/uni-rostock/UniHome/Startseite/Slider/1200x340_Hauptgebaeude.jpg", image.getAttribute("src"));
        }

        @Test
        @DisplayName("Verificar a largura da imagem")
        public void testImageWidth() {
            assertEquals("1170", image.getAttribute("width"), "A largura da imagem não está correta.");
        }

        @Test
        @DisplayName("Verificar a altura da imagem")
        public void testImageHeight() {
            assertEquals("332", image.getAttribute("height"), "A altura da imagem não está correta.");
        }

        @Test
        @DisplayName("Verificar a responsividade da imagem")
        public void testImageResponsiveness() {
            Dimension smallScreen = new Dimension(800, 600);
            Dimension largeScreen = new Dimension(1920, 1080);

            driver.manage().window().setSize(smallScreen);
            assertTrue(image.isDisplayed(), "A imagem não está visível em tela pequena.");

            driver.manage().window().setSize(largeScreen);
            assertTrue(image.isDisplayed(), "A imagem não está visível em tela grande.");
        }
    }
}
