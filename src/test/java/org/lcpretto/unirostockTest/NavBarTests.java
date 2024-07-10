package org.lcpretto.unirostockTest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

//Componentes testados: Nav, dropdown, ul, li, span e link
@DisplayName("Testes dos componentes do Nav")
public class NavBarTests {
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
    @DisplayName("Testes do Nav")
    class NavTests {

        WebElement navElement;

        @BeforeEach
        void setUp() {
            navElement = driver.findElement(By.id("nav"));
        }

        @Test
        @DisplayName("Verificar presença do nav")
        void testNavPresence() {
            assertNotNull(navElement);
        }

        @Test
        @DisplayName("Verificar o atributo aria-label do nav")
        void testNavAriaLabel() {
            String ariaLabel = navElement.getAttribute("aria-label");
            assertEquals("Haupt", ariaLabel);
        }

        @Test
        @DisplayName("Verificar se o nav contém pelo menos um <ul>")
        void testNavContainsUl() {
            List<WebElement> ulElements = navElement.findElements(By.tagName("ul"));
            assertFalse(ulElements.isEmpty());
        }

        @Test
        @DisplayName("Verificar se todos os botões dentro do nav estão visíveis e habilitados")
        void testNavButtonsFunctioning() {
            List<WebElement> buttonElements = navElement.findElements(By.tagName("button"));
            for (WebElement button : buttonElements) {
                assertTrue(button.isDisplayed());
                assertTrue(button.isEnabled());
            }
        }

        @Test
        @DisplayName("Verificar a resposta do botão de pesquisa em diferentes tamanhos de tela")
        public void testSearchButtonResponsiveness() {
            Dimension smallScreen = new Dimension(800, 600);
            Dimension largeScreen = new Dimension(1920, 1080);

            driver.manage().window().setSize(smallScreen);
            assertTrue(navElement.isDisplayed());

            driver.manage().window().setSize(largeScreen);
            assertTrue(navElement.isDisplayed());
        }

        @Test
        @DisplayName("Verificar a funcionalidade do dropdown")
        void testNavDropdownFunctionality() {
            WebElement dropdownButton = navElement.findElement(By.cssSelector("li[data-dropdown='true'] > button"));
            dropdownButton.click();

            WebElement dropdownContent = navElement.findElement(By.cssSelector("li[data-dropdown='true'] .navigation-list__dropdown"));
            wait.until(ExpectedConditions.visibilityOf(dropdownContent));

            assertTrue(dropdownContent.isDisplayed());
        }

        @Test
        @DisplayName("Verificar se o botão do dropdown pode alternar a visibilidade do menu")
        void testDropdownButtonToggle() {
            WebElement dropdownButton = navElement.findElement(By.cssSelector("li[data-dropdown='true'] > button"));
            dropdownButton.click();

            WebElement dropdownMenu = navElement.findElement(By.cssSelector("li[data-dropdown='true'] .navigation-list__dropdown"));
            wait.until(ExpectedConditions.visibilityOf(dropdownMenu));

            dropdownButton.click();
            wait.until(ExpectedConditions.invisibilityOf(dropdownMenu));

            assertFalse(dropdownMenu.isDisplayed());
        }
    }

    @Nested
    @DisplayName("Testes ul do Nav")
    class NavUlTests {

        WebElement navElement;

        @BeforeEach
        void setUp() {
            navElement = driver.findElement(By.id("nav"));
        }

        @Test
        @DisplayName("Verificar presença de pelo menos um <ul> no nav")
        void testUlPresenceInNav() {
            List<WebElement> ulElements = navElement.findElements(By.tagName("ul"));
            assertFalse(ulElements.isEmpty());
        }

        @Test
        @DisplayName("Verificar o número de <ul> no nav")
        void testUlCountInNav() {
            List<WebElement> ulElements = navElement.findElements(By.tagName("ul"));
            int ulCount = ulElements.size();
            assertEquals(79, ulCount );
        }

        @Test
        @DisplayName("Verificar se cada <ul> contém pelo menos um <li>")
        void testUlContainsLi() {
            List<WebElement> ulElements = navElement.findElements(By.tagName("ul"));
            for (WebElement ul : ulElements) {
                List<WebElement> liElements = ul.findElements(By.tagName("li"));
                assertFalse(liElements.isEmpty());
            }
        }

        @Test
        @DisplayName("Verificar se cada <ul> possui a classe 'navigation-list'")
        void testUlHasNavigationClass() {
            List<WebElement> ulElements = navElement.findElements(By.tagName("ul"));
            for (WebElement ul : ulElements) {
                String className = ul.getAttribute("class");
                assertTrue(className.contains("navigation-list"));
            }
        }

        @Test
        @DisplayName("Verificar a hierarquia <ul> -> <li> -> <a>")
        void testUlLiHierarchy() {
            List<WebElement> ulElements = navElement.findElements(By.tagName("ul"));
            for (WebElement ul : ulElements) {
                List<WebElement> liElements = ul.findElements(By.tagName("li"));
                for (WebElement li : liElements) {
                    WebElement link = li.findElement(By.tagName("a"));
                    assertNotNull(link);
                }
            }
        }
    }

    @Nested
    @DisplayName("Testes Li do Nav")
    class NavLiTests {

        WebElement navElement;

        @BeforeEach
        void setUp() {
            navElement = driver.findElement(By.id("nav"));
        }

        @Test
        @DisplayName("Verificar a presença de elementos <li> dentro do <nav>")
        public void testLiPresenceInNav() {
            List<WebElement> liElements = navElement.findElements(By.tagName("li"));
            assertFalse(liElements.isEmpty());
        }

        @Test
        @DisplayName("Verificar se cada <li> contém um link <a>")
        public void testLiContainsLinks() {
            List<WebElement> liElements = navElement.findElements(By.tagName("li"));
            for (WebElement li : liElements) {
                WebElement link = li.findElement(By.tagName("a"));
                assertNotNull(link);
            }
        }

        @Test
        @DisplayName("Verificar se todos os links dentro dos <li> estão habilitados após clicar no botão de dropdown")
        public void testLiLinksAreEnabled() {
            WebElement dropdownButton = navElement.findElement(By.cssSelector("li[data-dropdown='true'] > button"));
            dropdownButton.click();

            WebElement dropdownMenu = navElement.findElement(By.cssSelector("li[data-dropdown='true'] .navigation-list__dropdown"));
            wait.until(ExpectedConditions.visibilityOf(dropdownMenu));

            List<WebElement> liElements = navElement.findElements(By.tagName("li"));
            for (WebElement li : liElements) {
                WebElement link = li.findElement(By.tagName("a"));
                assertTrue(link.isEnabled());
            }
        }

        @Test
        @DisplayName("Verificar se cada <li> possui a classe 'navigation-list__item'")
        public void testLiHasNavigationClass() {
            List<WebElement> liElements = navElement.findElements(By.tagName("li"));
            for (WebElement li : liElements) {
                String className = li.getAttribute("class");
                assertTrue(className.contains("navigation-list__item"));
            }
        }

        @Test
        @DisplayName("Verificar a hierarquia dos níveis dos elementos <li>")
        public void testLiHierarchyLevels() {
            List<WebElement> liElements = navElement.findElements(By.tagName("li"));

            for (WebElement li : liElements) {
                String className = li.getAttribute("class");

                boolean hasValidLevel = className.contains("navigation-list__item--level-1") ||
                        className.contains("navigation-list__item--level-2") ||
                        className.contains("navigation-list__item--level-3");

                assertTrue(hasValidLevel);

                long countLevels = Stream.of("navigation-list__item--level-1", "navigation-list__item--level-2", "navigation-list__item--level-3")
                        .filter(className::contains)
                        .count();

                assertEquals(1, countLevels);
            }
        }
    }

    @Nested
    @DisplayName("Testes de spans do Nav")
    class NavSpanTests {

        WebElement spanElement;

        @BeforeEach
        void setUp() {
            driver.findElement(By.id("nav")).findElement(By.cssSelector("li[data-dropdown='true'] > button")).click();
            spanElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".navigation-list__headline")));
        }

        @Test
        @DisplayName("Verificar texto do span")
        public void testSpanText() {
            assertEquals("Organisation", spanElement.getText());
        }

        @Test
        @DisplayName("Verificar presença e visibilidade do span")
        public void testSpanVisibility() {
            assertTrue(spanElement.isDisplayed() && spanElement.isEnabled());
        }

        @Test
        @DisplayName("Verificar posição do span")
        public void testSpanPosition() {
            Point position = spanElement.getLocation();
            assertTrue(position.getX() > 0 && position.getY() > 0);
        }

        @Test
        @DisplayName("Verificar estilo do span")
        public void testSpanStyle() {
            String fontSize = spanElement.getCssValue("font-size");
            assertEquals("17px", fontSize);
        }

        @Test
        @DisplayName("Verificar a classe CSS do span")
        public void testSpanClass() {
            assertTrue(spanElement.getAttribute("class").contains("navigation-list__headline"));
        }
    }

    @Nested
    @DisplayName("Testes de links na Navegação")
    class LinkTests {

        WebElement linkElement;

        @BeforeEach
        void setUp() {
            driver.findElement(By.id("nav")).findElement(By.cssSelector("li[data-dropdown='true'] > button")).click();
            linkElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".navigation-list__link[href='/universitaet/organisation/fakultaeten/']")));
        }

        @Test
        @DisplayName("Verificar presença e visibilidade do link")
        public void testLinkPresence() {
            assertTrue(linkElement.isDisplayed() && linkElement.isEnabled());
        }

        @Test
        @DisplayName("Verificar texto do link")
        public void testLinkText() {
            assertEquals("Fakultäten", linkElement.getText());
        }

        @Test
        @DisplayName("Verificar funcionalidade do link")
        public void testLinkFunctionality() {
            linkElement.click();
            wait.until(ExpectedConditions.urlContains("/universitaet/organisation/fakultaeten/"));
            assertEquals("https://www.uni-rostock.de/universitaet/organisation/fakultaeten/", driver.getCurrentUrl());
        }

    }

}