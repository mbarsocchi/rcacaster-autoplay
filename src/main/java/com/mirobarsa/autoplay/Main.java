package com.mirobarsa.autoplay;

import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

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
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (Exception ex) {
        } finally {
            browser.closeAll();
        }
    }
}
