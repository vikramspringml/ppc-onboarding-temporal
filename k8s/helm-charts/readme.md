## Prerequisites

This sequence assumes

* that your machine has
  * [gcloud CLI](https://cloud.google.com/sdk/docs/install),
  * [kubectl](https://cloud.google.com/kubernetes-engine/docs/how-to/cluster-access-for-kubectl)
  installed and configured.
* that you already created DB instance in Cloud sql
  * [Postgres](https://cloud.google.com/sql/docs/postgres/create-instance)
* that you already clone [Temporal helm chart](https://github.com/temporalio/helm-charts) repo and replace the files from our helm chart respectively.

# set cloud project

```sh
gcloud config set project {PROJECT_ID}
```

# Connect to GKE Cluster

```sh
gcloud container clusters get-credentials GKE_CLUSTER --zone GKE_CLUSTER_ZONE --project PROJECT_ID
```

# Verify Cluster Connection

```sh
gcloud container clusters list --project PROJECT_ID
```

### If you already install Helm skip this step

# HELM SETUP

```sh
# Download Helm release
wget https://get.helm.sh/helm-v3.8.2-linux-amd64.tar.gz

#helm-v3.8.2-linux-amd64.tar.gz          100%[===============================================================================>]  13.00M  --.-KB/s    in 0.09s
#2022-04-19 08:31:53 (144 MB/s) - ‘helm-v3.8.2-linux-amd64.tar.gz’ saved [13633605/13633605]

# Decompress Archive
tar -zxvf helm-v3.8.2-linux-amd64.tar.gz

# Move Helm Binary to Executable Location
sudo mv linux-amd64/helm /usr/local/bin/helm

# Cleanup Junk Files
rm -rf helm-v3.8.2-linux-amd64.tar.gz linux-amd64/

# Verify installation
helm version
version.BuildInfo{Version:"v3.8.2", GitCommit:"6e3701edea09e5d55a8ca2aae03a68917630e91b",GitTreeState:"clean", GoVersion:"go1.17.5"}

#Once Helm is set up properly, add the repo as follows:
helm repo add stable https://charts.helm.sh/stable

helm repo update

#You can then run helm search repo stable to see the charts, or browse on CNCF Artifact Hub.
```

# Download the Cloud SQL Auth proxy in gcloud/local (its better to test the DB connection gcloud/locally first.)

```sh
wget https://dl.google.com/cloudsql/cloud_sql_proxy.linux.amd64 -O cloud_sql_proxy

# Make the Cloud SQL Auth proxy executable:
chmod +x cloud_sql_proxy

# Get the instance connection  name from Cloud SQL Instances
# Start the Cloud SQL Auth proxy
./cloud_sql_proxy -instances=_PROJECTNAME_:_REGION_:_INSTANCENAME_=tcp:5432

Example:
./cloud_sql_proxy -instances=cbregcpsandbox:us-central1:temporal-postgre-db=tcp:5432

# run below command in other terminal to confirm the connection and see the output in previous terminal
psql "host=127.0.0.1 port=5432 sslmode=disable dbname=temporal-test user=postgres"
```

# Installation

### Download Helm Chart Dependencies

```sh
# clone temporal helm chart git repo
git clone https://github.com/temporalio/helm-charts.git
cd helm-charts

# Download Helm dependencies:
helm dependencies update
```

### Creating secrets

```sh
# Create a credential file for your service account key:
Replace the following values:

#key-file: The path to a new output file for the private key—for example, ~/sa-private-key.json.
#sa-name: The name of the service account to create a key for.
#project-id: Your Google Cloud project ID.
gcloud iam service-accounts keys create KEY_FILE --iam-account SERVICE_ACCOUNT@PROJECT_ID.iam.gserviceaccount.com
#Example:
#gcloud iam service-accounts keys create ~/key.json --iam-account service_account@prj_id.iam.gserviceaccount.com

# Turn your service account key into a k8s Secret:
kubectl create secret generic temporalk8s-secret --from-file=service_account.json=key.json

# get list of secrets from GKE cluster
kubectl get secrets

# create a database credentials Secret:
kubectl create secret generic <YOUR-DB-SECRET> \
  --from-literal=username=<YOUR-DATABASE-USER> \
  --from-literal=password=<YOUR-DATABASE-PASSWORD> \
  --from-literal=database=<YOUR-DATABASE-NAME>

Example:
kubectl create secret generic temporalk8s-db-secret --from-literal=username=postgres --from-literal=password=tadmin --from-literal=database=temporal-test
```

### Install with sidecar containers

You may need to provide your own sidecar containers.

To do so, you may look at the example for Google's `cloud sql proxy` in the `values/values.cloudsqlproxy.yaml` and pass that file to `helm install`. Please update it accordingly.

Example:

```bash
~/temporal-helm$ helm install -f values/values.cloudsqlproxy.yaml temporaltest . --timeout 900s
```

### Exploring Your Cluster

You can use your favorite kubernetes tools ([k9s](https://github.com/derailed/k9s), [kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/), etc.) to interact with your cluster.

```bash
$ kubectl get svc
NAME                                   TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)                                        AGE
...
temporaltest-admintools                ClusterIP   172.20.237.59    <none>        22/TCP                                         15m
temporaltest-frontend-headless         ClusterIP   None             <none>        7233/TCP,9090/TCP                              15m
temporaltest-history-headless          ClusterIP   None             <none>        7234/TCP,9090/TCP                              15m
temporaltest-matching-headless         ClusterIP   None             <none>        7235/TCP,9090/TCP                              15m
temporaltest-worker-headless           ClusterIP   None             <none>        7239/TCP,9090/TCP                              15m
...
```

```bash
$ kubectl get pods
...
temporaltest-admintools-7b6c599855-8bk4x                1/1     Running   0          25m
temporaltest-frontend-54d94fdcc4-bx89b                  1/1     Running   2          25m
temporaltest-history-86d8d7869-lzb6f                    1/1     Running   2          25m
temporaltest-matching-6c7d6d7489-kj5pj                  1/1     Running   3          25m
temporaltest-worker-769b996fd-qmvbw                     1/1     Running   2          25m
...
```

### Running Temporal SQL TOOL From the Admin Tools Container

```sh
kubectl exec -it services/temporaltest-admintools /bin/bash
```

or

```sh
#if multiple container present in pod
kubectl exec -it services/temporaltest-admintools -c CONTAINER_NAME bash

Example:
kubectl exec -it services/temporaltest-admintools -c admin-tools bash
```

### create and initialize the databases

```sh
export SQL_PLUGIN=postgres
export SQL_HOST=127.0.0.1 #we are using Cloud SQL Auth proxy invocations for  Unix, using TCP 
export SQL_PORT=DB_PORT
export SQL_USER=DB_USER
export SQL_PASSWORD=DB_PASSWORD

#we can pass the HOST,USERRNAME,PASSWORD in the TEMPORAL SQL TOOL itself.
#temporal-sql-tool --ep 127.0.0.1 -p 5432 -u postgres -pw tadmin --pl postgres create-database -database temporal


temporal-sql-tool --pl postgres create-database -database temporal
temporal-sql-tool --pl postgres --db temporal setup -v 0.0
temporal-sql-tool --pl postgres --db temporal update-schema -d ./schema/postgresql/v96/temporal/versioned

temporal-sql-tool --pl postgres create --db temporal_visibility
temporal-sql-tool --pl postgres --db temporal_visibility setup-schema -v 0.0
temporal-sql-tool --pl postgres --db temporal_visibility update-schema -d ./schema/postgresql/v96/visibility/versioned
```

### Running Temporal CLI From the Admin Tools Container

You can also shell into `admin-tools` container via [k9s](https://github.com/derailed/k9s) or by running

```sh
kubectl exec -it services/temporaltest-admintools /bin/bash
```

or

```sh
#if multiple container present in pod
kubectl exec -it services/temporaltest-admintools -c admin-tools bash
```

and run Temporal CLI from there:

```bash
bash-5.0# tctl namespace list
Name: temporal-system
Id: 32049b68-7872-4094-8e63-d0dd59896a83
Description: Temporal internal system namespace
OwnerEmail: temporal-core@temporal.io
NamespaceData: map[string]string(nil)
Status: Registered
RetentionInDays: 7
EmitMetrics: true
ActiveClusterName: active
Clusters: active
HistoryArchivalStatus: Disabled
VisibilityArchivalStatus: Disabled
Bad binaries to reset:
+-----------------+----------+------------+--------+
| BINARY CHECKSUM | OPERATOR | START TIME | REASON |
+-----------------+----------+------------+--------+
+-----------------+----------+------------+--------+
```

```bash
bash-5.0# tctl --namespace nonesuch namespace desc
Error: Namespace nonesuch does not exist.
Error Details: Namespace nonesuch does not exist.
```

```bash
bash-5.0# tctl --namespace nonesuch namespace re
Namespace nonesuch successfully registered.
```

```bash
bash-5.0# tctl --namespace nonesuch namespace desc
Name: nonesuch
UUID: 465bb575-8c01-43f8-a67d-d676e1ae5eae
Description:
OwnerEmail:
NamespaceData: map[string]string(nil)
Status: Registered
RetentionInDays: 3
EmitMetrics: false
ActiveClusterName: active
Clusters: active
HistoryArchivalStatus: ArchivalStatusDisabled
VisibilityArchivalStatus: ArchivalStatusDisabled
Bad binaries to reset:
+-----------------+----------+------------+--------+
| BINARY CHECKSUM | OPERATOR | START TIME | REASON |
+-----------------+----------+------------+--------+
+-----------------+----------+------------+--------+
```

# Uninstalling the Temporal instances before we Install the new Temporal instances configured to use own Database

### Note: in this example chart, uninstalling a Temporal instance also removes all the data that might have been created during its lifetime

### Uninstalling

```sh
helm uninstall temporaltest
```

# Install Temporal service configured to use own DB instance on a Kubernetes cluster

### Once you initialized the two databases, instead of modifying values/values.postgresql.yaml, you can supply those values in your command line, and run

```sh
helm install -f values/values.cloudsqlproxy.yaml -f values/values.postgresql.yaml temporaltest --set elasticsearch.enabled=false --set prometheus.enabled=false --set grafana.enabled=false --set server.config.persistence.default.sql.user=postgres --set server.config.persistence.default.sql.password=tadmin --set server.config.persistence.visibility.sql.user=postgres --set server.config.persistence.visibility.sql.password=tadmin --set server.config.persistence.default.sql.host=127.0.0.1 --set server.config.persistence.visibility.sql.host=127.0.0.1 . --timeout 900s
```
