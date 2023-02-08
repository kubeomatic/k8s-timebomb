
package br.com.clusterlab.dto.validation.pod;

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
    "phase",
    "qosClass"
})
@Generated("jsonschema2pojo")
public class Status {

    @JsonProperty("phase")
    private String phase;
    @JsonProperty("qosClass")
    private String qosClass;
    @JsonIgnore
    private Map<String, java.lang.Object> additionalProperties = new LinkedHashMap<String, java.lang.Object>();

    @JsonProperty("phase")
    public String getPhase() {
        return phase;
    }

    @JsonProperty("phase")
    public void setPhase(String phase) {
        this.phase = phase;
    }

    @JsonProperty("qosClass")
    public String getQosClass() {
        return qosClass;
    }

    @JsonProperty("qosClass")
    public void setQosClass(String qosClass) {
        this.qosClass = qosClass;
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
