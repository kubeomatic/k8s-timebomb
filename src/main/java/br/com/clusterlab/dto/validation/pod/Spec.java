
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
    "volumes",
    "containers",
    "restartPolicy",
    "terminationGracePeriodSeconds",
    "dnsPolicy",
    "serviceAccountName",
    "serviceAccount",
    "securityContext",
    "schedulerName",
    "tolerations",
    "priority",
    "enableServiceLinks",
    "preemptionPolicy"
})
@Generated("jsonschema2pojo")
public class Spec {

    @JsonProperty("volumes")
    private List<Volume> volumes;
    @JsonProperty("containers")
    private List<Container> containers;
    @JsonProperty("restartPolicy")
    private String restartPolicy;
    @JsonProperty("terminationGracePeriodSeconds")
    private Integer terminationGracePeriodSeconds;
    @JsonProperty("dnsPolicy")
    private String dnsPolicy;
    @JsonProperty("serviceAccountName")
    private String serviceAccountName;
    @JsonProperty("serviceAccount")
    private String serviceAccount;
    @JsonProperty("securityContext")
    private SecurityContext securityContext;
    @JsonProperty("schedulerName")
    private String schedulerName;
    @JsonProperty("tolerations")
    private List<Toleration> tolerations;
    @JsonProperty("priority")
    private Integer priority;
    @JsonProperty("enableServiceLinks")
    private Boolean enableServiceLinks;
    @JsonProperty("preemptionPolicy")
    private String preemptionPolicy;
    @JsonIgnore
    private Map<String, java.lang.Object> additionalProperties = new LinkedHashMap<String, java.lang.Object>();

    @JsonProperty("volumes")
    public List<Volume> getVolumes() {
        return volumes;
    }

    @JsonProperty("volumes")
    public void setVolumes(List<Volume> volumes) {
        this.volumes = volumes;
    }

    @JsonProperty("containers")
    public List<Container> getContainers() {
        return containers;
    }

    @JsonProperty("containers")
    public void setContainers(List<Container> containers) {
        this.containers = containers;
    }

    @JsonProperty("restartPolicy")
    public String getRestartPolicy() {
        return restartPolicy;
    }

    @JsonProperty("restartPolicy")
    public void setRestartPolicy(String restartPolicy) {
        this.restartPolicy = restartPolicy;
    }

    @JsonProperty("terminationGracePeriodSeconds")
    public Integer getTerminationGracePeriodSeconds() {
        return terminationGracePeriodSeconds;
    }

    @JsonProperty("terminationGracePeriodSeconds")
    public void setTerminationGracePeriodSeconds(Integer terminationGracePeriodSeconds) {
        this.terminationGracePeriodSeconds = terminationGracePeriodSeconds;
    }

    @JsonProperty("dnsPolicy")
    public String getDnsPolicy() {
        return dnsPolicy;
    }

    @JsonProperty("dnsPolicy")
    public void setDnsPolicy(String dnsPolicy) {
        this.dnsPolicy = dnsPolicy;
    }

    @JsonProperty("serviceAccountName")
    public String getServiceAccountName() {
        return serviceAccountName;
    }

    @JsonProperty("serviceAccountName")
    public void setServiceAccountName(String serviceAccountName) {
        this.serviceAccountName = serviceAccountName;
    }

    @JsonProperty("serviceAccount")
    public String getServiceAccount() {
        return serviceAccount;
    }

    @JsonProperty("serviceAccount")
    public void setServiceAccount(String serviceAccount) {
        this.serviceAccount = serviceAccount;
    }

    @JsonProperty("securityContext")
    public SecurityContext getSecurityContext() {
        return securityContext;
    }

    @JsonProperty("securityContext")
    public void setSecurityContext(SecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    @JsonProperty("schedulerName")
    public String getSchedulerName() {
        return schedulerName;
    }

    @JsonProperty("schedulerName")
    public void setSchedulerName(String schedulerName) {
        this.schedulerName = schedulerName;
    }

    @JsonProperty("tolerations")
    public List<Toleration> getTolerations() {
        return tolerations;
    }

    @JsonProperty("tolerations")
    public void setTolerations(List<Toleration> tolerations) {
        this.tolerations = tolerations;
    }

    @JsonProperty("priority")
    public Integer getPriority() {
        return priority;
    }

    @JsonProperty("priority")
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @JsonProperty("enableServiceLinks")
    public Boolean getEnableServiceLinks() {
        return enableServiceLinks;
    }

    @JsonProperty("enableServiceLinks")
    public void setEnableServiceLinks(Boolean enableServiceLinks) {
        this.enableServiceLinks = enableServiceLinks;
    }

    @JsonProperty("preemptionPolicy")
    public String getPreemptionPolicy() {
        return preemptionPolicy;
    }

    @JsonProperty("preemptionPolicy")
    public void setPreemptionPolicy(String preemptionPolicy) {
        this.preemptionPolicy = preemptionPolicy;
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
