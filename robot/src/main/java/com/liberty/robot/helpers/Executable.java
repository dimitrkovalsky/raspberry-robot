package com.liberty.robot.helpers;

import static utils.LoggingUtil.error;

/**
 * User: Dimitr
 * Date: 21.11.2015
 * Time: 14:42
 */
@FunctionalInterface
public interface Executable {
    void execute();

    static void executeWithCatch(Executable executable) {
        try {
            executable.execute();
        }
        catch (Exception e) {
            error(Executable.class, e);
        }
    }
}