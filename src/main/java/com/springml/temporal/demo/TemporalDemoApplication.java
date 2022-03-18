package com.springml.temporal.demo;

import com.springml.temporal.demo.temporal.Activity;
import com.springml.temporal.demo.temporal.WorkFlow;
import com.springml.temporal.demo.temporal.WorkflowImpl;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TemporalDemoApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext context = SpringApplication.run(TemporalDemoApplication.class, args);
		WorkerFactory factory = context.getBean(WorkerFactory.class);
		Activity onboardActivities = context.getBean(Activity.class);
		Worker worker = factory.newWorker(WorkFlow.QUEUE_NAME);
		worker.registerWorkflowImplementationTypes(WorkflowImpl.class);
		worker.registerActivitiesImplementations(onboardActivities);
		factory.start();


	}

}
