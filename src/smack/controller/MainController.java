package smack.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import smack.Smack;
import smack.utilities.RoutingWarning;
import smack.utilities.Utilities;
import smack.verification.MissiveNominal;

/**
 * The MainController controls the Smack main application.
 * 
 * @author Ammit Heeramun <ammit.heeramun@idsoft.mu>
 * @version 1.0
 */
public class MainController {
    
    private Document xmlDocument;
    
    /**
     * MainController default constructor
     */
    public MainController() {
        try {
            
            // Log info
            Smack.logController.log(Level.INFO, Smack.class.getSimpleName(), "Reading the input XML file.");
            
            // XML document representing the missive XML file
            this.xmlDocument = Utilities.readXMLDocument(Smack.XML_IN_FILE_NAME);
            
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Loads the main controller
     */
    public void load() {
        
         try {
            // Check if the xmlDocument has been parsed correctly
            if(this.xmlDocument != null) {
            
                // Log info
                Smack.logController.log(Level.INFO, Smack.class.getSimpleName(), "Verifying if the file \""+ Smack.XML_IN_FILE_NAME +"\" is of type nominal.");
            
                // Check if its a nominal missive
                if(Utilities.isMissiveNominal(this.xmlDocument)) {
                    
                    // Create the missive nominal object
                    MissiveNominal missiveNominal = new MissiveNominal(this.xmlDocument);
                    
                    // Log info
                    Smack.logController.log(Level.INFO, Smack.class.getSimpleName(), "Verifying if the file \""+ Smack.XML_IN_FILE_NAME +"\" is valid for acknowledgement.");
                    
                    // Verify the nominal missive
                    Boolean isValid = missiveNominal.verify();
                    
                    // Get the return code
                    String returnCode = missiveNominal.getReturnCode();
                    
                    // Get the routing warning
                    RoutingWarning routingWarning = missiveNominal.getRoutingWarning();
                    
                    
                    // Log info
                    Smack.logController.log(Level.INFO, Smack.class.getSimpleName(), "Transforming the file \""+ Smack.XML_IN_FILE_NAME +"\" to an acknowledgement missive using the transformation \""+Smack.smackConfig.getProperty("xslFileName")+"\".");
                    
                    // Tranform the missive to an acknowledgement missive
                    Document outputDocument = Utilities.transformXMLDocument(Smack.XML_IN_FILE_NAME, Smack.XSL_FILE_NAME);
                    
                    // Get the status tag
                    NodeList statusNodeList = outputDocument.getElementsByTagName("sem:AcqSta");
                    
                    // Check if the missive is valid
                    if(isValid) {
                        
                        // Set the status node as 'ACK'
                        statusNodeList.item(0).setTextContent("ACK");
                        
                    } else {
                        
                        // Set the status node as 'NACK'
                        statusNodeList.item(0).setTextContent("NACK");
                        
                    }
                    
                    // Check if the return code is not null
                    if(null != returnCode) {
                        // Set the retun codes in the respective tags
                        NodeList classNodeList = outputDocument.getElementsByTagName("sem:AcqCla");
                        classNodeList.item(0).setTextContent(returnCode.split("-")[0]);
                        
                        NodeList subNodeList = outputDocument.getElementsByTagName("sem:AcqSub");
                        subNodeList.item(0).setTextContent(returnCode.split("-")[1]);
                        
                        NodeList detNodeList = outputDocument.getElementsByTagName("sem:AcqDet");
                        detNodeList.item(0).setTextContent(returnCode.split("-")[2]);
                    } else {}
                    
                    // Check if the routing warning is not null
                    if(null != routingWarning) {
                        
                        // Set the routing warning tags
                        NodeList codeNodeList = outputDocument.getElementsByTagName("sem:Code");
                        codeNodeList.item(0).setTextContent(routingWarning.getCode());
                        
                        NodeList descNodeList =  outputDocument.getElementsByTagName("sem:Descr");
                        descNodeList.item(0).setTextContent(routingWarning.getDecription());
                        
                    } else {}
                    
                    // Log info
                    Smack.logController.log(Level.INFO, Smack.class.getSimpleName(), "Writing acknowledgement the XML file \"" + Smack.XML_OUT_FILE_NAME +"\".");
                    
                    // Write the acknowledgement document to a file
                    Utilities.writeXmlDocument(outputDocument, Smack.XML_OUT_FILE_NAME);
                } else {
                    // Log warning
                    Smack.logController.log(Level.WARNING, Smack.class.getSimpleName(), "Input XML file  \""+ Smack.XML_IN_FILE_NAME +"\" is not of type nominal.");
                }
            }
            
        } catch (MalformedURLException ex) {
            Smack.logController.log(Level.SEVERE, Smack.class.getSimpleName(), ex.getLocalizedMessage());
        } catch (TransformerConfigurationException ex) {
            Smack.logController.log(Level.SEVERE, Smack.class.getSimpleName(), ex.getLocalizedMessage());
        } catch (TransformerException ex) {
            Smack.logController.log(Level.SEVERE, Smack.class.getSimpleName(), ex.getLocalizedMessage());
        } catch (ParserConfigurationException ex) {
            Smack.logController.log(Level.SEVERE, Smack.class.getSimpleName(), ex.getLocalizedMessage());
        } catch (FileNotFoundException ex) {
            Smack.logController.log(Level.SEVERE, Smack.class.getSimpleName(), ex.getLocalizedMessage());
        } catch (SAXException ex) {
            Smack.logController.log(Level.SEVERE, Smack.class.getSimpleName(), ex.getLocalizedMessage());
        } catch (IOException ex) {
            Smack.logController.log(Level.SEVERE, Smack.class.getSimpleName(), ex.getLocalizedMessage());
        }
    }
}