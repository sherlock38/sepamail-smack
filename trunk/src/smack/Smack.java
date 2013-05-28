package smack;

import java.util.Properties;
import java.util.logging.Level;
import org.apache.commons.cli.*;
import smack.controller.LogController;
import smack.controller.MainController;
import smack.utilities.ConfigReader;
import smack.utilities.Utilities;

/**
 * The Smack class provides the SMACK application entry point
 * 
 * @author Ammit Heeramun <ammit.heeramun@idsoft.mu>
 * @version 1.0
 */
public class Smack {
    
    public static LogController logController;
    public static Properties smackConfig;
    public static String CONFIG_FILE_NAME = "conf" + System.getProperty("file.separator") + "smack.properties";
    public static String LOG_FILE_NAME = "log" + System.getProperty("file.separator") + "smack.log";
    public static String XML_IN_FILE_NAME = "";
    public static String XML_OUT_FILE_NAME = "";
    public static String XSL_FILE_NAME = "";

    /**
     * SMACK entry point
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // Get instance of the log controller
        logController = LogController.getLogController();
        
        // Log info
        Smack.logController.log(Level.INFO, Smack.class.getSimpleName(), "Smack is now starting.");
        
        // Create Options object for the CLI
        Options cliOptions = new Options();
        
        // Add the options for the commandline option
        cliOptions.addOption("in", true, "Nominale missve to acknowledge");
        cliOptions.addOption("out", true, "Acknowledgement missive");
        cliOptions.addOption("conf", true, "Configuration file path (Optional)");

        // initialise the posix parser
        CommandLineParser parser = new PosixParser();
        
        try {
            // Log info
            Smack.logController.log(Level.INFO, Smack.class.getSimpleName(), "Parsing command line option.");
            CommandLine cmd = parser.parse(cliOptions, args);
            
            if(cmd.getOptions().length > 0) {
                
                ConfigReader configReader;
                
                // Log info
                Smack.logController.log(Level.INFO, Smack.class.getSimpleName(), "Getting the \"conf\" command line option.");
                if(cmd.getOptionValue("conf") != null) {
                    
                    Smack.logController.log(Level.INFO, Smack.class.getSimpleName(), "\"Conf\" command line option is define and using it");
                    configReader = new ConfigReader(cmd.getOptionValue("conf"));
                } else {
                    
                    Smack.logController.log(Level.INFO, Smack.class.getSimpleName(), "\"Conf\" command line option is not define and using the default config("+CONFIG_FILE_NAME+")");
                    // Configuration file reader instance
                    configReader = new ConfigReader(Utilities.getCurrentWorkingDirectory() + System.getProperty("file.separator") + CONFIG_FILE_NAME);
                }

                // Parse the properties file
                smackConfig = configReader.parse();

                // Log info
                Smack.logController.log(Level.INFO, Smack.class.getSimpleName(), "Getting the \"in\" command line option.");
                if(cmd.getOptionValue("in") != null) {

                    // Set the missive to process
                    XML_IN_FILE_NAME = cmd.getOptionValue("in");

                    // Log info
                    Smack.logController.log(Level.INFO, Smack.class.getSimpleName(), "Getting the \"out\" command line option.");
                    if(cmd.getOptionValue("out") != null) {

                        // Set the filename for the output file
                        XML_OUT_FILE_NAME = cmd.getOptionValue("out");

                        // Log info
                        Smack.logController.log(Level.INFO, Smack.class.getSimpleName(), "Getting the \"XSL file\" configuration.");
                        if(smackConfig.getProperty("xslFileName") != null)
                        {
                            XSL_FILE_NAME = Utilities.getCurrentWorkingDirectory() + System.getProperty("file.separator") + "xsl" + System.getProperty("file.separator") + smackConfig.getProperty("xslFileName");

                            // Main application controller
                            MainController mainController = new MainController();
                            mainController.load();

                        } else {
                            logController.log(Level.SEVERE, Smack.class.getSimpleName(), "XSL filname config is not define.");
                        }

                    } else {
                        logController.log(Level.SEVERE, Smack.class.getSimpleName(), "Output XML file name was not specify.");
                    }
                } else {
                    logController.log(Level.SEVERE, Smack.class.getSimpleName(), "Input XML file name was not specify.");
                }
            } else {
                // Log warning
                Smack.logController.log(Level.WARNING, Smack.class.getSimpleName(), "Input file name and output file name has not been specified.");
                
                System.out.println("Usage: java -jar \"Smack.jar\" -in input.xml -out output.xml" );
            }
        } catch (ParseException ex) {
            logController.log(Level.SEVERE, Smack.class.getSimpleName(), ex.getLocalizedMessage());
        }
        
        // Log info
        Smack.logController.log(Level.INFO, Smack.class.getSimpleName(), "Smack is now exiting.");
    }
}
