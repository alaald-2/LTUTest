package org.example;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Screenshots;
import com.codeborne.selenide.Selenide;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Configuration.reportsFolder;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.$;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LTUTest {

    private static final Logger logger = LoggerFactory.getLogger(LTUTest.class);

    private static String LTU_USERNAME;
    private static String LTU_PASSWORD;



    @BeforeAll
    public static void setup() {
        // Set up Selenide configuration options
        Configuration.browser = "chrome";
        Configuration.timeout = 10000;
        Configuration.headless = false;
        Configuration.downloadsFolder = "C:\\Users\\alaae\\IdeaProjects\\LTUTest\\target\\downloads";
        Configuration.fastSetValue = true;
        Configuration.baseUrl = "https://www.ltu.se/";
        Configuration.proxyEnabled = false;
        Configuration.screenshots = true;
        Configuration.savePageSource = false;
        Configuration.reportsFolder ="C:\\Users\\alaae\\IdeaProjects\\LTUTest\\target\\screenshots" ;

        // Load LTU credentials from JSON file
        try {
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
    }

    @BeforeEach
    public void beforeEachTest() {
        Selenide.switchTo().window(0);
    }

    @AfterEach
    public void afterEachTest() {
        Selenide.closeWindow();
    }

    @AfterAll
    public static void teardown() {
        // Switch to the first window or tab
        Selenide.switchTo().window(0);

        // Click on the user icon
        try {
            $(byCssSelector("a[class^='user-avatar-link']")).shouldBe(visible).click();
            logger.info("Clicked on the user icon.");

            // Click on the logout button
              $(byXpath("/html/body/div/div[1]/div[3]/div/div/div[1]/div/div/span[2]/div[2]/ul/li[3]/ul/li[2]/a/span"))
                      .shouldBe(visible)
                      .click();
                logger.info("Clicked on the logout button.");
        } catch (Exception e) {
                logger.error("Failed to wait for the user icon element to be visible and click it: " + e.getMessage());
                return;
            }




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
            $(byXpath("//*[@id='global_nav_courses_link']"))
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

        // Scroll down to the bottom of the page
        try {
            Selenide.executeJavaScript("window.scrollTo(0, document.body.scrollHeight)");
            logger.info("Scrolled down to the bottom of the page.");
        } catch (Exception e) {
            logger.error("Failed to scroll down to the bottom of the page: " + e.getMessage());
            return;
        }

        // Wait for the Final Examination element to be visible and click it
        try {
            $(byXpath("//*[@id='context_module_51135']"))
                    .shouldBe(visible)
                    .click();
            logger.info("Clicked on the Final Examination element.");
            $(byCssSelector("a[title*='Final Examination']"))
                    .shouldBe(visible)
                    .click();

        } catch (Exception e) {
            logger.error("Failed to click on the Final Examination element: " + e.getMessage());
            return;
        }

        // Check if the expected text is present
        String expectedText = "The examination is on Tuesday, May 30th, from 9:00 - 14:00.";
        try {
            $(byXpath("//*[@id='content']"))
                    .shouldBe(visible);
            String actualText = $(byXpath("//*[@id='content']"))
                    .getText();
            if (actualText.contains(expectedText)) {
                logger.info("Found the expected text: " + expectedText);
            } else {
                logger.error("Did not find the expected text: " + expectedText);
            }

        } catch (Exception e) {
            logger.error("Failed to find the expected text: " + e.getMessage());
        }

        // Wait for 2 seconds and then take a screenshot and save it as a JPEG
        Selenide.sleep(2000);

        try {
             // Take a screenshot
             File screenshot = Screenshots.takeScreenShotAsFile();
             // Read the full image
             BufferedImage fullImg = ImageIO.read(screenshot);
             // Crop the entire page screenshot to get only element screenshot
             BufferedImage finalExam = fullImg.getSubimage(0, 0, 1920, 1080);
             // Save the cropped image in reports folder
             File finalExamFile = new File(reportsFolder + "/final_examination.jpeg");
             // Write the cropped image to our file
             ImageIO.write(finalExam, "jpeg", finalExamFile);
             // if screenshot exists move it to reports folder and rename it to final_examination.jpeg
             if (screenshot.exists()) {
                 logger.info("Screenshot taken and saved as final_examination.jpeg");
                 Files.move(screenshot.toPath(), finalExamFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                 // set last modified to current time
                 finalExamFile.setLastModified(System.currentTimeMillis());
                 logger.info("Screenshot file moved to reports folder and renamed to final_examination.jpeg");

                  } else {
                    logger.error("Failed to take screenshot: ");
                    logger.warn("Screenshot file does not exist");
                }

        } catch (Exception e) {
                ;
        }

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
                // Click on the button to expand the menu
                $(byXpath("//button[@role='button']"))
                        .shouldBe(visible)
                        .click();
                // Click on the link to the "intyg" page
                $(byXpath("//a[@href='/student/app/studentwebb/intyg']"))
                        .shouldBe(visible)
                        .click();
                // Log a message to indicate that the link was clicked
                logger.info("Clicked on the link to the 'intyg' page.");
            } catch (Exception e) {
                // If an exception is thrown, log an error message and return
                logger.error("Failed to click on the link to the 'intyg' page: " + e.getMessage());
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

    }
    @Test
    @Order(3)
     void downloadTranscript() {
        // Wait for the Certificates element to be visible and click it to open the Ladok login page
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
            Thread.sleep(2000);
            logger.info("Clicked on first search result");

        } catch (Exception e) {
            logger.error("Failed to wait for first option and click it: " + e.getMessage());

        }
        try {
            // Click on the button to expand the menu
            $(byXpath("//button[@role='button']"))
                    .shouldBe(visible)
                    .click();
            // Click on the link to the "intyg" page
            $(byXpath("//a[@href='/student/app/studentwebb/intyg']"))
                    .shouldBe(visible)
                    .click();
            // Log a message to indicate that the link was clicked
            logger.info("Clicked on the link to the 'intyg' page.");
        } catch (Exception e) {
            // If an exception is thrown, log an error message and return
            logger.error("Failed to click on the link to the 'intyg' page: " + e.getMessage());
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
            // Verify that the file has been downloaded to the 'target/downloads' folder
            File downloadsFolder = new File(Configuration.downloadsFolder);
            File[] downloadedFiles = downloadsFolder.listFiles();
            if (downloadedFiles != null && downloadedFiles.length > 0) {
                Arrays.sort(downloadedFiles, Comparator.comparingLong(File::lastModified).reversed());
                File lastDownloadedFile = downloadedFiles[0];
                logger.info("Downloaded Transcript successfully: " + lastDownloadedFile.getName());
                logger.info("Last modified: " + new Date(lastDownloadedFile.lastModified()));
            } else {
                logger.error("Failed to download Transcript: File not found in " + Configuration.downloadsFolder);
            }

        } catch (Exception e) {
        }

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
            $(byXpath("//*[@id='global_nav_courses_link']"))
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
            logger.info("Clicked on the  download link.");

            // Wait for the file to download
            Thread.sleep(5000);

            // Verify that the file has been downloaded to the 'target/downloads' folder
            File downloadsFolder = new File(Configuration.downloadsFolder);
            File[] downloadedFiles = downloadsFolder.listFiles();
            if (downloadedFiles != null && downloadedFiles.length > 0) {
                Arrays.sort(downloadedFiles, Comparator.comparingLong(File::lastModified).reversed());
                File lastDownloadedFile = downloadedFiles[0];
                logger.info("Downloaded Syllabus successfully: " + lastDownloadedFile.getName());
                logger.info("Last modified: " + new Date(lastDownloadedFile.lastModified()));
            } else {
                logger.error("Failed to download Syllabus: File not found in " + Configuration.downloadsFolder);
            }

        } catch (Exception e) {
            logger.error("Failed to download Syllabus: " + e.getMessage());
        }


    }

}





