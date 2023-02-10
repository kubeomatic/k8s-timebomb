
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
    "apiVersion",
    "kind",
    "name",
    "uid",
    "controller",
    "blockOwnerDeletion"
})
@Generated("jsonschema2pojo")
public class OwnerReference {

    @JsonProperty("apiVersion")
    private String apiVersion;
    @JsonProperty("kind")
    private String kind;
    @JsonProperty("name")
    private String name;
    @JsonProperty("uid")
    private String uid;
    @JsonProperty("controller")
    private Boolean controller;
    @JsonProperty("blockOwnerDeletion")
    private Boolean blockOwnerDeletion;
    @JsonIgnore
    private Map<String, java.lang.Object> additionalProperties = new LinkedHashMap<String, java.lang.Object>();

    @JsonProperty("apiVersion")
    public String getApiVersion() {
        return apiVersion;
    }

    @JsonProperty("apiVersion")
    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    @JsonProperty("kind")
    public String getKind() {
        return kind;
    }

    @JsonProperty("kind")
    public void setKind(String kind) {
        this.kind = kind;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("uid")
    public String getUid() {
        return uid;
    }

    @JsonProperty("uid")
    public void setUid(String uid) {
        this.uid = uid;
    }

    @JsonProperty("controller")
    public Boolean getController() {
        return controller;
    }

    @JsonProperty("controller")
    public void setController(Boolean controller) {
        this.controller = controller;
    }

    @JsonProperty("blockOwnerDeletion")
    public Boolean getBlockOwnerDeletion() {
        return blockOwnerDeletion;
    }

    @JsonProperty("blockOwnerDeletion")
    public void setBlockOwnerDeletion(Boolean blockOwnerDeletion) {
        this.blockOwnerDeletion = blockOwnerDeletion;
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
