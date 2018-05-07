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