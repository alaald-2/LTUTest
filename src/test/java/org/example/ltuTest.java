package org.example;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;


public class ltuTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ltuTest.class);


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
        screenshot("jpeg");








        // Load LTU credentials from JSON file
        try {
            // Load LTU credentials from JSON file
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(new File("c:/temp/ltu.json"));
            LTU_USERNAME = node.get("ltuCredentials").get("Användarid").asText();
            LTU_PASSWORD = node.get("ltuCredentials").get("Lösenord").asText();
        } catch (Exception e) {
            LOGGER.error("Failed to load LTU credentials from JSON file", e);
        }
        // Navigate to the LTU website and accept cookies
        try {
            Selenide.open("https://www.ltu.se/");
            $(byId("CybotCookiebotDialog")).$(byId("CybotCookiebotDialogBodyLevelButtonLevelOptinAllowAll")).click();
        } catch (Exception e) {
            LOGGER.error("Failed to navigate to LTU website or accept cookies", e);
            return;
        }
        // Verify that the correct page is loaded
        try {
            $(byCssSelector("div[class^='ltu-big-logo']")).shouldBe(visible);
        } catch (Exception e) {
            LOGGER.error("Failed to verify that the correct page is loaded", e);
            return;
        }
        // Click on the "Student" button
        try {
            $(byId("main-nav")).$(byText("Student")).click();
        } catch (Exception e) {
            LOGGER.error("Failed to click on the 'Student' button", e);
            return;
        }

        try {
            // Wait for the login button to appear and click it
            $(byId("maincontent")).shouldBe(visible).$(byText("Logga in")).click();
        } catch (Exception e) {
            LOGGER.error("Failed to wait for login button to appear and click it: " + e.getMessage());
            return;
        }
        // Read the Facebook credentials from the JSON file
        try {
            $(byXpath("//*[@id='fm1']")).shouldBe(visible);
            $(byId("username")).val(LTU_USERNAME);
            $(byId("password")).val(LTU_PASSWORD);
            $(byCssSelector("[class$='btn-submit']")).click();
        } catch (Exception e) {
            LOGGER.error("Failed to fill in login form and click login button", e);
        }
    }
        @Test

       void searchForFinalExamInfo() {

        // Switch to the new window or tab
        try {
            $(byCssSelector("a[title$='Canvas']"))
                    .shouldBe(visible)
                    .click();
        } catch (Exception e) {
            LOGGER.error("Failed to wait Canvas element to be visible and click it: " + e.getMessage());
            return;
        }
        // Switch to the new window or tab
            Selenide.switchTo().window(1);
        try {
            $(byCssSelector("button[id*='courses']")).shouldBe(visible).click();

        } catch (Exception e) {
            LOGGER.error("Failed to wait courses element to be visible and click it: " + e.getMessage());
            return;
        }
            try {
                $(byXpath("//a[contains(text(),'I0015N, Test av IT-system, Lp4, V23')]"))
                        .shouldBe(visible)
                        .click();
            } catch (Exception e) {
                LOGGER.error("Failed to wait courses element to be visible and click it: " + e.getMessage());
                return;
            }
            try {
                $(byCssSelector("a[class='modules']"))
                        .shouldBe(visible)
                        .click();
            } catch (Exception e) {
                LOGGER.error("Failed to wait modules element to be visible and click it: " + e.getMessage());
                return;
            }
            try {
                $(byCssSelector("div[id='51135']")).shouldBe(visible);

            } catch (Exception e) {
                LOGGER.error("Failed to wait Final Examination element to be visible and click it: " + e.getMessage());
                return;
            }
            try {
                $(byCssSelector("div[id='51135']"))
                        .click();

                $(byCssSelector("a[title*='Final Examination']")).shouldBe(visible)
                        .click();

            } catch (Exception e) {
                LOGGER.error("Failed to click  Final Examination element: " + e.getMessage());
                return;
            }


            String expectedText = "The examination is on Tuesday, May 30th, from 9:00 - 14:00.";
            try {
                $(byXpath("//*[@id='content']"))
                        .shouldBe(visible);
                String actualText = $(byXpath("//*[@id='content']"))
                        .getText();
                screenshot("final_examination.jpeg");
                if (actualText.contains(expectedText)) {
                    LOGGER.info("Found the expected text: " + expectedText);
                } else {
                    LOGGER.error("Did not find the expected text: " + expectedText);
                }

            } catch (Exception e) {
                LOGGER.error("Failed to find the expected text: " + e.getMessage());
            }
            Selenide.closeWindow();
            Selenide.switchTo().window(0);
    }

        @Disabled
        @Test
        @Order(2)
        public void testCreateTranscript() {

        // Wait for the Certificates element to be visible and click it
        try {
            $(byCssSelector("a[id$='271']"))
                    .shouldBe(visible)
                    .click();
            LOGGER.info("Clicked on Certificates element");
        } catch (Exception e) {
            LOGGER.error("Failed to wait Certificates element to be visible and click it: " + e.getMessage());
            return;
        }
        // Switch to the new window or tab
        Selenide.switchTo().window(1);
        try {
            $(byCssSelector("a[class$='btn-ladok-inloggning']"))
                    .shouldBe(visible)
                    .click();
            LOGGER.info("Clicked on Ladok login button");
        } catch (Exception e) {
            LOGGER.error("Failed to wait for Ladok login button to appear and click it: " + e.getMessage());
            return;
        }
        String searchQuery = "LTU";
        try {
            $(byId("searchinput")).shouldBe(visible)
                    .val(searchQuery).pressEnter();
            LOGGER.info("Entered search query: " + searchQuery);
        } catch (Exception e) {
            LOGGER.error("Failed to wait for search label and click it: " + e.getMessage());
            return;
        }
        try {
            $(byCssSelector("div[class$='institution-text']")).shouldBe(visible)
                    .click();
            LOGGER.info("Clicked on first search result");
        } catch (Exception e) {
            LOGGER.error("Failed to wait for first option and click it: " + e.getMessage());
            return;
        }

        try {
            $(byXpath("//button[@role='button']"))
                    .shouldBe(visible)
                    .click();
            $(byXpath("//a[@href='/student/app/studentwebb/intyg']"))
                    .shouldBe(visible)
                    .click();
            LOGGER.info("");
        } catch (Exception e) {
            LOGGER.error(" " + e.getMessage());
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
                LOGGER.info("Created a Transcript successfully");
            } catch (Exception e) {
                LOGGER.error("Failed to creat a Transcript : " + e.getMessage());
            }
            Selenide.closeWindow();
            Selenide.switchTo().window(0);
    }
    @Test
    @Order(3)
    public void downloadTranscript() {
        // Wait for the Certificates element to be visible and click it
        try {
            $(byCssSelector("a[id$='271']"))
                    .shouldBe(visible)
                    .click();
            LOGGER.info("Clicked on Certificates element");
        } catch (Exception e) {
            LOGGER.error("Failed to wait Certificates element to be visible and click it: " + e.getMessage());
            return;
        }
        // Switch to the new window or tab
        Selenide.switchTo().window(1);
        try {
            $(byCssSelector("a[class$='btn-ladok-inloggning']"))
                    .shouldBe(visible)
                    .click();
            LOGGER.info("Clicked on Ladok login button");
        } catch (Exception e) {
            LOGGER.error("Failed to wait for Ladok login button to appear and click it: " + e.getMessage());
            return;
        }
        String searchQuery = "LTU";
        try {
            $(byId("searchinput")).shouldBe(visible)
                    .val(searchQuery).pressEnter();
            LOGGER.info("Entered search query: " + searchQuery);
        } catch (Exception e) {
            LOGGER.error("Failed to wait for search label and click it: " + e.getMessage());
            return;
        }
        try {
            $(byCssSelector("div[class$='institution-text']")).shouldBe(visible)
                    .click();
            Thread.sleep(2000);
            LOGGER.info("Clicked on first search result");

        } catch (Exception e) {
            LOGGER.error("Failed to wait for first option and click it: " + e.getMessage());

        }
        try {
            $(byXpath("//button[@role='button']"))
                    .shouldBe(visible)
                    .click();
            $(byXpath("//a[@href='/student/app/studentwebb/intyg']"))
                    .shouldBe(visible)
                    .click();
            LOGGER.info("");
        } catch (Exception e) {
            LOGGER.error(" " + e.getMessage());
            return;
        }


        try {
            $(byXpath("//div[@class='card-header'][.//a[contains(@href, 'aa')]]"))
                    .shouldBe(visible)
                    .click();
            // downloded file should be saved in Configuration.downloadsFolder = "C:\\Users\\alaae\\IdeaProjects\\LTUTest\\target\\downloads";

            //wait for the pdf file to be downloaded
            Thread.sleep(10000);
            //check if the file is downloaded by opening the last file in the downloads folder
            File folder = new File(Configuration.downloadsFolder);
            File[] listOfFiles = folder.listFiles();
            String fileName = listOfFiles[listOfFiles.length - 1].getName();
            LOGGER.info("Downloaded file name is: " + fileName);
            //check if the file is downloaded successfully and the file name contains the expected text by

            Assert.assertTrue(fileName.contains("intyg") || fileName.contains("Transcript"));

            LOGGER.info("Downloaded Transcript successfully");
        } catch (Exception e) {
            LOGGER.error("Failed to download Transcript : " + e.getMessage());

        }


    }

}



