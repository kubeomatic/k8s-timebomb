
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
    "br.com.clusterlab.timebomb.timer",
    "br.com.clusterlab.timebomb.valid"
})
@Generated("jsonschema2pojo")
public class Annotations {

    @JsonProperty("br.com.clusterlab.timebomb.timer")
    private String brComClusterlabTimebombTimer;
    @JsonProperty("br.com.clusterlab.timebomb.valid")
    private String brComClusterlabTimebombValid;
    @JsonIgnore
    private Map<String, java.lang.Object> additionalProperties = new LinkedHashMap<String, java.lang.Object>();

    @JsonProperty("br.com.clusterlab.timebomb.timer")
    public String getBrComClusterlabTimebombTimer() {
        return brComClusterlabTimebombTimer;
    }

    @JsonProperty("br.com.clusterlab.timebomb.timer")
    public void setBrComClusterlabTimebombTimer(String brComClusterlabTimebombTimer) {
        this.brComClusterlabTimebombTimer = brComClusterlabTimebombTimer;
    }

    @JsonProperty("br.com.clusterlab.timebomb.valid")
    public String getBrComClusterlabTimebombValid() {
        return brComClusterlabTimebombValid;
    }

    @JsonProperty("br.com.clusterlab.timebomb.valid")
    public void setBrComClusterlabTimebombValid(String brComClusterlabTimebombValid) {
        this.brComClusterlabTimebombValid = brComClusterlabTimebombValid;
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
