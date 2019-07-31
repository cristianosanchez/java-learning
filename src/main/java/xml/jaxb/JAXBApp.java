package xml.jaxb;

import java.io.FileInputStream;

import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * http://java.sun.com/developer/technicalArticles/xml/jaxb/
 *
 * Download WSDP 2.0
 * http://java.sun.com/webservices/downloads/previous/webservicespack.jsp
 */
public class JAXBApp {

    public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("java JAXBApp people.xml");
			return;
		}
        String fileName = args[0];
        try {
            // create a JAXBContext capable of handling classes generated into
            // the primer.po package
            JAXBContext jc = JAXBContext.newInstance("jaxb");

            // create an Unmarshaller
            Unmarshaller u = jc.createUnmarshaller();

            // unmarshal a po instance document into a tree of Java content
            // objects composed of classes from the primer.po package.
            PhoneBook po = (PhoneBook) u.unmarshal(new FileInputStream("phone.xml"));

            for (PersonType person : po.getPerson()) {
                System.out.println("person: " + person);
            }
            
            PersonType person = new PersonType();
            person.setName("Juliana Alves");
            PhoneType phone = new PhoneType();
            phone.setLocation("mobile");
            phone.setValue("123-8765-4321");            
            person.setPhone(phone);
            po.getPerson().add(person);


            Marshaller m = jc.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
                    Boolean.TRUE);
            m.marshal(po, System.out);

        } catch (JAXBException je) {
            je.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}