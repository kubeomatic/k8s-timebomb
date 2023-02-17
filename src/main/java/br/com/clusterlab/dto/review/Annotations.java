
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
    "kubectl.kubernetes.io/last-applied-configuration"
})
@Generated("jsonschema2pojo")
public class Annotations {

    @JsonProperty("kubectl.kubernetes.io/last-applied-configuration")
    private String kubectlKubernetesIoLastAppliedConfiguration;
    @JsonIgnore
    private Map<String, java.lang.Object> additionalProperties = new LinkedHashMap<String, java.lang.Object>();

    @JsonProperty("kubectl.kubernetes.io/last-applied-configuration")
    public String getKubectlKubernetesIoLastAppliedConfiguration() {
        return kubectlKubernetesIoLastAppliedConfiguration;
    }

    @JsonProperty("kubectl.kubernetes.io/last-applied-configuration")
    public void setKubectlKubernetesIoLastAppliedConfiguration(String kubectlKubernetesIoLastAppliedConfiguration) {
        this.kubectlKubernetesIoLastAppliedConfiguration = kubectlKubernetesIoLastAppliedConfiguration;
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
