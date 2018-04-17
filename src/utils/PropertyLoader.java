package utils;

import org.testng.Assert;

import java.io.IOException;
import java.util.Properties;


public class PropertyLoader {
    private static final String PROPERTY_FILE = "/application.properties";

    public static String loadProperty(String name) {
        return loadProperty(name, PROPERTY_FILE);
    }

    public static String loadProperty(String name, String fileName) {
        Properties props = new Properties();
        try {
            props.load(PropertyLoader.class.getResourceAsStream(fileName));
        } catch (IOException e) {
            Assert.fail("Incorrect property name - " + name);
        }
        String value = "";
        if (name != null) {
            value = props.getProperty(name);
        }
        return value;
    }

}

