package com.liberty.robot.messages;

import java.util.logging.Level;

/**
 * User: Dimitr
 * Date: 22.11.2015
 * Time: 0:58
 */
public class LoggingMessage {
    private String message;
    private Level level;

    public LoggingMessage() {
    }

    public LoggingMessage(String message, Level level) {

        this.message = message;
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
}
