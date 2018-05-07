## reports
Library for creating reports and exposing them through REST Api.
This exposes following 4 componets,
* abstract class Report
* interface Row
* interface Response
* interface Filter

Extending/implementing these components you can create your own report. Following reports are already implemented in this library,
* Ad Exchange report.
  Report on monthly data of an ad exchange. example:
	* All aggregated data: http://localhost:8080/reports
	  ```{"requests":68823820,"impressions":64422713,"clicks":184724,"conversions":39477,"revenue":128351.91,"fill_rate":93.6,"eCPM":1.99,"ctr":0.28,"cr":0.06}```
	  
	* For month February, 2018: http://localhost:8080/reports?year=2018&month=feb
	```{"month":"February","year":"2018","requests":33969832,"impressions":31322712,"clicks":97742,"conversions":18071,"revenue":62940.15,"fill_rate":92.2,"eCPM":2.0,"ctr":0.31,"cr":0.05}```
	
	* For site=android: http://localhost:8080/reports?site=android ```{"site":"android","requests":18835321,"impressions":17755397,"clicks":47329,"conversions":11365,"revenue":35320.53,"fill_rate":94.26,"eCPM":1.98,"ctr":0.26,"cr":0.06}```

* Calories Tracker. 
	Api to keep track of your calories intake.
	* Add breakfast: http://localhost:8080/caloriesTracker/add?date=2018-05-03&type=breakfast&calories=200
	* Add dinner: http://localhost:8080/caloriesTracker/add?date=2018-05-03&type=dinner&calories=400
	* Track calories: http://localhost:8080/caloriesTracker/track?date=2018-05-03


## setup

## prerequisite
* Java 8
* Mavan 3.5

Clone/download source and build and boot using mavan

```
mvn clean dependency:tree
```
Run tests
```
mvn test
```
Run application
```
mvn spring-boot:run
```

Open browser and go to url,
http://localhost:8080/reports

##
  

## setting up your own report
* create your row class that implements 'Row' interface, representing a row in data. This is core model of application. You need to specify dimension or primary key of your data by method prepareDimensionsKey 
```java
class AdExchangeDataRow implements Row{
	private String month;
	private String year;
	private String site;
	private int requests;
	private int impressions;
	private int clicks;
	private int conversions;
	private float revenue;
	
	@Override
	public String prepareDimensionsKey() {
			return year+"-"+month+"-"+site;
	}
	
  // ...... Setters and getters
}
```
* create your Filter class that implements 'Filter' interface. Filter contains parameters on basis of which you want to filter data. Here you define if a Row is filtered in or filtered out by implementing method filterIn().

```java
 public class AdExchangeDataFilter implements Filter{
	private String month;
	private String year;
	private String site;
	
	@Override
	public boolean filterIn(Row r) {
		AdExchangeDataRow row = (AdExchangeDataRow) r;
		if(
				(this.month == null || row.getMonth().equals(this.month))
				&&(this.year == null || row.getYear().equals(this.year))
				&&(this.site == null || row.getSite().equals(this.site))
				)
			return true;
		return false;
	}
}	
```

* create your Response class that impletent 'Response' interface. It needs to to implement setDerivedMetrics() and addMetrics(Row) methods. You specify how to aggregate data in addMetrics() method, and derived metrics calculation in setDerivedMetrics() method.
```java
@JsonInclude(JsonInclude.Include.NON_NULL)
class AdExchangeResponse implements Response{
	private String month;
	private String year;
	private String site;
	private int requests;
	private int impressions;
	private int clicks;
	private int conversions;
	private float revenue;
	private float CTR;
	private float CR;
	private float fill_rate;
	private float eCPM;
	
	/**
	 * Add metrics of data row to response.
	 */
	@Override
	public void addMetrics(Row r) {
		AdExchangeDataRow row = (AdExchangeDataRow) r;
		this.requests += row.getRequests();
		this.impressions += row.getImpressions();
		this.clicks += row.getClicks();
		this.conversions += row.getConversions();
		this.revenue += row.getRevenue();
	}
	
	@Override
	public void setDerivedMetrics() {
		if(requests !=0) {
			this.fill_rate = (float)this.impressions*100/this.requests;
		}
		if(impressions !=0) {
			this.CR = (float)this.conversions*100/this.impressions;
			this.CTR = (float)this.clicks*100/this.impressions;
			this.eCPM = this.revenue*1000/this.impressions;
		}
	}
// .....setters and getters
}
```
* create your report class extending 'Report' abstract class. You need to implement prepareData() and prepareEmptyResponse() methods.

```java
public class AdExchangeReport extends Report{
	public static final String DATA_PATH = "ad-exchange";
	
	@Override
	protected List<Row> prepareData() {
		List<Row> data = new ArrayList<Row>();
		ClassLoader classLoader = getClass().getClassLoader();
		File directory = new File(classLoader.getResource(DATA_PATH).getPath());
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
		}
		return data;
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
	 * Creates empty response
	 */
	@Override
	protected Response prepareEmptyResponse(Filter f) {
		AdExchangeDataFilter filter = (AdExchangeDataFilter) f; 
		AdExchangeResponse response = new AdExchangeResponse();
		response.setYear(filter.getYear());
		response.setMonth(filter.getMonth());
		response.setSite(filter.getSite());
		return response;
	}
	
}
```
* once this is ready, use fetchData() method to get your reports
```java
adExchangeReport = new AdExchangeReport();
adExchangeReport.init();
AdExchangeDataFilter filter = new AdExchangeDataFilter(month, year, site);
Response response = adExchangeReport.fetchData(filter);
		
```

* You can expose reports using REST API

```java
package api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import adExchangeReport.AdExchangeReport;
import report.Response;
import adExchangeReport.AdExchangeDataFilter;
import util.Month;

@RestController
public class ReportsController {
	static AdExchangeReport adExchangeReport;
	
	static {
		adExchangeReport = new AdExchangeReport();
		adExchangeReport.init();
	}
	
	@RequestMapping(value = "/reports", method = RequestMethod.GET)
	public Response  getReport(
			@RequestParam(required = false, value = "site") String site,
			@RequestParam(required = false, value = "year") String year,
			@RequestParam(required = false, value = "month") String month
			) {
		month = Month.toProperMonthString(month);
		AdExchangeDataFilter filter = new AdExchangeDataFilter(month, year, site);
		return adExchangeReport.fetchData(filter);
	}
}
```


