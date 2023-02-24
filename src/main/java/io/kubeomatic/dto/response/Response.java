
package io.kubeomatic.dto.response;

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
    "uid",
    "allowed",
    "patchType",
    "patch",
    "status"
})
@Generated("jsonschema2pojo")
public class Response {

    @JsonProperty("uid")
    private String uid;
    @JsonProperty("allowed")
    private Boolean allowed;
    @JsonProperty("patchType")
    private String patchType;
    @JsonProperty("patch")
    private String patch;
    @JsonProperty("status")
    private Status status;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("uid")
    public String getUid() {
        return uid;
    }

    @JsonProperty("uid")
    public void setUid(String uid) {
        this.uid = uid;
    }

    @JsonProperty("allowed")
    public Boolean getAllowed() {
        return allowed;
    }

    @JsonProperty("allowed")
    public void setAllowed(Boolean allowed) {
        this.allowed = allowed;
    }

    @JsonProperty("patchType")
    public String getPatchType() {
        return patchType;
    }

    @JsonProperty("patchType")
    public void setPatchType(String patchType) {
        this.patchType = patchType;
    }

    @JsonProperty("patch")
    public String getPatch() {
        return patch;
    }

    @JsonProperty("patch")
    public void setPatch(String patch) {
        this.patch = patch;
    }

    @JsonProperty("status")
    public Status getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(Status status) {
        this.status = status;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
