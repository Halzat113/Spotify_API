package com.spotify.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationReader {

    private static final Properties properties = new Properties();

    static {
        try {
            FileInputStream file = new FileInputStream("condiguration.properties");
            properties.load(file);
            file.close();
        }catch (IOException e){
            System.out.println("Properties file not found");
        }
    }

    public static String getProperty(String key){
        return properties.getProperty(key);
    }
}
