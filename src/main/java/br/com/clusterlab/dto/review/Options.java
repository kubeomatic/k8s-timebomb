
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
    "kind",
    "apiVersion",
    "fieldManager",
    "fieldValidation"
})
@Generated("jsonschema2pojo")
public class Options {

    @JsonProperty("kind")
    private String kind;
    @JsonProperty("apiVersion")
    private String apiVersion;
    @JsonProperty("fieldManager")
    private String fieldManager;
    @JsonProperty("fieldValidation")
    private String fieldValidation;
    @JsonIgnore
    private Map<String, java.lang.Object> additionalProperties = new LinkedHashMap<String, java.lang.Object>();

    @JsonProperty("kind")
    public String getKind() {
        return kind;
    }

    @JsonProperty("kind")
    public void setKind(String kind) {
        this.kind = kind;
    }

    @JsonProperty("apiVersion")
    public String getApiVersion() {
        return apiVersion;
    }

    @JsonProperty("apiVersion")
    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    @JsonProperty("fieldManager")
    public String getFieldManager() {
        return fieldManager;
    }

    @JsonProperty("fieldManager")
    public void setFieldManager(String fieldManager) {
        this.fieldManager = fieldManager;
    }

    @JsonProperty("fieldValidation")
    public String getFieldValidation() {
        return fieldValidation;
    }

    @JsonProperty("fieldValidation")
    public void setFieldValidation(String fieldValidation) {
        this.fieldValidation = fieldValidation;
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
