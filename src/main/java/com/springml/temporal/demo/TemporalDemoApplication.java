package com.springml.temporal.demo;

import com.springml.temporal.demo.temporal.Activity;
import com.springml.temporal.demo.temporal.ShipWorkFlow;
import com.springml.temporal.demo.temporal.ShipWorkflowImpl;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TemporalDemoApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext context = SpringApplication.run(TemporalDemoApplication.class, args);
		String workerFlag = StringUtils.defaultString( System.getenv("WORKER"), "APP");
//		if("WORKER".equals(workerFlag)) {
			WorkerFactory factory = context.getBean(WorkerFactory.class);
			Activity onboardActivities = context.getBean(Activity.class);
			Worker worker = factory.newWorker(ShipWorkFlow.QUEUE_NAME);
			worker.registerWorkflowImplementationTypes(ShipWorkflowImpl.class);
			worker.registerActivitiesImplementations(onboardActivities);
			factory.start();
//		}
	}

}
