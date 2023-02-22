package br.com.clusterlab.config;

import java.io.IOException;
import java.io.InputStream;

public class AppProperties {
    public static final String applicationPropertieFilePath = "application.properties";
    public static final String propertyResponsePatchType = "kubeomatic-io.timebomb.admission.response.patch-type";
    public static final String propertyReviewKind = "kubeomatic-io.timebomb.admission.review.kind";
    public static final String propertieReviewApiversion = "kubeomatic-io.timebomb.admission.review.apiversion";
    public static final String propertyTimerDefault = "kubeomatic-io.timebomb.timer.default";

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
