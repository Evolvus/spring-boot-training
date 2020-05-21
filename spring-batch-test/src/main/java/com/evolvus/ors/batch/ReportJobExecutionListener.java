package com.evolvus.ors.batch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ReportJobExecutionListener implements JobExecutionListener {

	private static final Logger LOG = LoggerFactory.getLogger(ReportJobExecutionListener.class);

	@Value("${report.file.path}")
	private String reportPath;
	
	@Override
	public void beforeJob(JobExecution jobExecution) {
		// TODO Auto-generated method stub
		LOG.info("Before Job");

	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		BatchStatus batchStatus = jobExecution.getStatus();
		String reportFileName = jobExecution.getJobParameters().getString("REPORT_FILE_NAME");
		String outputFileType = jobExecution.getJobParameters().getString("REPORT_OUTPUT_FILE_TYPE");
		SXSSFWorkbook workbook = (SXSSFWorkbook)jobExecution.getExecutionContext().get("WORKBOOK");
		
		if (LOG.isInfoEnabled()) {
			LOG.info("afterJob: FileName: {}, FileType: {}, Workbook: {}", reportFileName, outputFileType, workbook);
		}

		if (batchStatus == BatchStatus.COMPLETED) {

			if (!"CSV".equalsIgnoreCase(outputFileType)) {
				String outputFile = Paths.get(reportPath, reportFileName + "." + outputFileType).toString();
				try (FileOutputStream fos = new FileOutputStream(new File(outputFile))) {
					workbook.write(fos);
					fos.close();
					workbook.dispose();
				} catch (IOException e) {
					LOG.error(e.getMessage(), e);
				}
			}
		}

	}

}
