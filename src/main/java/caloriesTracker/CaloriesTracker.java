package caloriesTracker;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import report.Filter;
import report.Report;
import report.Response;
import report.Row;

public class CaloriesTracker extends Report{

	@Override
	protected List<Row> prepareData() {
		return new ArrayList<Row>();
	}
	
	public void addMeal(String date, String type, float calories) {
		 this.getData().add(new MealRow(date, type, calories));
	}
	public Filter getFilter(String date, String type) {
		return new MealFilter(type, date);
	}
	@Override
	protected Response prepareEmptyResponse(Filter f) {
		MealFilter filter = (MealFilter)f;
		CaloriesTrackerResponse r = new CaloriesTrackerResponse();
		r.date= filter.date;
		r.type=filter.type;
		return r;
	}
	public static void main(String [] args) {
		CaloriesTracker tracker = new CaloriesTracker();
		tracker.init();
		tracker.addMeal("1", "Dinner", 100.0f);
		tracker.addMeal("2", "Dinner", 100.0f);
		Filter f= tracker.getFilter(null, null);
		CaloriesTrackerResponse r = (CaloriesTrackerResponse) tracker.fetchData(f);
		System.out.println(r.calories);
		
	}
}
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
	public void setDerivedMetrics() {
	}
	public String getType() {
		return type;
	}
	public float getCalories() {
		return calories;
	}
	public String getDate() {
		return date;
	}
}
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
