
package io.kubeomatic.timebombadmission.dto.review;

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
    "kubeomatic-io-timebomb-timer",
    "kubeomatic-io-timebomb-valid",
    "kubeomatic-io-timebomb-valid-human",
    "kubeomatic-io-timebomb-sku",
    "kubeomatic-io-timebomb-description"
})
@Generated("jsonschema2pojo")
public class Annotations__1 {

    @JsonProperty("kubeomatic-io-timebomb-timer")
    private String kubeomaticIoTimebombTimer;
    @JsonProperty("kubeomatic-io-timebomb-valid")
    private String kubeomaticIoTimebombValid;
    @JsonProperty("kubeomatic-io-timebomb-valid-human")
    private String kubeomaticIoTimebombValidHuman;
    @JsonProperty("kubeomatic-io-timebomb-sku")
    private String kubeomaticIoTimebombSku;
    @JsonProperty("kubeomatic-io-timebomb-description")
    private String kubeomaticIoTimebombDescription;
    @JsonIgnore
    private Map<String, java.lang.Object> additionalProperties = new LinkedHashMap<String, java.lang.Object>();

    @JsonProperty("kubeomatic-io-timebomb-timer")
    public String getKubeomaticIoTimebombTimer() {
        return kubeomaticIoTimebombTimer;
    }

    @JsonProperty("kubeomatic-io-timebomb-timer")
    public void setKubeomaticIoTimebombTimer(String kubeomaticIoTimebombTimer) {
        this.kubeomaticIoTimebombTimer = kubeomaticIoTimebombTimer;
    }

    @JsonProperty("kubeomatic-io-timebomb-valid")
    public String getKubeomaticIoTimebombValid() {
        return kubeomaticIoTimebombValid;
    }

    @JsonProperty("kubeomatic-io-timebomb-valid")
    public void setKubeomaticIoTimebombValid(String kubeomaticIoTimebombValid) {
        this.kubeomaticIoTimebombValid = kubeomaticIoTimebombValid;
    }

    @JsonProperty("kubeomatic-io-timebomb-valid-human")
    public String getKubeomaticIoTimebombValidHuman() {
        return kubeomaticIoTimebombValidHuman;
    }

    @JsonProperty("kubeomatic-io-timebomb-valid-human")
    public void setKubeomaticIoTimebombValidHuman(String kubeomaticIoTimebombValidHuman) {
        this.kubeomaticIoTimebombValidHuman = kubeomaticIoTimebombValidHuman;
    }

    @JsonProperty("kubeomatic-io-timebomb-sku")
    public String getKubeomaticIoTimebombSku() {
        return kubeomaticIoTimebombSku;
    }

    @JsonProperty("kubeomatic-io-timebomb-sku")
    public void setKubeomaticIoTimebombSku(String kubeomaticIoTimebombSku) {
        this.kubeomaticIoTimebombSku = kubeomaticIoTimebombSku;
    }

    @JsonProperty("kubeomatic-io-timebomb-description")
    public String getKubeomaticIoTimebombDescription() {
        return kubeomaticIoTimebombDescription;
    }

    @JsonProperty("kubeomatic-io-timebomb-description")
    public void setKubeomaticIoTimebombDescription(String kubeomaticIoTimebombDescription) {
        this.kubeomaticIoTimebombDescription = kubeomaticIoTimebombDescription;
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
