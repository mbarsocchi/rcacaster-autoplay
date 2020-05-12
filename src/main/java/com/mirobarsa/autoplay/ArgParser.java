/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mirobarsa.autoplay;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.System.exit;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mbarsocchi
 */
class ArgParser {

    private String casterUrl;
    private static Logger LOGGER = LoggerFactory.getLogger(ArgParser.class);

    ArgParser(String[] args) throws IOException {
        String propertyPath = null;
        String chromeDriver;
        Properties prop = new Properties();
        if (args.length == 0) {
            chromeDriver = System.getProperty("webdriver.chrome.driver");
            casterUrl = System.getProperty("caster.url");
        } else {
            propertyPath = args[0];
            InputStream input = null;
            try {
                input = new FileInputStream(propertyPath);
            } catch (FileNotFoundException ex) {
                LOGGER.info("Error for file: " + propertyPath);
                exit(2);
            }
            prop.load(input);
            chromeDriver = prop.getProperty("webdriver.chrome.driver");
            casterUrl = prop.getProperty("caster.url");
            System.setProperty("whenGoToPause", prop.getProperty("whenGoToPause"));
            LOGGER.info("When go to pause exec:  "+System.getProperty("whenGoToPause"));
            System.setProperty("whenStartsPlayCmd", prop.getProperty("whenStartsPlayCmd"));
            LOGGER.info("When starts to play exec:  "+System.getProperty("whenStartsPlayCmd"));
            System.setProperty("whenEnded", prop.getProperty("whenEnded"));
            LOGGER.info("When ended exec:  "+System.getProperty("whenEnded"));
        }
        if (chromeDriver == null || chromeDriver.equals("") || casterUrl == null || casterUrl.equals("")) {
            LOGGER.info("Missing Chrome driver path or url. You can pass a property files via command line or set the property using java jvm options:\n"
                    + "-Dwebdriver.chrome.driver=\n"
                    + "-Dcaster.url=\n\n"
                    + "Additionally you can set 3 properties with commands to be executed on play/pause/end with:\n"
                    + "-DwhenGoToPause=\n"
                    + "-DwhenStartsPlayCmd=\n"
                    + "-DwhenEnded=\n");
            exit(1);
        }
        System.setProperty("webdriver.chrome.driver", chromeDriver);
    }

    String getUrl() {
        return casterUrl;
    }

}
