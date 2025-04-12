import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private ConfigReader() {
    }
    private static Properties properties;


    static {
        try {
            String path = "src/main/resources/db.properties";
            FileInputStream fileInputStream = new FileInputStream(path);
            properties = new Properties();
            properties.load(fileInputStream);
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static  String getValue(String key){
        return properties.getProperty(key).trim();
    }

    public static void main(String[] args) {
        System.out.println(getValue("server"));
    }
}
