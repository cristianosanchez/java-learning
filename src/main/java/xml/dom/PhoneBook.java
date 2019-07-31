package xml.dom;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class PhoneBook {

    private List<Person> personList;

    public PhoneBook() {
        super();
        personList = new ArrayList<Person>();
    }

    public void addPerson(Person p) {
        personList.add(p);
    }

    public List<Person> getPersonList() {
        return personList;
    }

    public static PhoneBook createFromDocument(Document doc) {
        PhoneBook pb = new PhoneBook();
        
        NodeList list = doc.getElementsByTagName("person");

        for (int i = 0; i < list.getLength(); i++) {
            Node personNode = list.item(i);
            NodeList elementsList = personNode.getChildNodes();
            String nameValue = "";
            String phoneValue = "";
            for (int j = 0; j < elementsList.getLength(); j++) {
                Node childNode = elementsList.item(j);
                String nodeName = childNode.getNodeName();
                if (nodeName.equals("name")) {
                    nameValue = childNode.getFirstChild().getNodeValue();
                }

                if (nodeName.equals("phone")) {
                    phoneValue = childNode.getFirstChild().getNodeValue();
                }
            }
            pb.addPerson(new Person(nameValue, phoneValue));
        }
        return pb;
    }
}