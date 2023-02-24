
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
    "uid",
    "kind",
    "resource",
    "requestKind",
    "requestResource",
    "name",
    "namespace",
    "operation",
    "object",
    "dryRun"
})
@Generated("jsonschema2pojo")
public class Request {

    @JsonProperty("uid")
    private String uid;
    @JsonProperty("kind")
    private Kind kind;
    @JsonProperty("resource")
    private Resource resource;
    @JsonProperty("requestKind")
    private RequestKind requestKind;
    @JsonProperty("requestResource")
    private RequestResource requestResource;
    @JsonProperty("name")
    private String name;
    @JsonProperty("namespace")
    private String namespace;
    @JsonProperty("operation")
    private String operation;
    @JsonProperty("object")
    private Object object;
    @JsonProperty("dryRun")
    private Boolean dryRun;
    @JsonIgnore
    private Map<String, java.lang.Object> additionalProperties = new LinkedHashMap<String, java.lang.Object>();

    @JsonProperty("uid")
    public String getUid() {
        return uid;
    }

    @JsonProperty("uid")
    public void setUid(String uid) {
        this.uid = uid;
    }

    @JsonProperty("kind")
    public Kind getKind() {
        return kind;
    }

    @JsonProperty("kind")
    public void setKind(Kind kind) {
        this.kind = kind;
    }

    @JsonProperty("resource")
    public Resource getResource() {
        return resource;
    }

    @JsonProperty("resource")
    public void setResource(Resource resource) {
        this.resource = resource;
    }

    @JsonProperty("requestKind")
    public RequestKind getRequestKind() {
        return requestKind;
    }

    @JsonProperty("requestKind")
    public void setRequestKind(RequestKind requestKind) {
        this.requestKind = requestKind;
    }

    @JsonProperty("requestResource")
    public RequestResource getRequestResource() {
        return requestResource;
    }

    @JsonProperty("requestResource")
    public void setRequestResource(RequestResource requestResource) {
        this.requestResource = requestResource;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("namespace")
    public String getNamespace() {
        return namespace;
    }

    @JsonProperty("namespace")
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @JsonProperty("operation")
    public String getOperation() {
        return operation;
    }

    @JsonProperty("operation")
    public void setOperation(String operation) {
        this.operation = operation;
    }

    @JsonProperty("object")
    public Object getObject() {
        return object;
    }

    @JsonProperty("object")
    public void setObject(Object object) {
        this.object = object;
    }

    @JsonProperty("dryRun")
    public Boolean getDryRun() {
        return dryRun;
    }

    @JsonProperty("dryRun")
    public void setDryRun(Boolean dryRun) {
        this.dryRun = dryRun;
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
