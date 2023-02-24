package io.kubeomatic.config;

import java.io.IOException;
import java.io.InputStream;

public class AppProperties {
    public static final String applicationPropertieFilePath = "application.properties";
    public static final String propertyResponsePatchType = "kubeomatic-io.timebomb.admission.response.patch-type";
    public static final String propertyReviewKind = "kubeomatic-io.timebomb.admission.review.kind";
    public static final String propertieReviewApiversion = "kubeomatic-io.timebomb.admission.review.apiversion";
    public static final String propertyTimerDefault = "kubeomatic-io.timebomb.timer.default";
    public static final String propertyAnnotationValidity = "kubeomatic-io.timebomb.annotation.valid";
    public static final String propertyAnnotationSku = "kubeomatic-io.timebomb.annotation.sku";
    public static final String propertyAnnotationDescription = "kubeomatic-io.timebomb.annotation.description";
    public static final String propertyLabelTimebomb = "kubeomatic-io.timebomb.label.timebomb";
    public static final String propertyScheduleIntervalInSeconds = "kubeomatic-io.timebomb.schedule.interval.seconds";

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
