
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
    "expirationSeconds",
    "path"
})
@Generated("jsonschema2pojo")
public class ServiceAccountToken {

    @JsonProperty("expirationSeconds")
    private Integer expirationSeconds;
    @JsonProperty("path")
    private String path;
    @JsonIgnore
    private Map<String, java.lang.Object> additionalProperties = new LinkedHashMap<String, java.lang.Object>();

    @JsonProperty("expirationSeconds")
    public Integer getExpirationSeconds() {
        return expirationSeconds;
    }

    @JsonProperty("expirationSeconds")
    public void setExpirationSeconds(Integer expirationSeconds) {
        this.expirationSeconds = expirationSeconds;
    }

    @JsonProperty("path")
    public String getPath() {
        return path;
    }

    @JsonProperty("path")
    public void setPath(String path) {
        this.path = path;
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
