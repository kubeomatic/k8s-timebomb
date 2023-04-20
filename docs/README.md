# KubeOMatic

The TimeBomb solution is a Java Spring app which rely on [Kubernetes Dynamic Admission Control ](https://kubernetes.io/docs/reference/access-authn-authz/extensible-admission-controllers/) to add validty to PODs based on a timer annotation.

When you, or your pipeline, deploy an app at a kubernetes cluster TimeBomb will read a timer annotation and sum this timer to the current time. If you add a timer of 15 minutes a validity of the current time + timer will be added to the deployment as an annotation. This process is done using a "mutation webhook".

Deployments and PODs will only be created if they have a valid validity. An [EPOCH](https://en.wikipedia.org/wiki/Epoch) number greater than the current EPOCH.

After a POD is expired, EPOCH number inferior to current EPOCH, the schedule will delete the expired POD and the replicaset will no be able to deploy new PODs with expired validity.