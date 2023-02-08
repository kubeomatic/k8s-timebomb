
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
    "serviceAccountToken",
    "configMap",
    "downwardAPI"
})
@Generated("jsonschema2pojo")
public class Source {

    @JsonProperty("serviceAccountToken")
    private ServiceAccountToken serviceAccountToken;
    @JsonProperty("configMap")
    private ConfigMap configMap;
    @JsonProperty("downwardAPI")
    private DownwardAPI downwardAPI;
    @JsonIgnore
    private Map<String, java.lang.Object> additionalProperties = new LinkedHashMap<String, java.lang.Object>();

    @JsonProperty("serviceAccountToken")
    public ServiceAccountToken getServiceAccountToken() {
        return serviceAccountToken;
    }

    @JsonProperty("serviceAccountToken")
    public void setServiceAccountToken(ServiceAccountToken serviceAccountToken) {
        this.serviceAccountToken = serviceAccountToken;
    }

    @JsonProperty("configMap")
    public ConfigMap getConfigMap() {
        return configMap;
    }

    @JsonProperty("configMap")
    public void setConfigMap(ConfigMap configMap) {
        this.configMap = configMap;
    }

    @JsonProperty("downwardAPI")
    public DownwardAPI getDownwardAPI() {
        return downwardAPI;
    }

    @JsonProperty("downwardAPI")
    public void setDownwardAPI(DownwardAPI downwardAPI) {
        this.downwardAPI = downwardAPI;
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
