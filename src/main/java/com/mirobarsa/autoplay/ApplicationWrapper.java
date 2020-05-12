/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mirobarsa.autoplay;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mbarsocchi
 */
public class ApplicationWrapper {

    private String whenGoToPause;
    private String whenStartsPlayCmd;
    private String whenEnded;
    private static Logger LOGGER = LoggerFactory.getLogger(ApplicationWrapper.class);

    public void setWhenGoToPause(String whenGoToPause) {
        this.whenGoToPause = whenGoToPause;
    }

    public void setWhenStartsPlayCmd(String whenStartsPlayCmd) {
        this.whenStartsPlayCmd = whenStartsPlayCmd;
    }

    public void setWhenEnded(String whenEnded) {
        this.whenEnded = whenEnded;
    }

    private void execute(String cmd) {
        if (cmd != null && !cmd.equals("")) {
            try {
                Runtime.getRuntime().exec(cmd);
            } catch (IOException ex) {
                LOGGER.error (ex.getMessage());
            }
        }
    }

    public void whenGoToPause() {
        LOGGER.info("Stream in pause");
        execute(whenGoToPause);
    }

    public void whenStartsPlay() {
        LOGGER.info("Stream play");
        execute(whenStartsPlayCmd);
    }

    public void whenEnded() {
        execute(whenEnded);
        LOGGER.info("Stream ended.");
    }
}
