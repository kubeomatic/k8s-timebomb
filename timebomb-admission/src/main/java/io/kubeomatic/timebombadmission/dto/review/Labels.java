
package io.kubeomatic.timebombadmission.dto.review;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "app",
    "kubeomatic-io-timebomb"
})
@Generated("jsonschema2pojo")
public class Labels {

    @JsonProperty("app")
    private String app;
    @JsonProperty("kubeomatic-io-timebomb")
    private String kubeomaticIoTimebomb;
    @JsonIgnore
    private Map<String, java.lang.Object> additionalProperties = new LinkedHashMap<String, java.lang.Object>();

    @JsonProperty("app")
    public String getApp() {
        return app;
    }

    @JsonProperty("app")
    public void setApp(String app) {
        this.app = app;
    }

    @JsonProperty("kubeomatic-io-timebomb")
    public String getKubeomaticIoTimebomb() {
        return kubeomaticIoTimebomb;
    }

    @JsonProperty("kubeomatic-io-timebomb")
    public void setKubeomaticIoTimebomb(String kubeomaticIoTimebomb) {
        this.kubeomaticIoTimebomb = kubeomaticIoTimebomb;
    }

    @JsonAnyGetter
    public Map<String, java.lang.Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, java.lang.Object value) {
        this.additionalProperties.put(name, value);
    }

}
