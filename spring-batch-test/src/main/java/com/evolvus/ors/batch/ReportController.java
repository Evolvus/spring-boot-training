package com.evolvus.ors.batch;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController {

	private static final Logger LOG = LoggerFactory.getLogger(ReportController.class);

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	@Qualifier("csvReportGenerationJob")
	private Job csvReportGenerationJob;

	@Autowired
	@Qualifier("xlxsReportGenerationJob")
	private Job xlxsReportGenerationJob;

	@Autowired
	private CamelContext context;

	@PostMapping(value = "/generateReport")
	public String generateReport(@RequestParam final String fileType) {

		final Map<String, Object> valueMap = new HashMap<String, Object>();
		valueMap.put("OPERATION_QUERY", "SELECT * FROM sample_data");
		valueMap.put("REPORT_FILE_NAME", "report_file");
		valueMap.put("REPORT_OUTPUT_FILE_TYPE", fileType);

		LOG.info("generateReport: {}", fileType);
		ProducerTemplate producerTemplate = context.createProducerTemplate();
		producerTemplate.sendBodyAndHeaders("direct:generateReport", valueMap, valueMap);
		String result = "ok";

		return result;
	}
	
	@PostMapping(value = "/generateReportBatch")
	public String generateReportBatch(@RequestParam final String fileType) {

		String uuid = UUID.randomUUID().toString();
		
		JobParametersBuilder jpb = new JobParametersBuilder();
		jpb.addString("OPERATION_QUERY", "SELECT * FROM sample_data");
		jpb.addString("REPORT_FILE_NAME", "report_file");
		jpb.addString("REPORT_OUTPUT_FILE_TYPE", fileType);
		jpb.addString("UUID", uuid);

		LOG.info("generateReportBatch: {}", fileType);
		JobExecution jobExecution = null;
		String result = "ok";

		try {
			if ("CSV".equalsIgnoreCase(fileType)) {
				jobExecution = jobLauncher.run(csvReportGenerationJob, jpb.toJobParameters());
			} else {
				jobExecution = jobLauncher.run(xlxsReportGenerationJob, jpb.toJobParameters());
			}
			result = jobExecution.getStatus().name();
		} catch (Exception e) {
			LOG.info("generateReportBatch: exception", e);
            result = "failed";
		}

		return result;
	}

}
