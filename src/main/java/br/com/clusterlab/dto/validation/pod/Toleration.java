
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
    "key",
    "operator",
    "effect",
    "tolerationSeconds"
})
@Generated("jsonschema2pojo")
public class Toleration {

    @JsonProperty("key")
    private String key;
    @JsonProperty("operator")
    private String operator;
    @JsonProperty("effect")
    private String effect;
    @JsonProperty("tolerationSeconds")
    private Integer tolerationSeconds;
    @JsonIgnore
    private Map<String, java.lang.Object> additionalProperties = new LinkedHashMap<String, java.lang.Object>();

    @JsonProperty("key")
    public String getKey() {
        return key;
    }

    @JsonProperty("key")
    public void setKey(String key) {
        this.key = key;
    }

    @JsonProperty("operator")
    public String getOperator() {
        return operator;
    }

    @JsonProperty("operator")
    public void setOperator(String operator) {
        this.operator = operator;
    }

    @JsonProperty("effect")
    public String getEffect() {
        return effect;
    }

    @JsonProperty("effect")
    public void setEffect(String effect) {
        this.effect = effect;
    }

    @JsonProperty("tolerationSeconds")
    public Integer getTolerationSeconds() {
        return tolerationSeconds;
    }

    @JsonProperty("tolerationSeconds")
    public void setTolerationSeconds(Integer tolerationSeconds) {
        this.tolerationSeconds = tolerationSeconds;
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
