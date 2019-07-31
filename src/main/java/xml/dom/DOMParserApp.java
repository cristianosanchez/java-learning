package xml.dom;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * http://developers.sun.com/sw/building/codesamples/dom/
 */
public class DOMParserApp {

    private Document document = null;

    public DOMParserApp(String xmlFileName) {
        DocumentBuilder builder = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(xmlFileName);

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public void addSomePerson(String name, String phone, String phoneLocation) {
        Node rootNode = document.getDocumentElement();

        Node personNode = document.createElement("person");
        rootNode.appendChild(personNode);

        Element nameElement = document.createElement("name");
        personNode.appendChild(nameElement);

        Node nameValue = document.createTextNode(name);
        nameElement.appendChild(nameValue);

        Element phoneElement = document.createElement("phone");
        personNode.appendChild(phoneElement);

        phoneElement.setAttribute(phoneLocation, phone);

        Node phoneValue = document.createTextNode(phone);
        phoneElement.appendChild(phoneValue);
        
        // Normalizing the DOM
        document.getDocumentElement().normalize();
    }

    private Document getDocument() {
        return document;
    }

    public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("java DOMParserApp people.xml");
			return;
		}
        String fileName = args[0];
        DOMParserApp phoneDom = new DOMParserApp(fileName);

        PhoneBook pb = PhoneBook.createFromDocument(phoneDom.getDocument());
        for (Person person : pb.getPersonList()) {
            System.out.println("person: " + person);
        }

        System.out.println(" --- --- --- ");

        phoneDom.addSomePerson("Juliana Alves", "011-9876-5432", "mobile");

        PhoneBook pb2 = PhoneBook.createFromDocument(phoneDom.getDocument());
        for (Person person : pb2.getPersonList()) {
            System.out.println("person: " + person);
        }

    }
}