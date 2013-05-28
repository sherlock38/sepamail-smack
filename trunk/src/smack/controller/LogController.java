package smack.controller;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import smack.Smack;
import smack.utilities.LogFormatter;
import smack.utilities.Utilities;

/**
 * LogController allows the application to log all error messages to a log in Syslog format 
 * 
 * @author Bishan Kumar Madhoo <bishan.madhoo@idsoft.mu>
 * @version 1.0
 * @see http://sepamail-smurf.googlecode.com
 */
public class LogController {
    
    private static final Logger LOGGER = Logger.getLogger(Smack.class.getName());
    private static LogController logController;
    
    /**
     * LogController default constructor
     */
    private LogController() {
    
        try {
            // File Formatter
            LogFormatter fileFormatter = new LogFormatter();
            
            // Logger file handler
            FileHandler logFileHandler = new FileHandler(Utilities.getCurrentWorkingDirectory() + 
                    System.getProperty("file.separator") + Smack.LOG_FILE_NAME, true);

            // File handler properties
            logFileHandler.setFormatter(fileFormatter);

            // Create logger
            Logger logger = Logger.getLogger(Smack.class.getName());

            // Set the properties of the logger
            logger.setLevel(Level.ALL);
            logger.addHandler(logFileHandler);
            
        } catch (IOException ex) {
        
            System.out.println(ex.getMessage());
            
        } catch (SecurityException ex) {

            // Show error message on screen since we do not have loggin mechanism in place yet
            System.out.println(ex.getMessage());
        }
    }
    
    /**
     * Get an instance of the LogController class
     * 
     * @return LogController class instance
     */
    public static synchronized LogController getLogController() {
        
        // Check if an instance of the controller has already been declared
        if (logController == null) {
            logController = new LogController();
        }

        return logController;
    }
    
    /**
     * Write application messages to the application log file
     * 
     * @param level Log level
     * @param source Log message source class
     * @param message Message that needs to be written to the application log file
     */
    public synchronized void log(Level level, String source, String message) {
        
        // Write message to the log file
        LOGGER.logp(level, source, "", message);
    }
    
    /**
     * Override the clone method to prevent cloning of the class
     * 
     * @return void
     * @throws CloneNotSupportedException 
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
