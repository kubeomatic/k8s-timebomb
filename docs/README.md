```diff
- THIS DOCUMENTATION IS UNDER CUNSTRUCTION. THE INFORMATION HERE MAY BE INCOMPLETE - 2023/04
```

---

# TimeBomb

TimeBomb does what the name suggests. You can add a timer to PODs to "explode" then.

The TimeBomb solution is a Spring Boot app which rely on [Kubernetes Dynamic Admission Control ](https://kubernetes.io/docs/reference/access-authn-authz/extensible-admission-controllers/) to add validty to PODs based on a timer annotation.

When you, or your pipeline, deploy an app at a kubernetes cluster TimeBomb will read a timer annotation and sum this timer to the current time to create a validity. If you add a timer of 15 minutes, a validity of the current time + 15 minutes will be added to the deployment as an annotation. This process is done using a "mutation webhook".

Deployments and PODs will only be created if they have a valid validity. An [EPOCH](https://en.wikipedia.org/wiki/Epoch) number greater than the current EPOCH.

After a POD is expired, EPOCH number inferior to current EPOCH, the schedule will delete the expired POD and the replicaset will not be able to deploy new PODs with expired validity.

You can warm up the app again by two methods.

1. Redeploy your app, so the mutation process can calculate a new validity based on the timer.
2. Do a JsonPatch on the resource by extending the validity annotation.

---

## Applicability

In a cloud environment a kubernetes cluster is a bunch of computers.

Those computers are called "nodes" and the growth of the cluster is pushed by the increase in the number of PODs running.
Thus, the growth of PODs forces the increase in the number of nodes. And you are charged by the cloud provider by the amount of compute, CPU, is consumed. TimeBomb will reduce the PODs running so it can reduce the number of nodes.

### POC - Proof of Concept

TimeBom is greate for POC where you may try a new tool/solution and don't want to forget to remove it from your test cluster.

With TimeBomb it will happen automatically. TimeBomb will delete ir for you after the timer expire.

### Development and test environment

You may have different environments for each purpose in software development, quality assurance, tests and security.

After an application has passed security tests, unit test and been deployed in a quality assurance cluster, this app may not be required anymore running on a development cluster or any other inthermediate environment. With TimeBomb you can unprovision the app to reduce infrastructure cost.

You may have similar scenario for an app that is not in active development anymore and wish to be unprovisioned after few days from non productive environments.

TimeBomb will keep your footprint small in your cluster.

### DR - Disaster Recovery

You may have a disaster recovery cluster but do not want to keep all apps running there, but at the same time whishes to run all apps there for an amount of time to validate connectivity and functionality.

TimeBomb will unprovision all apps from the cluster after the expiration of the validity.
You can warm up all apps in a cluster using a simple command of JsonPatch with kubectl to extend the validty. This command is offered here with the solution and explained below.

---

## Architecture

---

## Installation

### HELM Values
Example of [values.yaml](https://github.com/kubeomatic/k8s-timebomb/blob/main/helm-chart/kubeomatic-timebomb/values.yaml):
```yaml
admission:
  # imageTag: latest
  spec:
    replicas: 2
  config: |-
    server.port=8443
    logging.level.root=INFO
    management.endpoint.health.probes.enabled=true
  selector:
    # This label will be used to match events from the namespace.
    # Only namespaces with the label below and with one of the values below will have their operations intercepted by the kubernetes api for mutation/validation admission control.
    label: "kubeomatic-io-timebomb-cluster"
    values: ["dev-all","dev-02"]
  
schedule:
  # imageTag: latest
  spec:
    # Recommended number of replicas for the schedule is 1, one. Otherwise, you may have two PODs hitting the kubernetes API at the same time to delete PODs.
    replicas: 1
  config: |-
    server.port=8080
    logging.level.root=INFO
    management.endpoint.health.probes.enabled=true
    # The values below are used to search expired PODs. they need to match the selector from admission.
    kubeomatic-io.timebomb.label.selector.value=dev-02;dev-all
    # This cron expression has 6, six, values. The left one is for seconds. In this example schedule will search for expired PODs every 15 seconds.
    cron.expression= */15 * * * * *
certificate:
  rootCaCert: "" # Should be left empty if using CLI to load values
  keyStore: "" # Should be left empty if using CLI to load values
  keyStorePAss: "abcabc" # Keystore secret
common:
  name: "timebomb"
  nameSpace: "timebomb"
  registry: "registry.hub.docker.com"
  repository: "kubeomatic/"
  image: "timebomb"
  imageTag: "0.322"
  labels: {}
```
### Certificate

Kubernetes will send AdmissionReviews to the admission app only using TLS.

So, a [CA, Certificate authority,](https://en.wikipedia.org/wiki/Certificate_authority) with a certificate needs to be emitted.

ValidatingWebhookConfiguration and MutatingWebhookConfiguration needs to know the CA used to sign the certificate and the certificate needs to match the [DNS for Services](https://kubernetes.io/docs/concepts/services-networking/dns-pod-service/) in the cluster.

For that, there is a script that can be used to generate the Certificate authority, certificate and java keystore.

This script will read the information in the values.yaml from the helm chart.


### Install

1. Edit the values for the helm chart: [values.yaml](https://github.com/kubeomatic/k8s-timebomb/blob/main/helm-chart/kubeomatic-timebomb/values.yaml)
2. Add the [TimeBomb](https://helmchart.kubeomatic.io/timebomb/) helm repository.
3. Generate a keystore with a certificate. The certificate common name is dependable from the resource DNS name. Example: service.namespace.svc.cluster.local. To easy generate the certificate you may use this [script](https://github.com/kubeomatic/k8s-timebomb/blob/main/helm-chart/kubeomatic-timebomb/certificate_generator.sh) which reads the values.yaml.
4. Create the namespace in the cluster kubernetes for the solution.
5. Install solution via helm.

```shell
$ ./certificate_generator.sh values.yaml
$ kubectl create namespace timebomb
$ helm upgrade \
    --install \
    -n timebomb timebomb \
    -f values.yaml \
    --set "certificate.rootCaCert=$(cat tmp/rootCA.crt |base64 -w0),certificate.keyStore=$(cat tmp/ks.p12|base64 -w0)" \
    --version 0.1.4 \
    timebomb/kubeomatic-timebomb
```
---

## Extend Validity
After a validity been expired all resource for a solution will be there, in the cluster, except all the pods.

Example:

```shell
$ kubectl -n nginx get all
NAME                               READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/nginx-deployment   0/8     0            0           3d22h

NAME                                          DESIRED   CURRENT   READY   AGE
replicaset.apps/nginx-deployment-6598d88757   8         0         0       3d22h
```

To extend the validity all you have to do is a kubectl patching the validity.
```shell
$ export TIMER=90 # 90 minutes

$ export EPOCH=$(expr $(date +"%s") + $(expr 60 \* $TIMER ) )

$ kubectl -n nginx patch deployment nginx-deployment \
  --type='json' \
  -p='[ {"op": "replace", "path": "/spec/template/metadata/annotations/kubeomatic-io-timebomb-valid", "value":"'$EPOCH'"}]'
  
deployment.apps/nginx-deployment patched

$ kubectl -n nginx get all
NAME                                   READY   STATUS    RESTARTS   AGE
pod/nginx-deployment-fc8ff7b68-7k65n   1/1     Running   0          57s
pod/nginx-deployment-fc8ff7b68-jqsbw   1/1     Running   0          55s
pod/nginx-deployment-fc8ff7b68-k4smw   1/1     Running   0          58s
pod/nginx-deployment-fc8ff7b68-m8lgm   1/1     Running   0          58s
pod/nginx-deployment-fc8ff7b68-mbhdt   1/1     Running   0          58s
pod/nginx-deployment-fc8ff7b68-sgm2w   1/1     Running   0          55s
pod/nginx-deployment-fc8ff7b68-x5498   1/1     Running   0          56s
pod/nginx-deployment-fc8ff7b68-xlffk   1/1     Running   0          58s

NAME                               READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/nginx-deployment   8/8     8            8           3d22h

NAME                                          DESIRED   CURRENT   READY   AGE
replicaset.apps/nginx-deployment-6598d88757   0         0         0       3d22h
replicaset.apps/nginx-deployment-fc8ff7b68    8         8         8       58s
```
You may find a scenario where the replicaset still reflect the old validity annotation. In this case you may delete the outdated replicaset definition.

To make things easer, there is a [script](https://github.com/kubeomatic/k8s-timebomb/blob/main/tools/extend-timer-bash/run.sh) that help to those operations in large scale and is ideal to use with [RUNDECK](https://www.rundeck.com/) or other automation solutions.