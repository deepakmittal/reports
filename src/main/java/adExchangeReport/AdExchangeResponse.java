package adExchangeReport;

import com.fasterxml.jackson.annotation.JsonInclude;

import report.Filter;
import report.Response;
import report.Row;

/**
 * Response for AdExchange report request
 * @author deepak
 *
 */
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
	public void addMetrics(Row r) {
		AdExchangeDataRow row = (AdExchangeDataRow) r;
		this.requests += row.getRequests();
		this.impressions += row.getImpressions();
		this.clicks += row.getClicks();
		this.conversions += row.getConversions();
		this.revenue += row.getRevenue();
	}
	
	public void setFilterValues(Filter f) {
		AdExchangeDataFilter filter = (AdExchangeDataFilter) f;
		this.site = filter.getSite();
		this.month = filter.getMonth();
		this.year = filter.getYear();	
	}
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
	
	
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public int getRequests() {
		return requests;
	}
	public void setRequests(int requests) {
		this.requests = requests;
	}
	public int getImpressions() {
		return impressions;
	}
	public void setImpressions(int impressions) {
		this.impressions = impressions;
	}
	public int getClicks() {
		return clicks;
	}
	public void setClicks(int clicks) {
		this.clicks = clicks;
	}
	public int getConversions() {
		return conversions;
	}
	public void setConversions(int conversions) {
		this.conversions = conversions;
	}
	public float getRevenue() {
		return (float)(int)(100*revenue)/100;
	}
	public void setRevenue(float revenue) {
		this.revenue = revenue;
	}
	public float getCTR() {
		return (float)(int)(100*CTR)/100;
	}
	public void setCTR(float cTR) {
		CTR = cTR;
	}
	public float getCR() {
		return (float)(int)(100*CR)/100;
	}
	public void setCR(float cR) {
		CR = cR;
	}
	public float getFill_rate() {
		return (float)(int)(100*fill_rate)/100;
	}
	public void setFill_rate(float fill_rate) {
		this.fill_rate = fill_rate;
	}
	public float geteCPM() {
		return (float)(int)(100*eCPM)/100;
	}
	public void seteCPM(float eCPM) {
		this.eCPM = eCPM;
	}
	
}
