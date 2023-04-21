# THIS DOCUMENTATION IS UNDER CUNSTRUCTION. THE INFORMATION HERE MAY BE INCOMPLETE - 2023/04

***

# KubeOMatic

The TimeBomb solution is a Java Spring app which rely on [Kubernetes Dynamic Admission Control ](https://kubernetes.io/docs/reference/access-authn-authz/extensible-admission-controllers/) to add validty to PODs based on a timer annotation.

When you, or your pipeline, deploy an app at a kubernetes cluster TimeBomb will read a timer annotation and sum this timer to the current time. If you add a timer of 15 minutes, a validity of the current time + timer will be added to the deployment as an annotation. This process is done using a "mutation webhook".

Deployments and PODs will only be created if they have a valid validity. An [EPOCH](https://en.wikipedia.org/wiki/Epoch) number greater than the current EPOCH.

After a POD is expired, EPOCH number inferior to current EPOCH, the schedule will delete the expired POD and the replicaset will no be able to deploy new PODs with expired validity.

***

## Applicability

In a cloud environment a kubernetes cluster is a bunch of computers.

Those computers are called "nodes" and the growth of the cluster is pushed by the increase in the number of PODs running.
Thus, the growth of PODs forces the increase in the number of nodes. And you are charged by the cloud provider by the amount of compute, CPU, is consumed. TimeBomb will reduce the PODs running so it can reduce the number of nodes.
### POC - Proof of Concept

TimeBom is greate for POC where you may try a new tool/solution and don't want to forget to remove it from your test cluster.

With TimeBomb it will happen automatically. TimeBomb will delete ir for you after the timer expire. :wink:

### Development and test environment

You may have different environments for each purpose in software development, quality assurance, tests and security.

After an application has passed security tests, unit test and been deployed in a quality assurance cluster, this app may not be required anymore running on a development cluster or any other inthermediate environment. With TimeBomb you can unprovision the app to reduce infrastructure cost.

You may have similar scenario for an app that is not in active development anymore and wish to be unprovisioned after few days from non productive environments.

TimeBomb will keep your footprint small in your cluster.

### DR - Disaster Recovery

You may have a disaster recovery cluster but do not want to keep all apps running there, but at the same time whishes to run all apps there for an amount of time to validate connectivity and functionality.

TimeBomb will unprovision all apps from the cluster after the expiration of the validity.
You can warm up all apps in a cluster using a simple command of JsonPatch with kubectl to extend the validty. This command is offered here with the solution and explained below.

***
## Architecture
***
## Installation
### HELM Values
### Certificate
### Install
***
## Extend Validity
