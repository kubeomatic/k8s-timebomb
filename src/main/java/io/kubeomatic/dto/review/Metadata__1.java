
package io.kubeomatic.dto.review;

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
    "creationTimestamp",
    "labels",
    "annotations"
})
@Generated("jsonschema2pojo")
public class Metadata__1 {

    @JsonProperty("creationTimestamp")
    private java.lang.Object creationTimestamp;
    @JsonProperty("labels")
    private Labels__1 labels;
    @JsonProperty("annotations")
    private Annotations__1 annotations;
    @JsonIgnore
    private Map<String, java.lang.Object> additionalProperties = new LinkedHashMap<String, java.lang.Object>();

    @JsonProperty("creationTimestamp")
    public java.lang.Object getCreationTimestamp() {
        return creationTimestamp;
    }

    @JsonProperty("creationTimestamp")
    public void setCreationTimestamp(java.lang.Object creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    @JsonProperty("labels")
    public Labels__1 getLabels() {
        return labels;
    }

    @JsonProperty("labels")
    public void setLabels(Labels__1 labels) {
        this.labels = labels;
    }

    @JsonProperty("annotations")
    public Annotations__1 getAnnotations() {
        return annotations;
    }

    @JsonProperty("annotations")
    public void setAnnotations(Annotations__1 annotations) {
        this.annotations = annotations;
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
