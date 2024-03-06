import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Xml2SQL {

    public static void main(String[] args) {
        List<String> SQLList = getCreateSQL(Xml2SQL.class.getClassLoader().getResource("mydbdefine.xml"));
        SQLList.forEach(System.out::println);
    }

    static List<String> SQLList = new ArrayList<>();

    public static List<String> getCreateSQL(URL xmlPath) {

        SAXReader saxReader = new SAXReader();
        try {
            Element databases = saxReader.read(xmlPath).getRootElement();
            dataBaseSQL(databases.elements());
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return SQLList;
    }

    private static void dataBaseSQL(List<Element> databases) {
        databases.forEach( database -> {
            String dbName = database.attribute("name").getText();
            SQLList.add(" CREATE DATABASE " + dbName + "; ");
            tableSQL(database.elements(), dbName);
        });
    }

    private static void tableSQL(List<Element> tables, String dbName) {

        StringBuilder createTableSQL = new StringBuilder(" CREATE TABLE " + dbName + ".");
        tables.forEach( table -> {
            createTableSQL.append(table.attribute("name").getText())
                    .append("(")
                    .append(fieldSQL(table.elements()))
                    .append(")")
                    .append(" ")
                    .append("ENGINE")
                    .append("=")
                    .append(table.attribute("engine").getText())
                    .append(" ")
                    .append("DEFAULT")
                    .append(" ")
                    .append("CHARSET")
                    .append("=")
                    .append(table.attribute("charset").getText())
                    .append(" ")
                    .append("COLLATE")
                    .append("=")
                    .append(table.attribute("collate").getText())
                    .append(";");
            SQLList.add(createTableSQL.toString());;
        });
    }

    private static StringBuilder fieldSQL(List<Element> fields) {
        StringBuilder fieldSQL = new StringBuilder(" ");
        fields.forEach( field -> {
            fieldSQL.append(field.attribute("name").getText())
                    .append(" ")
                    .append(field.attribute("type").getText())
                    .append(" ")
                    .append((field.attribute("nullable").getText().equals("true"))? "NOT NULL":"")
                    .append(",");
        });
        return fieldSQL.deleteCharAt(fieldSQL.length()-1);
    }
}