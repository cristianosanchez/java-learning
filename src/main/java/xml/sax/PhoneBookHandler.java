package xml.sax;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;

/**
 * OrderHandler extends DefaultHandler to calculate price, number of items,
 * and number of orders.
 */
public class PhoneBookHandler extends DefaultHandler {

    private final static String PEOPLE = "people";
    private final static String PERSON = "person";
    private final static String PHONE = "phone";
    private int numberPeople = 0;
    private int numberPet = 0;

    public int getNumberOfPeople() {
        return numberPeople;
    }

    public int getNumberOfPet() {
        return numberPet;
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
            throws SAXException {
        System.out.println(String.format("starting '%s::%s::%s'", namespaceURI, qName, localName));
        if (PHONE.equals(localName)) {
            numberPet++;
        }

        if (PERSON.equals(localName)) {
            numberPeople++;
        }
    }

    /**
     * Receive notification of character data inside an element.
     * @param ch - The characters.
     * @param start - The start position in the character array.
     * @param length - The number of characters to use from the character array.
     * @throws SAXException - Any SAX exception, possibly wrapping another exception.
     */
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        String strValue = new String(ch, start, length);
        System.out.println(String.format("reading '%s'", strValue));
    }

    /**
     * Receive notification of the end of an element.
     * @param namespaceURI - The Namespace URI, or the empty string if the element has no Namespace URI or if Namespace processing is not being performed.
     * @param localName - The local name (without prefix), or the empty string if Namespace processing is not being performed.
     * @param qName - The qualified name (with prefix), or the empty string if qualified names are not available.
     * @throws SAXException - Any SAX exception, possibly wrapping another exception.
     */
    public void endElement(String namespaceURI, String localName, String qName)
            throws SAXException {
        System.out.println(String.format("ending '%s::%s::%s'", namespaceURI, qName, localName));
    }
}
