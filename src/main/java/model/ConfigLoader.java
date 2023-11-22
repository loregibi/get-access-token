package model;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {
    private final Properties properties;

    public ConfigLoader() {
        properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream("src/config.properties")) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Methods to get API config
    public String getAppKey() {
        return properties.getProperty("appKey");
    }

    public String getAppSecret() {
        return properties.getProperty("appSecret");
    }

    public String getClientID() {
        return properties.getProperty("clientID");
    }

    public String getClientSecret() {
        return properties.getProperty("clientSecret");
    }
}
