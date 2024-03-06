import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.Arrays;

public class Dom4jTest {
    public static void main(String[] args) {
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(Dom4jTest.class.getClassLoader().getResource("mydbdefine.xml"));
            Element rootElement = document.getRootElement();
            System.out.println(rootElement.getName());
            rootElement.elements().forEach(
                    database -> System.out.println(Arrays.toString(database.attributes().toArray()))
            );
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
