
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
    "manager",
    "operation",
    "apiVersion",
    "time",
    "fieldsType",
    "fieldsV1"
})
@Generated("jsonschema2pojo")
public class ManagedField {

    @JsonProperty("manager")
    private String manager;
    @JsonProperty("operation")
    private String operation;
    @JsonProperty("apiVersion")
    private String apiVersion;
    @JsonProperty("time")
    private String time;
    @JsonProperty("fieldsType")
    private String fieldsType;
    @JsonProperty("fieldsV1")
    private FieldsV1 fieldsV1;
    @JsonIgnore
    private Map<String, java.lang.Object> additionalProperties = new LinkedHashMap<String, java.lang.Object>();

    @JsonProperty("manager")
    public String getManager() {
        return manager;
    }

    @JsonProperty("manager")
    public void setManager(String manager) {
        this.manager = manager;
    }

    @JsonProperty("operation")
    public String getOperation() {
        return operation;
    }

    @JsonProperty("operation")
    public void setOperation(String operation) {
        this.operation = operation;
    }

    @JsonProperty("apiVersion")
    public String getApiVersion() {
        return apiVersion;
    }

    @JsonProperty("apiVersion")
    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    @JsonProperty("time")
    public String getTime() {
        return time;
    }

    @JsonProperty("time")
    public void setTime(String time) {
        this.time = time;
    }

    @JsonProperty("fieldsType")
    public String getFieldsType() {
        return fieldsType;
    }

    @JsonProperty("fieldsType")
    public void setFieldsType(String fieldsType) {
        this.fieldsType = fieldsType;
    }

    @JsonProperty("fieldsV1")
    public FieldsV1 getFieldsV1() {
        return fieldsV1;
    }

    @JsonProperty("fieldsV1")
    public void setFieldsV1(FieldsV1 fieldsV1) {
        this.fieldsV1 = fieldsV1;
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
