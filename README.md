# SpringML Temporal Workflow Demo
Workflow Demo using Temporal.io (Spring Boot Example)

## Pre-requisites
You must make sure a temporal server is running
https://github.com/temporalio/docker-compose#readme

## Run the application

Clone the repo ```https://github.com/vikramspringml/temporal-wf-demo.git```

Use ```mvn install clean package spring-boot:run``` to run the application.

Invoke the REST API to start the workflow instance using POST ```http://localhost:8080/startWorkflow?id=[workflowid]```

Invoke the REST API to view the status of the workflow instance using GET ```http://localhost:8080/getwfstatus?id=onboarding_[workflowid]```

## View the workflow using the Temporal Workflow UI

On a web browser, navigate to ```http://localhost:8088/``` to access the Temporal UI to observe the workflow instances. 

## What does the Application do?
The workflow application calls 4 activities in the following order
``Step 1`` --> ``Step 2`` --> ``Step 3`` --> ``Step 4``

Each Activity or step calls a Rest API and gets a response. If the API fails (when the REST API is not running for example), the temporal worflow engine persists the state of the activity to retry based on the following logic:

```java
private final RetryOptions retryoptions = RetryOptions.newBuilder().setInitialInterval(Duration.ofSeconds(1))
            .setMaximumInterval(Duration.ofSeconds(100)).setBackoffCoefficient(2).setMaximumAttempts(50000).build();
    private final ActivityOptions options = ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(30))
            .setRetryOptions(retryoptions).build();
```

## What is Temporal.io ?

[Temporal](https://docs.temporal.io/) ```https://github.com/temporalio``` is an orchestration engine (MIT License) that can be used to orchestrate services at scale.
