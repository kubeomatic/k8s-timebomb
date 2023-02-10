
package br.com.clusterlab.dto.validation.pod;

import java.util.LinkedHashMap;
import java.util.List;
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
    "sources",
    "defaultMode"
})
@Generated("jsonschema2pojo")
public class Projected {

    @JsonProperty("sources")
    private List<Source> sources;
    @JsonProperty("defaultMode")
    private Integer defaultMode;
    @JsonIgnore
    private Map<String, java.lang.Object> additionalProperties = new LinkedHashMap<String, java.lang.Object>();

    @JsonProperty("sources")
    public List<Source> getSources() {
        return sources;
    }

    @JsonProperty("sources")
    public void setSources(List<Source> sources) {
        this.sources = sources;
    }

    @JsonProperty("defaultMode")
    public Integer getDefaultMode() {
        return defaultMode;
    }

    @JsonProperty("defaultMode")
    public void setDefaultMode(Integer defaultMode) {
        this.defaultMode = defaultMode;
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
