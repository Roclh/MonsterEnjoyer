package com.GaysFromITMO.logger;


import org.apache.log4j.Logger;

public class Log {
    private static final Logger log = Logger.getLogger(Log.class);

    public static void info(String info) {
        log.info(info);
    }

    public static void info(String info, boolean isLogged) {
        if (isLogged) {
            log.info(info);
        }
    }

    public static void warn(String info) {
        log.warn(info);
    }
}
