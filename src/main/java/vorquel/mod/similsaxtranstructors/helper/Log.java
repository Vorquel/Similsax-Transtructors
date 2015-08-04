package vorquel.mod.similsaxtranstructors.helper;

import org.apache.logging.log4j.Logger;

public class Log {

    private static Logger logger;
    
    public static void setLogger(Logger logger) {
        Log.logger = logger;
    }

    public static void kill(String format, Object... args) {
        String msg = String.format(format, args);
        logger.fatal(msg);
        throw new RuntimeException(msg);
    }

    public static void fatal(String format, Object... args) {
        logger.fatal(String.format(format, args));
    }

    public static void error(String format, Object... args) {
        logger.error(String.format(format, args));
    }

    public static void warn(String format, Object... args) {
        logger.warn(String.format(format, args));
    }

    public static void info(String format, Object... args) {
        logger.info(String.format(format, args));
    }

    public static void debug(String format, Object... args) {
        logger.debug(String.format(format, args));
    }

    public static void trace(String format, Object... args) {
        logger.trace(String.format(format, args));
    }
}
