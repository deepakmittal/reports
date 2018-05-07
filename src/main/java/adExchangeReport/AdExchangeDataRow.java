package adExchangeReport;

import report.Row;

 /**
  * A row in Ad Exchange data
  * @author deepak
  *
  */
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
		return revenue;
	}
	public void setRevenue(float revenue) {
		this.revenue = revenue;
	}

	
	
}