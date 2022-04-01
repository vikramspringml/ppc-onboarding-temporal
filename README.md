# PPC Sample Temporal Workflow 
Workflow Demo using Temporal.io (Spring Boot Example)

## Pre-requisites
You must make sure a temporal server is running
https://github.com/temporalio/docker-compose#readme

## Run the application

Clone the repo ```https://github.com/vikramspringml/ppc-onboarding-demo.git```

Use ```mvn install clean package spring-boot:run``` to run the application.

Invoke the REST API to start the workflow instance using POST ```http://localhost:8080/startWorkflow?id=[workflowid]```

Invoke the REST API to view the status of the workflow instance using GET ```http://localhost:8080/getwfstatus?id=onboarding_[workflowid]```

## View the workflow using the Temporal Workflow UI

On a web browser, navigate to ```http://localhost:8088/``` to access the Temporal UI to observe the workflow instances. 

## What does the Application do?
The workflow application invokes 3 activities in the following sequence
``Step 1`` --> ``Step 2`` --> ``Step 3`` `

Each Activity or step calls a Rest API and gets a response. 
If the API fails (when the REST API is not running for example), 
the temporal worflow engine persists the state of the activity 
to retry based on the following logic:



## What is Temporal.io ?

[Temporal](https://docs.temporal.io/) ```https://github.com/temporalio``` is an orchestration engine (MIT License) that can be used to orchestrate services at scale.


### Manually Create container image

#### Build and package the code by running mvn
```shell
mvn -DskipTests=true clean install spring-boot:repackage
```

#### Login to gcloud with your auth
```shell
gcloud auth login
```

#### Deploy the container to us.gcr.io
```shell 
gcloud builds submit .
--tag us.gcr.io/{gcp-projectid}/{container-name}
```

### Alternatively - Cloud Build command to Build and create image on GCR.IO
```shell
gcloud builds submit .
```

### Deploy to GKE
Run the following commands on cloud shell to authenticate gcloud, authenticate w GKE cluster and add master authorized network
```
gcloud auth login

gcloud config set project {PROJECT_ID}

gcloud container clusters get-credentials 
${GKE_CLUSTER} --region {GKE_CLUSTER_REGION} 
--project {PROJECT_ID}

gcloud container clusters update {GKE_CLUSTER} 
--region {GKE_CLUSTER_REGION} 
--enable-master-authorized-networks 
--master-authorized-networks $(dig +short myip.opendns.com @resolver1.opendns.com)/32
```

### Apply the GKE Deployments

#### App containing the API to trigger the workflow
- Apply workflow API service deployment
```shell
kubectl apply -f k8s/service.yaml
```
Verify your deployment using the deployment name <b>temporal-demo-api</b>

#### App containing the worker to poll workflow task queue and handle activities
- Apply workflow worker (Poller) deployment
```shell
kubectl apply -f k8s/deployment.yaml
```
Verify your deployment using the deployment name <b>temporal-demo-worker</b>

### Test workflow on GKE

#### Create port forward for web to view the Temporal Web UI
```shell
kubectl port-forward services/temporaltest-web 8088:8088
```
Navigate to http://localhost:8888 on your browser

#### Create port forward for temporal-service-api to invoke the workflow
```shell
kubectl port-forward services/temporal-demo-api-svc 8080:80
```
Invoke the API on curl using 
```shell 
curl -X POST http://localhost:8080/startWorkflow?id=002
``` 
