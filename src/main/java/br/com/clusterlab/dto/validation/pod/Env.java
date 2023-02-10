
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
    "name",
    "valueFrom"
})
@Generated("jsonschema2pojo")
public class Env {

    @JsonProperty("name")
    private String name;
    @JsonProperty("valueFrom")
    private ValueFrom valueFrom;
    @JsonIgnore
    private Map<String, java.lang.Object> additionalProperties = new LinkedHashMap<String, java.lang.Object>();

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("valueFrom")
    public ValueFrom getValueFrom() {
        return valueFrom;
    }

    @JsonProperty("valueFrom")
    public void setValueFrom(ValueFrom valueFrom) {
        this.valueFrom = valueFrom;
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
