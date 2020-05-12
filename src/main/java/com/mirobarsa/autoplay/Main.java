package com.mirobarsa.autoplay;

import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Thread.sleep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws FileNotFoundException, IOException {
        ArgParser config = new ArgParser(args);
        BrowserProcess browser = new BrowserProcess(config.getUrl());
        try {
            while (true) {
                try {
                    if (BrowserProcess.State.PLAYING != browser.getStatus()) {
                        browser.manageNotPlaying();
                    }
                    sleep(4000);
                } catch (InterruptedException ex) {
                    LOGGER.error(ex.getMessage());
                }
            }
        } catch (Exception ex) {
        } finally {
            browser.closeAll();
        }
    }
}
