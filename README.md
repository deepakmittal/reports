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
  ..* All aggregated data: http://localhost:8080/reports
  ..* For month January, 2018: http://localhost:8080/reports?year=2018&month=jan
  ..* For site=android: http://localhost:8080/reports?site=android

* Calories Tracker. 
Api to keep track of your calories intake.
..* Add breakfast: http://localhost:8080/caloriesTracker/add?date=2018-05-03&type=breakfast&calories=200
..* Add dinner: http://localhost:8080/caloriesTracker/add?date=2018-05-03&type=dinner&calories=400
..* Track calories: http://localhost:8080/caloriesTracker/track?date=2018-05-03


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
public class CaloriesTracker extends Report{

	@Override
	protected List<Row> prepareData() {
    /**
    prepare data from any source.
    */
		return new ArrayList<Row>();
	}
  @Override
	protected Response prepareEmptyResponse(Filter f) {
		MealFilter filter = (MealFilter)f;
		CaloriesTrackerResponse r = new CaloriesTrackerResponse();
		r.date= filter.date;
		r.type=filter.type;
		return r;
	}
}  
```

```java
class MealRow implements Row{
	String type;
	float calories;
	String date;
	
	public MealRow(String date,String type, float calories) {
		super();
		this.type = type;
		this.calories = calories;
		this.date = date;
	}

	public String prepareDimensionsKey() {
		return type +"-" + date;
	}
	
}
```

```java

@JsonInclude(JsonInclude.Include.NON_NULL)
class CaloriesTrackerResponse implements Response{
	String type;
	float calories;
	String date;
	public void addMetrics(Row r) {
		MealRow row = (MealRow) r;
		this.calories += row.calories;
	}
	public void setFilterValues(Filter f) {
		MealFilter filter = (MealFilter) f;
		this.type = filter.type;
		this.date = filter.date;
		
	}
// write getters
}
```

```java
class MealFilter implements Filter{
	String type;	
	String date;
	
	public MealFilter(String type, String date) {
		this.type = type;
		this.date = date;
	}

	public boolean filterIn(Row r) {
		MealRow row =(MealRow) r;
		if(
				(this.type == null || this.type.equals(row.type))
				&&(this.date == null || this.date.equals(row.date))
				)
				return true;
		return false;
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


