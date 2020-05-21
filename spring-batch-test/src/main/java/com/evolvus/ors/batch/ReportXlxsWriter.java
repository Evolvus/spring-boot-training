package com.evolvus.ors.batch;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.ResourceAwareItemWriterItemStream;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.core.io.Resource;

public class ReportXlxsWriter implements ResourceAwareItemWriterItemStream<Map<String, Object>>, DisposableBean {

	private static final Logger LOG = LoggerFactory.getLogger(ReportXlxsWriter.class);
	private SXSSFWorkbook workbook;
	private SXSSFSheet sheet;
	private int currentRowNumber = 0;
	private Resource resource;
	private XlxsHeaderCallback headerCallback;
	private boolean headerWritten = false;
	
	public void setHeaderCallback(XlxsHeaderCallback xlxsHeaderCallback) {
		this.headerCallback = xlxsHeaderCallback;
	}

	private void writeRow(int index, Map<String, Object> item) {
		Set<String> columns = item.keySet();
		Row row = this.sheet.createRow(index);
		int i = 0;
		for (String s : columns) {
			writeCell(row, i, "" + item.get(s));
			i++;
		}
	}

	private void writeCell(Row row, int currentColumnNumber, String value) {
		Cell cell = row.createCell(currentColumnNumber);
		cell.setCellValue(value);
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		LOG.info("open: ");
		this.workbook = new SXSSFWorkbook();
		this.sheet = workbook.createSheet("Report");
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		// TODO Auto-generated method stub
		LOG.info("update: ");
	}

	@Override
	public void close() throws ItemStreamException {
		LOG.info("close:");

		try (FileOutputStream fos = new FileOutputStream(resource.getFile())) {
			this.workbook.write(fos);
			fos.close();
			this.workbook.dispose();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ItemStreamException(e);
		}
	}

	@Override
	public void setResource(Resource resource) {
		this.resource = resource;

	}

	@Override
	public void write(List<? extends Map<String, Object>> items) throws Exception {
		if(!headerWritten && headerCallback != null && items.size() > 0) {
	        Row row = this.sheet.createRow(0);
			headerCallback.writeHeader(row);
			headerWritten = true;
			currentRowNumber++;
		}
		
		for (int i = 0; i < items.size(); i++) {
			LOG.info("write: index: {}, value: {}, currentRowNumber: {}", i, items.get(i), currentRowNumber);
			writeRow(currentRowNumber++, items.get(i));
		}
	}

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		LOG.info("destroy:");
		this.workbook = null;
		this.resource = null;
		this.sheet = null;
		this.currentRowNumber = 0;

	}

}
