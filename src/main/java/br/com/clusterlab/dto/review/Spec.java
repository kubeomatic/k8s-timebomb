
package br.com.clusterlab.dto.review;

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
    "replicas",
    "selector",
    "containers",
    "template",
    "strategy",
    "revisionHistoryLimit",
    "progressDeadlineSeconds"
})
@Generated("jsonschema2pojo")
public class Spec {

    @JsonProperty("replicas")
    private Integer replicas;
    @JsonProperty("selector")
    private Selector selector;
    @JsonProperty("containers")
    private List<Container> containers;
    @JsonProperty("template")
    private Template template;
    @JsonProperty("strategy")
    private Strategy strategy;
    @JsonProperty("revisionHistoryLimit")
    private Integer revisionHistoryLimit;
    @JsonProperty("progressDeadlineSeconds")
    private Integer progressDeadlineSeconds;
    @JsonIgnore
    private Map<String, java.lang.Object> additionalProperties = new LinkedHashMap<String, java.lang.Object>();

    @JsonProperty("replicas")
    public Integer getReplicas() {
        return replicas;
    }

    @JsonProperty("replicas")
    public void setReplicas(Integer replicas) {
        this.replicas = replicas;
    }

    @JsonProperty("selector")
    public Selector getSelector() {
        return selector;
    }

    @JsonProperty("selector")
    public void setSelector(Selector selector) {
        this.selector = selector;
    }

    @JsonProperty("containers")
    public List<Container> getContainers() {
        return containers;
    }

    @JsonProperty("containers")
    public void setContainers(List<Container> containers) {
        this.containers = containers;
    }

    @JsonProperty("template")
    public Template getTemplate() {
        return template;
    }

    @JsonProperty("template")
    public void setTemplate(Template template) {
        this.template = template;
    }

    @JsonProperty("strategy")
    public Strategy getStrategy() {
        return strategy;
    }

    @JsonProperty("strategy")
    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    @JsonProperty("revisionHistoryLimit")
    public Integer getRevisionHistoryLimit() {
        return revisionHistoryLimit;
    }

    @JsonProperty("revisionHistoryLimit")
    public void setRevisionHistoryLimit(Integer revisionHistoryLimit) {
        this.revisionHistoryLimit = revisionHistoryLimit;
    }

    @JsonProperty("progressDeadlineSeconds")
    public Integer getProgressDeadlineSeconds() {
        return progressDeadlineSeconds;
    }

    @JsonProperty("progressDeadlineSeconds")
    public void setProgressDeadlineSeconds(Integer progressDeadlineSeconds) {
        this.progressDeadlineSeconds = progressDeadlineSeconds;
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
