# KubeOMatic TimeBomb

KubeOMatic TimeBomb automatically removes Kubernetes pods after a user-defined timer expires. A dynamic admission controller annotates each deployment with an expiration timestamp, and a scheduler deletes pods once that validity has passed. This keeps clusters tidy and helps reduce costs.

For installation steps and more details, see the [documentation](docs/README.md) or visit our [website](https://timebomb.kubeomatic.io/).
