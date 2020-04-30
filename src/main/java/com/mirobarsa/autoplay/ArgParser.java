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
import java.util.Properties;

/**
 *
 * @author mbarsocchi
 */
class ArgParser {

    private String casterUrl;

    ArgParser(String[] args) throws FileNotFoundException, IOException {
        String propertyPath = null;
        String chromeDriver;
        Properties prop = new Properties();
        if (args.length == 0) {
            chromeDriver = System.getProperty("webdriver.chrome.driver");
            casterUrl = System.getProperty("caster.url");
        } else {
            propertyPath = args[0];
            InputStream input = new FileInputStream(propertyPath);
            if (input == null) {
                System.out.println("Sorry, unable to find " + propertyPath);
                return;
            }
            prop.load(input);
            chromeDriver = prop.getProperty("webdriver.chrome.driver");
            casterUrl = prop.getProperty("caster.url");
        }
        if (chromeDriver == null || chromeDriver.equals("") ||casterUrl == null || casterUrl.equals("")  ){
            System.out.println("Missing Chrome driver path or url. You can pass a property files via command line\n"
                    + "or set the property using java jvm options:\n"
                    + "-Dwebdriver.chrome.driver=\n"
                    + "-Dcaster.url=\n\n");
            return;
        }
        System.setProperty("webdriver.chrome.driver", chromeDriver);
    }

    String getUrl() {
        return casterUrl;
    }

}
