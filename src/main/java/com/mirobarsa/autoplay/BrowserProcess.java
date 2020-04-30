/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mirobarsa.autoplay;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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

    public static enum State {
        NO_STREAM,
        PLAYING,
        PAUSED;
    }

    public BrowserProcess(WebDriver driver, String url) {
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        this.driver = driver;
        this.driver.get(url);
        audioEl = By.id("jp_audio_0");
        pauseBtn = By.xpath("//a[@class=\"jp-pause\"]");
        playBtn = By.xpath("//a[@class=\"jp-play\"]");

    }

    public void manageNotPlaying() {
        switch (status) {
            case NO_STREAM:
                this.reloadPage();
                evaluateStatus();
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
                System.out.println("Unrecognized status: " + status);
        }
    }

    private void reloadPage() {
        driver.navigate().refresh();
    }

    private void evaluateStatus() {
        List<WebElement> audioElements = driver.findElements(audioEl);
        List<WebElement> pauseElements = driver.findElements(pauseBtn);
        List<WebElement> playElements = driver.findElements(playBtn);

        previousState = status;
        if (audioElements.isEmpty()) {
            status = State.NO_STREAM;
        } else {
            if (pauseElements.get(0).isDisplayed()) {
                status = State.PLAYING;
            } else {
                if (!playElements.isEmpty()) {
                    status = State.PAUSED;
                }
            }
        }

    }

    public State getStatus() {
        this.evaluateStatus();
        return status;
    }

    public void play() {
        List<WebElement> playElements = driver.findElements(playBtn);
        if (!playElements.isEmpty()) {
            if (playElements.get(0).isDisplayed()) {
                playElements.get(0).click();
            }
        }
        this.evaluateStatus();
    }
}
