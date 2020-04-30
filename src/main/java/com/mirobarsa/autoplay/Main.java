package com.mirobarsa.autoplay;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.Thread.sleep;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.SystemUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        String propertyPath = null;
        if (args.length == 0) {
            System.out.println("ERROR. No property file given");
            propertyPath = "/Users/mbarsocchi/My_projects/rcacaster-autoplay/src/main/resources/config.properties";
        } else {
            propertyPath = args[0];
        }

        InputStream input = new FileInputStream(propertyPath);
        if (input == null) {
            System.out.println("Sorry, unable to find " + propertyPath);
            return;
        }

        Properties prop = new Properties();
        prop.load(input);

        String casterUrl = prop.getProperty("caster.url");
        String chromeDriver = prop.getProperty("webdriver.chrome.driver");
        System.setProperty("webdriver.chrome.driver", chromeDriver);
        HashMap<String, Object> chromePrefs = new HashMap<>();
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", chromePrefs);
        options.addArguments("--start-maximized");
        DesiredCapabilities cap = DesiredCapabilities.chrome();
        cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        cap.setCapability(ChromeOptions.CAPABILITY, options);

        WebDriver driver = new ChromeDriver(cap);
        String cmd;
        BrowserProcess browser = new BrowserProcess(driver, casterUrl);
        try {
            while (true) {
                try {
                    if (BrowserProcess.State.PLAYING != browser.getStatus()) {
                        browser.manageNotPlaying();
                    }
                    sleep(4000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (Exception ex) {
        } finally {
            if (SystemUtils.IS_OS_WINDOWS){
                cmd ="taskkill /F /IM ChromeDriver.exe" ;
            }else{
                cmd ="pkill chromedriver";
            }
            Runtime.getRuntime().exec(cmd);
            driver.quit();
        }

    }
}
