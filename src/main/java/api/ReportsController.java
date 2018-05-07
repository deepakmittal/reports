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