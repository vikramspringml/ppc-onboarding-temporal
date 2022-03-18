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


## What is Temporal.io ?

[Temporal](https://docs.temporal.io/) ```https://github.com/temporalio``` is an orchestration engine (MIT License) that can be used to orchestrate services at scale.
