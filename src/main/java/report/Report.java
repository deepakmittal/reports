package report;
import java.util.List;

/**
 * Base class for all reports. You need to implement prepareData and prepareEmptyResponse.
 * Fetch report using $report->fetchData($filter)
 * @author deepak
 */
public abstract class Report {
	List<Row> data;
	abstract protected List<Row> prepareData();
	abstract protected Response prepareEmptyResponse(Filter f);
	public void init() {
		this.data = this.prepareData();
	}
	
	/**
	 * Fetch report based on $filter
	 * @param filter
	 * @return
	 */
	public Response fetchData(Filter filter) {
		Response response = this.prepareEmptyResponse(filter);
		response.setFilterValues(filter);
		for(Row row: this.getData()) {
			if(filter.filterIn(row)) {
				response.addMetrics(row);
			}
		}
		response.setDerivedMetrics();
		return response;
	}
	
	protected List<Row> getData() {
		return this.data;
	} 
	protected void setData(List<Row> data) {
		this.data = data;
	}
	
	
	
}
