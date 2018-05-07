## reports
Library for creating reports and exposing them through REST Api

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
⋅⋅* create your report class that extends 'Report' abstract class.
..* create your Row class that implements 'Row' representing a row in data.
..* create your Filter class that implements 'Filter' interface.
..* create your Response class that impletent 'Response' interface.

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


