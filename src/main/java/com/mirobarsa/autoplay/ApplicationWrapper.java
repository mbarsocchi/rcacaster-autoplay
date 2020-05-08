/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mirobarsa.autoplay;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mbarsocchi
 */
public class ApplicationWrapper {

    private String whenGoToPause;
    private String whenStartsPlayCmd;
    private String whenEnded;


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
                Logger.getLogger(ApplicationWrapper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void whenGoToPause() {
        execute(whenGoToPause);
    }

    public void whenStartsPlay() {
        execute(whenStartsPlayCmd);
    }

    public void whenEnded() {
        execute(whenEnded);
    }
}
