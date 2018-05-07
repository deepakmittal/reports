package adExchangeReport;

import report.Filter;
import report.Row;

/**
 * Filter for AdExchange data
 * @author deepak
 *
 */
 public class AdExchangeDataFilter implements Filter{
	private String month;
	private String year;
	private String site;
	
	@Override
	public boolean filterIn(Row r) {
		AdExchangeDataRow row = (AdExchangeDataRow) r;
		if(
				(this.month == null || row.getMonth().equals(this.month))
				&&(this.year == null || row.getYear().equals(this.year))
				&&(this.site == null || row.getSite().equals(this.site))
				)
			return true;
		return false;
	}
	
	public AdExchangeDataFilter(String month, String year, String site) {
		this.month = month;
		this.year = year;
		this.site = site;
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
	
	
	
}
