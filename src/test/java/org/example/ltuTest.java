package org.example;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;


/**
 * Unit test for simple App.
 */


public class ltuTest {

    private static final Logger logger = LoggerFactory.getLogger(ltuTest.class);

    private static String LTU_USERNAME;
    private static String LTU_PASSWORD;



    @BeforeAll
      public static void setup() {
        // Set up Selenide configuration options
        Configuration.browser = "chrome";
        Configuration.timeout = 10000;
        Configuration.headless = false;
        Configuration.downloadsFolder = "C:\\Users\\alaae\\IdeaProjects\\LTUTest\\target\\downloads";
        // no new folder will be created in downloads folder
        Configuration.fastSetValue = true;
        Configuration.baseUrl = "https://www.ltu.se/";
        Configuration.proxyEnabled = false;
        Configuration.reportsFolder ="C:\\Users\\alaae\\IdeaProjects\\LTUTest\\target\\screenshots" ;
        Configuration.screenshots = true;
        Configuration.savePageSource = false;








        // Load LTU credentials from JSON file
        try {
            // Load LTU credentials from JSON file
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(new File("c:/temp/ltu.json"));
            LTU_USERNAME = node.get("ltuCredentials").get("Användarid").asText();
            LTU_PASSWORD = node.get("ltuCredentials").get("Lösenord").asText();
        } catch (Exception e) {
            logger.error("Failed to load LTU credentials from JSON file", e);
        }
        // Navigate to the LTU website and accept cookies
        try {
            Selenide.open("https://www.ltu.se/");
            $(byId("CybotCookiebotDialog")).$(byId("CybotCookiebotDialogBodyLevelButtonLevelOptinAllowAll")).click();
        } catch (Exception e) {
            logger.error("Failed to navigate to LTU website or accept cookies", e);
            return;
        }
        // Verify that the correct page is loaded
        try {
            $(byCssSelector("div[class^='ltu-big-logo']")).shouldBe(visible);
        } catch (Exception e) {
            logger.error("Failed to verify that the correct page is loaded", e);
            return;
        }
        // Click on the "Student" button
        try {
            $(byId("main-nav")).$(byText("Student")).click();
        } catch (Exception e) {
            logger.error("Failed to click on the 'Student' button", e);
            return;
        }

        try {
            // Wait for the login button to appear and click it
            $(byId("maincontent")).shouldBe(visible).$(byText("Logga in")).click();
        } catch (Exception e) {
            logger.error("Failed to wait for login button to appear and click it: " + e.getMessage());
            return;
        }
        // Read the Facebook credentials from the JSON file
        try {
            $(byXpath("//*[@id='fm1']")).shouldBe(visible);
            $(byId("username")).val(LTU_USERNAME);
            $(byId("password")).val(LTU_PASSWORD);
            $(byCssSelector("[class$='btn-submit']")).click();
        } catch (Exception e) {
            logger.error("Failed to fill in login form and click login button", e);
        }
        // after all tests are done, close all extra tabs, navigate to the first tab and log out
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                Selenide.closeWindow();
                Selenide.switchTo().window(0);
            } catch (Exception e) {
                logger.error("Failed to close extra tabs and navigate to the first tab", e);
            }

            try {
                $(byCssSelector("li[id$='userAvatar']")).shouldBe(visible).click();
                $(byCssSelector("a[title^='Logga'] span[class='nav-item-label']")).shouldBe(visible).click();
                logger.info("Logged out");

            } catch (Exception e) {
                logger.error("Failed to log out", e);
            }
            }));
    }
    @Test
    @Order(1)
    void searchForFinalExamInfo() {

        // Click on the Canvas link
        try {
            $(byCssSelector("a[title$='Canvas']"))
                    .shouldBe(visible)
                    .click();
            logger.info("Clicked on the Canvas link.");
        } catch (Exception e) {
            logger.error("Failed to wait for the Canvas element to be visible and click it: " + e.getMessage());
            return;
        }

        // Switch to the new window or tab
        Selenide.switchTo().window(1);

        // Click on the Courses button
        try {
            $(byCssSelector("button[id*='courses']"))
                    .shouldBe(visible)
                    .click();
            logger.info("Clicked on the Courses button.");
        } catch (Exception e) {
            logger.error("Failed to wait for the Courses element to be visible and click it: " + e.getMessage());
            return;
        }

        // Click on the course link
        try {
            $(byXpath("//a[contains(text(),'I0015N, Test av IT-system, Lp4, V23')]"))
                    .shouldBe(visible)
                    .click();
            logger.info("Clicked on the course link.");
        } catch (Exception e) {
            logger.error("Failed to wait for the course link element to be visible and click it: " + e.getMessage());
            return;
        }

        // Click on the Modules link
        try {
            $(byCssSelector("a[class='modules']"))
                    .shouldBe(visible)
                    .click();
            logger.info("Clicked on the Modules link.");
        } catch (Exception e) {
            logger.error("Failed to wait for the Modules element to be visible and click it: " + e.getMessage());
            return;
        }

        // Wait for the Final Examination element to be visible and click it
        try {
            $(byCssSelector("div[id='51135']")).shouldBe(visible);
            $(byCssSelector("div[id='51135']")).click();
            logger.info("Clicked on the Final Examination element.");
            $(byCssSelector("a[title*='Final Examination']")).shouldBe(visible)
                    .click();
            logger.info("Clicked on the Final Examination link.");
        } catch (Exception e) {
            logger.error("Failed to click on the Final Examination element: " + e.getMessage());
            return;
        }

        // Check if the expected text is present
        String expectedText = "The examination is on Tuesday, May 30th, from 9:00 - 14:00.";
        try {
            $(byXpath("//*[@id='content']")).shouldBe(visible);
            String actualText = $(byXpath("//*[@id='content']"))
                    .getText();
            // take screenshot and save it as jpeg
            screenshot("final_examination.jpeg");
            if (actualText.contains(expectedText)) {
                logger.info("Found the expected text: " + expectedText);
            } else {
                logger.error("Did not find the expected text: " + expectedText);
            }

        } catch (Exception e) {
            logger.error("Failed to find the expected text: " + e.getMessage());
        }

        // Close the current window or tab and switch back to the original one
        Selenide.closeWindow();
        Selenide.switchTo().window(0);
    }



        @Disabled
        @Test
        @Order(2)
        void testCreateTranscript() {

        // Wait for the Certificates element to be visible and click it
        try {
            $(byCssSelector("a[id$='271']"))
                    .shouldBe(visible)
                    .click();
            logger.info("Clicked on Certificates element");
        } catch (Exception e) {
            logger.error("Failed to wait Certificates element to be visible and click it: " + e.getMessage());
            return;
        }
        // Switch to the new window or tab
        Selenide.switchTo().window(1);
        try {
            $(byCssSelector("a[class$='btn-ladok-inloggning']"))
                    .shouldBe(visible)
                    .click();
            logger.info("Clicked on Ladok login button");
        } catch (Exception e) {
            logger.error("Failed to wait for Ladok login button to appear and click it: " + e.getMessage());
            return;
        }
        String searchQuery = "LTU";
        try {
            $(byId("searchinput")).shouldBe(visible)
                    .val(searchQuery).pressEnter();
            logger.info("Entered search query: " + searchQuery);
        } catch (Exception e) {
            logger.error("Failed to wait for search label and click it: " + e.getMessage());
            return;
        }
        try {
            $(byCssSelector("div[class$='institution-text']")).shouldBe(visible)
                    .click();
            logger.info("Clicked on first search result");
        } catch (Exception e) {
            logger.error("Failed to wait for first option and click it: " + e.getMessage());
            return;
        }

        try {
            $(byXpath("//button[@role='button']"))
                    .shouldBe(visible)
                    .click();
            $(byXpath("//a[@href='/student/app/studentwebb/intyg']"))
                    .shouldBe(visible)
                    .click();
            logger.info("");
        } catch (Exception e) {
            logger.error(" " + e.getMessage());
            return;
        }
        try {
                $(byXpath("//button[@title='Create']"))
                        .shouldBe(visible)
                        .click();
                $(byXpath("//*[@id='intygstyp']"))
                        .shouldBe(visible)
                        .click();
                $(byXpath("//*[@id=\"intygstyp\"]/option[2]"))
                        .shouldBe(visible)
                        .click();
                //$(byXpath("//button[contains(@class, 'me-lg-3')]")).shouldBe(visible).click();
                logger.info("Created a Transcript successfully");
            } catch (Exception e) {
                logger.error("Failed to creat a Transcript : " + e.getMessage());
            }
            Selenide.closeWindow();
            Selenide.switchTo().window(0);
    }
    @Test
    @Order(3)
     void downloadTranscript() {
        // Wait for the Certificates element to be visible and click it
        try {
            $(byCssSelector("a[id$='271']"))
                    .shouldBe(visible)
                    .click();
            logger.info("Clicked on Certificates element");
        } catch (Exception e) {
            logger.error("Failed to wait Certificates element to be visible and click it: " + e.getMessage());
            return;
        }
        // Switch to the new window or tab
        Selenide.switchTo().window(2);
        try {
            $(byCssSelector("a[class$='btn-ladok-inloggning']"))
                    .shouldBe(visible)
                    .click();
            logger.info("Clicked on Ladok login button");
        } catch (Exception e) {
            logger.error("Failed to wait for Ladok login button to appear and click it: " + e.getMessage());
            return;
        }
        String searchQuery = "LTU";
        try {
            $(byId("searchinput")).shouldBe(visible)
                    .val(searchQuery).pressEnter();
            logger.info("Entered search query: " + searchQuery);
        } catch (Exception e) {
            logger.error("Failed to wait for search label and click it: " + e.getMessage());
            return;
        }
        try {
            $(byCssSelector("div[class$='institution-text']")).shouldBe(visible)
                    .click();
            Thread.sleep(2000);
            logger.info("Clicked on first search result");

        } catch (Exception e) {
            logger.error("Failed to wait for first option and click it: " + e.getMessage());

        }
        try {
            $(byXpath("//button[@role='button']"))
                    .shouldBe(visible)
                    .click();
            $(byXpath("//a[@href='/student/app/studentwebb/intyg']"))
                    .shouldBe(visible)
                    .click();
            logger.info("");
        } catch (Exception e) {
            logger.error(" " + e.getMessage());
            return;
        }


        try {
            // Click on the link to download the file
            $(byXpath("//div[@class='card-header'][.//a[contains(@href, 'aa')]]"))
                    .shouldBe(visible)
                    .click();
            logger.info("Clicked on Transcript");

            // Wait for the file to download
            Thread.sleep(5000);
            logger.info("Downloaded Transcript successfully");
        } catch (Exception e) {
            logger.error("Failed to download Transcript: " + e.getMessage());

            }

        Selenide.closeWindow();
        Selenide.switchTo().window(0);


    }

    @Test
    @Order(4)
    void downloadCourseSyllabus() {
        // Click on the Canvas link
        try {
            $(byCssSelector("a[title$='Canvas']"))
                    .shouldBe(visible)
                    .click();
            logger.info("Clicked on the Canvas link.");
        } catch (Exception e) {
            logger.error("Failed to wait for the Canvas element to be visible and click it: " + e.getMessage());
            return;
        }

        // Switch to the new window or tab
        Selenide.switchTo().window(1);
        // Click on the Courses button
        try {
            $(byCssSelector("button[id*='courses']"))
                    .shouldBe(visible)
                    .click();
            logger.info("Clicked on the Courses button.");
        } catch (Exception e) {
            logger.error("Failed to wait for the Courses element to be visible and click it: " + e.getMessage());
            return;
        }

        // Click on the course link
        try {
            $(byXpath("//a[contains(text(),'I0015N, Test av IT-system, Lp4, V23')]"))
                    .shouldBe(visible)
                    .click();
            logger.info("Clicked on the course link.");
        } catch (Exception e) {
            logger.error("Failed to wait for the course link element to be visible and click it: " + e.getMessage());
            return;
        }
        try {
            $(byXpath("//a[contains(@href, 'av')]"))
                    .shouldBe(visible)
                    .click();
            logger.info("Clicked on the Syllabus link.");
        } catch (Exception e) {
            logger.error("Failed to wait for the Syllabus link element to be visible and click it: " + e.getMessage());
            return;
        }
        // Switch to the new window or tab
        Selenide.switchTo().window(2);
            try {
                // Click on the link to download the file
                $(byXpath("//img[@alt='PDF']"))
                        .shouldBe(visible)
                        .click();
                logger.info("Clicked on the Syllabus link.");

                // Wait for the file to download
                Thread.sleep(5000);
                logger.info("Downloaded Syllabus successfully");
            } catch (Exception e) {
                logger.error("Failed to download Syllabus: " + e.getMessage());

            }
            Selenide.closeWindow();
            Selenide.switchTo().window(0);

        }

    }





