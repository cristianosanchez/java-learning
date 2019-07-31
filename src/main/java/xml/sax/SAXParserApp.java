package xml.sax;

import java.io.File;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.helpers.DefaultHandler;

/**
 * http://developers.sun.com/sw/building/codesamples/sax/
 */
public class SAXParserApp {

    private DefaultHandler handler;
    private SAXParser saxParser;

    /**
     * Constructor
     * @param handler - DefaultHandler for the SAX parser
     */
    public SAXParserApp(DefaultHandler handler) {
        this.handler = handler;
        create();
    }

    /**
     * Create the SAX parser
     */
    private void create() {
        try {
            // Obtain a new instance of a SAXParserFactory.
            SAXParserFactory factory = SAXParserFactory.newInstance();
            // Specifies that the parser produced by this code will provide support for XML namespaces.
            factory.setNamespaceAware(true);
            // Specifies that the parser produced by this code will validate documents as they are parsed.
            factory.setValidating(true);
            // Creates a new instance of a SAXParser using the currently configured factory parameters.
            saxParser = factory.newSAXParser();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * Parse a File
     * @param file - File
     */
    public void parse(File file) {
        try {
            saxParser.parse(file, handler);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * Parse a URI
     * @param uri - String
     */
    public void parse(String uri) {
        try {
            saxParser.parse(uri, handler);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * Parse a Stream
     * @param stream - InputStream
     */
    public void parse(InputStream stream) {
        try {
            saxParser.parse(stream, handler);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void main(String[] args) {                                  
		if (args.length < 1) {
			System.out.println("java SAXParserApp people.xml");
			return;
		}
        String fileName = args[0];

        PhoneBookHandler oHandler = new PhoneBookHandler();

        SAXParserApp parser = new SAXParserApp(oHandler);

        parser.parse(fileName);

        System.out.println(String.format("\n\n The  parsed '%s' found %d people", fileName, oHandler.getNumberOfPeople()));
    }
}