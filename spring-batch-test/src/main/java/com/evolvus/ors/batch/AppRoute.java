package com.evolvus.ors.batch;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class AppRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("direct:generateReport")
		.choice()
			.when(header("REPORT_OUTPUT_FILE_TYPE").isEqualTo("CSV"))
				.to("spring-batch:csvReportGenerationJob")
			.otherwise()
				.to("spring-batch:xlxsReportGenerationJob");
	}

}
