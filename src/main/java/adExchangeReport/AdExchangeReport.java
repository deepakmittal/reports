package adExchangeReport;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import report.Filter;
import report.Report;
import report.Response;
import report.Row;
import util.CsvReader;
import util.Month;

/**
 * Caters reporting requests for monthly ad exchange data.
 * data source: resources/ad-exchange/*.csv
 * @author deepak
 *
 */
public class AdExchangeReport extends Report{
	public static final String DATA_PATH = "ad-exchange";
	
	@Override
	protected List<Row> prepareData() {
		List<Row> data = new ArrayList<Row>();
		ClassLoader classLoader = getClass().getClassLoader();
		File directory = new File(classLoader.getResource(DATA_PATH).getFile());
		for(File file: directory.listFiles()) {
			String year = file.getName().substring(0, 4);
			String month = Month.toProperMonthString(file.getName().substring(5, 7));
			if(!file.getName().contains(".csv"))
				continue;
			CsvReader csvReader = new CsvReader(file);
			String[][] csvData = csvReader.parse();
			for(String[] line: csvData) {
				Row row = getRow(line, year, month);
				if(row !=null)data.add(row);
			}
			return data;
		}
		return null;
	}
	
	/**
	 * Creates empty response
	 */
	@Override
	protected Response prepareEmptyResponse(Filter f) {
		return new AdExchangeResponse();
	}
	
	/**
	 * prepare 1 row of data from csv line, and year and month parsed from file's name
	 * @param line
	 * @param year
	 * @param month
	 * @return
	 */
	private AdExchangeDataRow getRow(String[] line, String year, String month) {
		AdExchangeDataRow row = new AdExchangeDataRow();
		if(line[0].trim().equals("site") || line.length < 6)
			return null;
		row.setSite(line[0].trim());
		row.setYear(year);
		row.setMonth(month);
		row.setRequests(Integer.parseInt(line[1].trim()));
		row.setImpressions(Integer.parseInt(line[2].trim()));
		row.setClicks(Integer.parseInt(line[3].trim()));
		row.setConversions(Integer.parseInt(line[4].trim()));
		row.setRevenue(Float.parseFloat(line[5].trim()));
		return row;
	}
	
	/**
	 * Used only for testing
	 * TODO: delete once ready.
	 * @param args
	 */
	public static void main(String[] args) {
		AdExchangeReport r= new AdExchangeReport();
		r.prepareData();
		Filter filter= new AdExchangeDataFilter(null, "2018", null);
		AdExchangeResponse response = (AdExchangeResponse)r.fetchData(filter);
		System.out.println("Impressions:"+response.getImpressions());
	}



}
