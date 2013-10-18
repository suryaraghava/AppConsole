package com.audiumcorp.webportal.admin;

import java.io.IOException;
import java.util.Properties;

public class PropertyFileParser
{

    private static Properties properties;
    private static String PROP_FILE;

    private PropertyFileParser()
    {
    }

    public static String getProperty(String key)
    {
        if(properties != null)
        {
            return properties.getProperty(key);
        } else
        {
            return null;
        }
    }

    static 
    {
        properties = null;
        PROP_FILE = "app.properties";
        java.io.InputStream inputStream = com.audiumcorp.webportal.admin.PropertyFileParser.class.getResourceAsStream(PROP_FILE);
        properties = new Properties();
        try
        {
            properties.load(inputStream);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
