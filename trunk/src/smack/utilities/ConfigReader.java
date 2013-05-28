package smack.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import smack.Smack;

/**
 * The ConfigReader class reads and parses the SMACK module configuration file.
 * 
 * @author Ammit Heeramun <ammit.heeramun@idsoft.mu>
 * @version 0.1
 */
public class ConfigReader {

    private File configFile;

    /**
     * ConfigReader class constructor
     * 
     * @param configFilename Path and name of SMACK configuration file
     */
    public ConfigReader(String configFilename) {
        
        // Log info
        Smack.logController.log(Level.INFO, Smack.class.getSimpleName(), "Reading config file.");

        // Initialise class attributes
        this.configFile = new File(configFilename);

        // Check if the configuration file exists
        if (!this.configFile.exists()) {
            
            Smack.logController.log(Level.SEVERE, Smack.class.getSimpleName(), "The SMACK configuration file could not be found.");
        }
    }

    /**
     * Parse the SMACK config
     * 
     * @return Properties object of configuration file
     */
    public Properties parse() {
        
        // Log info
        Smack.logController.log(Level.INFO, Smack.class.getSimpleName(), "Parsing config file.");
        
        // Load list of required and optional properties for the SMACK configuration file
        Properties configDefinitionProperties = new Properties();
        
        // File input stream
        FileInputStream fstream;
        
        try {
            fstream = new FileInputStream(this.configFile);
            configDefinitionProperties.load(fstream);
            
        } catch (IOException ex) {
           Smack.logController.log(Level.SEVERE, Smack.class.getSimpleName(), ex.getLocalizedMessage());
        }
         
        return configDefinitionProperties;
    }
}
