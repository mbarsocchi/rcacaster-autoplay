/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mirobarsa.autoplay;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang.SystemUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 *
 * @author mbarsocchi
 */
public class BrowserProcess {

    private final WebDriver driver;
    private State status;
    private State previousState;
    private final By playBtn;
    private final By pauseBtn;
    private final By audioEl;
    private final ApplicationWrapper ap;
    private static final Logger LOGGER = LoggerFactory.getLogger(BrowserProcess.class);

    public static enum State {
        NO_STREAM,
        PLAYING,
        PAUSED;
    }

    private WebDriver initializeDriver() {
        HashMap<String, Object> chromePrefs = new HashMap<>();
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", chromePrefs);
        options.addArguments("--start-maximized");
        options.addArguments("disable-infobars");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("disable-web-security");
        options.addArguments("disable-popup-blocking");
        ChromeDriver chromeDriver = new ChromeDriver(options);
        chromeDriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        return chromeDriver;
    }

    public BrowserProcess(String url) {
        this.driver = initializeDriver();
        this.driver.get(url);
        audioEl = By.id("jp_audio_0");
        pauseBtn = By.xpath("//a[@class=\"jp-pause\"]");
        playBtn = By.xpath("//a[@class=\"jp-play\"]");
        ap = new ApplicationWrapper();
        ap.setWhenStartsPlayCmd(System.getProperty("whenStartsPlayCmd"));
        ap.setWhenGoToPause(System.getProperty("whenGoToPause"));
        ap.setWhenEnded(System.getProperty("whenEnded"));
    }

    public void manageNotPlaying() {
        switch (status) {
            case NO_STREAM:
                this.reloadPage();
                break;
            case PAUSED:
                if (previousState == BrowserProcess.State.PLAYING) {
                    this.reloadPage();
                }
                this.play();
                break;
            case PLAYING:
                break;
            default:
                LOGGER.error("Unrecognized status: " + status);
        }
    }

    private void reloadPage() {
        driver.navigate().refresh();
        this.evaluateStatus();
    }

    private void evaluateStatus() {
        List<WebElement> audioElements = driver.findElements(audioEl);
        List<WebElement> pauseElements = driver.findElements(pauseBtn);
        List<WebElement> playElements = driver.findElements(playBtn);

        previousState = status;
        if (audioElements.isEmpty()) {
            LOGGER.debug("NO_STREAM");
            status = State.NO_STREAM;
        } else {
            if (pauseElements.get(0).isDisplayed()) {
                LOGGER.debug("PLAYING");
                status = State.PLAYING;
            } else {
                if (!playElements.isEmpty()) {
                    LOGGER.debug("PAUSED");
                    status = State.PAUSED;
                }
            }
        }
        if ((previousState == State.PLAYING && status == State.PAUSED)
                || (previousState == State.PLAYING && status == State.NO_STREAM)) {
            ap.whenGoToPause();
        }
    }

    public State getStatus() {
        this.evaluateStatus();
        return status;
    }

    private void play() {
        List<WebElement> playElements = driver.findElements(playBtn);
        if (!playElements.isEmpty()) {
            if (playElements.get(0).isDisplayed()) {
                playElements.get(0).click();
                ap.whenStartsPlay();
            }
        }
        this.evaluateStatus();
    }

    void closeAll() {
        String cmd = "pkill chromedriver";
        if (SystemUtils.IS_OS_WINDOWS) {
            cmd = "taskkill /F /IM ChromeDriver.exe";
        }
        try {
            driver.quit();
            Runtime.getRuntime().exec(cmd);
            ap.whenEnded();
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
        }
    }
}
