package report;

/**
 * Response to be returned by api
 * @author deepak
 *
 */
public interface Response {
	 void addMetrics(Row r);
	 void setFilterValues(Filter f); 
	 void setDerivedMetrics();
}
