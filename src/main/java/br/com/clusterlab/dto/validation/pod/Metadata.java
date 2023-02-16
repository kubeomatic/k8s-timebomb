
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
    "name",
    "generateName",
    "namespace",
    "uid",
    "creationTimestamp",
    "labels",
    "annotations",
    "ownerReferences",
    "managedFields"
})
@Generated("jsonschema2pojo")
public class Metadata {

    @JsonProperty("name")
    private String name;
    @JsonProperty("generateName")
    private String generateName;
    @JsonProperty("namespace")
    private String namespace;
    @JsonProperty("uid")
    private String uid;
    @JsonProperty("annotations")
    private Annotations annotations;
    @JsonProperty("creationTimestamp")
    private String creationTimestamp;
    @JsonProperty("labels")
    private Labels labels;
    @JsonProperty("ownerReferences")
    private List<OwnerReference> ownerReferences;
    @JsonProperty("managedFields")
    private List<ManagedField> managedFields;
    @JsonIgnore
    private Map<String, java.lang.Object> additionalProperties = new LinkedHashMap<String, java.lang.Object>();
    @JsonProperty("annotations")
    public Annotations getAnnotations() {
        return annotations;
    }

    @JsonProperty("annotations")
    public void setAnnotations(Annotations annotations) {
        this.annotations = annotations;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("generateName")
    public String getGenerateName() {
        return generateName;
    }

    @JsonProperty("generateName")
    public void setGenerateName(String generateName) {
        this.generateName = generateName;
    }

    @JsonProperty("namespace")
    public String getNamespace() {
        return namespace;
    }

    @JsonProperty("namespace")
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @JsonProperty("uid")
    public String getUid() {
        return uid;
    }

    @JsonProperty("uid")
    public void setUid(String uid) {
        this.uid = uid;
    }

    @JsonProperty("creationTimestamp")
    public String getCreationTimestamp() {
        return creationTimestamp;
    }

    @JsonProperty("creationTimestamp")
    public void setCreationTimestamp(String creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    @JsonProperty("labels")
    public Labels getLabels() {
        return labels;
    }

    @JsonProperty("labels")
    public void setLabels(Labels labels) {
        this.labels = labels;
    }

    @JsonProperty("ownerReferences")
    public List<OwnerReference> getOwnerReferences() {
        return ownerReferences;
    }

    @JsonProperty("ownerReferences")
    public void setOwnerReferences(List<OwnerReference> ownerReferences) {
        this.ownerReferences = ownerReferences;
    }

    @JsonProperty("managedFields")
    public List<ManagedField> getManagedFields() {
        return managedFields;
    }

    @JsonProperty("managedFields")
    public void setManagedFields(List<ManagedField> managedFields) {
        this.managedFields = managedFields;
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
