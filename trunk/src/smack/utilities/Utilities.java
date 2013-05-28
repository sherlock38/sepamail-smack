package smack.utilities;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import sun.misc.IOUtils;


/**
 * Utilities class groups static methods used throughout the application
 * 
 * @author Ammit Heeramun <ammit.heeramun@idsoft.mu>
 * @version 1.0
 */
public class Utilities {

    /**
     * Get the current working directory of the application
     *
     * @return The absolute path to the application working directory
     */
    public static String getCurrentWorkingDirectory() {

        // Current class directory
        URL location = Utilities.class.getProtectionDomain().getCodeSource().getLocation();
        String path;

        try {
            path = URLDecoder.decode(location.getPath(), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            path = location.getPath();
        }

        // Absolute current working directory
        return new File(path).getParentFile().getPath() + System.getProperty("file.separator") + ".";

        // Absolute current working directory - added for debugging
        //return new File(path).getParentFile().getPath() + System.getProperty("file.separator") + "./..";
    }
    
    /**
     * Check if the parameterized XML document is of type nominal
     * 
     * @param xmlDocument
     * @return Whether the parameterized XML document is of type nominal
     */
    public static Boolean isMissiveNominal(Document xmlDocument) {
        
        // Get the missive XML node from the metadata XML document
        NodeList nl = xmlDocument.getElementsByTagName("sem:MsvTyp");
        
        // Check if the metadata XML document object contains the required missive XML document tag
        if (nl.getLength() > 0) {
             // Value node
            Node valueNode = nl.item(0).getFirstChild();
            
            // Check if the value node is not null
            if(valueNode != null) {
                
                if("Nominal".equals(valueNode.getNodeValue())) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Read the parameterized XML file
     * 
     * @param xmlInputFilename XML file to read
     * @return Document
     * @throws ParserConfigurationException
     * @throws FileNotFoundException
     * @throws SAXException
     * @throws IOException 
     */
    public static Document readXMLDocument(String xmlInputFilename) throws ParserConfigurationException, FileNotFoundException, SAXException, IOException {

        // Parse the source XML file
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        // XML source file instance
        File sourceFile = new File(xmlInputFilename);

        // Check if the source file exists
        if (sourceFile.exists()) {

            // Source file exists so we parse the XML file
            return db.parse(sourceFile);

        } else {

            // File was not found
            throw new FileNotFoundException("The specified source file, " + xmlInputFilename + ", was not found.");
        }
    }
    
    /**
     * Transform the parameterized XML file with the parameterized XSL file
     * 
     * @param xmlInputFilename
     * @param xslFilename
     * @return Transformed XML document
     * @throws TransformerConfigurationException
     * @throws TransformerException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException 
     */
    public static Document transformXMLDocument(String xmlInputFilename, String xslFilename) throws TransformerConfigurationException, TransformerException, ParserConfigurationException, SAXException, IOException
    {
        
        // Instantiate the transformer factory object
        TransformerFactory factory = TransformerFactory.newInstance();
        
        // Read the xsl file
        Source xslt = new StreamSource(new File(xslFilename));
        
        // Instatiate the transformer
        Transformer transformer = factory.newTransformer(xslt);
        
        // Read the input xml file
        Source text = new StreamSource(new File(xmlInputFilename));
        
        // Instantiate the output stream
        //StreamResult outputStream = new StreamResult(new File(xmlOutputFilename));
        StreamResult outputStream = new StreamResult(new StringWriter());
        
        //new StreamResult(new File("output.xml"))
        transformer.transform(text, outputStream);
        
        String outputString = outputStream.getWriter().toString();
        
         // Parse the source XML file
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbFactory.newDocumentBuilder();
        
        return builder.parse(new InputSource(new StringReader(outputString)));
    }
    
    
    /**
     * Write the parameterized document to a file
     * @param doc
     * @param filename
     * @throws TransformerConfigurationException
     * @throws TransformerException 
     */
    public static void writeXmlDocument(Document doc, String filename) throws TransformerConfigurationException, TransformerException {
        
            // Prepare the DOM document for writing
            Source source = new DOMSource(doc);
 
            // Prepare the output file
            File file = new File(filename);
            Result result = new StreamResult(file);
 
            // Write the DOM document to the file
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.transform(source, result);
    }
    
    /**
     * Read an HTTP URL
     * 
     * @param url
     * @return The returned string of data
     * @throws MalformedURLException
     * @throws IOException 
     */
    public static String readFromUrl(String url) throws MalformedURLException, IOException {
        
        // Convert the string to a url
        URL u = new URL(url);
        
        // Open input stream
        InputStream in = u.openStream();
        String response;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                    sb.append(line);
            }
            
            response = sb.toString();
        }
        
        return response;
    }
}
