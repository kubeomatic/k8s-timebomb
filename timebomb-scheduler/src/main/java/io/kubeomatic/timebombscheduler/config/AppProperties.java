package io.kubeomatic.timebombscheduler.config;

import java.io.IOException;
import java.io.InputStream;

public class AppProperties {
    public static final String applicationPropertieFilePath = "application.properties";
    public static final String propertyAnnotationValidity = "kubeomatic-io.timebomb.annotation.valid";
    public static final String propertyLabelTimebomb = "kubeomatic-io.timebomb.label.timebomb";
    public static final String labelSelectorKey = "kubeomatic-io.timebomb.label.selector.key";
    public static final String labelSelectorValue = "kubeomatic-io.timebomb.label.selector.value";


    public static String getProperty(String property){
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        java.util.Properties properties = new java.util.Properties();
        try (InputStream resourceStream = loader.getResourceAsStream(applicationPropertieFilePath)) {
            properties.load(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty(property);
    }
}
