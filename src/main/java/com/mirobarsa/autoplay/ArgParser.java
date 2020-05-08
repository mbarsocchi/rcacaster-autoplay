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

/**
 *
 * @author mbarsocchi
 */
class ArgParser {

    private String casterUrl;

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
                System.out.println("Error for file: " + propertyPath);
                exit(2);
            }
            prop.load(input);
            chromeDriver = prop.getProperty("webdriver.chrome.driver");
            casterUrl = prop.getProperty("caster.url");
            System.setProperty("whenGoToPause", prop.getProperty("whenGoToPause"));
            System.setProperty("whenStartsPlayCmd", prop.getProperty("whenStartsPlayCmd"));
            System.setProperty("whenEnded", prop.getProperty("whenEnded"));
        }
        if (chromeDriver == null || chromeDriver.equals("") || casterUrl == null || casterUrl.equals("")) {
            System.out.println("Missing Chrome driver path or url. You can pass a property files via command line or set the property using java jvm options:\n"
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
