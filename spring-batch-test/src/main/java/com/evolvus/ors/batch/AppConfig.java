package com.evolvus.ors.batch;

import java.nio.file.Paths;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.PassThroughFieldExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.ColumnMapRowMapper;

@Configuration
public class AppConfig {

	public static final Logger LOG = LoggerFactory.getLogger(AppConfig.class);

	@Value("${report.file.path}")
	private String reportPath;

	@Value("${report.chunk.size:1000}")
	private String chunkSize;

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job csvReportGenerationJob(Step csvReportGenerationStep) {
		return jobBuilderFactory
				.get("reportGenerationJob")
				.incrementer(new RunIdIncrementer())
				.flow(csvReportGenerationStep)
				.end()
				.build();
	}
	
	@Bean
	public Job xlxsReportGenerationJob(Step xlxsReportGenerationStep, JobExecutionListener listener) {
		return jobBuilderFactory
				.get("reportGenerationJob")
//				.listener(listener)
				.incrementer(new RunIdIncrementer())
				.flow(xlxsReportGenerationStep)
				.end()
				.build();
	}

	@Bean
	public Step csvReportGenerationStep(FlatFileItemWriter<Map<String, Object>> csvReportWriter,
			JdbcCursorItemReader<Map<String, Object>> reportQueryJdbcReader) {
		return stepBuilderFactory
				.get("csvReportGenerationStep")
				.<Map<String, Object>, Map<String, Object>>chunk(Integer.parseInt(chunkSize))
				.reader(reportQueryJdbcReader)
				.writer(csvReportWriter)
				.build();
	}
	
	@Bean
	public Step xlxsReportGenerationStep(ReportXlxsWriter xlxsReportWriter,
			JdbcCursorItemReader<Map<String, Object>> reportQueryJdbcReader) {
		return stepBuilderFactory
				.get("xlxsReportGenerationStep")
				.<Map<String, Object>, Map<String, Object>>chunk(Integer.parseInt(chunkSize))
				.reader(reportQueryJdbcReader)
				.writer(xlxsReportWriter)
				.build();
	}

	@Bean
	@StepScope
	public JdbcCursorItemReader<Map<String, Object>> reportQueryJdbcReader(DataSource dataSource,
			@Value("#{jobParameters[OPERATION_QUERY]}") String sql) {
		if (LOG.isInfoEnabled()) {
			LOG.info("reportQueryJdbcReader: sql: {}", sql);
		}
		JdbcCursorItemReader<Map<String, Object>> result = new JdbcCursorItemReader<Map<String, Object>>();
		result.setName("reportQueryReader");
		result.setDataSource(dataSource);
		result.setSql(sql);
		result.setRowMapper(new ColumnMapRowMapper());

		return result;
	}


	@Bean
	@StepScope
	public FlatFileItemWriter<Map<String, Object>> csvReportWriter(
			@Value("#{jobParameters[REPORT_FILE_NAME]}") String dataFile,
			@Value("#{jobParameters[REPORT_OUTPUT_FILE_TYPE]}") String outputFileType) {
		if (LOG.isInfoEnabled()) {
			LOG.info("csvReportWriter: fileName: {}", dataFile);
			LOG.info("csvReportWriter: outputFileType: {}", outputFileType);
		}

		FlatFileItemWriter<Map<String, Object>> result = new FlatFileItemWriter<Map<String, Object>>();
		result.setName("csvReportWriter");
		result.setResource(new FileSystemResource(Paths.get(reportPath, dataFile + "." + outputFileType).toString()));
		result.setAppendAllowed(true);
		result.setHeaderCallback(new CsvHeaderCallback());
		result.setLineAggregator(new DelimitedLineAggregator<Map<String, Object>>() {
			{
				setDelimiter(",");
				setFieldExtractor(new PassThroughFieldExtractor<Map<String, Object>>());
			}
		});

		return result;
	}

	@Bean
	@StepScope
	public ReportXlxsWriter xlxsReportWriter(@Value("#{jobParameters[REPORT_FILE_NAME]}") String dataFile,
			@Value("#{jobParameters[REPORT_OUTPUT_FILE_TYPE]}") String outputFileType) {
		if (LOG.isInfoEnabled()) {
			LOG.info("xlxsReportWriter: fileName: {}", dataFile);
			LOG.info("xlxsReportWriter: outputFileType: {}", outputFileType);
		}

		ReportXlxsWriter xlxsWriter = new ReportXlxsWriter();
		xlxsWriter.setResource(new FileSystemResource(Paths.get(reportPath, dataFile + "." + outputFileType).toString()));
		return xlxsWriter;
	}

}
