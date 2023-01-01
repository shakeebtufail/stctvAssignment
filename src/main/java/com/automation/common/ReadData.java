package com.automation.common;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadData extends SetUpTest {
	static Logger log = Log4jUtil.loadLogger(ReadData.class);

	

	private File csvReaderFile;

	@DataProvider(name = "getCSVDataReader", parallel = true)
	public Object[][] getCSVData(ITestContext ctx, Method method)  {
		String strClassName=method.getDeclaringClass().getSimpleName();
		Object[][] obj = getCSVFileData(ctx.getName(), method.getName() ,strClassName);
		return obj;
	}

	public Object[][] getCSVFileData(String testName, String methodName,String strClassName) {
		String testType = System.getProperty("testType");
		Object[][] obj = null;
		List<Map<String, Object>> matrix = new ArrayList<Map<String, Object>>();
		/*CSVParser csvParser = null;
		Reader csvReader = null;*/
		int objPos = 0;
			
				csvReaderFile = new File(System.getProperty("user.dir") + "//src//test//resources//testData//"
						+ "//" + methodName + ".csv");
			
		try (Reader csvReader = Files.newBufferedReader(Paths.get(csvReaderFile.toURI()));
		CSVParser csvParser = new CSVParser(csvReader, CSVFormat.DEFAULT)){
			List<String> columns = new ArrayList<String>();
			List<CSVRecord> csvRecords = csvParser.getRecords();
			// List<Map<String, Object>> matrix = new ArrayList<Map<String, Object>>();
			csvRecordsOuterLoop: for (CSVRecord record : csvRecords) {

				Map<String, Object> row = new HashMap<String, Object>();
				for (int i = 0; i < record.size(); i++) {
					if (record.getRecordNumber() == 1) {
						for (String column : record)
							columns.add(column);
						continue csvRecordsOuterLoop;
					}
					String key = columns.get(i);
					Object val = record.get(i);
					row.put(key, val);
				}
				if (row.containsKey("Execute") && !row.containsValue("TRUE")) {
					// matrix.remove(entry);
					// log.info("The row is not required to be executed");
				} else {
					if (row.containsKey("TestType") && row.containsValue(testType)) {
						matrix.add(row);
					}
				}
				// matrix.add(row);
			}
			if (matrix.size() != 0) {
				obj = new Object[matrix.size()][1];
				for (Map<String, Object> entry : matrix) {
					obj[objPos][0] = entry;
					objPos++;
				}
				//csvParser.close();
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return obj;
	}

}

