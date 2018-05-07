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
	  ```{"requests":33969832,"impressions":31322712,"clicks":97742,"conversions":18071,"revenue":62940.15,"fill_rate":92.2,"eCPM":2.0,"ctr":0.31,"cr":0.05}```
	  
	* For month January, 2018: http://localhost:8080/reports?year=2018&month=feb
	```{"month":"February","year":"2018","requests":33969832,"impressions":31322712,"clicks":97742,"conversions":18071,"revenue":62940.15,"fill_rate":92.2,"eCPM":2.0,"ctr":0.31,"cr":0.05}```
	
	* For site=android: http://localhost:8080/reports?site=android ```{"site":"android","requests":8921215,"impressions":8342439,"clicks":22934,"conversions":5347,"revenue":17210.11,"fill_rate":93.51,"eCPM":2.06,"ctr":0.27,"cr":0.06}```

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
mvn spring-boot:run
```

Open browser and go to url,
http://localhost:8080/reports


## setting up your own report
* create your report class that extends 'Report' abstract class.
* create your Row class that implements 'Row' representing a row in data.
* create your Filter class that implements 'Filter' interface.
* create your Response class that impletent 'Response' interface.

```java
package report;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

public class GroceryStore extends Report{

	@Override
	protected List<Row> prepareData() {
		List<Row> data = new ArrayList<Row>();
		data.add(new TestRow("apple", "fruit",20.5f, 2));
		data.add(new TestRow("banana", "fruit",10.5f, 5));
		data.add(new TestRow("potato", "vegitable",15.2f, 5));
		return data;	
	}

	@Override
	protected Response prepareEmptyResponse(Filter f) {
		TestFilter filter = (TestFilter) f;
		TestResponse response = new TestResponse();
		response.name= filter.name;
		response.type = filter.type;
		return response;
	}
	
	public Filter getFilter(String name, String type) {
		return new TestFilter(name, type);
	}

}
class TestRow implements Row{
	String name;
	String type;
	float price;
	int quantity;
	
	@Override
	public String prepareDimensionsKey() {
		return name + "-" + type;
	}

	public TestRow(String dimension1, String dimension2, float metric1, int metric2) {
		super();
		this.name = dimension1;
		this.type = dimension2;
		this.price = metric1;
		this.quantity = metric2;
	}
	
}
class TestFilter implements Filter{
	String name;
	String type;
	@Override
	public boolean filterIn(Row row) {
		TestRow r = (TestRow) row;
		if( 
				(name == null || name == r.name)
				&&(type == null || type == r.type)
				)
			return true;
		return false;
	}
	public TestFilter(String dimension1, String dimension2) {
		super();
		this.name = dimension1;
		this.type = dimension2;
	}	
}

@JsonInclude(JsonInclude.Include.NON_NULL)
class TestResponse implements Response{
	// marking public just for test. do write getters.
	public String name; 
	public String type;
	public float price;
	public int quantity;
	public float averagePricePerItem;
	@Override
	public void addMetrics(Row row) {
		TestRow r = (TestRow) row;
		price += r.price;
		quantity += r.quantity;
	}
	@Override
	public void setFilterValues(Filter f) {
		TestFilter filter = (TestFilter) f;
		name = filter.name;
		type = filter.type;
	}
	@Override
	public void setDerivedMetrics() {
		averagePricePerItem = quantity==0 ? 0 :price/quantity;		
	}
	
}

```

* when your reports are ready expose them wring spring rest controller.

```java
package api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import adExchangeReport.AdExchangeReport;
import caloriesTracker.CaloriesTracker;
import report.Filter;
import report.Response;
import adExchangeReport.AdExchangeDataFilter;
import util.Month;

@RestController
@RequestMapping(value = "/caloriesTracker", method = RequestMethod.GET)
public class CaloriesTrackerController {
	static CaloriesTracker tracker;
	
	static {
		tracker = new CaloriesTracker();
		tracker.init();
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public void addMeal(
			@RequestParam(required = false, value = "date") String date,
			@RequestParam(required = false, value = "type") String type,
			@RequestParam(required = false, value = "calories") float calories
			) {
		tracker.addMeal(date, type, calories);
	}
	
	@RequestMapping(value = "/track", method = RequestMethod.GET)
	public Response track(
			@RequestParam(required = false, value = "date") String date,
			@RequestParam(required = false, value = "type") String type
			) {
		Filter filter = tracker.getFilter(date, type);
		return tracker.fetchData(filter);
	}
}
```


