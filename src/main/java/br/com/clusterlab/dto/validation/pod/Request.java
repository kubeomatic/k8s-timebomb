
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
    "uid",
    "kind",
    "resource",
    "requestKind",
    "requestResource",
    "name",
    "namespace",
    "operation",
    "userInfo",
    "object",
    "oldObject",
    "dryRun",
    "options"
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
    @JsonProperty("userInfo")
    private UserInfo userInfo;
    @JsonProperty("object")
    private br.com.clusterlab.dto.validation.pod.Object object;
    @JsonProperty("oldObject")
    private br.com.clusterlab.dto.validation.pod.Object oldObject;
//    private java.lang.Object oldObject;
    @JsonProperty("dryRun")
    private Boolean dryRun;
    @JsonProperty("options")
    private Options options;
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

    @JsonProperty("userInfo")
    public UserInfo getUserInfo() {
        return userInfo;
    }

    @JsonProperty("userInfo")
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @JsonProperty("object")
    public br.com.clusterlab.dto.validation.pod.Object getObject() {
        return object;
    }

    @JsonProperty("object")
    public void setObject(br.com.clusterlab.dto.validation.pod.Object object) {
        this.object = object;
    }

    @JsonProperty("oldObject")
    public br.com.clusterlab.dto.validation.pod.Object getOldObject() {
        return oldObject;
    }

    @JsonProperty("oldObject")
    public void setOldObject(br.com.clusterlab.dto.validation.pod.Object  oldObject) {
        this.oldObject = oldObject;
    }
//    public void setOldObject(java.lang.Object oldObject) {
//        this.oldObject = oldObject;
//    }

    @JsonProperty("dryRun")
    public Boolean getDryRun() {
        return dryRun;
    }

    @JsonProperty("dryRun")
    public void setDryRun(Boolean dryRun) {
        this.dryRun = dryRun;
    }

    @JsonProperty("options")
    public Options getOptions() {
        return options;
    }

    @JsonProperty("options")
    public void setOptions(Options options) {
        this.options = options;
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
