package com.evolvus.ors.batch;

import org.apache.poi.ss.usermodel.Row;

public interface XlxsHeaderCallback {
	void writeHeader(Row headerRow);
}
