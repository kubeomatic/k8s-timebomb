
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
    "admission-webhook",
    "app",
    "pod-template-hash",
    "timebomb-timeout",
    "br.com.clusterlab.timebomb"
})
@Generated("jsonschema2pojo")
public class Labels {

    @JsonProperty("admission-webhook")
    private String admissionWebhook;
    @JsonProperty("br.com.clusterlab.timebomb")
    private String brComClusterlabTimebomb;
    @JsonProperty("app")
    private String app;
    @JsonProperty("pod-template-hash")
    private String podTemplateHash;
    @JsonProperty("timebomb-timeout")
    private String timebombTimeout;

    @JsonIgnore
    private Map<String, java.lang.Object> additionalProperties = new LinkedHashMap<String, java.lang.Object>();

    @JsonProperty("admission-webhook")
    public String getAdmissionWebhook() {
        return admissionWebhook;
    }

    @JsonProperty("admission-webhook")
    public void setAdmissionWebhook(String admissionWebhook) {
        this.admissionWebhook = admissionWebhook;
    }

    @JsonProperty("app")
    public String getApp() {
        return app;
    }

    @JsonProperty("app")
    public void setApp(String app) {
        this.app = app;
    }

    @JsonProperty("pod-template-hash")
    public String getPodTemplateHash() {
        return podTemplateHash;
    }

    @JsonProperty("pod-template-hash")
    public void setPodTemplateHash(String podTemplateHash) {
        this.podTemplateHash = podTemplateHash;
    }

    @JsonProperty("timebomb-timeout")
    public String getTimebombTimeout() {
        return timebombTimeout;
    }

    @JsonProperty("timebomb-timeout")
    public void setTimebombTimeout(String timebombTimeout) {
        this.timebombTimeout = timebombTimeout;
    }
    @JsonProperty("br.com.clusterlab.timebomb")
    public String getBrComClusterlabTimebomb() {
        return brComClusterlabTimebomb;
    }

    @JsonProperty("br.com.clusterlab.timebomb")
    public void setBrComClusterlabTimebomb(String brComClusterlabTimebomb) {
        this.brComClusterlabTimebomb = brComClusterlabTimebomb;
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
