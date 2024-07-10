package org.lcpretto.unirostockTest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//Componentes testados: Form, btn, label, field e checkbox
@DisplayName("Testes dos componentes de Pesquisa")
public class SearchTests {
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
    public void tearDown() {
        driver.manage().deleteAllCookies();
        driver.get("https://www.uni-rostock.de/");
    }

    @AfterAll
    public static void close(){
        driver.quit();
    }

    private WebElement getSearchButton() {
        return driver.findElement(By.cssSelector("button[data-content=\"search\"]"));
    }

    private WebElement getSearchField() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search-field")));
    }

    private WebElement getSearchForm() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("form[action=\"/suche/\"]")));
    }

    @Nested
    @DisplayName("Testes do Botão que Abre a Pesquisa")
    class SearchButtonTests {

        @Test
        @DisplayName("Verificar presença e habilitação do botão")
        public void testSearchButtonPresenceAndEnabled() {
            WebElement searchButton = getSearchButton();
            assertTrue(searchButton.isDisplayed());
            assertTrue(searchButton.isEnabled());
        }

        @Test
        @DisplayName("Verificar texto do botão")
        public void testSearchButtonText() {
            assertEquals("Suche", getSearchButton().getText());
        }

        @Test
        @DisplayName("Verificar funcionalidade do botão")
        public void testSearchButtonFunctionality() {
            getSearchButton().click();
            assertTrue(getSearchField().isDisplayed());
        }

        @Test
        @DisplayName("Verificar a resposta do botão em diferentes tamanhos de tela")
        public void testSearchButtonResponsiveness() {
            Dimension smallScreen = new Dimension(800, 600);
            Dimension largeScreen = new Dimension(1920, 1080);

            driver.manage().window().setSize(smallScreen);
            assertTrue(getSearchButton().isDisplayed());

            driver.manage().window().setSize(largeScreen);
            assertTrue(getSearchButton().isDisplayed());
        }

        @Test
        @DisplayName("Verificar se o botão possui o estilo adequado")
        public void testSearchButtonCssClass() {
            String classAttribute = getSearchButton().getAttribute("class");
            assertTrue(classAttribute.contains("header-submenu__icon"));
            assertTrue(classAttribute.contains("header-submenu__icon--search"));
        }
    }

    @Nested
    @DisplayName("Testes do Form da Pesquisa")
    class SearchFormTests {

        @BeforeEach
        void setUp() {
            getSearchButton().click();
        }

        @Test
        @DisplayName("Verificar presença do form")
        public void testSearchFormPresence() {
            assertTrue(getSearchForm().isDisplayed());
        }

        @Test
        @DisplayName("Verificar o redirecionamento do form")
        public void testSearchFormAction() {
            String action = getSearchForm().getAttribute("action");
            assertNotNull(action);
            assertEquals("https://www.uni-rostock.de/suche/", action);
        }

        @Test
        @DisplayName("Verificar a submissão do form com dados válidos")
        public void testSearchFormSubmissionValidInput() {
            getSearchField().sendKeys("project");
            getSearchForm().submit();

            WebElement result = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("search-results__num-rows")));
            assertTrue(result.isDisplayed());
        }

        @Test
        @DisplayName("Verificar a submissão do form com dados inválidos")
        public void testSearchFormSubmissionInvalidInput() {
            getSearchField().sendKeys("invalidinput");
            getSearchForm().submit();

            WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert--danger")));
            assertTrue(errorMessage.isDisplayed());
            assertEquals("Keine Ergebnisse gefunden.", errorMessage.getText());
        }

        @Test
        @DisplayName("Verificar o método de envio do form")
        public void testSearchFormMethod() {
            String method = getSearchForm().getAttribute("method");
            assertNotNull(method);
            assertEquals("get", method.toLowerCase());
        }

        @Test
        @DisplayName("Verificar se o botão de submit está presente e habilitado")
        public void testSearchFormSubmitButton() {
            WebElement submitButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button[type='Submit']")));
            assertTrue(submitButton.isDisplayed());
            assertTrue(submitButton.isEnabled());
        }
    }

    @Nested
    @DisplayName("Testes do Campo de Pesquisa")
    class SearchFieldTests {

        @BeforeEach
        void setUp() {
            getSearchButton().click();
        }

        @Test
        @DisplayName("Verificar a pesquisa com caracteres especiais")
        public void testSearchWithSpecialCharacters() {
            getSearchField().sendKeys("@#$%^&*()!");
            driver.findElement(By.name("Submit")).click();

            WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert--danger")));
            assertTrue(errorMessage.isDisplayed());
            assertEquals("Keine Ergebnisse gefunden.", errorMessage.getText());
        }

        @Test
        @DisplayName("Verificar a pesquisa com entrada vazia")
        public void testSearchFunctionalityWithEmptyInput() {
            getSearchField().clear();
            driver.findElement(By.name("Submit")).click();

            WebElement info = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".csc-frame p strong")));
            assertTrue(info.isDisplayed());
            assertEquals("Bitte geben Sie einen Suchbegriff ein.", info.getText());
        }

        @Test
        @DisplayName("Verificar a pesquisa com espaços")
        public void testSearchWithSpaces() {
            getSearchField().sendKeys("    ");
            driver.findElement(By.name("Submit")).click();

            WebElement info = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".csc-frame p strong")));
            assertTrue(info.isDisplayed());
            assertEquals("Bitte geben Sie einen Suchbegriff ein.", info.getText());
        }

        @Test
        @DisplayName("Verificar a pesquisa com entrada longa")
        public void testSearchWithLongInput() {
            String longInput = new String(new char[500]).replace('\0', 'a');
            getSearchField().sendKeys(longInput);
            driver.findElement(By.name("Submit")).click();

            WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("alert--danger")));
            assertTrue(errorMessage.isDisplayed());
            assertEquals("Keine Ergebnisse gefunden.", errorMessage.getText());
        }

        @Test
        @DisplayName("Verificar a responsividade do campo de pesquisa")
        public void testSearchFieldResponsiveness() {
            driver.manage().window().setSize(new Dimension(800, 600));
            assertTrue(getSearchField().isDisplayed());

            driver.manage().window().setSize(new Dimension(1920, 1080));
            assertTrue(getSearchField().isDisplayed());
        }

        @Test
        @DisplayName("Verificar a pesquisa com combinação de letras e números")
        public void testSearchWithAlphanumericInput() {
            String alphanumericInput = "test123";
            getSearchField().sendKeys(alphanumericInput);
            driver.findElement(By.name("Submit")).click();

            WebElement result = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("search-results__num-rows")));
            assertTrue(result.isDisplayed());
        }
    }

    @Nested
    @DisplayName("Testes do label campo de pesquisa")
    class SearchFieldLabelTests {
        WebElement label;

        @BeforeEach
        void setUp() {
            getSearchButton().click();
            label = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("label[for='search-field']")));
        }

        @Test
        @DisplayName("Verificar presença do label")
        public void testLabelPresence() {
            assertTrue(label.isDisplayed());
        }

        @Test
        @DisplayName("Verificar o texto do label")
        public void testLabelText() {
            assertEquals("Suche", label.getText());
        }

        @Test
        @DisplayName("Verificar associação do label com o campo de entrada")
        public void testLabelAssociation() {
            assertEquals("search-field", label.getAttribute("for"));
            assertEquals(getSearchField(), driver.findElement(By.cssSelector("input#search-field")));
        }

        @Test
        @DisplayName("Verificar estilo do label")
        public void testLabelStyle() {
            String classAttribute = label.getAttribute("class");
            assertTrue(classAttribute.contains("h3-style"));
        }
    }

    @Nested
    @DisplayName("Testes do Checkbox de Filtro da Pesquisa")
    class SearchFilterCheckboxTests {

        @BeforeEach
        void setUp() {
            driver.findElement(By.cssSelector("button[data-content=\"search\"]")).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search-field"))).sendKeys("project");
            driver.findElement(By.name("Submit")).click();
            driver.findElement(By.className("accordion__header")).click();
        }

        @Test
        @DisplayName("Verificar a presença dos checkboxes")
        public void testCheckboxesPresence() {
            List<String> checkboxIds = Arrays.asList(
                    "label[for='type:pages']",
                    "label[for='type:news']",
                    "label[for='type:event']",
                    "label[for='type:file']",
                    "label[for='type:studies']"
            );

            for (String checkboxId : checkboxIds) {
                WebElement checkbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(checkboxId)));
                assertTrue(checkbox.isDisplayed());
            }
        }

        @Test
        @DisplayName("Verificar a seleção de todos os checkboxes")
        public void testAllCheckboxesSelection() {
            List<String> checkboxIds = Arrays.asList(
                    "type:pages",
                    "type:news",
                    "type:event",
                    "type:file",
                    "type:studies"
            );

            for (String checkboxId : checkboxIds) {
                WebElement checkboxLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("label[for='" + checkboxId + "']")));
                WebElement checkboxInput = driver.findElement(By.id(checkboxId));

                if (!checkboxInput.isSelected()) {
                    checkboxLabel.click();
                }
                assertTrue(checkboxInput.isSelected());
            }
        }

        @Test
        @DisplayName("Verificar a desmarcação de todos os checkboxes")
        public void testAllCheckboxesDeselection() {
            List<String> checkboxIds = Arrays.asList(
                    "type:pages",
                    "type:news",
                    "type:event",
                    "type:file",
                    "type:studies"
            );

            for (String checkboxId : checkboxIds) {
                WebElement checkboxLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("label[for='" + checkboxId + "']")));
                WebElement checkboxInput = driver.findElement(By.id(checkboxId));

                if (!checkboxInput.isSelected()) {
                    checkboxLabel.click();
                }
                assertTrue(checkboxInput.isSelected());
                checkboxLabel.click();
                assertFalse(checkboxInput.isSelected());
            }
        }

        @Test
        @DisplayName("Verificar a pesquisa com um checkbox selecionado")
        public void testSearchWithCheckbox() {
            WebElement checkbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("label[for='type:news']")));
            if (!checkbox.isSelected()) {
                checkbox.click();
            }
            driver.findElement(By.cssSelector("button[class*='button--primary']")).click();

            WebElement result = driver.findElement(By.className("search-results__num-rows"));
            assertTrue(result.isDisplayed());
        }

        @Test
        @DisplayName("Verificar a pesquisa com múltiplos checkboxes selecionados")
        public void testSearchWithMultiCheckbox() {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("label[for='type:news']"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("label[for='type:file']"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("label[for='type:studies']"))).click();

            driver.findElement(By.cssSelector("button[class*='button--primary']")).click();

            WebElement result = driver.findElement(By.className("search-results__num-rows"));
            assertTrue(result.isDisplayed());
        }
    }

}
