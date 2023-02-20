
package br.com.clusterlab.dto.review;

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
    "br.com.clusterlab.timebomb"
})
@Generated("jsonschema2pojo")
public class Labels {

    @JsonProperty("app")
    private String app;
    @JsonProperty("br.com.clusterlab.timebomb")
    private String brComClusterlabTimebomb;
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

    @JsonProperty("br.com.clusterlab.timebomb")
    public String getBrComClusterlabTimebomb() {
        return brComClusterlabTimebomb;
    }

    @JsonProperty("br.com.clusterlab.timebomb")
    public void setBrComClusterlabTimebomb(String brComClusterlabTimebomb) {
        this.brComClusterlabTimebomb = brComClusterlabTimebomb;
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
