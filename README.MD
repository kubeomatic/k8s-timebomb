# KubeOMatic TimeBomb

KubeOMatic TimeBomb is a cleanup tool for Kubernetes clusters that removes pods once a configurable timer runs out. It installs a dynamic admission controller which mutates deployments with an expiration timestamp based on that timer. A separate scheduler constantly checks for expired pods and deletes them so the ReplicaSet cannot recreate outdated containers. This automated pruning helps keep clusters small and reduces infrastructure costs. For installation steps, see [docs/README.md](docs/README.md) or visit our [website](https://timebomb.kubeomatic.io/).
