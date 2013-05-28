package smack.verification;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.logging.Level;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import smack.Smack;
import smack.utilities.RoutingWarning;
import smack.utilities.Utilities;

/**
 * The MissiveNominal verification class contains all the verification functions
 * that is needed to verify a nominal missive for it to be valid for transformation
 * 
 * @author Ammit Heeramun <ammit.heeramun@idsoft.mu>
 * @version 1.0
 */
public class MissiveNominal {
    
    private Document missiveXMLDocument;
    private String returnCode;
    private RoutingWarning routingWarning;
    private String priotityLevel;
    
    /**
     * Get the return code after the verification process
     * @return String that represents the return code
     */
    public String getReturnCode() {
        return returnCode;
    }
    
    /**
     * Get the routing warning object after the verification process
     * @return RoutingWarning object that represents the routing warning
     */
    public RoutingWarning getRoutingWarning() {
        return routingWarning;
    }
    
    /**
     * MissiveNominal class constructor
     * 
     * @param missiveXMLDocument 
     */
    public MissiveNominal (Document missiveXMLDocument) {
            
        // XML document representing the missive XML file
        this.missiveXMLDocument = missiveXMLDocument;
    
    }
    
    /**
     * Perform the verification process for a missive
     * 
     * @return Whether the missive XML document is valid
     */
    public Boolean verify() throws MalformedURLException, IOException {
        
        if(!this.verifyReceiver()) {
            // Log info
            Smack.logController.log(Level.INFO, Smack.class.getSimpleName(), "Verifying receiver [KO].");
            
            this.returnCode = "4-2-4";
            return false;
        } else {
            // Log info
            Smack.logController.log(Level.INFO, Smack.class.getSimpleName(), "Verifying receiver [OK].");
        }
        
        if(!this.verifySender()) {
            // Log info
            Smack.logController.log(Level.INFO, Smack.class.getSimpleName(), "Verifying sender [KO].");
            
            this.returnCode = "4-2-5";
            return false;
        } else {
            // Log info
            Smack.logController.log(Level.INFO, Smack.class.getSimpleName(), "Verifying sender [OK].");
        }
        
        if(!this.verifyCorrectDate()) {
            // Log info
            Smack.logController.log(Level.INFO, Smack.class.getSimpleName(), "Verifying correct date [KO].");
            
            this.returnCode = "4-3-3";
            return false;
        } else {
            // Log info
            Smack.logController.log(Level.INFO, Smack.class.getSimpleName(), "Verifying correct date [OK].");
        }
        
        if(this.verifyCorrectDate() && !this.verifyPassedDate()) {
            // Log info
            Smack.logController.log(Level.INFO, Smack.class.getSimpleName(), "Verifying passed date [KO].");
        
            // Set routing warning object
            this.routingWarning = new RoutingWarning("BAD_TIME", 
            "timestamp SndDtTm (missive id #MsvId) is greater than the datetime "
            + "of receipt server ; the difference is #n milliseconds");
        } else {
            // Log info
            Smack.logController.log(Level.INFO, Smack.class.getSimpleName(), "Verifying passed date [OK].");
        }
              
        if(!this.verifyPriority()) {
            // Log info
            Smack.logController.log(Level.INFO, Smack.class.getSimpleName(), "Verifying priority [KO].");
            
            switch(this.priotityLevel) {
                case "HIGHEST":
                    this.routingWarning = new RoutingWarning("PRI_HIGHEST", 
                                        "missive will be handled with \"HIGHEST\" priority only");
                    break;
                case "HIGH":
                    this.routingWarning = new RoutingWarning("PRI_HIGH", 
                                        "missive will be handled with \"HIGH\" priority only");
                    break;
                case "NORMAL":
                    this.routingWarning = new RoutingWarning("PRI_NORM", 
                                        "missive will be handled with \"NORMAL\" priority only");
                    break;
                case "LOW":
                    this.routingWarning = new RoutingWarning("PRI_LOW", 
                                        "missive will be handled with \"LOW\" priority only");
                    break;
                case "LOWEST":
                    this.routingWarning = new RoutingWarning("PRI_LOWEST", 
                                        "missive will be handled with \"LOWEST\" priority only");
                    break;
                default:
                    break;
            }
            
        } else {
        
            // Log info
            Smack.logController.log(Level.INFO, Smack.class.getSimpleName(), "Verifying priority [OK].");
        }
        
        this.returnCode = "2-1-9";
        return true;
    }
    
    /**
     * Verify the receiver field of the missive
     * 
     * @return Whether the receiver field of the missive is valid
     * @todo Get the receiver's node using XPATH
     */
    private Boolean verifyReceiver() throws MalformedURLException, IOException {
        
        // Get the receiver tag
        NodeList rcvNodeList = this.missiveXMLDocument.getElementsByTagName("sem:Rcv");
        
        // Get the BIC and IBAN content
        String receiverBIC = URLEncoder.encode(rcvNodeList.item(0).getChildNodes().item(1).getTextContent().trim(),"UTF-8");
        String receiverIBAN = URLEncoder.encode(rcvNodeList.item(0).getChildNodes().item(3).getTextContent().trim(),"UTF-8");
        
        Boolean response = false;
        
        // Get the web service domain form the config file
        String wsDomain = Smack.smackConfig.getProperty("verificationWSUrl");
        if(wsDomain != null) {
            // Get the response after verification
            response = Boolean.parseBoolean(Utilities.readFromUrl(wsDomain+"?action=verify_receiver&receiverBIC="+receiverBIC+"&receiverIBAN="+receiverIBAN));
        } else {
            Smack.logController.log(Level.SEVERE, Smack.class.getSimpleName(), "Webservice url is not define in the config file.");
        }
        
        return response;
    }

    
    /**
     * Verify the sender field of the missive
     * 
     * @return Whether the sender field of the missive is valid
     * @todo Get the sender's node using XPATH
     */
    private Boolean verifySender() throws MalformedURLException, IOException {
        
        // Get the sender tag
        NodeList sndNodeList = this.missiveXMLDocument.getElementsByTagName("sem:Snd");
        
        // Get the BIC and IBAN content
        String senderBIC = URLEncoder.encode(sndNodeList.item(0).getChildNodes().item(1).getTextContent().trim(),"UTF-8");
        String senderIBAN = URLEncoder.encode(sndNodeList.item(0).getChildNodes().item(3).getTextContent().trim(),"UTF-8");
        
        Boolean response = false;
        
        // Get the web service domain form the config file
        String wsDomain = Smack.smackConfig.getProperty("verificationWSUrl");
        
        if(wsDomain != null) {
            // Get the response after verification
            response = Boolean.parseBoolean(Utilities.readFromUrl(wsDomain+"?action=verify_sender&senderBIC="+senderBIC+"&senderIBAN="+senderIBAN));
        } else {
            Smack.logController.log(Level.SEVERE, Smack.class.getSimpleName(), "Webservice url is not define in the config file.");
        }
        
        return response;
    }
    
    /**
     * Verify the send datetime of the missive
     * 
     * @return Whether the sent datetime of the missive valid
     * @todo Get the date node using XPATH
     */
    private Boolean verifyCorrectDate() throws MalformedURLException, IOException {
        
        // Get the date tag
        NodeList dateNodeList = this.missiveXMLDocument.getElementsByTagName("sem:SndDtTm");
        
        // Set the sender
        String date = dateNodeList.item(0).getTextContent().trim();
        
        Boolean response = false;
        
        // Get the web service domain form the config file
        String wsDomain = Smack.smackConfig.getProperty("verificationWSUrl");
        
        if(wsDomain != null) {
            // Get the response after verification
            response = Boolean.parseBoolean(Utilities.readFromUrl(wsDomain+"?action=verify_date_correct&date="+date));
        } else {
            Smack.logController.log(Level.SEVERE, Smack.class.getSimpleName(), "Webservice url is not define in the config file.");
        }
        
        return response;
    }
    
    /**
     * Verify if the sent datetime has been passed
     * 
     * @return Whether the sent datetime has been passed
     * @todo Get the date node using XPATH
     */
    private Boolean verifyPassedDate() throws MalformedURLException, IOException {
        
        // Get the date tag
        NodeList dateNodeList = this.missiveXMLDocument.getElementsByTagName("sem:SndDtTm");
        
        // Set the sender
        String date = dateNodeList.item(0).getTextContent().trim();
        
        Boolean response = false;
        
        // Get the web service domain form the config file
        String wsDomain = Smack.smackConfig.getProperty("verificationWSUrl");
        
        if(wsDomain != null) {
            // Get the response after verification
            response = Boolean.parseBoolean(Utilities.readFromUrl(wsDomain+"?action=verify_date_passed&date="+date));
        } else {
            Smack.logController.log(Level.SEVERE, Smack.class.getSimpleName(), "Webservice url is not define in the config file.");
        }
        
        return response;
    }
    
    /**
     * Verify the priority field of the missive
     * 
     * @return Whether the priority is taken care.
     * @todo Get the priority node using XPATH
     */
    private Boolean verifyPriority() throws MalformedURLException, IOException {
        
        // Get the priority tag
        NodeList priorityNodeList = this.missiveXMLDocument.getElementsByTagName("sem:MsvPri");
        
        // Set the sender
        String priority = priorityNodeList.item(0).getTextContent().trim();
        
        String strResponse;
        
        Boolean response = false;
        
        // Get the web service domain form the config file
        String wsDomain = Smack.smackConfig.getProperty("verificationWSUrl");
        
        if(wsDomain != null) {
            
            strResponse = Utilities.readFromUrl(wsDomain+"?action=verify_priority&priority="+priority);
                    
            // Get the response after verification
            response = Boolean.parseBoolean(strResponse);
            
            if(!response) {
               priotityLevel = strResponse;
            } else {}
            
        } else {
            Smack.logController.log(Level.SEVERE, Smack.class.getSimpleName(), "Webservice url is not define in the config file.");
        }
        
        return response;
    }
}
