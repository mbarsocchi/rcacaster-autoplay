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

        casterUrl = prop.getProperty("caster.url");
        String chromeDriver = prop.getProperty("webdriver.chrome.driver");
        System.setProperty("webdriver.chrome.driver", chromeDriver);
    }

    String getUrl() {
       return casterUrl;
    }

}
